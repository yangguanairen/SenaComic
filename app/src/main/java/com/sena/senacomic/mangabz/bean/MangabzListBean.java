package com.sena.senacomic.mangabz.bean;

import java.util.List;

public class MangabzListBean {

    private List<UpdateComicItems> UpdateComicItems;
    private int Count;

    public List<MangabzListBean.UpdateComicItems> getUpdateComicItems() {
        return UpdateComicItems;
    }

    public void setUpdateComicItems(List<MangabzListBean.UpdateComicItems> updateComicItems) {
        UpdateComicItems = updateComicItems;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public class UpdateComicItems {

        private String ID;
        private String Title;
        private String UrlKey;
        private String Logo;
        private String LastPartUrl;
        private String ShowLastPartName;
        private String ShowPicUrlB;
        private String ShowConver;
        private String ComicPart;
        private List<String> Author;
        private String ShowReads;
        private String Content;
        private int Star;
        private String ShowSource;
        private int Status;
        private String LastUpdateTime;
        private String ShelvesTime;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public String getUrlKey() {
            return UrlKey;
        }

        public void setUrlKey(String urlKey) {
            UrlKey = urlKey;
        }

        public String getLogo() {
            return Logo;
        }

        public void setLogo(String logo) {
            Logo = logo;
        }

        public String getLastPartUrl() {
            return LastPartUrl;
        }

        public void setLastPartUrl(String lastPartUrl) {
            LastPartUrl = lastPartUrl;
        }

        public String getShowLastPartName() {
            return ShowLastPartName;
        }

        public void setShowLastPartName(String showLastPartName) {
            ShowLastPartName = showLastPartName;
        }

        public String getShowPicUrlB() {
            return ShowPicUrlB;
        }

        public void setShowPicUrlB(String showPicUrlB) {
            ShowPicUrlB = showPicUrlB;
        }

        public String getShowConver() {
            return ShowConver;
        }

        public void setShowConver(String showConver) {
            ShowConver = showConver;
        }

        public String getComicPart() {
            return ComicPart;
        }

        public void setComicPart(String comicPart) {
            ComicPart = comicPart;
        }

        public List<String> getAuthor() {
            return Author;
        }

        public void setAuthor(List<String> author) {
            Author = author;
        }

        public String getShowReads() {
            return ShowReads;
        }

        public void setShowReads(String showReads) {
            ShowReads = showReads;
        }

        public String getContent() {
            return Content;
        }

        public void setContent(String content) {
            Content = content;
        }

        public int getStar() {
            return Star;
        }

        public void setStar(int star) {
            Star = star;
        }

        public String getShowSource() {
            return ShowSource;
        }

        public void setShowSource(String showSource) {
            ShowSource = showSource;
        }

        public int getStatus() {
            return Status;
        }

        public void setStatus(int status) {
            Status = status;
        }

        public String getLastUpdateTime() {
            return LastUpdateTime;
        }

        public void setLastUpdateTime(String lastUpdateTime) {
            LastUpdateTime = lastUpdateTime;
        }

        public String getShelvesTime() {
            return ShelvesTime;
        }

        public void setShelvesTime(String shelvesTime) {
            ShelvesTime = shelvesTime;
        }
    }
}
