package com.example.day10okhttp.okhttp;

import android.os.Environment;
import android.util.Log;

import com.example.day10okhttp.MyApp;
import com.example.day10okhttp.utils.NetUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import rx.Observer;

/**
 * Created by yfeng on 2017/10/12.
 */

public class OkHttpUtils {
//    private Handler handler = new Handler();
//    public Handler getHandler(){
//        return handler;
//    }



    //单例
    private static OkHttpUtils okHttpUtils = new OkHttpUtils();
    private OkHttpUtils(){};
    public static OkHttpUtils getInstance(){
        return okHttpUtils;
    }
    public static Observer o;
    private OkHttpClient client;
    private void initOkHttpClient(){

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.i("=========log", "log: " + message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if(client == null){
            File file = new File(Environment.getExternalStorageDirectory()+"/cache");
            if(!file.exists()){
                file.mkdirs();
            }


            client = new OkHttpClient.Builder()
                    .connectTimeout(8, TimeUnit.SECONDS)
                    .writeTimeout(8,TimeUnit.SECONDS)
                    .readTimeout(8, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                   .addNetworkInterceptor(new CacheInterceptor())
                    //参数1：缓存文件
                    //设置最大的缓存大小  如果超出  则会使用LRU算法进行替换
                    .cache(new Cache(file, 1024*1024*10))
                    .build();

        }
    }

    //公用的get请求方法  完成的功能不确定
    public void doGet(String url, Callback callback){
        initOkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    class CacheInterceptor implements Interceptor{

        @Override
        public Response intercept(Chain chain) throws IOException {
            //有网状态下的 最大缓存时间  1分钟
            int maxAge = 60;
            //无网状态下的 最大缓存时间  1天
            int maxStale = 60 * 60 * 24;
            Request request = chain.request();
            if(NetUtils.isNetAvailable(MyApp.context)){
                Log.i("======Available", "intercept: ");
                request.newBuilder().cacheControl(CacheControl.FORCE_NETWORK).build();
            }else{
                Log.i("======not Available", "intercept: ");
                request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
            }
            Response response = chain.proceed(request);
            if(NetUtils.isNetAvailable(MyApp.context)){
                response.newBuilder()
                        .removeHeader("Pragma")
//                        .removeHeader("Cache-Control")
                        //cache for cache seconds
                        .header("Cache-Control", "public, max-age="+maxAge)
                        .build();
            }else{
                response.newBuilder()
                        .removeHeader("Pragma")
//                        .removeHeader("Cache-Control")
                        //cache for cache seconds
                        .header("Cache-Control", "public, only-if-cached, max-stale="+maxStale)
                        .build();
            }
            return response;
        }
    }

}
