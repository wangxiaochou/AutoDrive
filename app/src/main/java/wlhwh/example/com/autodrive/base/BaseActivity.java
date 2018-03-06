package wlhwh.example.com.autodrive.base;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import wlhwh.example.com.autodrive.ui.login.LoginActivity;

/**
 * Created by WLHWH on 2017/4/13.
 */

public class BaseActivity extends AppCompatActivity{

    //private ForceOfflineReceiver receiver;

    private MyApplication app;
    private BaseActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BaseActivity", getClass().getSimpleName());


        if (app == null){
            //得到application对象
            app = (MyApplication)getApplication();
        }
        //把当前上下文对象赋值给baseActivity
        context = this;
        //调用添加方法
        addActivity();

        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            //View decorView = getWindow().getDecorView();
            //int option = View.SYSTEM_UI_FLAG_FULLSCREEN| View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            //decorView.setSystemUiVisibility(option);
            //getWindow().setStatusBarColor(Color.TRANSPARENT);
            //getWindow().setNavigationBarColor(Color.TRANSPARENT);
        //}
    }

    //添加activity方法
    public void addActivity(){
        //调用MyApplication的添加activity方法
        app.addActivity(context);
    }

    //销毁当前activity方法
    public void removeActivity(){
        //调用MyApplication的销毁当前activity的方法
        app.removeActivity(context);
    }

    //销毁所有activity的方法
    public void removeAllActivity(){
        //调用MyApplication销毁所有activity的方法
        app.removeAllActivity();
    }

}
