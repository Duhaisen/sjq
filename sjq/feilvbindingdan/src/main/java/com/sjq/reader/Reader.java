package com.sjq.reader;

import com.alibaba.excel.EasyExcel;
import com.sjq.bo.CustomerOrderInfos;
import com.sjq.bo.Product;
import com.sjq.readlistener.ExcelReadListener;

import java.io.File;
import java.util.List;

public class Reader {

    // 读取excel文件，返回Product列表
    public static List<Product> readProduct(String fileName) {
        ExcelReadListener<Product> excelReadListener = new ExcelReadListener<Product>();
        EasyExcel.read(fileName, Product.class, excelReadListener).headRowNumber(1).sheet().doRead();
        return excelReadListener.getBody();
    }

    // 读取excel文件，返回CustomerOrderInfos列表
    public static List<CustomerOrderInfos> readProductInfos(File fileName) {
        ExcelReadListener<CustomerOrderInfos> excelReadListener = new ExcelReadListener<CustomerOrderInfos>();
        EasyExcel.read(fileName, CustomerOrderInfos.class, excelReadListener).headRowNumber(1).sheet().doRead();
        return excelReadListener.getBody();
    }


}
