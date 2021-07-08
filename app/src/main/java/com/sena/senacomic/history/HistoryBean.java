package com.sena.senacomic.history;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class HistoryBean extends RealmObject {

    @PrimaryKey
    private String id;  //"{origin}_{comicId}"

    private int origin;
    private String comicId;
    private String coverUrl;
    private String comicTitle;
    private String author;
    private String chapterId;
    private String chapterName;
    private Date lastReadTime;

    public HistoryBean() {

    }

    public HistoryBean(String id, int origin, String comicId, String coverUrl, String comicTitle, String author, String chapterId, String chapterName, Date lastReadTime) {
        this.id = id;
        this.origin = origin;
        this.comicId = comicId;
        this.coverUrl = coverUrl;
        this.comicTitle = comicTitle;
        this.author = author;
        this.chapterId = chapterId;
        this.chapterName = chapterName;
        this.lastReadTime = lastReadTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getOrigin() {
        return origin;
    }

    public void setOrigin(int origin) {
        this.origin = origin;
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

    public String getComicTitle() {
        return comicTitle;
    }

    public void setComicTitle(String comicTitle) {
        this.comicTitle = comicTitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public Date getLastReadTime() {
        return lastReadTime;
    }

    public void setLastReadTime(Date lastReadTime) {
        this.lastReadTime = lastReadTime;
    }
}
