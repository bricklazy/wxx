package com.wlx.data;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.wlx.base.BaseCallBack;
import com.wlx.network.DelMsg;
import com.wlx.network.GetMsg;
import com.wlx.network.RetrofitManager;
import com.wlx.utils.GsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DataModel {

    private static final String TAG = "DataModel";
    private int page = 1;
    private final int pageSize = 20;
    private int pageCount = 1;

    public void getDatas(String imei, BaseCallBack<List<Data>> baseCallBack) {
        page = 1;
        getMoreDatas(imei, baseCallBack);
    }

    public void getMoreDatas(String imei, BaseCallBack<List<Data>> baseCallBack) {
        Map<String, Object> map = new HashMap<>();
        if(page>pageCount){
            baseCallBack.faild("没有了");
            return;
        }
        map.put("imei", imei);
        map.put("page", page);
        map.put("rows", pageSize);

        RetrofitManager.getInstance()
                .createReq(DataApi.class)
                .GetMsg(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetMsg<List<Data>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        baseCallBack.faild(e.getMessage());
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(GetMsg<List<Data>> getMsg) {
                        if(getMsg.getRCode()==0) {
                            page+=1;
                            pageCount = getMsg.getCount();
                            baseCallBack.success(getMsg.getRows());
                        }
                    }
                });
    }


    public void delMsg(int id, String imei,BaseCallBack<Integer> baseCallBack) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("imei", imei);

        RetrofitManager.getInstance()
                .createReq(DataApi.class)
                .DelMsg(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DelMsg<Integer>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        baseCallBack.faild(e.getMessage());
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(DelMsg<Integer> delMsg) {
                        if(delMsg.getRCode()==0) {
                            baseCallBack.success(0);
                        }
                    }
                });
    }

    public void UploadMsg(String imei, String msg_type, String msg, String imgName, BaseCallBack<Integer> baseCallBack) {
        Map<String, Object> map = new HashMap<>();
        map.put("imei", imei);
        map.put("messageType", msg_type);
        map.put("message", msg);
        map.put("imgName", imgName);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("imei", imei);
            jsonObject.put("messageType", msg_type);
            jsonObject.put("imgName", imgName);
            jsonObject.put("message", msg);
        }catch (Exception e){
            e.printStackTrace();
        }

//        String body = "imei="+ imei +"&messageType=" + msg_type+"&imgName="+ imgName+"&message="+msg;

//        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), jsonObject.toString());
//        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), body);

        RetrofitManager.getInstance()
                .createReq(DataApi.class)
                .UploadMsg(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DelMsg<Integer>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        baseCallBack.faild(e.getMessage());
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(DelMsg<Integer> delMsg) {
                        if(delMsg.getRCode()==0) {
                            baseCallBack.success(0);
                        }
                    }
                });
    }
}
