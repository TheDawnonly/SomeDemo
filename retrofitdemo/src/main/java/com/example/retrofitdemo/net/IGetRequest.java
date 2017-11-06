package com.example.retrofitdemo.net;

import com.example.retrofitdemo.Bean;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by yfeng on 2017/11/4.
 */

public interface IGetRequest{
    //get注解中的值 就是拼在baseUrl地址后面的值   配合组成一个完成的请求地址
    @GET("ajax.php?a=fy&f=auto&t=auto&w=hello%20world")
    Call<Bean> getData();

    //@Path只能用来替换地址中的变量值
    @GET("{name}?a=fy&f=auto&t=auto&w=hello%20world")
    Observable<Bean> getDataUseRx(@Path("name") String ab);

    @GET("ajax.php?a=fy&f=auto&t=auto&w=hello%20world")
    Observable<Bean> getDataUseRx1();

    @GET("ajax.php?a=fy&f=auto&t=auto&w=hello%20world&")
    Observable<Bean> getDataUseRx2(@QueryMap() Map dfd);

}
