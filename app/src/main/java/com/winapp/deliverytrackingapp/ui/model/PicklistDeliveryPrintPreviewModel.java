package com.winapp.deliverytrackingapp.ui.model;


import java.util.ArrayList;

public class PicklistDeliveryPrintPreviewModel {

    private String invoiceNumber;
    private String invoiceDate;
    private String customerCode;
    private String customerName;
    private String address;
    private String addressCust1;
    private String addressCust2;
    private String addressCust3;
    private String deliveryAddress;
    private String billDiscount;
    private String itemDiscount;
    private String subTotal;
    private String netTax;
    private String netTotal;
    private String taxType;
    private String taxValue;
    private String outStandingAmount;
    private String balanceAmount;
    private String address1;
    private String address2;
    private String address3;
    private String addressstate;
    private String addresssZipcode;
    private String soNumber;
    private String doNumber;
    private String soDate;
    private String doDate;
    private String overAllTotal;
    private String paymentTerm;
    private String excQty;
    private String allowDeliveryAddress;
    private String phoneNo;
    private String lineNo;
    private String fromShipZipcode;
    private String toShipZipcode;

    public String getFromShipZipcode() {
        return fromShipZipcode;
    }

    public void setFromShipZipcode(String fromShipZipcode) {
        this.fromShipZipcode = fromShipZipcode;
    }

    public String getToShipZipcode() {
        return toShipZipcode;
    }

