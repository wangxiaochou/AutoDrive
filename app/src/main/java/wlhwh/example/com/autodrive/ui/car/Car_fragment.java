package wlhwh.example.com.autodrive.ui.car;

/**
 * Created by WLHWH on 2017/3/22.
 */

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.bmob.v3.Bmob;
import wlhwh.example.com.autodrive.R;

public class Car_fragment extends Fragment implements View.OnClickListener{

    //这里是点击解锁的时候的第一个flag，汽车解锁位置
    int flag = -1;

    //汽车页面整体的布局
    private LinearLayout llayout_1;
    private LinearLayout carlock_layout;

    //解锁那块的布局
    private TextView tv_1;
    private TextView tv_2;
    private ImageView iv_1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.car_fragment,container,false);
        llayout_1 = (LinearLayout)view.findViewById(R.id.car_layout);
        //初始化Bmob
        Bmob.initialize(getActivity(), "ae5732a5beb2d4c3f859fa8c78538013");
        setupCarOne();
        return view;
    }

    public void setupCarOne(){
        tv_1 = (TextView)llayout_1.findViewById(R.id.car_door_tv);
        tv_2 = (TextView)llayout_1.findViewById(R.id.car_engine_tv);
        iv_1 = (ImageView)llayout_1.findViewById(R.id.car_lock_image);
        carlock_layout = (LinearLayout)llayout_1.findViewById(R.id.car_lock_layout);
        carlock_layout.setClickable(true);
        tv_1.setText(String.valueOf("车门已锁"));
        tv_2.setText(String.valueOf("发动机未启动"));
        tv_1.setTextColor(getResources().getColor(R.color.font_gray));
        tv_2.setTextColor(getResources().getColor(R.color.font_gray));
        carlock_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.car_lock_layout:
                flag++;
                    if (flag%3 == 0) {
                        tv_1.setText(String.valueOf("车门解锁"));
                        tv_2.setText(String.valueOf("发动机未启动"));
                        tv_1.setTextColor(getResources().getColor(R.color.A100));
                        tv_2.setTextColor(getResources().getColor(R.color.font_gray));
                        iv_1.setImageResource(R.drawable.door_open);
                    }else if (flag%3 == 1){
                        tv_1.setText(String.valueOf("车门解锁"));
                        tv_2.setText(String.valueOf("发动机已启动"));
                        tv_1.setTextColor(getResources().getColor(R.color.A100));
                        tv_2.setTextColor(getResources().getColor(R.color.A100));
                        iv_1.setImageResource(R.drawable.engine_start);
                    }else if (flag%3 == 2){
                        tv_1.setText(String.valueOf("车门已锁"));
                        tv_2.setText(String.valueOf("发动机未启动"));
                        tv_1.setTextColor(getResources().getColor(R.color.font_gray));
                        tv_2.setTextColor(getResources().getColor(R.color.font_gray));
                        iv_1.setImageResource(R.drawable.car_lock);
                    }
            break;
            default:
                break;
        }
    }
}

