package com.example.day10okhttp.okhttp;

import android.os.Handler;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by yfeng on 2017/10/12.
 */

public abstract class OnUiCallback implements Callback{
    private Handler handler = OkHttpUtils.getInstance().getHandler();
    private Observer o = new Observer<String>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            onFailed("");
        }

        @Override
        public void onNext(String o) {
            try {
                onSuccess(o);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };;
    public abstract void onFailed(Call call, IOException e);
    public abstract void onFailed(String eStr);
    public abstract void onSuccess(String result) throws IOException;

    @Override
    public void onFailure(final Call call, final IOException e) {
        //该方式  存在问题  网络请求也跑到了主线程   待解决
        //该方法就是把  线程post到handler所在的线程
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                onFailed(call, e);
//            }
//        });

        String eStr = e.getMessage();
        Observable.just(eStr).subscribe(o);
    }

    @Override
    public void onResponse(final Call call, final Response response) throws IOException {
        final String result = response.body().string();
        //该方式  存在问题  网络请求也跑到了主线程   待解决
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    onSuccess(result);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        Observable.just(result).observeOn(AndroidSchedulers.mainThread()).subscribe(o);

    }
}
