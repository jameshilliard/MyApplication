package com.lry.songmachine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lry.songmachine.R;
import com.lry.songmachine.bean.VideoInfo;

import java.util.List;

/**
 * 已点歌曲的适配器，ListView适配
 */
public class SongNumberAdapter extends BaseAdapter {

    private List<VideoInfo> selectedVideos;
    private LayoutInflater inflater;

    public OnImageClickListener onImageClickListener;

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    public SongNumberAdapter(Context context, List<VideoInfo> selectedVideos) {
        inflater = LayoutInflater.from(context);
        this.selectedVideos = selectedVideos;
    }


    @Override
    public int getCount() {
        return selectedVideos.size();
    }

    @Override
    public Object getItem(int i) {
        return selectedVideos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_listview_song_number, null);
            viewHolder.tvSongName = (TextView) view.findViewById(R.id.tv_song_name);
            viewHolder.imageSetTop = (ImageView) view.findViewById(R.id.image_set_top);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvSongName.setText(selectedVideos.get(i).getVideoName());
        viewHolder.imageSetTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onImageClickListener != null) {
                    onImageClickListener.OnImageClickSetTo(i);
                }
            }
        });
        return view;
    }

    private class ViewHolder {
        TextView tvSongName;
        ImageView imageSetTop;
    }

    public interface OnImageClickListener {
        void OnImageClickSetTo(int i); // 点击置顶方法
    }

}