    public void setToShipZipcode(String toShipZipcode) {
        this.toShipZipcode = toShipZipcode;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getPaymentTerm() {
        return paymentTerm;
    }

    public void setPaymentTerm(String paymentTerm) {
        this.paymentTerm = paymentTerm;
    }

    private ArrayList<InvoiceList> invoiceList;
    private ArrayList<SalesReturnList> salesReturnList;

    public String getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(String balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public String getSoNumber() {
        return soNumber;
    }

    public void setSoNumber(String soNumber) {
        this.soNumber = soNumber;
    }

    public String getAllowDeliveryAddress() {
        return allowDeliveryAddress;
    }

    public void setAllowDeliveryAddress(String allowDeliveryAddress) {
        this.allowDeliveryAddress = allowDeliveryAddress;
    }

    public String getDoNumber() {
        return doNumber;
    }

    public void setDoNumber(String doNumber) {
        this.doNumber = doNumber;
    }

    public String getSoDate() {
        return soDate;
    }

    public void setSoDate(String soDate) {
        this.soDate = soDate;
    }

    public String getDoDate() {
        return doDate;
    }

    public void setDoDate(String doDate) {
        this.doDate = doDate;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddressstate() {
        return addressstate;
    }

    public void setAddressstate(String addressstate) {
        this.addressstate = addressstate;
    }

    public String getAddresssZipcode() {
        return addresssZipcode;
    }

    public void setAddresssZipcode(String addresssZipcode) {
        this.addresssZipcode = addresssZipcode;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getOutStandingAmount() {
        return outStandingAmount;
    }

    public void setOutStandingAmount(String outStandingAmount) {
        this.outStandingAmount = outStandingAmount;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public String getTaxValue() {
        return taxValue;
    }

    public void setTaxValue(String taxValue) {
        this.taxValue = taxValue;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
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

    public String getAddressCust1() {
        return addressCust1;
    }

    public void setAddressCust1(String addressCust1) {
        this.addressCust1 = addressCust1;
    }

    public String getAddressCust2() {
        return addressCust2;
    }

    public void setAddressCust2(String addressCust2) {
        this.addressCust2 = addressCust2;
    }

    public String getAddressCust3() {
        return addressCust3;
    }

    public void setAddressCust3(String addressCust3) {
        this.addressCust3 = addressCust3;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getBillDiscount() {
        return billDiscount;
    }

    public void setBillDiscount(String billDiscount) {
        this.billDiscount = billDiscount;
    }

    public String getItemDiscount() {
        return itemDiscount;
    }

    public void setItemDiscount(String itemDiscount) {
        this.itemDiscount = itemDiscount;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getNetTax() {
        return netTax;
    }

    public void setNetTax(String netTax) {
        this.netTax = netTax;
    }

    public String getNetTotal() {
        return netTotal;
    }

    public void setNetTotal(String netTotal) {
        this.netTotal = netTotal;
    }

    public ArrayList<InvoiceList> getInvoiceList() {
        return invoiceList;
    }

    public void setInvoiceList(ArrayList<InvoiceList> invoiceList) {
        this.invoiceList = invoiceList;
    }

    public String getOverAllTotal() {
        return overAllTotal;
    }

    public void setOverAllTotal(String overAllTotal) {
        this.overAllTotal = overAllTotal;
    }

    public ArrayList<SalesReturnList> getSalesReturnList() {
        return salesReturnList;
    }

    public void setSalesReturnList(ArrayList<SalesReturnList> salesReturnList) {
        this.salesReturnList = salesReturnList;
    }

    public static class InvoiceList {
        private String productCode;
        private String sno;
        private String description;
        private String lqty;
        private String cqty;
        private String netQty;
        private String netQuantity;
        private String returnQty;
        private String price;
        private String total;
        private String transTotal;
        private String cartonPrice;
        private String focQty;
        private String unitPrice;
        private String pcsperCarton;
        private String itemtax;
        private String subTotal;
        private String uomCode;
        private String pricevalue;
        private String customerItemCode;
        private String saleType;

        private String accountName;
        private String excQty;
        private String stockQty;
        private String grossPrice;
        private String grossTotal;

        public String getGrossPrice() {
            return grossPrice;
        }

        public void setGrossPrice(String grossPrice) {
            this.grossPrice = grossPrice;
        }

        public String getGrossTotal() {
            return grossTotal;
        }

        public void setGrossTotal(String grossTotal) {
            this.grossTotal = grossTotal;
        }

        public String getStockQty() {
            return stockQty;
        }

        public void setStockQty(String stockQty) {
            this.stockQty = stockQty;
        }

        public String getExcQty() {
            return excQty;
        }

        public void setExcQty(String excQty) {
            this.excQty = excQty;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public String getCustomerItemCode() {
            return customerItemCode;
        }
        public void setCustomerItemCode(String customerItemCode) {
            this.customerItemCode = customerItemCode;
        }

        public String getSaleType() {
            return saleType;
        }

        public void setSaleType(String saleType) {
            this.saleType = saleType;
        }

        public String getTransTotal() {
            return transTotal;
        }

        public void setTransTotal(String transTotal) {
            this.transTotal = transTotal;
        }

        public String getFocQty() {
            return focQty;
        }

        public void setFocQty(String focQty) {
            this.focQty = focQty;
        }

        public String getNetQuantity() {
            return netQuantity;
        }

        public void setNetQuantity(String netQuantity) {
            this.netQuantity = netQuantity;
        }

        public String getReturnQty() {
            return returnQty;
        }

        public void setReturnQty(String returnQty) {
            this.returnQty = returnQty;
        }

        public String getPricevalue() {
            return pricevalue;
        }

        public void setPricevalue(String pricevalue) {
            this.pricevalue = pricevalue;
        }

        public String getUomCode() {
            return uomCode;
        }

        public void setUomCode(String uomCode) {
            this.uomCode = uomCode;
        }

        public String getSubTotal() {
            return subTotal;
        }

        public void setSubTotal(String subTotal) {
            this.subTotal = subTotal;
        }

        public String getItemtax() {
            return itemtax;
        }

        public void setItemtax(String itemtax) {
            this.itemtax = itemtax;
        }

        public String getPcsperCarton() {
            return pcsperCarton;
        }

        public void setPcsperCarton(String pcsperCarton) {
            this.pcsperCarton = pcsperCarton;
        }

        public String getCartonPrice() {
            return cartonPrice;
        }

        public void setCartonPrice(String cartonPrice) {
            this.cartonPrice = cartonPrice;
        }

        public String getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(String unitPrice) {
            this.unitPrice = unitPrice;
        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public String getSno() {
            return sno;
        }

        public void setSno(String sno) {
            this.sno = sno;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLqty() {
            return lqty;
        }

        public void setLqty(String lqty) {
            this.lqty = lqty;
        }

        public String getCqty() {
            return cqty;
        }

        public void setCqty(String cqty) {
            this.cqty = cqty;
        }

        public String getNetQty() {
            return netQty;
        }

        public void setNetQty(String netQty) {
            this.netQty = netQty;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }
    }
    public static class SalesReturnList {

        private String salesReturnNumber;
        private String sRSubTotal;
        private String sRTaxTotal;
        private String sRNetTotal;

        public String getSalesReturnNumber() {
            return salesReturnNumber;
        }

        public void setSalesReturnNumber(String salesReturnNumber) {
            this.salesReturnNumber = salesReturnNumber;
        }

        public String getsRSubTotal() {
            return sRSubTotal;
        }

        public void setsRSubTotal(String sRSubTotal) {
            this.sRSubTotal = sRSubTotal;
        }

        public String getsRTaxTotal() {
            return sRTaxTotal;
        }

        public void setsRTaxTotal(String sRTaxTotal) {
            this.sRTaxTotal = sRTaxTotal;
        }

        public String getsRNetTotal() {
            return sRNetTotal;
        }

        public void setsRNetTotal(String sRNetTotal) {
            this.sRNetTotal = sRNetTotal;
        }
    }

}
