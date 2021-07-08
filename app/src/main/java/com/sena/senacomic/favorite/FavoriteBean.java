package com.sena.senacomic.favorite;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class FavoriteBean extends RealmObject {

    @PrimaryKey
    private String id; // "{origin}_{comicId}"
    private int origin;
    private String comicId;
    private String coverUrl;
    private String title;
    private String author;
    private String lastChapterId;
    private String lastChapterName;
    private Date date;

    public FavoriteBean() {

    }

    public FavoriteBean(String id, int origin, String comicId, String coverUrl, String title, String author, String lastChapterId, String lastChapterName, Date date) {
        this.id = id;
        this.origin = origin;
        this.comicId = comicId;
        this.coverUrl = coverUrl;
        this.title = title;
        this.author = author;
        this.lastChapterId = lastChapterId;
        this.lastChapterName = lastChapterName;
        this.date = date;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLastChapterId() {
        return lastChapterId;
    }

    public void setLastChapterId(String lastChapterId) {
        this.lastChapterId = lastChapterId;
    }

    public String getLastChapterName() {
        return lastChapterName;
    }

    public void setLastChapterName(String lastChapterName) {
        this.lastChapterName = lastChapterName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
