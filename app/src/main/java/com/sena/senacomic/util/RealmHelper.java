package com.sena.senacomic.util;

import android.content.Context;

import com.sena.senacomic.favorite.FavoriteBean;
import com.sena.senacomic.history.HistoryBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

// Realm以及通过它操作得到的对象，都只能在创建它的线程中使用
// Realm.close()关闭后，所有的对象都不能再继续使用，
// 所以，使用Realm时，最后根据对象里的数据重新生成新的对象来使用，不能使用赋值语句，详见getAllHistory()方法
// 故，这里没有写成单例

public class RealmHelper {

    public static String REALM_NAME = "SenaRealm.realm";

    public static int REALM_VERSION = 0;


    public static Realm getRealmInstance(Context context) {
        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(REALM_NAME)
                .schemaVersion(REALM_VERSION)
                .build();

        Realm realm = null;
        try {
            realm = Realm.getInstance(config);
        } catch (Exception e) {
            LogUtil.e("数据库出错" + e.getMessage());
        }
        return realm;
    }

    public static List<HistoryBean> getAllHistory(Context context) {
        Realm realm = getRealmInstance(context);
        if (realm == null) {
            return null;
        }
        List<HistoryBean> list = new ArrayList<>();
        // 按时间降序, 升序为Sort.DESCENDING
        RealmResults<HistoryBean> result = realm.where(HistoryBean.class).sort("lastReadTime", null).findAll();
        // copy query出来的对象数据，方便在别的线程使用
        for (HistoryBean s : result) {
            HistoryBean tmp = new HistoryBean(s.getId(), s.getOrigin(), s.getComicId(), s.getCoverUrl(),
                    s.getComicTitle(), s.getAuthor(), s.getChapterId(), s.getChapterName(), s.getLastReadTime());
            list.add(tmp);
        }
        realm.close();
        return list;
    }

