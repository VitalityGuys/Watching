package tv.danmaku.ijk.media.example;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MyIJKVideoView extends LinearLayout {
    private Activity mActivity;
    public MyIJKVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mActivity= (Activity) context;
        LayoutInflater.from(context).inflate(R.layout.activity_player,this);

//        ImageButton fullScreen=findViewById(R.id.setting);
//        fullScreen.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(),"点击全屏",Toast.LENGTH_SHORT).show();
//                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//设置videoView横屏播放
//                mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            }
//        });
    }
}
