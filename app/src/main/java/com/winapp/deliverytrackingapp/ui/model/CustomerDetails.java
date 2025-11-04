package com.winapp.deliverytrackingapp.ui.model;

public class CustomerDetails {
    // this class used to get the customer details to calculating the product price

    private String customerCode;
    private String customerName;
    private String taxPerc;
    private String taxType;
    private String customerAddress1;
    private String customerAddress2;
    private String customerAddress3;
    private String currencyCode;
    private String creditLimit;
    private String balanceAmount;
    private String remarks;

    private String phoneNo;
    private String isActive;
    private String haveTax;
    private String country;
    private String taxCode;
    private String allowFOC;

    public String getAllowFOC() {
        return allowFOC;
    }

    public void setAllowFOC(String allowFOC) {
        this.allowFOC = allowFOC;
    }

    public String getBillDiscPercentage() {
        return billDiscPercentage;
    }

    public void setBillDiscPercentage(String billDiscPercentage) {
        this.billDiscPercentage = billDiscPercentage;
    }

    private String billDiscPercentage;

    public String getCustomerAddress2() {
        return customerAddress2;
    }

    public void setCustomerAddress2(String customerAddress2) {
        this.customerAddress2 = customerAddress2;
    }

    public String getCustomerAddress3() {
        return customerAddress3;
    }

    public void setCustomerAddress3(String customerAddress3) {
        this.customerAddress3 = customerAddress3;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getHaveTax() {
        return haveTax;
    }

    public void setHaveTax(String haveTax) {
        this.haveTax = haveTax;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }


    //  response.optString("CustomerCode"),
            //     response.optString("CustomerName"),
             //    response.optString("PhoneNo"),
              //   response.optString("Address1"),
              //   response.optString("Address2"),
              //   response.optString("Address3"),
              //   response.optString("IsActive"),
              //   response.optString("HaveTax"),
              //   response.optString("TaxType"),
               //  response.optString("TaxPerc"),
              //   response.optString("TaxCode"),
              //   response.optString("CreditLimit"),
              // //  response.optString("Country"),
              //   response.optString("CurrencyCode")

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(String creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(String balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTaxPerc() {
        return taxPerc;
    }

    public void setTaxPerc(String taxPerc) {
        this.taxPerc = taxPerc;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public String getCustomerAddress1() {
        return customerAddress1;
    }

    public void setCustomerAddress1(String customerAddress1) {
        this.customerAddress1 = customerAddress1;
    }
}
