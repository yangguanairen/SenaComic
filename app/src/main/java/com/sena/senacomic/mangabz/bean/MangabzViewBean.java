package com.sena.senacomic.mangabz.bean;

public class MangabzViewBean {

    private String mid;
    private String cid;
    private String readMode;
    private String imageUrl;

    public MangabzViewBean(String mid, String cid, String readMode, String imageUrl) {
        this.mid = mid;
        this.cid = cid;
        this.readMode = readMode;
        this.imageUrl = imageUrl;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getReadMode() {
        return readMode;
    }

    public void setReadMode(String readMode) {
        this.readMode = readMode;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
