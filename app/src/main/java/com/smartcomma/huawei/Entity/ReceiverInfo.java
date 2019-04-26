package com.smartcomma.huawei.Entity;

public class ReceiverInfo {
    private String materil;
    private String companyBarcode;
    private String type;
    private String num;
    private boolean isSelect = false;
    private boolean isReceiver;

    public ReceiverInfo(String materil, String companyBarcode, String type, String num, boolean isReceiver) {
        this.materil = materil;
        this.companyBarcode = companyBarcode;
        this.type = type;
        this.num = num;
        this.isReceiver = isReceiver;
    }

    public boolean isReceiver() {
        return isReceiver;
    }

    public void setReceiver(boolean receiver) {
        isReceiver = receiver;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getMateril() {
        return materil;
    }

    public void setMateril(String materil) {
        this.materil = materil;
    }

    public String getCompanyBarcode() {
        return companyBarcode;
    }

    public void setCompanyBarcode(String companyBarcode) {
        this.companyBarcode = companyBarcode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
