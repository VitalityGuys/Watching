package com.example.moviepro.Adapter;//package com.example.moviepro.Adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//
//import com.example.moviepro.Base.VideoInfo;
//import com.example.moviepro.R;
//
//import java.util.List;
//
//public class VideoAdapter extends ArrayAdapter<VideoInfo> {
//    private int resourceId;
//    public VideoAdapter(@NonNull Context context, int resource, @NonNull List<VideoInfo> objects) {
//        super(context, resource, objects);
//        resourceId=resource;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        VideoInfo videoInfo=getItem(position);
//        View view;
//        ViewHolder viewHolder;
//        if(convertView==null){
//            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
//            viewHolder=new ViewHolder();
//            viewHolder.videoname=(TextView)view.findViewById(R.id.videoname);
//            viewHolder.videotype=(TextView)view.findViewById(R.id.videotype);
//            view.setTag(viewHolder);
//        }else {
//            view=convertView;
//            viewHolder=(ViewHolder)view.getTag();
//        }
//        viewHolder.videoname.setText(videoInfo.getVideoname());
//        viewHolder.videotype.setText(videoInfo.getVideotype());
//        return view;
//    }
//    class ViewHolder{
//        TextView videoname;
//        TextView videotype;
//    }
//}


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviepro.Base.VideoInfo;
import com.example.moviepro.PlayActivity;
import com.example.moviepro.R;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private List<VideoInfo> mVideoInfoList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View videoInfoView;
        TextView videoName;
        TextView videoType;

        public ViewHolder(View view){
            super(view);
            videoInfoView=view;
            videoName=(TextView)view.findViewById(R.id.videoname);
            videoType=(TextView)view.findViewById(R.id.videotype);
        }
    }
    public VideoAdapter(List<VideoInfo> videoInfoList){
        mVideoInfoList =videoInfoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.videolist,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.videoInfoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                VideoInfo videoInfo= mVideoInfoList.get(position);
                //从当前页面跳转到播放页面，并将选中视频信息传递过去
                Intent intent=new Intent(v.getContext(), PlayActivity.class);

                intent.putExtra("url",videoInfo.getVideourl());
                intent.putExtra("name",videoInfo.getVideoname());
                if(videoInfo.getVideotype().equals("直播")){
                    intent.putExtra("type","live");
                }else{
                    intent.putExtra("type","video");
                }
                v.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VideoInfo videoInfo=mVideoInfoList.get(position);
        holder.videoName.setText(videoInfo.getVideoname());
        holder.videoType.setText(videoInfo.getVideotype());
    }

    @Override
    public int getItemCount() {
        return mVideoInfoList.size();
    }
}

