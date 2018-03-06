package wlhwh.example.com.autodrive;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import wlhwh.example.com.autodrive.base.BaseActivity;
import wlhwh.example.com.autodrive.ui.login.LoginActivity;

/**
 * Created by WLHWH on 2017/5/7.
 */

public class LaunchActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        RxPermissions rxPermissions = new RxPermissions(LaunchActivity.this);
        rxPermissions
                .requestEach(
                        //这里申请了几组权限
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new Consumer<Permission>()  {
                    @Override
                    public void accept(@NonNull Permission permission) throws Exception {
                        if (permission.granted) {
                            //同意后跳转
                            LaunchActivity.this.startActivity(new Intent(LaunchActivity.this, LoginActivity.class));
                            LaunchActivity.this.finish();
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            Log.d("LaunchActivity", permission.name + " is denied. More info should be provided.");
                        }else {
                            //不同意，给提示
                            Toast.makeText(LaunchActivity.this, "请同意软件的权限，才能继续提供服务", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}
