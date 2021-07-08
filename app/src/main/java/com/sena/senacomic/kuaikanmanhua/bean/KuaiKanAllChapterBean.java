package com.sena.senacomic.kuaikanmanhua.bean;

import java.util.List;

/**
 * Created by camdora on 18-1-16.
 */

public class KuaiKanAllChapterBean {

   private int code;
   private String message;
   private Data data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
       private List<Comic_records> comic_records;

        public List<Comic_records> getComic_records() {
            return comic_records;
        }

        public void setComic_records(List<Comic_records> comic_records) {
            this.comic_records = comic_records;
        }

        public class Comic_records {
           private long id;
           private int read_count;
           private int has_read;
           private boolean continue_read_comic;

           public long getId() {
               return id;
           }

           public void setId(long id) {
               this.id = id;
           }

           public int getRead_count() {
               return read_count;
           }

           public void setRead_count(int read_count) {
               this.read_count = read_count;
           }

           public int getHas_read() {
               return has_read;
           }

           public void setHas_read(int has_read) {
               this.has_read = has_read;
           }

           public boolean isContinue_read_comic() {
               return continue_read_comic;
           }

           public void setContinue_read_comic(boolean continue_read_comic) {
               this.continue_read_comic = continue_read_comic;
           }
       }
   }

}
