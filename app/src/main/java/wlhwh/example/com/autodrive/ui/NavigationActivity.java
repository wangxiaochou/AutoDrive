package wlhwh.example.com.autodrive.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import cn.bmob.v3.BmobUser;
import wlhwh.example.com.autodrive.base.BaseActivity;
import wlhwh.example.com.autodrive.R;
import wlhwh.example.com.autodrive.leftnavigation.MoneyActivity;
import wlhwh.example.com.autodrive.model.User;
import wlhwh.example.com.autodrive.ui.car.Car_fragment;
import wlhwh.example.com.autodrive.ui.login.LoginActivity;
import wlhwh.example.com.autodrive.ui.map.Map_fragment;
import wlhwh.example.com.autodrive.ui.music.Music_fragment;
import wlhwh.example.com.autodrive.ui.weizhang.ChaxunActivity;

/**
 * Created by WLHWH on 2017/3/18.
 */

public class NavigationActivity extends BaseActivity implements View.OnClickListener{

    //进入之后显示的进度条
    private ProgressDialog Reading_dialog;

    //这是顶部的控件
    private Toolbar toolbar;
    private DrawerLayout mDLayout;
    private NavigationView mNView;
    private View nav_head_view;
    private ImageView mIView_4;
    private TextView mTView_1;
    private TextView mTView_2;
    //这是底部的三个导航控件
    private LinearLayout mLLayout_1;
    private LinearLayout mLLayout_2;
    private LinearLayout mLLayout_3;
    private ImageView mIView_1;
    private ImageView mIView_2;
    private ImageView mIView_3;
    private HashMap<Integer, Fragment> fragments;
    private int tabIndex=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_activity);
        initup();
        initbottom();
    }

    //这是顶部逻辑
    private void initup(){
        //找到顶部toolbar
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //找到DrawerLayout,设置顶部的按钮
        mDLayout = (DrawerLayout)findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDLayout.setDrawerListener(toggle);
        toggle.syncState();

        //找到navigationview
        mNView = (NavigationView)findViewById(R.id.nav_view);
        //左侧点击跳转
        initleft_nav();
        //navigationview头部的布局，以及逻辑
        initheader_nav();

        //模拟读取数据
        reading_data();
    }


    //模拟读取数据
    private void reading_data(){
        Reading_dialog = new ProgressDialog(this);
        Reading_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Reading_dialog.setTitle("正在读取行车电脑数据");
        Reading_dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(5000);
                    Reading_dialog.cancel();
                    //将Looper.prepare();和Looper.loop();把Toast包裹起来，就可以解决问题，具体原因有待考究
                    Looper.prepare();
                    Toast.makeText(NavigationActivity.this, "读取数据成功，欢迎使用", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }



    //navigationview头部的布局，以及逻辑
    private void initheader_nav(){
        //找到navigationview头部的布局
        //注意这里使用的inflateHeaderView()方法，以及后面找到整个headview里面控件的方法
        nav_head_view = mNView.inflateHeaderView(R.layout.nav_header);
        mTView_1 = (TextView)nav_head_view.findViewById(R.id.nav_header_name);
        mTView_2 = (TextView)nav_head_view.findViewById(R.id.nav_header_phone);
        mIView_4 = (ImageView)nav_head_view.findViewById(R.id.nav_header_avatar);

        //逻辑部分
        User localuser = BmobUser.getCurrentUser(User.class);
        String name = (String)BmobUser.getObjectByKey("username");
        String phone = (String)BmobUser.getObjectByKey("mobilePhoneNumber");
        if (localuser != null){
            mTView_1.setText(name);
            mTView_2.setText(phone);
        }else{
            Log.d("NavigationActivity", "" + name+phone);
        }
    }



    //这是侧边栏点击跳转到其他不同页面的逻辑
    private void initleft_nav(){
        mNView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_money){
                    Intent intent_money = new Intent(NavigationActivity.this, MoneyActivity.class);
                    startActivity(intent_money);
                    mDLayout.closeDrawers();
                }else if (id == R.id.nav_menu_offline){
                    AlertDialog.Builder exit_dialog = new AlertDialog.Builder(NavigationActivity.this);
                    exit_dialog.setTitle("退出");
                    exit_dialog.setMessage("当前退出会清除所有用户数据，请谨慎");
                    exit_dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent_offline = new Intent(NavigationActivity.this, LoginActivity.class);
                            BmobUser.logOut();
                            startActivity(intent_offline);
                        }
                    });
                    exit_dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    exit_dialog.create();
                    exit_dialog.show();
                    mDLayout.closeDrawers();
                }else if (id == R.id.nav_menu_chaxun){
                    Intent intent_chaxun = new Intent(NavigationActivity.this, ChaxunActivity.class);
                    startActivity(intent_chaxun);
                    mDLayout.closeDrawers();
                }
                return true;
            }
        });
    }



    //底部导航栏逻辑部分
    private void initbottom(){
        //这是底部控件
        fragments = new HashMap<Integer,Fragment>();
        //fragments = new SparseArray<Fragment>();
        showFragment(0);

        mIView_1 = (ImageView)findViewById(R.id.home);
        mIView_2 = (ImageView)findViewById(R.id.map);
        mIView_3 = (ImageView)findViewById(R.id.music);
        mLLayout_1 = (LinearLayout)findViewById(R.id.layout_1);
        mLLayout_2 = (LinearLayout)findViewById(R.id.layout_2);
        mLLayout_3 = (LinearLayout)findViewById(R.id.layout_3);

        mLLayout_1.setOnClickListener(this);
        mLLayout_2.setOnClickListener(this);
        mLLayout_3.setOnClickListener(this);

        mIView_1.setImageResource(R.drawable.home_change);
        mIView_2.setImageResource(R.drawable.map_normal);
        mIView_3.setImageResource(R.drawable.music_normal);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_1:
                mIView_1.setImageResource(R.drawable.home_change);
                mIView_2.setImageResource(R.drawable.map_normal);
                mIView_3.setImageResource(R.drawable.music_normal);
                showFragment(0);
                toolbar.setTitle("Car");
                break;
            case R.id.layout_2:
                mIView_2.setImageResource(R.drawable.map_change);
                mIView_1.setImageResource(R.drawable.home_normal);
                mIView_3.setImageResource(R.drawable.music_normal);
                showFragment(1);
                toolbar.setTitle("Map");
                break;
            case R.id.layout_3:
                mIView_3.setImageResource(R.drawable.music_change);
                mIView_1.setImageResource(R.drawable.home_normal);
                mIView_2.setImageResource(R.drawable.map_normal);
                showFragment(2);
                toolbar.setTitle("Music");
                break;
        }
    }

    //展示不同fragment的方法,属于底部导航栏的逻辑
    private void showFragment(int tag){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = fragments.get(Integer.valueOf(tabIndex));
        if (fragment!=null){
            transaction.hide(fragment);
        }
        Fragment newfragment = fragments.get(Integer.valueOf(tag));
        if (newfragment==null){
            switch(tag){
                case 0:
                    newfragment = new Car_fragment();
                    break;
                case 1:
                    newfragment = new Map_fragment();
                    break;
                case 2:
                    newfragment = new Music_fragment();
                    break;
            }
            transaction.add(R.id.content,newfragment);
            fragments.put(Integer.valueOf(tag),newfragment);
        }
        transaction.show(newfragment);
        transaction.commit();
        tabIndex = tag;
    }

    //这里暂时不管，与其他逻辑不交互，但不能删除
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                mDLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }


    //退出程序
    public boolean onKeyDown(int keyCode,KeyEvent event){
        //如果点击了返回键
        if (keyCode == event.KEYCODE_BACK){
            //那么程序退出，这是退出的方法
            exit();
        }
        return true;
    }
    //退出方法
    private void exit(){
        //移除所有的activity的方法
        removeAllActivity();
    }
}
