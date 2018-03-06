package wlhwh.example.com.autodrive.adapter;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import wlhwh.example.com.autodrive.R;
import wlhwh.example.com.autodrive.model.Music;

/**
 * Created by WLHWH on 2017/4/24.
 */

public class MusicAdapter extends BaseAdapter{

    private Context context;        //上下文对象引用
    private List<Music> mp3Infos;   //存放Mp3Info引用的集合
    private Music mp3Info;        //Mp3Info对象引用
    private int pos = -1;           //列表位置
    private ViewHolder viewholder;


    public MusicAdapter(Context context,List<Music> mp3Infos){
        this.context = context;
        this.mp3Infos = mp3Infos;
    }


    @Override
    public int getCount() {
        return mp3Infos.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        viewholder = null;
        if(convertView == null){
            viewholder = new ViewHolder();
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.music_list_item_layout, null);
            viewholder.music_title = (TextView)convertView.findViewById(R.id.music_title);
            viewholder.music_artist = (TextView)convertView.findViewById(R.id.music_artist);
            viewholder.music_duration = (TextView)convertView.findViewById(R.id.music_duration);
            convertView.setTag(viewholder);
        }
        else{
            viewholder = (ViewHolder) convertView.getTag();
        }
        mp3Info = mp3Infos.get(position);

        viewholder.music_title.setText(mp3Info.getTitle());         //显示标题
        viewholder.music_artist.setText(mp3Info.getArtist()+ " " + "-" + " " +mp3Info.getAlbum());       //显示艺术家
        viewholder.music_duration.setText(String.valueOf(formatTime(mp3Info.getDuration()))); //显示长度
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    public static String formatTime(Long time){                     //将歌曲的时间转换为分秒的制度
        String min = time / (1000 * 60) + "";
        String sec = time % (1000 * 60) + "";

        if(min.length() < 2)
            min = "0" + min;
        switch (sec.length()){
            case 4:
                sec = "0" + sec;
                break;
            case 3:
                sec = "00" + sec;
                break;
            case 2:
                sec = "000" + sec;
                break;
            case 1:
                sec = "0000" + sec;
                break;
        }
        return min + ":" + sec.trim().substring(0,2);
    }


    private class ViewHolder{
        TextView music_title;
        TextView music_artist;
        TextView music_duration;
    }

}
