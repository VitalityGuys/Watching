package com.example.moviepro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.moviepro.Base.VideoDetail;
import com.example.moviepro.Utils.HttpUtil;
import com.example.moviepro.Utils.ParseHtml;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import tv.danmaku.ijk.media.example.PlayLink;

public class PlayActivity extends AppCompatActivity {
//    String mVideoPath = "http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8";
            String mVideoPath = "http://ok.renzuida.com/2001/仙王的日常生活-01.mp4";
    PlayerManager playerManager;
    //视频详细信息控件
    private ImageView coverimg;
    private TextView videotype;
    private TextView videoname;
    private TextView videodirector;
    private TextView videoactors;
    private TextView videoarea;
    private TextView videolanguage;
    private TextView videointroduce;
    private TextView playnum;

    private VideoDetail videoDetail;
    ArrayList<String> cids=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //允许主线程中调用http请求
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        //获取上个页面传递过来的视频详细链接
        Intent intent=getIntent();
        String type=intent.getStringExtra("type");
        String url=intent.getStringExtra("url");
        String name=intent.getStringExtra("name");

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }



        if(type.equals("live")){//播放直播
            setContentView(R.layout.activity_playlive);
            playerManager=new PlayerManager(this);
            playerManager.isLive(true);
            playerManager.setVideoPath(url);
            playerManager.start();
        }else if(type.equals("video")){
            setContentView(R.layout.activity_play);
            playerManager=new PlayerManager(this);
            //获取弹幕cid 测试
            String link=playerManager.getVideoLink(name);
            if(link!=null&& !link.equals("")){
                cids=playerManager.getCids(link);
            }
            findView();
            getVideodata(url);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        playerManager.pause();
        playerManager.danmaPause();
    }



    @Override
    protected void onResume() {
        super.onResume();
//        if(playerManager!=null){
//            playerManager.showMediaController();
//        }
        playerManager.start();
        playerManager.danmaResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        playerManager.danmarelease();
    }

    @Override
    public void onBackPressed() {
        if(playerManager!=null){
            playerManager.backPressed();
        }

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("onConfigurationChanged", "onConfigurationChanged: 被处罚");
        Toast.makeText(this,"旋转被触发",Toast.LENGTH_SHORT).show();
//        if(newConfig.orientation== ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
//            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        }else if(newConfig.orientation== ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
//            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }else if(newConfig.orientation== ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE){
//            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
//        }else if(newConfig.orientation== ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT){
//            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
//        }


    }

