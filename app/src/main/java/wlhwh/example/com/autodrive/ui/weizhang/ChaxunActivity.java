package wlhwh.example.com.autodrive.ui.weizhang;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import wlhwh.example.com.autodrive.R;
import wlhwh.example.com.autodrive.base.BaseActivity;

/**
 * Created by WLHWH on 2017/6/1.
 */

public class ChaxunActivity extends BaseActivity{

    private AppCompatButton mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.csy_activity_main);

        mButton = (AppCompatButton)findViewById(R.id.chaxun_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChaxunActivity.this,WeizhangResult.class);
                startActivity(intent);
            }
        });
    }
}
