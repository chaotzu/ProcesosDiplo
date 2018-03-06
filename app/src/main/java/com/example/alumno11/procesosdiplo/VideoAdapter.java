package com.example.alumno11.procesosdiplo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.alumno11.procesosdiplo.webservice.Entity;
import com.example.alumno11.procesosdiplo.webservice.JSONParser;
import com.example.alumno11.procesosdiplo.webservice.Petition;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by alumno11 on 24/02/18.
 */


public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder>{

    private List<Video> videos = null;
    private OnVideoClickListener onVideoClickListener = null;
    private Context context = null;

    public  VideoAdapter(List<Video> videos) { this.videos = videos; }

    public VideoAdapter(List<Video> videos, Context context) {
        this.videos = videos;
        this.context = context;
    }


    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video,parent,false);
        VideoViewHolder videoViewHolder=new VideoViewHolder(item);
        videoViewHolder.setOnVideoClickListener(onVideoClickListener);

        Log.d("Listener",onVideoClickListener + "");

        return videoViewHolder;
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        holder.setVideo(videos.get(position));
        holder.typeTextView.setText(videos.get(position).getType());
        holder.titleTextView.setText(videos.get(position).getTitle());

        Glide.with(context).load(videos.get(position).getPoster()).into(holder.thumbnailImageView);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }


    public void setOnVideoClickListener(OnVideoClickListener onVideoClickListener) {
        this.onVideoClickListener = onVideoClickListener;
    }


    private Bitmap LoadImage(String URL, BitmapFactory.Options options)
    {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            JSONParser rss = new JSONParser();
            in = rss.createConnection(URL,new Petition(Entity.NONE)).getInputStream();
            bitmap = BitmapFactory.decodeStream(in, null, options);
            in.close();
        } catch (IOException e1) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {

        protected ImageView thumbnailImageView = null;
        protected TextView typeTextView = null;
        protected TextView titleTextView = null;
        private Video video = null;

        private OnVideoClickListener onVideoClickListener = null;

        public VideoViewHolder(View itemView) {
            super(itemView);
            typeTextView = (TextView) itemView.findViewById(R.id.typeTextView);
            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            thumbnailImageView = (ImageView) itemView.findViewById(R.id.thumbnailImageView);

            Log.d("Listener 1",onVideoClickListener + "");


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onVideoClickListener != null && video != null)
                        onVideoClickListener.onVideoClick(video);
                }
            });
        }

        public VideoViewHolder(View itemView, Video video) {
            this(itemView);
            this.video = video;
        }

        public void setVideo(Video video) {
            this.video = video;
        }

        public void setOnVideoClickListener(OnVideoClickListener onVideoClickListener) {
            this.onVideoClickListener = onVideoClickListener;
        }
    }

}
