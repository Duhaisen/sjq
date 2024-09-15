package com.sjq.bo;

import com.alibaba.excel.annotation.ExcelProperty;

public class CustomerOrderInfos {

    @ExcelProperty("SIZE")
    private String size = "";
    @ExcelProperty("TYPE")
    private String type = "";
    @ExcelProperty("RO")
    private String quantity = "";

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
