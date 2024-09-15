package com.sjq.bo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author dhs
 * @version 1.0
 * @date 2024/08/11
 */
public class CommercialInvoice {


    private static final Pattern COUNTRY_NAME = Pattern.compile("[\\u4E00-\\u9FA5]+\\B");
    private static final Pattern CONTRACT_NO = Pattern.compile("[A-Za-z0-9_-]+");


    public static String splitCountry(String input) {
        Matcher matcher = COUNTRY_NAME.matcher(input);
        if (matcher.find()) {
            return matcher.group().trim();
        }
        return "";
    }

    public static String splitContractNo(String input) {
        Matcher matcher = CONTRACT_NO.matcher(input);
        if (matcher.find()) {
            return matcher.group().trim();
        }
        return "";
    }


    String contractNoStrList;
    List<String> contractNoList;

    String invoiceNoStrList;
    List<String> invoiceNoList;

    String addressStr;
    String fromCountry;
    String toCountry;

    String date; // 日期

    String refNo;
    String country = "";
    String contractNo = ""; // 合同号


    String quantity; // 数量

    String blNo; // 提单号

    String filePath;

    List<CommercialInvoiceDetail> details;

    public String getContractNoStrList() {
        return contractNoStrList;
    }

    public void setContractNoStrList(String contractNoStrList) {
        this.contractNoStrList = contractNoStrList;
        String[] split = this.contractNoStrList.split("\n");
        this.contractNoList = new ArrayList<String>();
        Collections.addAll(this.contractNoList, split);
    }

    public List<String> getContractNoList() {
        return contractNoList;
    }

    public void setContractNoList(List<String> contractNoList) {
        this.contractNoList = contractNoList;
    }

    public String getInvoiceNoStrList() {
        return invoiceNoStrList;
    }

    public void setInvoiceNoStrList(String invoiceNoStrList) {
        this.invoiceNoStrList = invoiceNoStrList;

        String[] split = this.invoiceNoStrList.split("\n");
        this.invoiceNoList = new ArrayList<String>();
        Collections.addAll(this.invoiceNoList, split);

    }

    public List<String> getInvoiceNoList() {
        return invoiceNoList;
    }

    public void setInvoiceNoList(List<String> invoiceNoList) {
        this.invoiceNoList = invoiceNoList;
    }

    public String getAddressStr() {
        return addressStr;
    }

    public void setAddressStr(String addressStr) {
        this.addressStr = addressStr;
        String[] s = this.addressStr.split(" ");
        this.setFromCountry(s[0].split(":")[2]);
        this.setToCountry(s[1].split(":")[2]);
    }

    public String getFromCountry() {
        return fromCountry;
    }

    public void setFromCountry(String fromCountry) {
        this.fromCountry = fromCountry;
    }

    public String getToCountry() {
        return toCountry;
    }

    public void setToCountry(String toCountry) {
        this.toCountry = toCountry;
    }

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
        this.setCountry(splitCountry(this.refNo));
        this.setContractNo(splitContractNo(this.refNo));
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

    public List<CommercialInvoiceDetail> getDetails() {
        return details;
    }

    public void setDetails(List<CommercialInvoiceDetail> details) {
        this.details = details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommercialInvoice)) return false;
        CommercialInvoice that = (CommercialInvoice) o;
        return Objects.equals(getContractNoStrList(), that.getContractNoStrList()) &&
                Objects.equals(getContractNoList(), that.getContractNoList()) &&
                Objects.equals(getInvoiceNoStrList(), that.getInvoiceNoStrList()) &&
                Objects.equals(getInvoiceNoList(), that.getInvoiceNoList()) &&
                Objects.equals(getAddressStr(), that.getAddressStr()) &&
                Objects.equals(getFromCountry(), that.getFromCountry()) &&
                Objects.equals(getToCountry(), that.getToCountry()) &&
                Objects.equals(getDate(), that.getDate()) &&
                Objects.equals(getRefNo(), that.getRefNo()) &&
                Objects.equals(getCountry(), that.getCountry()) &&
                Objects.equals(getContractNo(), that.getContractNo()) &&
                Objects.equals(getQuantity(), that.getQuantity()) &&
                Objects.equals(getBlNo(), that.getBlNo()) &&
                Objects.equals(getFilePath(), that.getFilePath()) &&
                Objects.equals(getDetails(), that.getDetails());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getContractNoStrList(), getContractNoList(), getInvoiceNoStrList(), getInvoiceNoList(), getAddressStr(), getFromCountry(), getToCountry(), getDate(), getRefNo(), getCountry(), getContractNo(), getQuantity(), getBlNo(), getFilePath(), getDetails());
    }
}
