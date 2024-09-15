package com.sjq.bo;

import com.alibaba.excel.annotation.ExcelProperty;

public class Product {

    @ExcelProperty("CODE")
    private String code = "";
    @ExcelProperty("PRODUCTCODE")
    private String productCode = "";
    @ExcelProperty("TYPE")
    private String type = "";
    @ExcelProperty("SIZE")
    private String size = "";

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
