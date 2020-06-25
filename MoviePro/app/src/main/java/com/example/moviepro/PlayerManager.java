package com.example.moviepro;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Handler;
import android.os.SystemProperties;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.logging.LogRecord;

import tv.danmaku.ijk.media.example.widget.media.IjkVideoView;
import tv.danmaku.ijk.media.example.widget.media.MyMediaController;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class PlayerManager {

    private final int SCALE_16_9 = 0;
    private final int SCALE_4_3 = 1;
    private final int SCALE_1_1 = 2;
    private int MY_DEVICE_SCALE=SCALE_16_9;

    private Activity mActivity;
    private IjkVideoView mVideoView;
    private TableLayout mHubView;
    private MyMediaController mMyMediaController;
    private AudioManager mAudioManager;
    private GestureDetector mGestureDetector;
    private myGestureDetector mMyGestureDetector;
    private ActionBar mActionBar;                   //播放器标题栏
    private ImageView mToolbarBack;                 //返回
    private TextView mToolbarTitle;                 //视频标题
    OrientationEventListener mOrientationEventListener;
    private ProgressBar mLoadVideoProgressbar;
    private SeekBar brightnessSeekBar;
    private SeekBar volumeSeekBar;

    //变量
    private int screenWidth;
    private int screenHeight;
    private long currentPosition;                   //单位毫米


    //控制变量
    private boolean currentOriention_LANDSCAPE=false;
    private boolean enablerotation=false;
    private boolean isLive=false;

    public PlayerManager(Activity mActivity) {


        IjkMediaPlayer.loadLibrariesOnce(null);                 //初始化ijkplayer
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");

        this.mActivity = mActivity;
        this.mMyMediaController=new MyMediaController(mActivity);
        this.mAudioManager=(AudioManager) mActivity.getSystemService(Service.AUDIO_SERVICE);

        mVideoView=mActivity.findViewById(R.id.ijk_video_View);
        mHubView=mActivity.findViewById(R.id.hud_view);
        mLoadVideoProgressbar=mActivity.findViewById(R.id.loadVideoProgress);
        //初始化toolbar
        Toolbar toolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        mToolbarBack=mActivity.findViewById(R.id.toolbar_back);
        mToolbarTitle=mActivity.findViewById(R.id.toolbar_title);
        ((AppCompatActivity)mActivity).setSupportActionBar(toolbar);
        mActionBar = ((AppCompatActivity)mActivity).getSupportActionBar();
        mMyMediaController.setSupportActionBar(mActionBar);

        mVideoView.setMediaController(mMyMediaController);
        mVideoView.setHudView(mHubView);

        setListener();
        initMY_DEVICE_SCALE();
        setVideoScale(MY_DEVICE_SCALE);
    }

    private void initMY_DEVICE_SCALE(){
        initScreenInfo();
        double scale=(screenWidth*1.0)/screenHeight;
        if(scale-(9/16.0)<0.00000001){
            MY_DEVICE_SCALE=SCALE_16_9;
        }else if(scale-(3/4.0)<0.00000001){
            MY_DEVICE_SCALE=SCALE_4_3;
        }else if((scale-1.0)<0.00000001){
            MY_DEVICE_SCALE=SCALE_1_1;
        }
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    private void setListener(){
        //开始监听手势
        mMyGestureDetector=new myGestureDetector();                      //监听步骤②
        mGestureDetector=new GestureDetector(mActivity.getApplicationContext(),mMyGestureDetector);
        mVideoView.setClickable(true);
        mVideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });
//        ((AppCompatActivity)mActivity)

        //添加屏幕旋转监听
        mOrientationEventListener=new OrientationEventListener(mActivity) {
            @Override
            public void onOrientationChanged(int orientation) {
                if(enablerotation){
                    if((orientation>150&&orientation<210)||(orientation>330||orientation<30)){//竖屏 自适应
//                        convertToPortScreen();
                        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                        setVideoScale(MY_DEVICE_SCALE);
                    }else if((orientation>240&&orientation<300)||(orientation>60&&orientation<120)){//横屏 自适应
//                        convertToLandScreen();
                        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                        setVideoScale(MY_DEVICE_SCALE);
                    }
                }
            }
        };
        if(enablerotation) {
            mOrientationEventListener.enable();     //启用该监听
        }

        //监听视频准备完成事件
        mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                Runnable runnable=new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mActivity,"加载完成",Toast.LENGTH_SHORT).show();
                    }
                };
                brightnessSeekBar=mActivity.findViewById(R.id.brightnessSeekBar);
                volumeSeekBar=mActivity.findViewById(R.id.volumeSeekBar);
                //加载成功，进度条不可见
                mLoadVideoProgressbar.setVisibility(View.INVISIBLE);


                //开始播放视频
                mp.start();

                if(isLive){
                    mMyMediaController.hideProgressBar(true);
                }


                //以下的监听需要视频加载出来才可以使用，故放在setOnPreparedListener中
                //监听全屏按钮
                mMyMediaController.setFullScreenListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!currentOriention_LANDSCAPE){//切换到全屏播放
                            Toast.makeText(mActivity,"切换到全屏播放"+ currentOriention_LANDSCAPE,Toast.LENGTH_SHORT).show();
                            convertToLandScreen();
                        }else {//退出全屏播放
                            Toast.makeText(mActivity,"退出全屏播放"+ currentOriention_LANDSCAPE,Toast.LENGTH_SHORT).show();
                            convertToPortScreen();
                        }
                    }
                });

                //监听自动旋转
                mMyMediaController.setRotationSwitchListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        enablerotation=isChecked;
                        if(enablerotation){
                            Toast.makeText(mActivity,"开启",Toast.LENGTH_SHORT).show();
                            mOrientationEventListener.enable();     //启用该监听
                        }else {
                            Toast.makeText(mActivity,"关闭",Toast.LENGTH_SHORT).show();
                            mOrientationEventListener.disable();    //禁用
                        }
                    }
                });

                //监听toolbar的返回键
                mToolbarBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(currentOriention_LANDSCAPE){
                            convertToPortScreen();
                        }else {
                            mVideoView.stopPlayback();
                            mActivity.finish();
                        }

                    }
                });

            }
        });

    }

    public void setVideoUrl(String videoUrl){
        if(mVideoView!=null){
            mVideoView.setVideoPath(videoUrl);
        }
    }
    public void startPlay(){
        if(mVideoView!=null){
            mVideoView.start();
        }
    }
    public void pausePlay(){
        if(mVideoView!=null){
            currentPosition=mVideoView.getCurrentPosition();
            mVideoView.pause();
        }
    }
    public void stopPlay(){
        if(mVideoView!=null){
            mVideoView.stopPlayback();
            mVideoView=null;
        }
    }
    public void backPressed(){
        if(!currentOriention_LANDSCAPE){
            stopPlay();
            mActivity.finish();
        }else{
            convertToPortScreen();
        }
    }
    public void showMediaController(){
        mMyMediaController.show();
        mVideoView.canPause();
    }
    public void setVideoTitle(String videoTitle){
        if(mToolbarTitle!=null){
            mToolbarTitle.setText(videoTitle);
        }
    }
    public void showLoadProgressBar(boolean show){
        if(show){
            mLoadVideoProgressbar.setVisibility(View.VISIBLE);
        }else {
            mLoadVideoProgressbar.setVisibility(View.INVISIBLE);
        }
    }

    //获取屏幕的宽高
    private void initScreenInfo(){
        screenWidth=((WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        screenHeight= ((WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();

    }


    //正常播放
    private void convertToPortScreen() {
        currentOriention_LANDSCAPE =false;
        mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置videoView竖屏播放
//        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setVideoScale(MY_DEVICE_SCALE);

    }

    //全屏播放
    private void convertToLandScreen() {
        currentOriention_LANDSCAPE =true;
        mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//设置videoView横屏播放
//        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        setVideoScale(MY_DEVICE_SCALE);
    }

    private void setVideoScale(int scale){
        initScreenInfo();
        int height=screenHeight;
        int width=screenWidth;
        FrameLayout relativeLayout=mActivity.findViewById(R.id.media_box);
        LinearLayout.LayoutParams params=null;
        switch (scale){
            case SCALE_16_9:
                params=new LinearLayout.LayoutParams(width,width*9/16);
                break;
            case SCALE_4_3:
                params=new LinearLayout.LayoutParams(width,width*3/4);
                break;
            case SCALE_1_1:
                params=new LinearLayout.LayoutParams(width,width);
                break;
            default:
                break;
        }
//        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(width,width*9/16);
        relativeLayout.setLayoutParams(params);
    }


    /**
     * 内部类 播放器的手势控制
     * 双击控制         播放/暂停
     * 长按控制         全屏/正常
     * 左划右划         快退/快进
     * 左边上下         亮度调整
     * 右边上下         音量调整
     */
    public class myGestureDetector extends GestureDetector.SimpleOnGestureListener {
        AudioManager audioManager=(AudioManager)mActivity.getSystemService(Service.AUDIO_SERVICE);
        float FLIP_DISTANCE = 50;               //高于此数值的滑动才触发滑动事件

        public myGestureDetector() {
            super();
        }

//        @Override
//        public boolean onSingleTapUp(MotionEvent e) {
//            return super.onSingleTapUp(e);
//        }

        @Override
        public void onLongPress(MotionEvent e) {

//            getScreenOrientation();
            if(!currentOriention_LANDSCAPE){//切换到全屏播放
//                currentOriention_LANDSCAPE =true;
//                Toast.makeText(getApplicationContext(),"切换到全屏播放"+ currentOriention_LANDSCAPE,Toast.LENGTH_SHORT).show();
                convertToLandScreen();


            }else {//退出全屏播放
//                currentOriention_LANDSCAPE =false;
//                Toast.makeText(getApplicationContext(),"退出全屏播放"+ currentOriention_LANDSCAPE,Toast.LENGTH_SHORT).show();
                convertToPortScreen();
            }
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//            return super.onScroll(e1, e2, distanceX, distanceY);
//            Toast.makeText(mActivity,"x:"+distanceX+"    y"+distanceY,Toast.LENGTH_SHORT).show();

            if(Math.max(Math.abs(distanceX),Math.abs(distanceY))>FLIP_DISTANCE){//距离大于设定的最小滑动距离
                if(!isLive&&Math.abs(distanceX)>Math.abs(distanceY)){//非直播情况下允许快进快退
                    int currentPosition=mVideoView.getCurrentPosition();
                    int Duration=mVideoView.getDuration();
                    int seektime=currentPosition+swipe_time(distanceX);
                    if (seektime < 0) {
                        seektime=0;
                    }else if(seektime>Duration){
                        seektime=Duration;
                    }
                    mVideoView.seekTo(seektime);
                }
                if(Math.abs(distanceX)<Math.abs(distanceY)){//亮度、音量调整
                    if(e1.getX()<=(int)(getScreenWidth()/2)){//调整亮度
                        Window window=mActivity.getWindow();
                        WindowManager.LayoutParams layoutParams=window.getAttributes();
                        float brightness=layoutParams.screenBrightness+(float) swipe_volume(distanceY);
                        if(brightness<0.05f){
                            brightness=0.05f;
                        }else if(brightness>1.0f){
                            brightness=1.0f;
                        }
                        int percent=(int)(brightness*100);//格式如：42%
                        layoutParams.screenBrightness=brightness;
                        window.setAttributes(layoutParams);

                        //隐藏控制器，防止干扰
//                        mMyMediaController.hide();
                        brightnessSeekBar.setVisibility(View.VISIBLE);
                        brightnessSeekBar.setProgress(percent);
//                        brightnessSeekBar.setVisibility(View.INVISIBLE);
                        Handler handler=new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                brightnessSeekBar.setVisibility(View.INVISIBLE);
                            }
                        },1000);

//                        Toast.makeText(mActivity,"当前亮度："+percent,Toast.LENGTH_SHORT).show();
                    }else{//调整声音
                        int maxvolum=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                        int currentVolume=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        currentVolume+=(int)(maxvolum*swipe_volume(distanceY));
                        if(currentVolume<0){
                            currentVolume=0;
                        }else if(currentVolume>maxvolum){
                            currentVolume=maxvolum;
                        }
                        int percent=(int) ((currentVolume*1.0)/maxvolum*100);//格式如：42%
                        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,currentVolume,AudioManager.FLAG_PLAY_SOUND);

                        //隐藏控制器，防止干扰
//                        mMyMediaController.hide();
                        volumeSeekBar.setVisibility(View.VISIBLE);
                        volumeSeekBar.setProgress(percent);
                        Handler handler=new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                volumeSeekBar.setVisibility(View.INVISIBLE);
                            }
                        },1000);
//                        Toast.makeText(mActivity,"当前声音："+percent,Toast.LENGTH_SHORT).show();
                    }
                }
            }

            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //return false;
            return super.onFling(e1, e2, velocityX, velocityY);
        }

