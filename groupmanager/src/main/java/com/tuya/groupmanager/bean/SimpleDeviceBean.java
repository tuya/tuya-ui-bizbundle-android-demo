package com.tuya.groupmanager.bean;

public class SimpleDeviceBean {
    private String id;
    private String name;
    private boolean isOnLine;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOnLine() {
        return isOnLine;
    }

    public void setOnLine(boolean onLine) {
        isOnLine = onLine;
    }
}
