package wlhwh.example.com.autodrive.ui.music;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import wlhwh.example.com.autodrive.R;
import wlhwh.example.com.autodrive.adapter.MusicAdapter;
import wlhwh.example.com.autodrive.model.Music;

/**
 * Created by WLHWH on 2017/3/22.
 */

public class Music_fragment extends Fragment{

    private ListView mListView;
    private FindSongs finder;
    private List<Music> mp3Infos;
    private MusicAdapter adapter;
    //private SwipeRefreshLayout mSrfLayout;
    private Music music;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.music_fragment, container, false);
        mListView=(ListView)view.findViewById(R.id.listview);
        Log.d("Music_fragment", "查看是否显示listview" + mListView);
        //mSrfLayout = (SwipeRefreshLayout)view.findViewById(R.id.refresh);
        init();
        return view;
    }

    public void init(){

        //定义一个类，是寻找手机中音乐文件的类
        finder = new FindSongs();
        Log.d("Music_fragment", "查看是否查找到音乐" + finder);
        mp3Infos = finder.getMp3Infos(Music_fragment.this.getActivity().getContentResolver());
        adapter = new MusicAdapter(getContext(), mp3Infos);
        finder.setListAdpter(getContext(), mp3Infos, mListView);
        mListView.setAdapter(adapter);

        //刷新的逻辑
        //refresh();

        //这是点击item并且跳转页面传递数据的逻辑
        itemclick();

    }

    //这是刷新的逻辑
//    public void refresh(){
//        mSrfLayout.setProgressBackgroundColorSchemeResource(R.color.background);
//        mSrfLayout.setColorSchemeResources(R.color.colorAccent);
//        mSrfLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//            }
//        });
//    }


    //这是点击item并且跳转页面传递数据的逻辑
    public void itemclick(){
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),MusicPlayActivity.class);
                //从列表中获取当前歌曲的位置
                music = mp3Infos.get(position);
                //传输当前歌曲的路径
                intent.putExtra("url",music.getUrl());
                //传递当前歌曲的名字
                intent.putExtra("title",music.getTitle());
                //传递当前歌曲的时间
                intent.putExtra("duration",music.getDuration());
                //页面跳转
                getActivity().startActivity(intent);
            }
        });
    }


}
