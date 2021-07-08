package com.sena.senacomic;

import com.sena.senacomic.dmzj.bean.DMZJClassifyBean;
import com.sena.senacomic.kuaikanmanhua.bean.KuaiKanBrowserBean;
import com.sena.senacomic.kuaikanmanhua.bean.KuaiKanComicListBean;
import com.sena.senacomic.kuaikanmanhua.bean.KuaiKanAllChapterBean;
import com.sena.senacomic.kuaikanmanhua.bean.KuaiKanSearchBean;
import com.sena.senacomic.mangabz.bean.MangabzListBean;

import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {

    String data = "";

    /***************快看漫画***************/
    // 主页面，展示分类
    @GET("search/mini/topic/multi_filter")
    Observable<KuaiKanComicListBean> refreshKuaiKanComicList(
            @Query("tag_id") String tagId,
            @Query("update_status") String updateStatus,
            @Query("sort") String sort,

            @Query("page") int page,
            @Query("size") int size,
            @Query("pag_status") int payStatus,
            @Query("fav_filter") int favFilter,
            @Query("gender") int gender
    );

    // comic所有章节
    @GET("mini/v1/comic/mkuaikan/topic/read_record")
    Observable<KuaiKanAllChapterBean> refreshKuaiKanAllChapter(
           @Query("topic_id") String id
    );

    // 一章漫画
    @GET("v2/mweb/comic/{id}")
    Observable<KuaiKanBrowserBean> refreshKuaiKanBrowser(
            @Path("id") String id
    );

    // 搜索
    @GET("search/mini/suggest")
    Observable<KuaiKanSearchBean> refreshKuaiKanSearch(
            @Query("q") String q,
            @Query("size") int size
    );
    /***************快看漫画***************/

    /***************动漫之家***************/
    // 获取分类结果
//    https://m.dmzj.com/classify/0-0-0-0-0-0.json
    @GET("classify/{id}.json")
    Observable<List<DMZJClassifyBean>> refreshDMZJClassify(
            @Path("id") String id
    );
    /***************动漫之家***************/

    /***************Mangabz***************/

//    @POST("http://www.mangabz.com/manga-list-31-0-10{id}/mangabz.ashx")

    @Multipart
    @POST("manga-list{id}/mangabz.ashx")
    Observable<MangabzListBean> refreshMangabzList(
            @Path("id") String id,
            @PartMap Map<String, RequestBody> requestBodyMap
            );


    /***************Mangabz***************/







}
