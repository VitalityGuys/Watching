package com.example.moviepro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviepro.Adapter.VideoAdapter;
import com.example.moviepro.Base.VideoInfo;
import com.example.moviepro.Manager.PlaySourceRuleManager;
import com.example.moviepro.Utils.HttpUtil;
import com.example.moviepro.Utils.ParseHtml;
import com.example.moviepro.bean.PlaySourceRule;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {

    EditText mSearch_edit;
    Button mSearchButton;
    RecyclerView recyclerView;
    private Spinner mSpinner;
    //    String[] spinnerItems={"最大资源网","OK资源网","速播资源网","最新资源网","麻花资源网","135资源网"};
//    String[] spinnerItems={};
    ArrayList<String> spinnerItems=new ArrayList<>();
    private int selectedItemIdx=0;
    public static PlaySourceRuleManager playSourceRuleManager;
    public static PlaySourceRule currentplaySourceRule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //允许主线程中调用http请求
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        boolean flag=initPlayResource();
        if(!flag){
            return;
        }
        initSpinnerItems();

        findView();
        setSpinner();

        initlive();

        addListener();

    }

    //初始化播放源
    private boolean initPlayResource(){

        SharedPreferences preferences=getSharedPreferences("playResource",MODE_PRIVATE);
        String playResource=preferences.getString("playResource","");
        if(playResource.equals("")){//网络加载
            Toast.makeText(SearchActivity.this,"网络加载片源。。。",Toast.LENGTH_SHORT).show();
            playResource=HttpUtil.synGet("https://yuumiandyasuo.github.io/fyz.github.io/PlayResource.json");
            int i=0;
            while (playResource.equals("")&&i<3){//请求失败重试
                Toast.makeText(SearchActivity.this,"网络加载失败，正在重试。。。第"+i+"次",Toast.LENGTH_SHORT).show();
                playResource=HttpUtil.synGet("https://yuumiandyasuo.github.io/fyz.github.io/PlayResource.json");
                i++;
            }
            if(playResource.equals("")){
                Toast.makeText(SearchActivity.this,"你的网络有问题，请检查。。。",Toast.LENGTH_SHORT).show();
                return false;
            }else {
                Toast.makeText(SearchActivity.this,"网络加载successfully。。。",Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(SearchActivity.this,"本地加载片源。。。",Toast.LENGTH_SHORT).show();
        }
        try {//解析json
            playSourceRuleManager=ParseHtml.ParsePlayResource(playResource.replace("\\",""));
            currentplaySourceRule=playSourceRuleManager.getPlaySourceRules().get(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //将片源保存到SharedPreferences
        SharedPreferences.Editor editor=getSharedPreferences("playResource",MODE_PRIVATE).edit();
        editor.putString("playResource",playResource);
        editor.apply();
        return true;

    }

    private void initSpinnerItems(){
        int length=playSourceRuleManager.getPlaySourceRules().size();
        for(int i=0;i<length;i++){
            spinnerItems.add(playSourceRuleManager.getPlaySourceRules().get(i).getPlaysourcename());
        }
        System.out.println(spinnerItems.toString());
    }


    private void initlive(){
        final ArrayList<VideoInfo> videolist=new ArrayList<>();

        videolist.add(new VideoInfo("cctv1  综合","直播","http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8"));
        videolist.add(new VideoInfo("cctv2  财经","直播","http://ivi.bupt.edu.cn/hls/cctv2hd.m3u8"));
        videolist.add(new VideoInfo("cctv3  综艺","直播","http://ivi.bupt.edu.cn/hls/cctv3hd.m3u8"));
        videolist.add(new VideoInfo("cctv4  中文国际（亚）","直播","http://ivi.bupt.edu.cn/hls/cctv4hd.m3u8"));
        videolist.add(new VideoInfo("cctv5  体育","直播","http://120.241.133.167/outlivecloud-cdn.ysp.cctv.cn/cctv/2000205103.m3u8"));
        videolist.add(new VideoInfo("cctv5+ 体育赛事","直播","http://ivi.bupt.edu.cn/hls/cctv5phd.m3u8"));
        videolist.add(new VideoInfo("cctv6  电影","直播","http://ivi.bupt.edu.cn/hls/cctv6hd.m3u8"));
        videolist.add(new VideoInfo("cctv7  国防军事","直播","http://ivi.bupt.edu.cn/hls/cctv7hd.m3u8"));
        videolist.add(new VideoInfo("cctv8  电视剧","直播","http://ivi.bupt.edu.cn/hls/cctv8hd.m3u8"));
        videolist.add(new VideoInfo("cctv9  记录","直播","http://ivi.bupt.edu.cn/hls/cctv9hd.m3u8"));
        videolist.add(new VideoInfo("cctv10 科教","直播","http://ivi.bupt.edu.cn/hls/cctv10hd.m3u8"));
        videolist.add(new VideoInfo("cctv11 戏曲","直播","http://120.241.133.167/outlivecloud-cdn.ysp.cctv.cn/cctv/2000204103.m3u8"));
        videolist.add(new VideoInfo("cctv12 社会与法","直播","http://ivi.bupt.edu.cn/hls/cctv12hd.m3u8"));
        videolist.add(new VideoInfo("cctv13 新闻","直播","https://xigua-cdn.haima-zuida.com/20200121/1022_227c1fa5/index.m3u8"));
        videolist.add(new VideoInfo("cctv14 少儿","直播","http://ivi.bupt.edu.cn/hls/cctv14hd.m3u8"));
        videolist.add(new VideoInfo("cctv15 音乐","直播","http://120.241.133.167/outlivecloud-cdn.ysp.cctv.cn/cctv/2000205003.m3u8"));
        videolist.add(new VideoInfo("cctv17 农业农村","直播","http://ivi.bupt.edu.cn/hls/cctv17hd.m3u8"));

        VideoAdapter videoAdapter=new VideoAdapter(videolist);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(videoAdapter);

//        //监听listview中的每一项
//        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                VideoInfo videoInfo = videolist.get(position);
////                Toast.makeText(SearchActivity.this,videoInfo.getVideoname(),Toast.LENGTH_SHORT).show();
//                //从当前页面跳转到播放页面，并将选中视频信息传递过去
//                Intent intent=new Intent(SearchActivity.this,PlayActivity.class);
//                intent.putExtra("url",videoInfo.getVideourl());
//                intent.putExtra("type","live");
//                startActivity(intent);
//            }
//        });
    }

    //获取控件
    private void findView(){
        mSearch_edit=(EditText) findViewById(R.id.search_edit);
        mSearchButton=findViewById(R.id.search_button);
        mSpinner=findViewById(R.id.mSpinner);
        recyclerView =(RecyclerView) findViewById(R.id.videolist);
    }

    private void setSpinner(){

        ArrayAdapter<String> spinnerAdapter=new ArrayAdapter<>(SearchActivity.this,android.R.layout.simple_spinner_item,spinnerItems);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(spinnerAdapter);
    }

    //添加监听
    private void addListener(){
        mSearch_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(event!=null&& KeyEvent.KEYCODE_ENTER==event.getKeyCode()&&KeyEvent.ACTION_DOWN==event.getAction()){
                    search_video();
                    return true;
                }
                return false;
            }
        });
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_video();
            }
        });

        //播放源监听
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

