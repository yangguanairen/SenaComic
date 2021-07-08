package com.sena.senacomic.mangabz.bean;

public class MangabzInfoBean {

    private String name;
    private String chapterId;

    public MangabzInfoBean(String name, String chapterId) {
        this.name = name;
        this.chapterId = chapterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }
}
