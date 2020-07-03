package com.example.moviepro;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TableLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import master.flame.danmaku.ui.widget.DanmakuView;
import tv.danmaku.ijk.media.example.PlayLink;
import tv.danmaku.ijk.media.example.widget.media.AndroidMediaController;
import tv.danmaku.ijk.media.example.widget.media.IjkVideoView;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class PlayerManager {

    private Activity mActivity;
    private IjkVideoView mVideoView;
    private DanmakuView mDanmakuView;
    AndroidMediaController mMediaController;
    TableLayout mHudView;
    public PlayerManager(Activity activity) {
        mActivity=activity;
        init();
    }


    private void init(){
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");

        mVideoView = (IjkVideoView) mActivity.findViewById(R.id.video_view);
        mDanmakuView=mActivity.findViewById(R.id.danmaku_view);
        mMediaController = new AndroidMediaController(mActivity, false);
        mHudView = (TableLayout) mActivity.findViewById(R.id.hud_view);

        Toolbar toolbar=mActivity.findViewById(R.id.toolbar);
        ((AppCompatActivity)mActivity).setSupportActionBar(toolbar);
        ActionBar actionBar=((AppCompatActivity)mActivity).getSupportActionBar();
        mMediaController.setSupportActionBar(actionBar);

        mVideoView.setMediaController(mMediaController);
        mVideoView.setHudView(mHudView);
    }

    public void setVideoPath(String videoPath){
        mVideoView.setVideoURI(Uri.parse(videoPath));
    }


    public void start(){
        mVideoView.start();
    }
    public void pause(){
        mVideoView.pause();
    }
    public void stop(){
        mVideoView.stopPlayback();
    }

    public void danmaPause(){
        if(mDanmakuView!=null&&mDanmakuView.isPrepared()){
            mDanmakuView.pause();
        }
    }
    public void danmaResume(){
        if(mDanmakuView!=null&&mDanmakuView.isPrepared()&&mDanmakuView.isPaused()){
            mDanmakuView.resume();
        }
    }
    public void danmarelease(){
        if(mDanmakuView!=null){
            mDanmakuView.release();
            mDanmakuView=null;
        }
    }


    public void backPressed(){
        if(mActivity.getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT){
            stop();
            mActivity.finish();
        }else{
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }
    public void setPlaylist(ArrayList<PlayLink> playLinks){
        mMediaController.setPlayLinks(playLinks);
    }
    public void isLive(boolean islive){
        mVideoView.setLive(islive);
    }
    public void setVideoTitle(String title){
        mMediaController.setToolBarTitle(title);
    }
    public String getVideoLink(String searchword){
        try {
//            Document document= Jsoup.connect(String.format("https://search.bilibili.com/bangumi?keyword=%s", searchword)).get();
            Document document= Jsoup.connect(String.format("https://search.bilibili.com/all?keyword=%s", searchword)).get();
            String html=document.toString().replace("\n","");
            String pattern="\"url\":\"(.*?)\",";
            Pattern r=Pattern.compile(pattern,Pattern.MULTILINE);
            Matcher matcher=r.matcher(html);
            String link;
            if(matcher.find()){
                link=matcher.group().replace("\"url\":\"","").replace("\",","").replace("u002F","");
                Log.e("getCids", "getCids: "+link);
                return link;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<String> getCids(String link){
        ArrayList<String> cids=new ArrayList<>();
        try {
            Document document=Jsoup.connect(link).get();
            String html=document.toString().replace("\n","");
            String pattern="epList\":\\[(.*?)]";
            Pattern r=Pattern.compile(pattern);
            Matcher matcher=r.matcher(html);
            if(matcher.find()){
                html=matcher.group();
                pattern="\"cid\":(.*?),";
                r=Pattern.compile(pattern);
                matcher=r.matcher(html);
                while (matcher.find()){
                    Log.e("#",matcher.group());
                    cids.add(matcher.group().replace("\"cid\":","").replace(",",""));
                }
                return cids;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cids;
    }
    public void setCurrentCid(String currentCid){
        mMediaController.setCurrentCid(currentCid);
    }
    public void setCids(ArrayList<String> cids){
        mMediaController.setCids(cids);
    }


}
