package com.sjq.bo;

import java.math.BigDecimal;

/**
 * @author dhs
 * @version 1.0
 * @date 2024/08/11
 */
public class CommercialInvoiceDetail {
    String
            brand,
            code, // 编号
            size,
            li,
            ss,
            xl,
            pattern,
            pcs,// 数量
            usdUnitPrice,
            usdTotalAmount;
    BigDecimal price;
    BigDecimal amount;


    String date; // 日期

    String refNo;
    String country = "";
    String contractNo = ""; // 合同号

    String quantity; // 数量

    String blNo; // 提单号

    String filePath;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getBlNo() {
        return blNo;
    }

    public void setBlNo(String blNo) {
        this.blNo = blNo;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getLi() {
        return li;
    }

    public void setLi(String li) {
        this.li = li;
    }

    public String getSs() {
        return ss;
    }

    public void setSs(String ss) {
        this.ss = ss;
    }

    public String getXl() {
        return xl;
    }

    public void setXl(String xl) {
        this.xl = xl;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPcs() {
        return pcs;
    }

    public void setPcs(String pcs) {
        this.pcs = pcs;
    }

    public String getUsdUnitPrice() {
        return usdUnitPrice;
    }

    public void setUsdUnitPrice(String usdUnitPrice) {
        this.usdUnitPrice = usdUnitPrice;
    }

    public String getUsdTotalAmount() {
        return usdTotalAmount;
    }

    public void setUsdTotalAmount(String usdTotalAmount) {
        this.usdTotalAmount = usdTotalAmount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
