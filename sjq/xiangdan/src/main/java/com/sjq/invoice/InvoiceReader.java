package com.sjq.invoice;

import com.sjq.bo.CommercialInvoice;
import com.sjq.bo.CommercialInvoiceDetail;
import com.sjq.readlistener.ExcelReadListener;

import java.io.File;
import java.util.*;

/**
 * 箱单发票 读取
 *
 * @author dhs
 * @version 1.0
 * @date 2024/08/17
 */
public class InvoiceReader {

    public static final String COMMERCIAL_INVOICE = "COMMERCIAL INVOICE";
    public static final String PACKING_LIST = "PACKING LIST";

    public static final String REMARK = "REMARK";

    public static final String DETAILS = "DETAILS";
    public static final String TOTAL = "TOTAL";

    public static final String BLNO = "B/L NO";


    String path = "./工厂箱单发票/PCR";


    public List<CommercialInvoice> process() {

        File parentPath = new File(path);

        File[] files = parentPath.listFiles();

        assert files != null;


        List<CommercialInvoice> datas = new ArrayList<>();

        Arrays.stream(files).forEach(file -> {
            datas.addAll(readExcel(file));
        });

        datas.sort(
                Comparator.comparing(CommercialInvoice::getDate)
                        .thenComparing(CommercialInvoice::getCountry)
        );


        return datas;

    }


    public List<CommercialInvoice> readExcel(File file) {
        List<CommercialInvoice> result = new ArrayList<>();

        if (file.isDirectory()) {
            for (File listFile : Objects.requireNonNull(file.listFiles())) {
                result.addAll(readExcel(listFile));
            }
        }
        if (!file.isFile()) {
            return result;
        }


        ExcelReadListener<Map<Integer, String>> read0 = ExcelReadListener.readExcel(file, 0, 11);
        ExcelReadListener<Map<Integer, String>> read1 = ExcelReadListener.readExcel(file, 1, 11);

        List<Map<Integer, String>> header0 = read0.getHeader();

        List<Map<Integer, String>> header1 = read1.getHeader();


        List<Map<Integer, String>> commercialHeader = null;
        List<Map<Integer, String>> packingHeader = null;

        List<Map<Integer, String>> commercialBody = null;
        List<Map<Integer, String>> packingBody = null;

        if (header0.size() > 2) {
            Map<Integer, String> sheet0TypeMap = read0.getHeader().get(2);
            if (COMMERCIAL_INVOICE.equals(sheet0TypeMap.get(0))) {
                commercialHeader = read0.getHeader();
                commercialBody = read0.getBody();
            } else if (PACKING_LIST.equals(sheet0TypeMap.get(0))) {
                packingHeader = read0.getHeader();
                packingBody = read0.getBody();
            } else {
                System.out.println(file.getPath() + " sheet0 中的信息 缺少 类型信息 ");
                return result;
            }
        } else {
            System.out.print(file.getPath() + "  没有sheet0");
        }
        if (header1.size() > 2) {
            Map<Integer, String> sheet0TypeMap = read1.getHeader().get(2);
            if (COMMERCIAL_INVOICE.equals(sheet0TypeMap.get(0))) {
                commercialHeader = read0.getHeader();
                commercialBody = read0.getBody();
            } else if (PACKING_LIST.equals(sheet0TypeMap.get(0))) {
                packingHeader = read0.getHeader();
                packingBody = read0.getBody();
            } else {
                System.out.println(file.getPath() + " sheet1 中的信息 缺少 类型信息 ");
                return result;
            }
        } else {
            System.out.println(file.getPath() + "  没有sheet1");
        }

        if (null == commercialHeader || null == commercialBody || null == packingHeader || null == packingBody) {
            System.out.println(file.getPath() + "   解析失败  ");
            return result;
        }

        Map<String, List<Map<Integer, String>>> commercialBodyGroupMap = groupBody(commercialBody);

        CommercialInvoice commercialInvoice = new CommercialInvoice();

        List<CommercialInvoiceDetail> commercialInvoiceDetails = new ArrayList<>();

        commercialInvoice.setDate(getDate(commercialHeader));
        commercialInvoice.setQuantity(getTotal(commercialBodyGroupMap.get(TOTAL)));
        commercialInvoice.setBlNo(getBlNo(commercialBodyGroupMap.get(REMARK)).split(":")[1].trim());
        commercialInvoice.setFilePath(file.getPath());
        String refNo = getRefNo(commercialHeader);

        if (refNo == null) {
            System.out.println(file.getPath() + " 缺少 refNO ！！！！！！！！！！！！！！！！！！！");
        } else {
            commercialInvoice.setRefNo(refNo);
        }

        commercialInvoice.setDetails(commercialInvoiceDetails);

        List<Map<Integer, String>> maps = commercialBodyGroupMap.get(DETAILS);

        for (Map<Integer, String> detailInfo : maps) {
            CommercialInvoiceDetail detail = new CommercialInvoiceDetail();

            detail.setDate(commercialInvoice.getDate());
            detail.setRefNo(commercialInvoice.getRefNo());
            detail.setCountry(commercialInvoice.getCountry());
            detail.setContractNo(commercialInvoice.getContractNo());
            detail.setQuantity(commercialInvoice.getQuantity());
            detail.setBlNo(commercialInvoice.getBlNo());
            detail.setFilePath(commercialInvoice.getFilePath());


            detail.setBrand(detailInfo.get(0));
            detail.setCode(detailInfo.get(1));
            detail.setSize(detailInfo.get(2));

            detail.setLi(detailInfo.get(3));
            detail.setSs(detailInfo.get(4));
            detail.setXl(detailInfo.get(5));

            detail.setPattern(detailInfo.get(6));
            detail.setPcs(detailInfo.get(7));
            detail.setUsdUnitPrice(detailInfo.get(8));
            detail.setUsdTotalAmount(detailInfo.get(9));
            commercialInvoiceDetails.add(detail);
        }

        result.add(commercialInvoice);

        return result;
    }


    public static String getDate(List<Map<Integer, String>> header) {
        return header.get(5).get(8);
    }

    public static String getRefNo(List<Map<Integer, String>> header) {
        return header.get(6).get(8);
    }

    public static String getTotal(List<Map<Integer, String>> totals) {
        for (Map<Integer, String> total : totals) {
            if (total.get(7) != null) {
                return total.get(7);
            }
        }
        return "";
    }

    public static String getBlNo(List<Map<Integer, String>> remarks) {
        for (Map<Integer, String> remark : remarks) {
            if (remark.get(1) != null && remark.get(1).startsWith(BLNO)) {
                return remark.get(1);
            }
        }
        return "";
    }


    public static Map<String, List<Map<Integer, String>>> groupBody(List<Map<Integer, String>> body) {

        List<Map<Integer, String>> details = new ArrayList<>();
        List<Map<Integer, String>> total = new ArrayList<>();
        List<Map<Integer, String>> remark = new ArrayList<>();

        boolean addDetail = true;
        boolean addTotal = false;

        for (Map<Integer, String> detail : body) {
            if (addDetail) {
                if (detail.get(0) != null) {
                    details.add(detail);
                } else {
                    addDetail = false;
                    addTotal = true;
                    total.add(detail);
                }
            } else if (addTotal) {
                if (REMARK.equals(detail.get(0))) {
                    addTotal = false;
                    remark.add(detail);
                } else {
                    total.add(detail);
                }
            } else {
                remark.add(detail);
            }
        }

        Map<String, List<Map<Integer, String>>> result = new HashMap<>();

        result.put(DETAILS, details);
        result.put(TOTAL, total);
        result.put(REMARK, remark);

        return result;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
