package com.sena.senacomic.kuaikanmanhua.bean;

import java.util.List;

public class KuaiKanComicListBean {
    /**
     * Copyright 2021 bejson.com
     */

    private int code;
    private String message;
    private int total;
    private Hits hits;

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal() {
        return total;
    }

    public void setHits(Hits hits) {
        this.hits = hits;
    }

    public Hits getHits() {
        return hits;
    }


    public class TopicCategories {

        private String title;
        private int priority;
        private int tagId;
        public void setTitle(String title) {
            this.title = title;
        }
        public String getTitle() {
            return title;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }
        public int getPriority() {
            return priority;
        }

        public void setTagId(int tagId) {
            this.tagId = tagId;
        }
        public int getTagId() {
            return tagId;
        }

    }

    public class Action_type {

        private int type;
        private String parent_target_id;
        public void setType(int type) {
            this.type = type;
        }
        public int getType() {
            return type;
        }

        public void setParent_target_id(String parent_target_id) {
            this.parent_target_id = parent_target_id;
        }
        public String getParent_target_id() {
            return parent_target_id;
        }

    }

    public class ContinueReadComic {

        private String id;
        private String title;
        public void setId(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }

        public void setTitle(String title) {
            this.title = title;
        }
        public String getTitle() {
            return title;
        }

    }

    public class TopicMessageList {

        private String id;
        private String topic_id;
        private String title;
        private String vertical_image_url;
        private String cover_image_url;
        private boolean is_favourite;
        private boolean favourite;
        private List<String> category;
        private int likes_count;
        private int comments_count;
        private int favourite_count;
        private int comics_count;
        private String first_comic_publish_time;
        private String author_name;
        private long popularity;
        private String latest_comic_title;
        private boolean is_free;
        private Action_type action_type;
        private ContinueReadComic continueReadComic;
        public void setId(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }

        public void setTopic_id(String topic_id) {
            this.topic_id = topic_id;
        }
        public String getTopic_id() {
            return topic_id;
        }

        public void setTitle(String title) {
            this.title = title;
        }
        public String getTitle() {
            return title;
        }

        public void setVertical_image_url(String vertical_image_url) {
            this.vertical_image_url = vertical_image_url;
        }
        public String getVertical_image_url() {
            return vertical_image_url;
        }

        public void setCover_image_url(String cover_image_url) {
            this.cover_image_url = cover_image_url;
        }
        public String getCover_image_url() {
            return cover_image_url;
        }

        public void setIs_favourite(boolean is_favourite) {
            this.is_favourite = is_favourite;
        }
        public boolean getIs_favourite() {
            return is_favourite;
        }

        public void setFavourite(boolean favourite) {
            this.favourite = favourite;
        }
        public boolean getFavourite() {
            return favourite;
        }

        public void setCategory(List<String> category) {
            this.category = category;
        }
        public List<String> getCategory() {
            return category;
        }

        public void setLikes_count(int likes_count) {
            this.likes_count = likes_count;
        }
        public int getLikes_count() {
            return likes_count;
        }

        public void setComments_count(int comments_count) {
            this.comments_count = comments_count;
        }
        public int getComments_count() {
            return comments_count;
        }

        public void setFavourite_count(int favourite_count) {
            this.favourite_count = favourite_count;
        }
        public int getFavourite_count() {
            return favourite_count;
        }

        public void setComics_count(int comics_count) {
            this.comics_count = comics_count;
        }
        public int getComics_count() {
            return comics_count;
        }

        public void setFirst_comic_publish_time(String first_comic_publish_time) {
            this.first_comic_publish_time = first_comic_publish_time;
        }
        public String getFirst_comic_publish_time() {
            return first_comic_publish_time;
        }

        public void setAuthor_name(String author_name) {
            this.author_name = author_name;
        }
        public String getAuthor_name() {
            return author_name;
        }

        public void setPopularity(long popularity) {
            this.popularity = popularity;
        }
        public long getPopularity() {
            return popularity;
        }

        public void setLatest_comic_title(String latest_comic_title) {
            this.latest_comic_title = latest_comic_title;
        }
        public String getLatest_comic_title() {
            return latest_comic_title;
        }

        public void setIs_free(boolean is_free) {
            this.is_free = is_free;
        }
        public boolean getIs_free() {
            return is_free;
        }

        public void setAction_type(Action_type action_type) {
            this.action_type = action_type;
        }
        public Action_type getAction_type() {
            return action_type;
        }

        public void setContinueReadComic(ContinueReadComic continueReadComic) {
            this.continueReadComic = continueReadComic;
        }
        public ContinueReadComic getContinueReadComic() {
            return continueReadComic;
        }

    }

    public class Hits {

        private List<TopicCategories> topicCategories;
        private List<TopicMessageList> topicMessageList;
        public void setTopicCategories(List<TopicCategories> topicCategories) {
            this.topicCategories = topicCategories;
        }
        public List<TopicCategories> getTopicCategories() {
            return topicCategories;
        }

        public void setTopicMessageList(List<TopicMessageList> topicMessageList) {
            this.topicMessageList = topicMessageList;
        }
        public List<TopicMessageList> getTopicMessageList() {
            return topicMessageList;
        }

    }




}
