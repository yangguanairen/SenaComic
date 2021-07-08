package com.sena.senacomic.dmzj.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sena.senacomic.R;
import com.sena.senacomic.databinding.ActivityDmzjInfoBinding;
import com.sena.senacomic.dmzj.adapter.DMZJInfoAdapter;
import com.sena.senacomic.dmzj.bean.DMZJInfoBean;
import com.sena.senacomic.favorite.FavoriteBean;
import com.sena.senacomic.history.HistoryBean;
import com.sena.senacomic.util.AppConstants;
import com.sena.senacomic.util.GlideUtil;
import com.sena.senacomic.util.LogUtil;
import com.sena.senacomic.util.RealmHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DMZJInfoActivity extends AppCompatActivity {


    private ActivityDmzjInfoBinding binding;
    private DMZJInfoAdapter adapter;
    private String comicId;
    private boolean isError = false;

    private String imageUrl;
    private String title;
    private String authorName;
    private String classify1 = "";  // 必须设置，不然会显示null
    private String classify2 = "";
    private String updateTime;
    private String description;

    private String jsonData = "[{\"title\": \"\", \"data\":[{}]}]"; // 设置默认空数据，用于页面出错时传递，参数不得为null
//    private String jsonData = "[{\"title\":\"\\u8fde\\u8f7d\",\"data\":[{\"id\":113148,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d33\",\"chapter_order\":870,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":110714,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d32\",\"chapter_order\":860,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":108483,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d31\",\"chapter_order\":850,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":106155,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d30\",\"chapter_order\":840,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":105105,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d29\",\"chapter_order\":830,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":103389,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d28\",\"chapter_order\":820,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":101926,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d27\",\"chapter_order\":810,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":100411,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d26\",\"chapter_order\":800,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":99311,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d25\",\"chapter_order\":790,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":98168,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d24\",\"chapter_order\":780,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":95875,\"comic_id\":45854,\"chapter_name\":\"\\u5355\\u884c\\u672c3\\u5377\\u53d1\\u552e\\u544a\\u77e5\",\"chapter_order\":770,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":95498,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d23\",\"chapter_order\":760,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":94608,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d22\",\"chapter_order\":750,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":93709,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d21\",\"chapter_order\":740,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":93309,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d20\",\"chapter_order\":730,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":92767,\"comic_id\":45854,\"chapter_name\":\"\\u65b0\\u5e74\\u7279\\u522b\\u7bc7\",\"chapter_order\":720,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":92460,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d19\",\"chapter_order\":710,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":92459,\"comic_id\":45854,\"chapter_name\":\"\\u5355\\u884c\\u672c\\u518d\\u7248\\u53ca20w\\u90e8\\u7eaa\\u5ff5\",\"chapter_order\":700,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":91391,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d18\",\"chapter_order\":690,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":90692,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d17\",\"chapter_order\":680,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":89761,\"comic_id\":45854,\"chapter_name\":\"\\u5355\\u884c\\u672c\\u544a\\u77e53\",\"chapter_order\":670,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":89430,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d16\",\"chapter_order\":660,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":88635,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d15\",\"chapter_order\":650,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":88028,\"comic_id\":45854,\"chapter_name\":\"\\u756a\\u5916\\u7bc72\",\"chapter_order\":640,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":87742,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d14\\u540e\\u7bc7\",\"chapter_order\":630,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":87335,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d14\\u524d\\u7bc7\",\"chapter_order\":620,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":86884,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d13\",\"chapter_order\":610,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":86230,\"comic_id\":45854,\"chapter_name\":\"02-10\\u8bdd\\u756a\\u5916\\u7bc7\",\"chapter_order\":510,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":86229,\"comic_id\":45854,\"chapter_name\":\"01-\\u6d3b\\u52a8\\u756a\\u5916\",\"chapter_order\":500,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":85922,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d12\",\"chapter_order\":490,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":85110,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d11\",\"chapter_order\":480,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":84299,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d10\",\"chapter_order\":470,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":84162,\"comic_id\":45854,\"chapter_name\":\"\\u5355\\u884c\\u672c\\u53d1\\u552e\\u7eaa\\u5ff5\",\"chapter_order\":460,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":83760,\"comic_id\":45854,\"chapter_name\":\"\\u756a\\u5916\\u7bc7\",\"chapter_order\":450,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":83274,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d09\",\"chapter_order\":440,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":82488,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d08\",\"chapter_order\":420,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":82098,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d07\",\"chapter_order\":410,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":81733,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d06\",\"chapter_order\":360,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":81393,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d05\",\"chapter_order\":350,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":81171,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d\\u7279\\u522b\\u7bc7\",\"chapter_order\":340,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":81170,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d04\",\"chapter_order\":330,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":81090,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d03\",\"chapter_order\":320,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":79330,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d02\",\"chapter_order\":220,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":78835,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d01\",\"chapter_order\":190,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":77609,\"comic_id\":45854,\"chapter_name\":\"\\u7b2c04\\u8bdd\\u540e\\u7bc7\",\"chapter_order\":50,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":77571,\"comic_id\":45854,\"chapter_name\":\"\\u7b2c04\\u8bdd\\u524d\\u7bc7\",\"chapter_order\":40,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":77563,\"comic_id\":45854,\"chapter_name\":\"\\u7b2c03\\u8bdd\",\"chapter_order\":30,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":77562,\"comic_id\":45854,\"chapter_name\":\"\\u7b2c02\\u8bdd\",\"chapter_order\":20,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":77495,\"comic_id\":45854,\"chapter_name\":\"\\u7b2c01\\u8bdd\",\"chapter_order\":10,\"chaptertype\":0,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0},{\"id\":99970,\"comic_id\":45854,\"chapter_name\":\"\\u7b2c01\\u5377\",\"chapter_order\":1,\"chaptertype\":1,\"title\":\"\\u8fde\\u8f7d\",\"sort\":0}]},{\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"data\":[{\"id\":86239,\"comic_id\":45854,\"chapter_name\":\"10-\\u5976\\u8336\\u6311\\u6218\",\"chapter_order\":600,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":86238,\"comic_id\":45854,\"chapter_name\":\"09-\\u91cd\\u7248\",\"chapter_order\":590,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":86237,\"comic_id\":45854,\"chapter_name\":\"08-\\u4ee4\\u548c\",\"chapter_order\":580,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":86236,\"comic_id\":45854,\"chapter_name\":\"07-\\u7279\\u5178\\u6837\\u54c1\",\"chapter_order\":570,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":86235,\"comic_id\":45854,\"chapter_name\":\"06-13wfo\",\"chapter_order\":560,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":86234,\"comic_id\":45854,\"chapter_name\":\"05-12wfo\",\"chapter_order\":550,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":86233,\"comic_id\":45854,\"chapter_name\":\"04-\\u7a81\\u7136\\u5730\\u8c6a\\u585a\",\"chapter_order\":540,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":86232,\"comic_id\":45854,\"chapter_name\":\"03-\\u63a8\\u56fe\",\"chapter_order\":530,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":86231,\"comic_id\":45854,\"chapter_name\":\"02-\\u50f5\\u5c3805-21\",\"chapter_order\":520,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":83076,\"comic_id\":45854,\"chapter_name\":\"\\u5355\\u884c\\u672c\\u544a\\u77e5\",\"chapter_order\":430,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":81737,\"comic_id\":45854,\"chapter_name\":\"\\u53cc\\u9a6c\\u5c3e\\u4e4b\\u65e5\",\"chapter_order\":400,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":81736,\"comic_id\":45854,\"chapter_name\":\"\\u5168\\u76db\\u671f\\u795e\\u6bcd\",\"chapter_order\":390,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":81735,\"comic_id\":45854,\"chapter_name\":\"\\u63a8\\u727907\",\"chapter_order\":380,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":81734,\"comic_id\":45854,\"chapter_name\":\"\\u732a\\u5e74\\u8d3a\\u56fe\",\"chapter_order\":370,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":80727,\"comic_id\":45854,\"chapter_name\":\"\\u9664\\u5915\\u63a8\\u56fe\",\"chapter_order\":310,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":80608,\"comic_id\":45854,\"chapter_name\":\"\\u5723\\u8bde\\u8d3a\\u56fe\",\"chapter_order\":300,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":80272,\"comic_id\":45854,\"chapter_name\":\"\\u7a81\\u7136\\u7684\\u81ea\\u6211\\u4ecb\\u7ecd\",\"chapter_order\":290,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":80271,\"comic_id\":45854,\"chapter_name\":\"\\u63a8\\u56fe06\",\"chapter_order\":280,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":79944,\"comic_id\":45854,\"chapter_name\":\"FANBOX-3\",\"chapter_order\":270,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":79943,\"comic_id\":45854,\"chapter_name\":\"10wfo\\u63a8\\u56fe\",\"chapter_order\":260,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":79942,\"comic_id\":45854,\"chapter_name\":\"MCA\\u520a\\u8f7d\\u63a8\\u56fe\",\"chapter_order\":250,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":79941,\"comic_id\":45854,\"chapter_name\":\"FANBOX-2\",\"chapter_order\":240,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":79940,\"comic_id\":45854,\"chapter_name\":\"\\u63a8\\u56fe05\",\"chapter_order\":230,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":78837,\"comic_id\":45854,\"chapter_name\":\"FANBOX-1\",\"chapter_order\":210,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":78836,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d\\u9884\\u544a2\",\"chapter_order\":200,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":78770,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d\\u9884\\u544a\",\"chapter_order\":180,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":78769,\"comic_id\":45854,\"chapter_name\":\"\\u4e07\\u5723\\u8282\\u8d3a\\u56fe\",\"chapter_order\":170,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":78768,\"comic_id\":45854,\"chapter_name\":\"\\u63a8\\u56fe04\",\"chapter_order\":160,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":78767,\"comic_id\":45854,\"chapter_name\":\"9wfo\\u63a8\\u56fe\",\"chapter_order\":150,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":78766,\"comic_id\":45854,\"chapter_name\":\"\\u63a8\\u56fe03\",\"chapter_order\":140,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":78013,\"comic_id\":45854,\"chapter_name\":\"\\u63a8\\u56fe02\",\"chapter_order\":130,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":77950,\"comic_id\":45854,\"chapter_name\":\"8.5wfo\\u63a8\\u56fe\",\"chapter_order\":120,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":77881,\"comic_id\":45854,\"chapter_name\":\"\\u63a8\\u56fe01\",\"chapter_order\":110,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":77782,\"comic_id\":45854,\"chapter_name\":\"\\u8fde\\u8f7d\\u51b3\\u5b9a\",\"chapter_order\":100,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":77648,\"comic_id\":45854,\"chapter_name\":\"7wfo\\u63a8\\u56fe\",\"chapter_order\":90,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":77647,\"comic_id\":45854,\"chapter_name\":\"6wfo\\u63a8\\u56fe\",\"chapter_order\":80,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":77646,\"comic_id\":45854,\"chapter_name\":\"4wfo\\u63a8\\u56fe\",\"chapter_order\":70,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12},{\"id\":77645,\"comic_id\":45854,\"chapter_name\":\"3wfo\\u63a8\\u56fe\",\"chapter_order\":60,\"chaptertype\":17,\"title\":\"\\u5176\\u4ed6\\u7cfb\\u5217\",\"sort\":12}]}]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDmzjInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        comicId = getIntent().getStringExtra(AppConstants.COMIC_ID);
        binding.recyclerViewComicList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DMZJInfoAdapter(this);
        binding.recyclerViewComicList.setAdapter(adapter);

        initRxJava();

        initTextView();


    }

    private void initRxJava() {
        Observable<List<DMZJInfoBean>> observable = Observable.create(new ObservableOnSubscribe<List<DMZJInfoBean>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<DMZJInfoBean>> emitter) throws Throwable {
                Document doc = null;
                try {
                    LogUtil.d("DMZJInfoUrl: " + getString(R.string.dmzj_info_url, comicId));
                    doc = Jsoup.connect(getString(R.string.dmzj_info_url, comicId)).get();
                    Element introduct = doc.getElementsByClass("Introduct_Sub autoHeight").get(0);
                    description = doc.getElementsByClass("txtDesc autoHeight").text();
                    // 获取封面和小说名称
                    Element pic = introduct.getElementsByClass("pic").get(0);
                    imageUrl = pic.getElementsByTag("img").attr("src");
                    title = pic.getElementsByTag("img").attr("title");
                    // 获取作者名称，分类，最后更新时间
                    Elements sub_r = doc.getElementsByClass("sub_r").get(0).getElementsByClass("txtItme");
                    authorName = sub_r.get(0).getElementsByTag("a").text();
                    Elements pd1 = sub_r.get(1).getElementsByClass("pd");
                    for (int i = 0; i < pd1.size(); i++) {
                        classify1 += pd1.get(i).text() + "  ";
                    }
                    Elements pd2 = sub_r.get(2).getElementsByClass("pd");
                    for (int i = 0; i < pd2.size(); i++) {
                        classify2 += pd2.get(i).text() + "  ";
                    }
                    updateTime = sub_r.get(3).getElementsByClass("date").text();
                    // 获取comic List的jsonData
                    Elements javascript = doc.getElementsByTag("script");
                    String data = "";
                    for (Element e : javascript) {
                        if (e.toString().contains("initIntroData")) {
                            data = e.toString();
                            break;
                        }
                    }
                    String gsonData = "";
                    for (String s : data.split("\n")) {
                        if (s.contains("initIntroData")) {
                            gsonData = s;
                            break;
                        }
                    }
                    int startIndex = gsonData.indexOf("[");
                    int endIndex = gsonData.lastIndexOf("]");
                    jsonData = gsonData.substring(startIndex, endIndex + 1);
                } catch (Exception e) {

                }
                // 这个很重要,用于history和favorite
                adapter.setOtherData(imageUrl, authorName, title);
                // 表示页面出错，更改isError值，加载默认出错页面
                if (doc.body().text().equals(AppConstants.DMZJ_INFO_ERROR)) {
                    isError = true;
                }
                emitter.onNext(new Gson().fromJson(jsonData, new TypeToken<List<DMZJInfoBean>>() {
                }.getType()));
            }
        });

        Consumer<List<DMZJInfoBean>> observer = new Consumer<List<DMZJInfoBean>>() {
            @Override
            public void accept(List<DMZJInfoBean> beans) throws Throwable {
                if (isError) {
                    binding.context.setVisibility(View.GONE);
                    binding.tvNoData.setVisibility(View.VISIBLE);
                } else {
                    adapter.setList(beans);
                    Glide.with(DMZJInfoActivity.this)
                            .load(GlideUtil.getInstance(DMZJInfoActivity.this).addDMZJCookie(imageUrl))
                            .into(binding.comicCoverImage);
                    binding.comicTitle.setText(title);
                    binding.comicAuthor.setText(authorName);
                    binding.comicClassify1.setText(classify1);
                    binding.comicClassify2.setText(classify2);
                    binding.comicUpdateTime.setText(updateTime);
                    binding.comicDescription.setText(description);
                    // 订阅和继续观看设置可点击，因为数据为异步获取
                    binding.subscribe.setClickable(true);
                    binding.continueRead.setClickable(true);
                }
            }
        };

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private void initTextView() {

        getWindow().setStatusBarColor(Color.parseColor("#39adff"));


        String id = getString(R.string.realm_id, AppConstants.DMZJ_ORIGIN, comicId);
        // 设置订阅按钮初始状态，数据库有数据为已订阅
        FavoriteBean bean = RealmHelper.queryFavorite(getApplicationContext(), id);
        binding.subscribe.setText(bean == null ? "订阅漫画" : "取消订阅");
        binding.subscribe.setContentDescription(bean == null ? "0" : "1");

        // 关于if条件，这里还有一种方法，查询favorite表是否有数据，Realm.queryFavorite(getApplicationContext(), id)
        // null值表示未订阅，非null值表示已订阅
        binding.subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("0".equals(v.getContentDescription())) {
                    // 查看历史记录是否有该漫画，有则把最后一次阅读的章节
                    HistoryBean tmp = RealmHelper.queryHistory(DMZJInfoActivity.this, id);
                    FavoriteBean favoriteBean = new FavoriteBean(id, AppConstants.DMZJ_ORIGIN, comicId, imageUrl, title, authorName,
                            tmp == null ? "" : tmp.getChapterId(), tmp == null ? "未观看" : tmp.getChapterName(), new Date());
                    RealmHelper.createFavorite(getApplicationContext(), favoriteBean);
                    ((TextView) v).setText("取消订阅");
                    ((TextView) v).setContentDescription("1");
                } else if ("1".equals(v.getContentDescription())) {
                    RealmHelper.deleteFavorite(getApplicationContext(), id);
                    ((TextView) v).setText("订阅漫画");
                    ((TextView) v).setContentDescription("0");
                }
            }
        });

        // 跳转，View页面，传递漫画id和章节id
        // 跳转至用户最新阅读章节，不支持无阅读记录跳转至第一章
        binding.continueRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryBean historyBean = RealmHelper.queryHistory(getApplicationContext(), id);
                if (historyBean != null) {
                    Intent intent = new Intent(DMZJInfoActivity.this, DMZJViewActivity.class);
                    intent.putExtra(AppConstants.CHAPTER_ID, historyBean.getChapterId());
                    intent.putExtra(AppConstants.COMIC_ID, historyBean.getComicId());
                    startActivity(intent);
                }
            }
        });

        // 简单实现TextView折叠效果
        binding.comicDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.comicDescription.getMaxLines() == 4) {
                    binding.comicDescription.setMaxLines(999);
                } else {
                    binding.comicDescription.setMaxLines(4);
                }
            }
        });


    }
}