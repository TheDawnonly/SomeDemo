package com.example.day10okhttp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.day10okhttp.okhttp.OkHttpUtils;
import com.example.day10okhttp.okhttp.OnUiCallback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {

    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tv);

        OkHttpUtils.getInstance().doGet("http://www.baidu.com", new OnUiCallback() {
            @Override
            public void onFailed(Call call, IOException e) {

            }

            @Override
            public void onFailed(String str) {

            }

            @Override
            public void onSuccess(String result) throws IOException{
                Log.i("=====", "onResponse: " + result);
                tv.setText(result);


            }


//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.i("=====", "onResponse: " + response.body().string());
//            }
        });

        OkHttpClient clien = new OkHttpClient();
        Request requ = new Request.Builder().build();
        clien.newCall(requ).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Observable.just(response.body().string()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        showView();
                    }
                });
            }
        });

//


    }

    public void showView(){

    }

}
