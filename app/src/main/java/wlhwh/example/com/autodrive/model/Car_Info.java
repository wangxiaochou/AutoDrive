package wlhwh.example.com.autodrive.model;

import android.widget.ImageView;

import cn.bmob.v3.BmobObject;

/**
 * Created by WLHWH on 2017/4/23.
 */

public class Car_Info extends BmobObject{

    //汽车拥有者是指向用户的
    private User owner;

    public User getOwner(){
        return owner;
    }
    public void setOwner(User owner){
        this.owner = owner;
    }

    //汽车型号
    private String car_xinghao;

    //当前汽车的车牌
    private String car_paizhao;

    //当前型号车的照片
    private ImageView car_zhaopian;

    //当前汽车品牌标志
    private ImageView car_pinpai_biaozhi;

    //判断当前汽车已行驶路程
    private String car_yixingshi;

    //判断当前汽车距离下次保养的时间
    private String car_baoyang;

    //判断车门是否关闭
    private boolean car_chemen;

    //判断汽车发动机是否启动
    private boolean car_fadongji;

    //判断汽车剩余汽油量
    private String car_shengyu_qiyou;

    //判断剩下可以行驶的路程
    private String car_qiyou_lucheng;

    //判断当前水箱的实时水温
    private String car_shuixiang_wendu;

    //判断当前水箱冷却液容量
    private String car_shuixiang_rl;

    //判断电瓶剩余电量
    private String car_dianchi_shengyu;

    //判断当前剩余电量可以使用时间
    private String car_dianchi_shijian;

    //判断轮胎位置
    private String car_luntai_weizhi;

    //判断轮胎的胎压
    private String car_luntai_taiya;

    //判断当前汽车发动机是否运行正常
    private boolean car_fadongji_yunxing;

    //判断当前汽车发动机缸是否运行正常
    private boolean car_fadongji_gang;

    //判断当前汽车机油油量
    private String car_jiyou_shengyu;

    //判断当前汽车机油可用时间
    private String car_jiyou_shijian;

    //判断当前底盘传动运行是否正常
    private boolean car_dipan_chuandong;

    //判断当前汽车底盘制动运行是否正常
    private boolean car_dipan_zhidong;

}