    // 在Info页面点击章节
    public static boolean createOrUpdateHistory(Context context, HistoryBean historyBean) {
        Realm realm = getRealmInstance(context);
        if (realm == null) {
            return false;
        }
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(historyBean);
            }
        });
        realm.close();
        return true;
    }

    // 在View页面点击上一章/下一章.
    public static boolean updateHistory(Context context, String id, String chapterId, String chapterName, Date date) {
        Realm realm = getRealmInstance(context);
        if (realm == null) {
            return false;
        }
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                HistoryBean historyBean = realm.where(HistoryBean.class).equalTo("id", id).findFirst();
                historyBean.setChapterId(chapterId);
                historyBean.setChapterName(chapterName);
                historyBean.setLastReadTime(date);
            }
        });
        realm.close();
        return true;
    }

    // 无法在Search页面获取作者名称，直接createOrUpdate导致作者变成null
    public static boolean updateHistoryForMangabzSearch(Context context, HistoryBean historyBean) {
        Realm realm = getRealmInstance(context);
        if (realm == null) {
            return false;
        }
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                HistoryBean oldHistory = realm.where(HistoryBean.class).equalTo("id", historyBean.getId()).findFirst();
                oldHistory.setChapterId(historyBean.getChapterId());
                oldHistory.setChapterName(historyBean.getChapterName());
                oldHistory.setLastReadTime(historyBean.getLastReadTime());
            }
        });
        realm.close();
        return true;
    }

    public static boolean deleteHistory(Context context, String id) {
        Realm realm = getRealmInstance(context);
        if (realm == null) {
            return false;
        }
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<HistoryBean> results = realm.where(HistoryBean.class).equalTo("id", id).findAll();
                results.deleteAllFromRealm();
            }
        });
        realm.close();
        return true;
    }

    public static boolean deleteAllHistory(Context context) {
        Realm realm = getRealmInstance(context);
        if (realm == null) {
            return false;
        }
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<HistoryBean> results = realm.where(HistoryBean.class).findAll();
                results.deleteAllFromRealm();
            }
        });
        realm.close();
        return true;

    }

    public static List<FavoriteBean> getAllFavorite(Context context) {
        Realm realm = getRealmInstance(context);
        if (realm == null) {
            return null;
        }
        List<FavoriteBean> list = new ArrayList<>();
        RealmResults<FavoriteBean> results = realm.where(FavoriteBean.class).sort("date").findAll();
        for (FavoriteBean bean: results) {
            FavoriteBean tmp = new FavoriteBean(bean.getId(),bean.getOrigin(), bean.getComicId(), bean.getCoverUrl(), bean.getTitle(),
                    bean.getAuthor(), bean.getLastChapterId(), bean.getLastChapterName(), bean.getDate());
            list.add(tmp);
        }
        realm.close();
        return list;
    }

    public static boolean createFavorite(Context context, FavoriteBean bean) {
        Realm realm = getRealmInstance(context);
        if (realm == null) {
            return false;
        }
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(bean);
            }
        });

        realm.close();
        return true;
    }

    public static boolean deleteFavorite(Context context, String id) {
        Realm realm = getRealmInstance(context);
        if (realm == null) {
            return false;
        }
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults results = realm.where(FavoriteBean.class).equalTo("id", id).findAll();
                results.deleteAllFromRealm();
            }
        });
        realm.close();
        return true;
    }

    public static boolean updateFavorite(Context context, String id, String chapterId, String chapterName, Date date) {
        Realm realm = getRealmInstance(context);
        if (realm == null) {
            return false;
        }
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
               FavoriteBean oldFavorite = realm.where(FavoriteBean.class).equalTo("id", id).findFirst();
                oldFavorite.setLastChapterId(chapterId);
                oldFavorite.setLastChapterName(chapterName);
                oldFavorite.setDate(date);
            }
        });
        realm.close();
        return true;
    }

    public static HistoryBean queryHistory(Context context, String id) {
        Realm realm = getRealmInstance(context);
        if (realm == null) {
            return null;
        }
        HistoryBean historyBean = null;
        HistoryBean result = realm.where(HistoryBean.class).equalTo("id", id).findFirst();
        if (result != null) {
            historyBean = new HistoryBean(result.getId(), result.getOrigin(), result.getComicId(), realm.getPath(), result.getComicTitle(),
                    result.getAuthor(), result.getChapterId(), result.getChapterName(), result.getLastReadTime());
        }
        realm.close();
        return historyBean;
    }

    public static FavoriteBean queryFavorite(Context context, String id) {
        Realm realm = getRealmInstance(context);
        if (realm == null) {
            return null;
        }
        FavoriteBean favoriteBean = null;
        FavoriteBean result = realm.where(FavoriteBean.class).equalTo("id", id).findFirst();
        if (result != null) {
            favoriteBean = new FavoriteBean(result.getId(), result.getOrigin(), result.getComicId(), realm.getPath(),
                    result.getTitle(), result.getAuthor(), result.getLastChapterId(), result.getLastChapterName(), result.getDate());
        }
        realm.close();
        return favoriteBean;
    }

    public static List<FavoriteBean> queryFavoriteByOrigin(Context context, int origin) {
        Realm realm = getRealmInstance(context);
        if (realm == null) {
            LogUtil.d("realm is null");
            return null;
        }
        List<FavoriteBean>  list = new ArrayList<>();
        RealmResults<FavoriteBean> results = realm.where(FavoriteBean.class).equalTo("origin", origin).sort("date").findAll();
        for (FavoriteBean f: results) {
            list.add(new FavoriteBean(f.getId(), f.getOrigin(), f.getComicId(), f.getCoverUrl(), f.getTitle(), f.getAuthor(), f.getLastChapterId(), f.getLastChapterName(), f.getDate()));
        }
        LogUtil.e("querySize: " + list.size());
        realm.close();
        return list;
    }
}
