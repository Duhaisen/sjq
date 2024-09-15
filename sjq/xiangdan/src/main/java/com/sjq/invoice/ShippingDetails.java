package com.sjq.invoice;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.enums.WriteDirectionEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.sjq.bo.CommercialInvoice;
import com.sjq.bo.CommercialInvoiceDetail;
import com.sjq.readlistener.ExcelReadListener;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 走货总明细   按照国家进行区分
 *
 * @author dhs
 * @version 1.0
 * @date 2024/08/17
 */
public class ShippingDetails {

    Set<String> slaveCodeSet = new HashSet<>();
    Set<String> codeSet = new HashSet<>();
    Map<String, String> slave2masterMap = new HashMap<>();

    String shippingDetailsTemplate = "./走货总明细汇总模板.xlsx";

    String writeErrorCodeFileName = "./走货总明细/不存在code明细.xlsx";

    String writeErrorCodeTemplate = "./不存在code模板.xlsx";

    List<CommercialInvoiceDetail> detailList = new ArrayList<>();

    List<CommercialInvoiceDetail> unContainsList = new ArrayList<>();


    public void process(List<CommercialInvoice> datas) {
        initCodeInfo();
        initDetails(datas);
        writeErrorCodeInfo();
        writeShippingDetailsByCountry();
    }


    public void initCodeInfo() {

        ExcelReadListener<Map<Integer, String>> shippingDetailsTemplateReader =
                ExcelReadListener.readExcel(shippingDetailsTemplate, 0, 5);

        List<Map<Integer, String>> codeInfos = shippingDetailsTemplateReader.getBody();

        codeInfos.forEach(body -> {
            slaveCodeSet.add(body.get(0));
            codeSet.add(body.get(1));
            slave2masterMap.put(body.get(0), body.get(1));
        });

    }

    public void initDetails(List<CommercialInvoice> datas) {
        for (CommercialInvoice data : datas) {
            List<CommercialInvoiceDetail> details = data.getDetails();
            List<CommercialInvoiceDetail> collect = details.stream().filter(
                    d -> {
                        String code = d.getCode();
                        boolean contains = codeSet.contains(code) || slaveCodeSet.contains(code);
                        if (!contains) {
                            unContainsList.add(d);
                        }
                        return contains;
                    }
            ).collect(Collectors.toList());
            detailList.addAll(collect);
        }

    }

    public void writeErrorCodeInfo() {
        if (unContainsList.size() > 0) {
            EasyExcel.write(writeErrorCodeFileName).withTemplate(writeErrorCodeTemplate).sheet().doFill(unContainsList);
        }
    }

    public void writeShippingDetailsByCountry() {
        Map<String, List<CommercialInvoiceDetail>> countryGroup = detailList.stream().collect(
                Collectors.groupingBy(CommercialInvoiceDetail::getCountry)
        );

        // ShippingDetails
        countryGroup.forEach((country, countryDetails) -> {
            Map<String, List<CommercialInvoiceDetail>> dateGroup = countryDetails.stream().collect(Collectors.groupingBy(CommercialInvoiceDetail::getDate));

            List<Map<String, Object>> outDatas = new ArrayList<>();
            dateGroup.forEach((date, dateDetails) -> {

                Map<String, List<CommercialInvoiceDetail>> blNoGroup = dateDetails.stream().collect(Collectors.groupingBy(CommercialInvoiceDetail::getBlNo));

                blNoGroup.forEach((blNo, blNoDetails) -> {

                    Map<String, List<CommercialInvoiceDetail>> contractNoGroup = blNoDetails.stream().collect(Collectors.groupingBy(CommercialInvoiceDetail::getContractNo));

                    contractNoGroup.forEach((contractNo, contractDetails) -> {
                        Map<String, Object> outMap = new HashMap<>();

                        outMap.put("date", date);
                        outMap.put("blNo", blNo);
                        outMap.put("contractNo", contractNo);


                        outMap.put("quantity", contractDetails.stream().map(d -> Long.parseLong(d.getPcs())).reduce(Long::sum).get());

                        contractDetails.forEach(d -> {

                            String pcs = d.getPcs();
                            String code = d.getCode();

                            if (codeSet.contains(code)) {
                                outMap.put(code, pcs);
                            } else if (slaveCodeSet.contains(code)) {
                                outMap.put(slave2masterMap.get(code), pcs);
                            }

                        });
                        outDatas.add(outMap);

                        codeSet.forEach(
                                code -> outMap.putIfAbsent(code, "")
                        );
                    });
                });
            });

            String writeOutFileName = "./走货总明细/" + country + ".xlsx";

            try (ExcelWriter excelWriter = EasyExcel.write(writeOutFileName).withTemplate(shippingDetailsTemplate).build()) {
                WriteSheet writeSheet = EasyExcel.writerSheet().build();
                FillConfig fillConfig = FillConfig.builder().direction(WriteDirectionEnum.HORIZONTAL).build();
                excelWriter.fill(outDatas, fillConfig, writeSheet);
            }

        });
    }


    public Set<String> getSlaveCodeSet() {
        return slaveCodeSet;
    }

    public void setSlaveCodeSet(Set<String> slaveCodeSet) {
        this.slaveCodeSet = slaveCodeSet;
    }

    public Set<String> getCodeSet() {
        return codeSet;
    }

    public void setCodeSet(Set<String> codeSet) {
        this.codeSet = codeSet;
    }

    public Map<String, String> getSlave2masterMap() {
        return slave2masterMap;
    }

    public void setSlave2masterMap(Map<String, String> slave2masterMap) {
        this.slave2masterMap = slave2masterMap;
    }

    public String getShippingDetailsTemplate() {
        return shippingDetailsTemplate;
    }

    public void setShippingDetailsTemplate(String shippingDetailsTemplate) {
        this.shippingDetailsTemplate = shippingDetailsTemplate;
    }

    public String getWriteErrorCodeFileName() {
        return writeErrorCodeFileName;
    }

    public void setWriteErrorCodeFileName(String writeErrorCodeFileName) {
        this.writeErrorCodeFileName = writeErrorCodeFileName;
    }

    public String getWriteErrorCodeTemplate() {
        return writeErrorCodeTemplate;
    }

    public void setWriteErrorCodeTemplate(String writeErrorCodeTemplate) {
        this.writeErrorCodeTemplate = writeErrorCodeTemplate;
    }

    public List<CommercialInvoiceDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<CommercialInvoiceDetail> detailList) {
        this.detailList = detailList;
    }

    public List<CommercialInvoiceDetail> getUnContainsList() {
        return unContainsList;
    }

    public void setUnContainsList(List<CommercialInvoiceDetail> unContainsList) {
        this.unContainsList = unContainsList;
    }
}
