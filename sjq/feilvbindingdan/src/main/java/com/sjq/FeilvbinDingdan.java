package com.sjq;

import com.alibaba.excel.EasyExcel;
import com.sjq.bo.CustomerOrderInfos;
import com.sjq.bo.CustomerOrderOutInfos;
import com.sjq.bo.Product;
import com.sjq.reader.Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class FeilvbinDingdan {

    public static final Logger log = LoggerFactory.getLogger(FeilvbinDingdan.class);
    public static final String WRITE_TEMPLATE_FILE_NAME = "./菲律宾订单输出模板.xlsx";
    public static final String PRODUCT_INFO_FILE_NAME = "./productInfos.xlsx";
    public static final String ORDER_FILE_NAME = "./菲律宾订单资料/";
    public static final String ORDER_OUT_FILE_NAME = "./菲律宾订单资料增加code/";


    public static final String STUFF = "-WithCode.xlsx";

    private static String toUpperCaseAndNoSpace(String str) {
        return str.toUpperCase().replace(" ", "");
    }

    public static void main(String[] args) {
        log.info("菲律宾订单生成软件--开始运行");
        try {
            List<Product> productInfos = Reader.readProduct(PRODUCT_INFO_FILE_NAME);
            log.info("读取产品信息完成 count:" + productInfos.size());

            // 根据产品类型和尺寸进行分组
            Map<String, Map<String, List<Product>>> productGroup = productInfos.stream().
                    collect(
                            Collectors.groupingBy(
                                    product -> toUpperCaseAndNoSpace(product.getType()),
                                    Collectors.groupingBy(Product::getSize) // 然后在每个类型内部按尺寸分组
                            ));
            File parentPath = new File(ORDER_FILE_NAME);

            File[] files = parentPath.listFiles();

            assert files != null;

            for (File file : files) {
                log.info("read file:{} start", file.getName());
                if (!file.isDirectory()) {
                    String name = file.getName();
                    if (!name.endsWith(".xlsx") && !name.endsWith(".xls")) {
                        log.info("skip file:{}", file.getName());
                        continue;
                    }
                    log.info("read file:{}", file.getName());
                    List<CustomerOrderInfos> customerOrderInfos = readExcel(file);
                    log.info("read file:{} 的数据总数:{}", file.getName(),customerOrderInfos.size());

                    List<CustomerOrderOutInfos> customerOrderOutInfos = customerOrderInfos.stream().map(
                            orderInfo -> {
                                log.info("开始处理数据 {} {} ", orderInfo.getType(),orderInfo.getSize());
                                CustomerOrderOutInfos outInfos = new CustomerOrderOutInfos();
                                outInfos.setQuantity(orderInfo.getQuantity());
                                outInfos.setType(orderInfo.getType());
                                outInfos.setSize(orderInfo.getSize());
                                Map<String, List<Product>> typeInfos = productGroup.get(toUpperCaseAndNoSpace(orderInfo.getType()));
                                if (typeInfos != null) {
                                    // 正式获取
                                    List<Product> sizeInfos =  typeInfos.get(orderInfo.getSize());
                                    if (sizeInfos == null || sizeInfos.isEmpty()) {
                                        // 尝试大小写不敏感 并且去掉空格 获取
                                        sizeInfos = typeInfos.get(toUpperCaseAndNoSpace(orderInfo.getSize()));
                                    }

                                    if (sizeInfos == null || sizeInfos.isEmpty()) {
                                        List<Product> finalSizeInfos = new ArrayList<>();
                                        typeInfos.values().forEach(
                                                products -> {
                                                    products.forEach(product -> {
                                                        if (toUpperCaseAndNoSpace(product.getSize()).startsWith(toUpperCaseAndNoSpace(orderInfo.getSize()))) {
                                                            finalSizeInfos.addAll(products);
                                                        }
                                                    });
                                                });
                                        sizeInfos = finalSizeInfos;
                                    }

                                    if (!sizeInfos.isEmpty()) {
                                        Product product = sizeInfos.get(0);
                                        outInfos.setCode(product.getCode());
                                        outInfos.setProductCode(product.getProductCode());
                                    } else {
                                        log.warn("通过size找不到 not found product with type:{}, size:{}", orderInfo.getType(), orderInfo.getSize());
                                    }
                                } else {
                                    log.warn("通过type找不到 not found product with type:{}, size:{}", orderInfo.getType(), orderInfo.getSize());
                                }
                                log.info("结束处理数据 {} {} ", orderInfo.getType(),orderInfo.getSize());
                                return outInfos;
                            }
                    ).collect(Collectors.toList());

                    String outFileName = ORDER_OUT_FILE_NAME + name;
                    log.info("write file:{}", outFileName);
                    write2File(outFileName, customerOrderOutInfos);
                }
                log.info("read file:{} end ", file.getName());
            }
            log.info("菲律宾订单生成软件--运行结束");
        } catch (Exception e) {

            log.error("菲律宾订单生成软件--报错@@@@@:", e);
        }


    }


    public static List<CustomerOrderInfos> readExcel(File file) {
        return Reader.readProductInfos(file);
    }

    public static void write2File(String fileName, List<?> data) {
        EasyExcel.write(fileName).withTemplate(WRITE_TEMPLATE_FILE_NAME).sheet().doFill(data);
    }

}