//        @Override
//        public void onShowPress(MotionEvent e) {
//            super.onShowPress(e);
//        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
//            Toast.makeText(BaseApplication.getContext(),"双击",Toast.LENGTH_SHORT).show();
            if(mVideoView.isPlaying()){
//                Toast.makeText(getApplicationContext(),"暂停播放",Toast.LENGTH_SHORT).show();
                mVideoView.pause();
            }else {
//                Toast.makeText(getApplicationContext(),"继续播放",Toast.LENGTH_SHORT).show();
                mVideoView.start();
            }
            return super.onDoubleTap(e);
        }

//        @Override
//        public boolean onDoubleTapEvent(MotionEvent e) {
//            return super.onDoubleTapEvent(e);
//        }

//        @Override
//        public boolean onSingleTapConfirmed(MotionEvent e) {
//            return super.onSingleTapConfirmed(e);
//        }

//        @Override
//        public boolean onContextClick(MotionEvent e) {
//            return super.onContextClick(e);
//        }

        //返回快进或快退多少毫米
        int swipe_time(double distance){//最多快进快退100s
            return Math.abs(distance)<100?(int) distance*100:100*100;
        }
        double swipe_volume(double distance){//返回声音比例
            double percent=distance/mVideoView.getHeight();
            return percent;
        }
        //获取屏幕宽度
        int getScreenWidth(){
            int width = 0;
            width=((WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
            return width;
        }

        //获取屏幕高度
        int getScreenHeight(){
            int height=0;
            height= ((WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
            return height;
        }

        //获取屏幕方向
        private int getScreenOrientation() {
            //rotation 0 1 2 3分别代表顺时针旋转0、90、180、270
            int rotation = ((WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
            int width = ((WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
            int height = ((WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();

//            Toast.makeText(getApplicationContext(),rotation+":"+width+":"+height,Toast.LENGTH_SHORT).show();

            int orientation;
            // if the device's natural orientation is portrait:
            if ((rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) && height > width ||
                    (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) && width > height) {
                switch (rotation) {
                    case Surface.ROTATION_0:
                        orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                        break;
                    case Surface.ROTATION_90:
                        orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                        break;
                    case Surface.ROTATION_180:
                        orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                        break;
                    case Surface.ROTATION_270:
                        orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                        break;
                    default:
                        orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                        break;
                }
            }
            // if the device's natural orientation is landscape or if the device
            // is square:
            else {
                switch (rotation) {
                    case Surface.ROTATION_0:
                        orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                        break;
                    case Surface.ROTATION_90:
                        orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                        break;
                    case Surface.ROTATION_180:
                        orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                        break;
                    case Surface.ROTATION_270:
                        orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                        break;
                    default:
                        orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                        break;
                }
            }
            return orientation;
        }



        private int dip2px(Context context, float dpValue) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }

    }
}
