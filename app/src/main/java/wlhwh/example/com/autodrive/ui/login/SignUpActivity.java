package wlhwh.example.com.autodrive.ui.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import wlhwh.example.com.autodrive.base.BaseActivity;
import wlhwh.example.com.autodrive.R;
import wlhwh.example.com.autodrive.model.User;

/**
 * Created by WLHWH on 2017/3/18.
 */

public class SignUpActivity extends BaseActivity{

    private TextInputEditText et_signup_name;
    private TextInputEditText et_signup_email;
    private TextInputEditText et_signup_phone;
    private TextInputEditText et_signup_password;
    private AppCompatButton btn_signup;
    private TextView tv_back_login;

    private Matcher matcher;
    private Pattern pattern_3 = Pattern.compile(NAME_PATTERN);
    private Pattern pattern_2 = Pattern.compile(EMAIL_PATTERN);
    private Pattern pattern = Pattern.compile(PHONE_PATTERN);
    private static final String PHONE_PATTERN = "^1(3[0-9]|4[57]|5[0-35-9]|7[0135678]|8[0-9])\\d{8}$";
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    private static final String NAME_PATTERN = "[\\u4E00-\\u9FA5]+";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        //初始化Bmob
        Bmob.initialize(this, "ae5732a5beb2d4c3f859fa8c78538013");
       //找到控件
        et_signup_name = (TextInputEditText)findViewById(R.id.signup_name);
        et_signup_email = (TextInputEditText)findViewById(R.id.signup_email);
        et_signup_phone = (TextInputEditText)findViewById(R.id.signup_phone);
        et_signup_password = (TextInputEditText)findViewById(R.id.signup_password);
        btn_signup = (AppCompatButton)findViewById(R.id.signup);
        tv_back_login = (TextView)findViewById(R.id.back_login);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    signup();
                }
        });

        //注册完成返回登录
        tv_back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void signup(){
        //获得输入
        String su_name = et_signup_name.getText().toString();
        String su_email = et_signup_email.getText().toString();
        String su_phone = et_signup_phone.getText().toString();
        String su_password = et_signup_password.getText().toString();
        //判断输入
        if (!validataName(su_name)) {
            et_signup_name.setError("enter correct chinese name");
        } else if (!validateEmail(su_email)) {
            et_signup_email.setError("enter correct email");
        } else if (!validatePhone(su_phone)) {
            et_signup_phone.setError("enter correct phone");
        } else if (!validatePassword(su_password)) {
            et_signup_password.setError("enter correct password");
        } else {
            et_signup_name.setError(null);
            et_signup_email.setError(null);
            et_signup_phone.setError(null);
            et_signup_password.setError(null);
        }
        //新建一个用户对象
        final User user = new User();
        user.setUsername(su_name);
        user.setEmail(su_email);
        user.setMobilePhoneNumber(su_phone);
        user.setPassword(su_password);
        //存储输入的对象
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e==null){
                    Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(SignUpActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(SignUpActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                    Log.d("SignUpActivity", "done: " + e);
                }
            }
        });
    }

    //输入判断
    public boolean validataName(String name){
        matcher = pattern_3.matcher(name);
        return matcher.matches();
    }
    public boolean validatePhone(String phone) {
        matcher = pattern.matcher(phone);
        return matcher.matches();
    }
    public boolean validatePassword(String password) {
        return password.length() > 5 && password.length() < 13;
    }
    public boolean validateEmail(String email){
        matcher = pattern_2.matcher(email);
        return matcher.matches();
    }
}
