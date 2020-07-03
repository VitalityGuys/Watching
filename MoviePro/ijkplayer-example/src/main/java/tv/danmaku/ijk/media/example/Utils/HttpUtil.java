package tv.danmaku.ijk.media.example.Utils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {


    public static void get(String address, okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(address)
                .addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .addHeader("Cookie","_uuid=4F93A393-F46D-F619-0C10-456B181C28A997789infoc; buvid3=3096F810-F50D-4E38-8302-768C2962DB99155842infoc; CURRENT_FNVAL=16; stardustvideo=1; LIVE_BUVID=AUTO4315766544416478; laboratory=1-1; rpdid=|(u|JRul|RR~0J'ul~YYlRRRk; sid=it7vs9jh; im_notify_type_13684288=0; PVID=2; DedeUserID=260164644; DedeUserID__ckMd5=e49b49c42494fdbc; SESSDATA=43112894%2C1600772730%2C9ccdc*31; bili_jct=1201dce15bff748aec5b0d41f3623401")
                .addHeader("Host","comment.bilibili.com")
                .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:77.0) Gecko/20100101 Firefox/77.0")
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static String synGet(String address){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(address)
                .build();
        Call call=client.newCall(request);
        try {
            String result=call.execute().body().string();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