//    private VideoDetail getdata(){
//       String string=HttpUtil.synGet("http://www.zuidazy5.com/?m=vod-detail-id-33988.html");
//        VideoDetail videoDetail=ParseHtml.parsedetailinfo(string);
//        return videoDetail;
//    }

    //请求视频播放链接等数据、并进行相关设置
    private void getVideodata(final String videoDetailUrl){
        HttpUtil.get(videoDetailUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PlayActivity.this,"加载视频失败，请刷新重试",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                //获得网页返回的数据
                final String html=response.body().string();
                videoDetail=ParseHtml.parsedetailinfo(html,SearchActivity.currentplaySourceRule);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //设置详细信息
                        Glide.with(PlayActivity.this).load(videoDetail.getCoverimage()).into(coverimg);
                        playnum.setText(videoDetail.getPlaylists_m3u8().get(0).getVideonum());
                        videotype.setText(videoDetail.getVideotype());
                        videoname.setText(videoDetail.getVideoname());
                        videodirector.setText(videoDetail.getDirector());
                        videoactors.setText(videoDetail.getActors());
                        videoarea.setText(videoDetail.getArea());
                        videolanguage.setText(videoDetail.getLanguage());
                        videointroduce.setText("\u3000\u3000" +videoDetail.getIntroduce());

                        GridLayout gridLayoutm3u8=(GridLayout)findViewById(R.id.gridlayoutm3u8);
                        GridLayout gridLayoutmp4=(GridLayout)findViewById(R.id.gridlayoutmp4);
                        GridLayout gridLayoutweb=(GridLayout)findViewById(R.id.gridlayoutweb);

                        //设置m3u8播放列表（选集列表）
                        setPlayList(videoDetail.getPlaylists_m3u8(),gridLayoutm3u8,false);
                        setPlayList(videoDetail.getPlaylists_mp4(),gridLayoutmp4,false);
                        setPlayList(videoDetail.getPlaylists_web(),gridLayoutweb,true);







                        playerManager.setPlaylist(videoDetail.getPlaylists_m3u8());
                        playerManager.setVideoPath(videoDetail.getPlaylists_m3u8().get(0).getVideourl());
                        if(cids!=null&&cids.size()!=0){
                            playerManager.setCids(cids);
                            playerManager.setCurrentCid(cids.get(0));
                        }

                        playerManager.setVideoTitle(videoDetail.getVideoname()+" "+videoDetail.getPlaylists_m3u8().get(0).getVideonum());
                        playerManager.start();
                    }
                });

            }
        });
    }

    //获取所有的全局控件
    private void findView(){
        //获取各个控件
        coverimg=(ImageView)findViewById(R.id.videocover);
        videotype=findViewById(R.id.videotype);
        videoname=findViewById(R.id.videoname);
        videoactors=findViewById(R.id.videoactors);
        videodirector=findViewById(R.id.videodirector);
        videoarea=findViewById(R.id.videoarea);
        videolanguage=findViewById(R.id.videolanguage);
        videointroduce=findViewById(R.id.videointroduce);
        playnum=findViewById(R.id.playnum);

    }

    //设置视频的选集列表
    private void setPlayList(final ArrayList<PlayLink> PlayList, final GridLayout gridLayout, boolean isweb){

        GridLayout.LayoutParams params = null;

        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        int length=PlayList.size();
        int i,j,k=0;
        for(i=0;i<length/4+1;i++){
            for(j=0;j<4;j++){
                if(k>=length){
                    break;
                }
                final Button button=new Button(PlayActivity.this);
                button.setText(PlayList.get(k).getVideonum());
                button.setWidth((int) ((screenWidth-60)/4.0));
                if(k==0){
                    button.setBackgroundColor(Color.parseColor("#fb7299"));
                }
                //设置行
                GridLayout.Spec rowSpec=GridLayout.spec(i);
                //设置列
                GridLayout.Spec columnSpec=GridLayout.spec(j);
                params=new GridLayout.LayoutParams(rowSpec,columnSpec);
                params.setMargins(5, 5, 5, 5);
                final int index=k;

                if(isweb){
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            playerManager.pause();
                            Uri uri = Uri.parse(PlayList.get(index).getVideourl());
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    });
                }else {
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            playerManager.setVideoPath(PlayList.get(index).getVideourl());
                            if(cids!=null&&cids.size()>index)
                            playerManager.setCurrentCid(cids.get(index));
                            playerManager.setVideoTitle(videoDetail.getVideoname()+" "+PlayList.get(index).getVideonum());

                            for (int i = 0; i < gridLayout.getChildCount(); i++) {//清空所有背景色
                                Button b = (Button) gridLayout.getChildAt(i);
                                b.setBackgroundColor(Color.parseColor("#d6d7d7"));
                            }
                            //设置选中的按钮背景色
                            button.setBackgroundColor(Color.parseColor("#fb7299"));
                            playnum.setText(PlayList.get(index).getVideonum());
                            Toast.makeText(PlayActivity.this,"正在加载"+PlayList.get(index).getVideonum(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //长按复制播放链接
                button.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        //获取剪贴板管理器：
                        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        // 创建普通字符型ClipData
                        ClipData mClipData = ClipData.newRawUri("Label", Uri.parse(PlayList.get(index).getVideourl()));
                        // 将ClipData内容放到系统剪贴板里。
                        cm.setPrimaryClip(mClipData);
                        Toast.makeText(PlayActivity.this,"复制"+PlayList.get(index).getVideonum()+"链接成功，快去使用吧！！！",Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                gridLayout.addView(button,params);
                k++;
            }
        }
    }


}
