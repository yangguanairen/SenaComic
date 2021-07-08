package com.sena.senacomic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.sena.senacomic.dmzj.fragment.DMZJClassifyFragment;
import com.sena.senacomic.dmzj.view.DMZJSearchActivity;
import com.sena.senacomic.favorite.FavoriteActivity;
import com.sena.senacomic.guide.GuideActivity;
import com.sena.senacomic.history.HistoryActivity;
import com.sena.senacomic.kuaikanmanhua.fragment.KuaiKanComicListFragment;
import com.sena.senacomic.kuaikanmanhua.view.KuaiKanSearchActivity;
import com.sena.senacomic.mangabz.fragment.MangabzListFragment;
import com.sena.senacomic.mangabz.view.MangabzSearchActivity;
import com.sena.senacomic.util.LogUtil;

import org.jetbrains.annotations.NotNull;

public class HomeActivity extends AppCompatActivity {


    private FragmentManager manager;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initLayout();

        manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.frame_home);
        if (fragment == null) {
            fragment = new DMZJClassifyFragment(this);
            manager.beginTransaction().replace(R.id.frame_home, fragment).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.toolbar_search:
                Intent intent = new Intent();
                for (Fragment f: manager.getFragments()) {
                    LogUtil.e(f.getClass().toString() + "");
                    if (f.getClass().equals(KuaiKanComicListFragment.class)) {
                        intent.setClass(this, KuaiKanSearchActivity.class);break;
                    } else if (f.getClass().equals(DMZJClassifyFragment.class) ) {
                        LogUtil.e("intent dmzj");
                        intent.setClass(this, DMZJSearchActivity.class);break;
                    } else if (f.getClass().equals(MangabzListFragment.class)) {
                        LogUtil.e("intent mangabz");
                        intent.setClass(this, MangabzSearchActivity.class);break;
                    } else {

                    }
                }
                startActivity(intent);
                break;
            default: break;
        }



        return true;
    }

    private void initLayout() {
        toolbar = findViewById(R.id.toolbar);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);
        // 和toolbar绑定
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, 0, 0);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Window window = getWindow();
//        View decorView = window.getDecorView();
//        int option = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//        decorView.setSystemUiVisibility(option);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.setStatusBarColor(Color.TRANSPARENT);

        // 设置状态栏颜色
        window.setStatusBarColor(Color.parseColor("#39adff"));

        // 获取头布局
        View headerView = navigationView.getHeaderView(0);
        // 寻找头布局里的控件
        ImageView imageView = headerView.findViewById(R.id.iv_header);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "点击了头像", Toast.LENGTH_SHORT).show();
            }
        });
        // 寻找item里的控件,设置了actionLayout
        // MenuItem s = binding.navigationView.getMenu().findItem(R.id.item1_1);
        // 设置条目点击监听
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem menuItem) {
                Intent intent;
                switch (menuItem.getItemId()) {
                    case R.id.item_kuai_kan:
                        manager.beginTransaction()
                                .replace(R.id.frame_home, new KuaiKanComicListFragment(HomeActivity.this))
                                .commit();
                        toolbar.setTitle("快看漫画");
                        break;
                    case R.id.item_dmzj:
                        manager.beginTransaction()
                                .replace(R.id.frame_home, new DMZJClassifyFragment(HomeActivity.this))
                                .commit();
                        toolbar.setTitle("动漫之家");
                        break;
                    case R.id.item_mangabz:
                        manager.beginTransaction()
                                .replace(R.id.frame_home, new MangabzListFragment(HomeActivity.this))
                                .commit();
                        toolbar.setTitle("Mangabz");
                        break;
                    case R.id.item_history:
                        intent = new Intent(HomeActivity.this, HistoryActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_favorite:
                        intent = new Intent(HomeActivity.this, FavoriteActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_guide:
                        intent = new Intent(HomeActivity.this, GuideActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }

                drawer.closeDrawers();
                return false;
            }
        });
//         寻找脚步据里的控件
//        Button footerItemSetting = navigationView.findViewById(R.id.footer_item_setting);
//        footerItemSetting.setTextColor(Color.BLACK);

    }

}