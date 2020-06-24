package com.example.moviepro;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.moviepro.Base.PlayLink;
import com.example.moviepro.Base.VideoDetail;
import com.example.moviepro.Utils.HttpUtil;
import com.example.moviepro.Utils.ParseHtml;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import tv.danmaku.ijk.media.example.widget.media.IjkVideoView;
import tv.danmaku.ijk.media.example.widget.media.MyMediaController;
import tv.danmaku.ijk.media.example.widget.media.SurfaceRenderView;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class PlayActivity extends AppCompatActivity {

    private PlayerManager playerManager;
    private VideoDetail videoDetail;
    private boolean enablem3u8=false;
    private boolean switchPlaysourceStatus =true;              //是否首次加载切换播放源


    //视频详细信息控件
    private Switch mPlaySourceSwitch;
    private ImageView coverimg;
    private TextView videotype;
    private TextView videoname;
    private TextView videodirector;
    private TextView videoactors;
    private TextView videoarea;
    private TextView videolanguage;
    private TextView videointroduce;
    private TextView playnum;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        playerManager = new PlayerManager(this);

        //获取上个页面传递过来的视频详细链接
        Intent intent=getIntent();
        String type=intent.getStringExtra("type");
        String url=intent.getStringExtra("url");

        if(type.equals("live")){//播放直播
            playerManager.setLive(true);
            playerManager.setVideoUrl(url);
            playerManager.startPlay();
        }else if(type.equals("video")){
            playerManager.setLive(false);
            //获取视频并进行相关处理
            findView();
            getVideodata(url);
            //监听切换播放源开关
            mPlaySourceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //切换为mp4
                    enablem3u8= !isChecked;
                    switchPlaylist();
                    playerManager.showLoadProgressBar(true);
                }
            });
        }




    }


    @Override
    protected void onPause() {
        super.onPause();
        playerManager.pausePlay();
    }

    @Override
    protected void onResume() {
        super.onResume();
        playerManager.showMediaController();
    }


    @Override
    public void onBackPressed() {
        playerManager.backPressed();
    }

    //获取所有的全局控件
    private void findView(){
        //获取各个控件
        mPlaySourceSwitch=findViewById(R.id.playsourceSwitch);
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

    //请求视频播放链接等数据、并进行相关设置
    private void getVideodata(String videoDetailUrl){
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

                /**
                 * 开一个线程进行界面的设置，否则报错
                 * android.view.ViewRootImpl$CalledFromWrongThreadException:
                 * Only the original thread that created a view hierarchy can touch its views.
                 */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //获取视频详细信息
                        videoDetail= ParseHtml.parsedetailinfo(html);
                        //设置播放列表（选集列表）
                        setPlayList(videoDetail);
                        //设置详细信息
                        setDetailInfo(videoDetail);
                        //提示信息
                        Toast.makeText(PlayActivity.this,"正在加载，请稍后",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    //设置视频的选集列表
    private void setPlayList(VideoDetail videoDetail){
        final GridLayout gridLayout=(GridLayout)findViewById(R.id.gridlayout);
        GridLayout.LayoutParams params = null;
        //移除该布局上的所有控件
        gridLayout.removeAllViews();
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();

        final ArrayList<PlayLink> tmp;
        if(enablem3u8){
            tmp=videoDetail.getPlaylists_m3u8();
        }else {
            tmp=videoDetail.getPlaylists_mp4();
        }
//      final ArrayList<PlayLink> tmp=videoDetail.getPlaylists_mp4();

        int length=tmp.size();
        int i,j,k=0;
        for(i=0;i<length/4+1;i++){
            for(j=0;j<4;j++){
                if(k>=length){
                    break;
                }
                final Button button=new Button(PlayActivity.this);
                button.setText(tmp.get(k).getVideonum());
                button.setWidth((screenWidth-60)/4);
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

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playerManager.setVideoUrl(tmp.get(index).getVideourl());
                        for (int i = 0; i < gridLayout.getChildCount(); i++) {//清空所有背景色
                            Button b = (Button) gridLayout.getChildAt(i);
                            b.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        }
                        //设置选中的按钮背景色
                        button.setBackgroundColor(Color.parseColor("#fb7299"));
                        playnum.setText(tmp.get(index).getVideonum());
                        Toast.makeText(PlayActivity.this,"正在加载"+tmp.get(index).getVideonum(),Toast.LENGTH_SHORT).show();

                    }
                });
                gridLayout.addView(button,params);
                k++;
            }
        }
    }

    //设置视频的详细信息，并添加播放链接
    private void setDetailInfo(VideoDetail videoDetail){
        Glide.with(PlayActivity.this).load(videoDetail.getCoverimage()).into(coverimg);

        if(enablem3u8){
            playerManager.setVideoUrl(videoDetail.getPlaylists_m3u8().get(0).getVideourl());
            playerManager.setVideoTitle(videoDetail.getVideoname()+" "+videoDetail.getPlaylists_m3u8().get(0).getVideonum());
            playnum.setText(videoDetail.getPlaylists_m3u8().get(0).getVideonum());
        }else {
            playerManager.setVideoUrl(videoDetail.getPlaylists_mp4().get(0).getVideourl());
            playerManager.setVideoTitle(videoDetail.getVideoname()+" "+videoDetail.getPlaylists_mp4().get(0).getVideonum());
            playnum.setText(videoDetail.getPlaylists_mp4().get(0).getVideonum());
        }

        //首次执行时加载
        if(switchPlaysourceStatus){
            videotype.setText(videoDetail.getVideotype());
            videoname.setText(videoDetail.getVideoname());
            videodirector.setText(videoDetail.getDirector());
            videoactors.setText(videoDetail.getActors());
            videoarea.setText(videoDetail.getArea());
            videolanguage.setText(videoDetail.getLanguage());
            videointroduce.setText("\u3000\u3000" +videoDetail.getIntroduce());
        }
    }

    //设置视频的选集列表和详细信息，并添加播放链接
    private void switchPlaylist(){
        setPlayList(videoDetail);
        setDetailInfo(videoDetail);
        switchPlaysourceStatus =false;
    }


}

