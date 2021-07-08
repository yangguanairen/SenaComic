package com.sena.senacomic.kuaikanmanhua.bean;

import java.util.List;


public class KuaiKanBrowserBean {
    /**
     * Copyright 2021 bejson.com
     */
    private int code;
    private Data data;
    private String message;

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public class Comic_info {

        private String id;
        private String title;
        private String cover_image_url;
        // 漫画图片
        private List<String> images;
        private List<Comic_images> comic_images;
        private boolean is_pay_comic;
        private boolean need_vip;
        private boolean is_ip_block;
        private boolean is_sensitive;
        private boolean is_published;
        private boolean locked;
        private int locked_code;
        private boolean liked;
        private String likes_count;
        private int likes_count_number;
        private String created_at;
        private boolean is_danmu_hidden;
        private boolean is_free;
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

        public void setCover_image_url(String cover_image_url) {
            this.cover_image_url = cover_image_url;
        }
        public String getCover_image_url() {
            return cover_image_url;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
        public List<String> getImages() {
            return images;
        }

        public void setComic_images(List<Comic_images> comic_images) {
            this.comic_images = comic_images;
        }
        public List<Comic_images> getComic_images() {
            return comic_images;
        }

        public void setIs_pay_comic(boolean is_pay_comic) {
            this.is_pay_comic = is_pay_comic;
        }
        public boolean getIs_pay_comic() {
            return is_pay_comic;
        }

        public void setNeed_vip(boolean need_vip) {
            this.need_vip = need_vip;
        }
        public boolean getNeed_vip() {
            return need_vip;
        }

        public void setIs_ip_block(boolean is_ip_block) {
            this.is_ip_block = is_ip_block;
        }
        public boolean getIs_ip_block() {
            return is_ip_block;
        }

        public void setIs_sensitive(boolean is_sensitive) {
            this.is_sensitive = is_sensitive;
        }
        public boolean getIs_sensitive() {
            return is_sensitive;
        }

        public void setIs_published(boolean is_published) {
            this.is_published = is_published;
        }
        public boolean getIs_published() {
            return is_published;
        }

        public void setLocked(boolean locked) {
            this.locked = locked;
        }
        public boolean getLocked() {
            return locked;
        }

        public void setLocked_code(int locked_code) {
            this.locked_code = locked_code;
        }
        public int getLocked_code() {
            return locked_code;
        }

        public void setLiked(boolean liked) {
            this.liked = liked;
        }
        public boolean getLiked() {
            return liked;
        }

        public void setLikes_count(String likes_count) {
            this.likes_count = likes_count;
        }
        public String getLikes_count() {
            return likes_count;
        }

        public void setLikes_count_number(int likes_count_number) {
            this.likes_count_number = likes_count_number;
        }
        public int getLikes_count_number() {
            return likes_count_number;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
        public String getCreated_at() {
            return created_at;
        }

        public void setIs_danmu_hidden(boolean is_danmu_hidden) {
            this.is_danmu_hidden = is_danmu_hidden;
        }
        public boolean getIs_danmu_hidden() {
            return is_danmu_hidden;
        }

        public void setIs_free(boolean is_free) {
            this.is_free = is_free;
        }
        public boolean getIs_free() {
            return is_free;
        }

    }
    public class Comic_images {

        private int width;
        private int height;
        private String url;
        private String key;
        public void setWidth(int width) {
            this.width = width;
        }
        public int getWidth() {
            return width;
        }

        public void setHeight(int height) {
            this.height = height;
        }
        public int getHeight() {
            return height;
        }

        public void setUrl(String url) {
            this.url = url;
        }
        public String getUrl() {
            return url;
        }

        public void setKey(String key) {
            this.key = key;
        }
        public String getKey() {
            return key;
        }

    }

    public class Topic_info {

        private int id;
        private String cover_image_url;
        private String vertical_image_url;
        private String square_image_url;
        private String title;
        private String description;
        private List<String> tags;
        private User user;
        private String signing_status;
        private boolean is_free;
        private String update_remind;
        private boolean is_favourite;
        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setCover_image_url(String cover_image_url) {
            this.cover_image_url = cover_image_url;
        }
        public String getCover_image_url() {
            return cover_image_url;
        }

        public void setVertical_image_url(String vertical_image_url) {
            this.vertical_image_url = vertical_image_url;
        }
        public String getVertical_image_url() {
            return vertical_image_url;
        }

        public void setSquare_image_url(String square_image_url) {
            this.square_image_url = square_image_url;
        }
        public String getSquare_image_url() {
            return square_image_url;
        }

        public void setTitle(String title) {
            this.title = title;
        }
        public String getTitle() {
            return title;
        }

        public void setDescription(String description) {
            this.description = description;
        }
        public String getDescription() {
            return description;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }
        public List<String> getTags() {
            return tags;
        }

        public void setUser(User user) {
            this.user = user;
        }
        public User getUser() {
            return user;
        }

        public void setSigning_status(String signing_status) {
            this.signing_status = signing_status;
        }
        public String getSigning_status() {
            return signing_status;
        }

        public void setIs_free(boolean is_free) {
            this.is_free = is_free;
        }
        public boolean getIs_free() {
            return is_free;
        }

        public void setUpdate_remind(String update_remind) {
            this.update_remind = update_remind;
        }
        public String getUpdate_remind() {
            return update_remind;
        }

        public void setIs_favourite(boolean is_favourite) {
            this.is_favourite = is_favourite;
        }
        public boolean getIs_favourite() {
            return is_favourite;
        }

    }
    public class User {

        private long user_id;
        private String nickname;
        private String avatar;
        public void setUser_id(long user_id) {
            this.user_id = user_id;
        }
        public long getUser_id() {
            return user_id;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
        public String getNickname() {
            return nickname;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
        public String getAvatar() {
            return avatar;
        }

    }

    public class Next_comic_info {

        private String next_comic_id;
        private int next_status;
        public void setNext_comic_id(String next_comic_id) {
            this.next_comic_id = next_comic_id;
        }
        public String getNext_comic_id() {
            return next_comic_id;
        }

        public void setNext_status(int next_status) {
            this.next_status = next_status;
        }
        public int getNext_status() {
            return next_status;
        }

    }

    public class Recommend_topics {

        private int id;
        private String vertical_image_url;
        private String title;
        private String description;
        private List<String> tags;
        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setVertical_image_url(String vertical_image_url) {
            this.vertical_image_url = vertical_image_url;
        }
        public String getVertical_image_url() {
            return vertical_image_url;
        }

        public void setTitle(String title) {
            this.title = title;
        }
        public String getTitle() {
            return title;
        }

        public void setDescription(String description) {
            this.description = description;
        }
        public String getDescription() {
            return description;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }
        public List<String> getTags() {
            return tags;
        }

    }

    public class Share_info {

        private String title;
        private String content;
        private String action;
        public void setTitle(String title) {
            this.title = title;
        }
        public String getTitle() {
            return title;
        }

        public void setContent(String content) {
            this.content = content;
        }
        public String getContent() {
            return content;
        }

        public void setAction(String action) {
            this.action = action;
        }
        public String getAction() {
            return action;
        }

    }

    public class Data {

        private Comic_info comic_info;
        private Topic_info topic_info;
        private String previous_comic_id;
        private Next_comic_info next_comic_info;
        private List<Recommend_topics> recommend_topics;
        private String source;
        private Share_info share_info;
        private boolean logged_in;
        private String comic_auth;
        public void setComic_info(Comic_info comic_info) {
            this.comic_info = comic_info;
        }
        public Comic_info getComic_info() {
            return comic_info;
        }

        public void setTopic_info(Topic_info topic_info) {
            this.topic_info = topic_info;
        }
        public Topic_info getTopic_info() {
            return topic_info;
        }

        public void setPrevious_comic_id(String previous_comic_id) {
            this.previous_comic_id = previous_comic_id;
        }
        public String getPrevious_comic_id() {
            return previous_comic_id;
        }

        public void setNext_comic_info(Next_comic_info next_comic_info) {
            this.next_comic_info = next_comic_info;
        }
        public Next_comic_info getNext_comic_info() {
            return next_comic_info;
        }

        public void setRecommend_topics(List<Recommend_topics> recommend_topics) {
            this.recommend_topics = recommend_topics;
        }
        public List<Recommend_topics> getRecommend_topics() {
            return recommend_topics;
        }

        public void setSource(String source) {
            this.source = source;
        }
        public String getSource() {
            return source;
        }

        public void setShare_info(Share_info share_info) {
            this.share_info = share_info;
        }
        public Share_info getShare_info() {
            return share_info;
        }

        public void setLogged_in(boolean logged_in) {
            this.logged_in = logged_in;
        }
        public boolean getLogged_in() {
            return logged_in;
        }

        public void setComic_auth(String comic_auth) {
            this.comic_auth = comic_auth;
        }
        public String getComic_auth() {
            return comic_auth;
        }

    }
}
