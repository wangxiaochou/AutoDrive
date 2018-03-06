package wlhwh.example.com.autodrive.base;

import android.app.Activity;
import android.app.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WLHWH on 2017/4/16.
 */

public class MyApplication extends Application{

    //用于存放所有activity的集合
    private List<Activity> list_activity = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    //添加activity
    public void addActivity(Activity activity){
        //判断当前activity不存在集合当中
        if (!list_activity.contains(activity)){
            //将当前activity添加到集合当中
            list_activity.add(activity);
        }
    }

    //销毁单个activity
    public void removeActivity(Activity activity){
        //判断当前activity存在于集合当中
        if (list_activity.contains(activity)){
            //将当前activity移除集合
            list_activity.remove(activity);
            //将当前activity销毁
            activity.finish();
        }
    }


    //销毁所有的activity
    public void removeAllActivity(){
        //通过循环，把当前集合中所有activity销毁
        for (Activity activity:list_activity){
            activity.finish();
        }
    }
}
