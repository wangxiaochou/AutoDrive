package wlhwh.example.com.autodrive.ui.music;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wlhwh.example.com.autodrive.R;
import wlhwh.example.com.autodrive.base.BaseActivity;
import wlhwh.example.com.autodrive.model.Music;

/**
 * Created by WLHWH on 2017/4/24.
 */

public class MusicPlayActivity extends BaseActivity implements View.OnClickListener{

    private CircleProgressBar circleProgressBar;
    private long totalProgress = 10000;
    private long currentProgress;

    private double music_now;
    private double music_all;

    private MediaPlayer mediaplayer;
    private Intent intent;
    private String url;
    private String title;
    private long duration;

    private TextView music_title;

    private ImageView music_play;
    private ImageView music_stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.musicplay_activity);


        music_title = (TextView)findViewById(R.id.playing_music_name);
        //通过id找到当前歌曲
        intent = this.getIntent();
        //获取传输过来的歌曲路径
        url = intent.getStringExtra("url");
        Log.d("MusicPlayActivity", "这是传过来的歌曲的路径" + url);
        //获取传输过来的歌曲名字
        title = intent.getStringExtra("title");
        Log.d("MusicPlayActivity", "这是传过来的歌曲的名称" + "  " +title);
        //获取传输过来的时间
        duration = intent.getLongExtra("duration",0);
        Log.d("MusicPlayActivity", "这是传过来的歌曲的时间" + " " + duration);

        mediaplayer = new MediaPlayer();
        try{
            mediaplayer.setDataSource(MusicPlayActivity.this,Uri.parse(url));
            mediaplayer.prepare();
            music_title.setText(title);
        }catch (Exception e){
            e.printStackTrace();
        }


        //这是圆形进度条
        circleProgressBar = (CircleProgressBar) findViewById(R.id.circleprogressbar);


        //这是播放和暂停的图片按钮
        music_play = (ImageView)findViewById(R.id.music_play_icon);
        music_play.setClickable(true);
        music_stop = (ImageView)findViewById(R.id.music_stop_icon);
        music_stop.setVisibility(View.INVISIBLE);
        music_stop.setClickable(true);
        music_play.setOnClickListener(this);
        music_stop.setOnClickListener(this);
    }


    private class ProgressRunable implements Runnable {
        @Override
        public void run() {
            while (currentProgress < totalProgress){
                //获得当前歌曲的播放进度，并转化为double型数据
                music_now = mediaplayer.getCurrentPosition();
                //获得整首歌曲的播放总时间，并转化为double型数据
                music_all = mediaplayer.getDuration();
                Log.d("MusicPlayActivity", "这是歌曲当前时间" + music_now);
                Log.d("MusicPlayActivity", "这是歌曲总时间" + music_all);
                Log.d("MusicPlayActivity", "这是歌曲当前时间除以总时间" + (music_now/music_all));
                //将转化后的当前播放进度除以总的播放时长，乘以10000转化为long型数据
                currentProgress = (long)((music_now/music_all)*10000);
                Log.d("MusicPlayActivity", "这是转换后的当前进度" + " " + currentProgress);
                //设置当前额度播放进度屏显示在圆形progressbar上
                circleProgressBar.setProgress(currentProgress);
                try {
                    //这是每10毫秒刷新一次界面。每10毫秒进程sleep一次，之间代码不执行，之后代码再执行
                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.music_play_icon:
                if (!mediaplayer.isPlaying()){
                    mediaplayer.start();
                    new Thread(new ProgressRunable()).start();
                    music_play.setVisibility(View.INVISIBLE);
                    music_stop.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.music_stop_icon:
                if (mediaplayer.isPlaying()){
                    mediaplayer.pause();
                    music_play.setVisibility(View.VISIBLE);
                    music_stop.setVisibility(View.INVISIBLE);
                }
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
