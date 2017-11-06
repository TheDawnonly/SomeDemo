package com.example.retrofitdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.retrofitdemo.net.IGetRequest;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tv);
        request();
    }

    //使用retrofit发起网络请求
    public void request() {

        //步骤4:创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fy.iciba.com/") // 设置 网络请求 Url
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        // 步骤5:创建 网络请求接口 的实例
        IGetRequest request = retrofit.create(IGetRequest.class);

        //对 发送请求 进行封装
        Call<Bean> call = request.getData();
        //步骤6:发送网络请求(异步)
        call.enqueue(new Callback<Bean>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<Bean> call, Response<Bean> response) {
                // 步骤7：处理返回的数据结果
                response.body().show();
//                tv.setText(s);
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<Bean> call, Throwable throwable) {
                System.out.println("连接失败");
            }
        });

       //a=fy&f=auto&t=auto&w=hello%20world
        Map<String, Object> map = new HashMap<>();
        map.put("a", "fy");
        map.put("f", "auto");
        map.put("w", "hello%20world");
        request.getDataUseRx("9").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Bean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Bean bean) {
                bean.show();
                showView();
            }
        });
    }

    public void showView(){

    }
}
