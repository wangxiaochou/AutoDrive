package wlhwh.example.com.autodrive.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import wlhwh.example.com.autodrive.base.BaseActivity;
import wlhwh.example.com.autodrive.R;
import wlhwh.example.com.autodrive.model.User;
import wlhwh.example.com.autodrive.ui.NavigationActivity;

public class LoginActivity extends BaseActivity {

    private TextInputEditText et_Account;
    private TextInputEditText et_Password;
    private AppCompatButton btn_Login;
    private TextView tv_Signup;

    private SharedPreferences mSharedPF = null;

    private Matcher matcher;
    private Pattern pattern = Pattern.compile(NAME_PATTERN);
    private static final String NAME_PATTERN = "[\\u4E00-\\u9FA5]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        //初始化Bmob
        Bmob.initialize(this, "ae5732a5beb2d4c3f859fa8c78538013");
        //自动登录
        auto();
        //跳转到注册
        signup();
        //找到控件
        et_Account = (TextInputEditText) findViewById(R.id.account);
        et_Password = (TextInputEditText) findViewById(R.id.password);
        btn_Login = (AppCompatButton) findViewById(R.id.login_btn);
        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    //跳转到注册
    private void signup(){
        tv_Signup = (TextView) findViewById(R.id.sign_up);
        //注册点击事件
        tv_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    //自动登录
    private void auto(){
        //自动登录
        User localuser = BmobUser.getCurrentUser(User.class);
        if (localuser != null){
            Log.d("LoginActivity", "用户信息：" + localuser.getUsername());
            Intent intent = new Intent(LoginActivity.this,NavigationActivity.class);
            startActivity(intent);
        }
    }

    private void login(){
        //获得输入
        String name = et_Account.getText().toString();
        String password = et_Password.getText().toString();
        //判断输入的手机号码和密码是否正确且符合标准
        if (!validatePhone(name)) {
            et_Account.setError("enter a correct name");
        } else if (!validatePassword(password)) {
            et_Password.setError("enter a correct password");
        } else {
            et_Account.setError(null);
            et_Password.setError(null);
        }
        //登录
        User user = new User();
        user.setUsername(name);
        user.setPassword(password);
        user.login(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null){
                    Intent intent = new Intent(LoginActivity.this,NavigationActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this, "登录失败，请重新登录", Toast.LENGTH_SHORT).show();
                    Log.d("LoginActivity", ""+ e );
                }
            }
        });

    }
    //手机号码和密码的判断
    public boolean validatePhone(String name) {
        matcher = pattern.matcher(name);
        return matcher.matches();
    }
    public boolean validatePassword(String password) {
        return password.length() > 5 && password.length() < 13;
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