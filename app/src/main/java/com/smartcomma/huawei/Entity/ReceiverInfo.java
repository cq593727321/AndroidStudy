package com.smartcomma.huawei.Entity;

public class ReceiverInfo {
    private String material;
    private String companyBarcode;
    private String type;
    private String location;
    private String num;
    private boolean isSelect = false;
    private boolean isReceiver;

    public ReceiverInfo(String material, String companyBarcode, String type, String num, String location, boolean isReceiver) {
        this.material = material;
        this.companyBarcode = companyBarcode;
        this.type = type;
        this.num = num;
        this.isReceiver = isReceiver;
        this.location = location;
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

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
