package com.example.moviepro.Utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {
    public static void post(String address, RequestBody requestBody, okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(address)
//                .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36")
//                .addHeader("Origin","http://135zy0.com")
//                .addHeader("Referer","http://135zy0.com/index.php?m=vod-search")
//                .addHeader("Host","135zy0.com")
//                .addHeader("Cookie","PHPSESSID=7ije843i0vkvcbseq03ifsfs13")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void get(String address, okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
