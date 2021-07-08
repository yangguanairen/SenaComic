package com.sena.senacomic.mangabz.bean;

public class MangabzSearchBean {

    private String comicId;
    private String coverUrl;
    private String title;
    private String status;
    private String chapterName;  // 连载是最新章节，完结是第一章
    private String chapterId;

    public MangabzSearchBean(String comicId, String coverUrl, String title, String status, String chapterName, String chapterId) {
        this.comicId = comicId;
        this.coverUrl = coverUrl;
        this.title = title;
        this.status = status;
        this.chapterName = chapterName;
        this.chapterId = chapterId;
    }

    public String getComicId() {
        return comicId;
    }

    public void setComicId(String comicId) {
        this.comicId = comicId;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }
}
