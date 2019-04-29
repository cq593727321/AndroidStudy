package com.smartcomma.huawei.Entity;

public class Location {
    private String id;
    private String location;
    private boolean isDefault;

    public Location(String location, boolean isDefault) {
        this.location = location;
        this.isDefault = isDefault;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