//                Toast.makeText(SearchActivity.this,position+"当前选中："+spinnerItems[position],Toast.LENGTH_SHORT).show();

                if(position==4||position==5){
                    Toast.makeText(SearchActivity.this,position+"当前选中的不可用，请更换其他源",Toast.LENGTH_SHORT).show();
                }else {
                    selectedItemIdx=position;
                    currentplaySourceRule=playSourceRuleManager.getPlaySourceRules().get(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedItemIdx=0;
            }
        });
    }


    //搜索视频
    private void search_video(){
        String keyword=mSearch_edit.getText().toString();
        Toast.makeText(SearchActivity.this,"搜索词："+keyword,Toast.LENGTH_SHORT).show();

        search_zy(keyword);


    }

    //搜索资源
    private void search_zy(String searchword){
        RequestBody requestBody=new FormBody.Builder()
                .add(currentplaySourceRule.getSearchrequestbody().getFirstkey(),searchword)
                .add(currentplaySourceRule.getSearchrequestbody().getSecondkey(),currentplaySourceRule.getSearchrequestbody().getSecondvalue())
                .add(currentplaySourceRule.getSearchrequestbody().getThirdkey(),currentplaySourceRule.getSearchrequestbody().getThirdvalue())
//                .add("m","vod-search")
//                .add("wd",searchword)
//                .add("submit","search")
                .build();
        HttpUtil.post(currentplaySourceRule.getSearchurl(), requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SearchActivity.this,"搜索失败，请重新搜索",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String html=response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final ArrayList<VideoInfo> videolist= ParseHtml.parsebaseinfo(html,currentplaySourceRule);
                        VideoAdapter videoAdapter=new VideoAdapter(videolist);
                        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.videolist);
                        recyclerView.setAdapter(videoAdapter);
                    }
                });
            }
        });

    }

}
