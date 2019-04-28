package com.wlx.data;

import com.wlx.network.DelMsg;
import com.wlx.network.GetMsg;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface DataApi {

    @GET("GetMsg")
    Observable<GetMsg<List<Data>>> GetMsg(@QueryMap Map<String, Object> map);

    @GET("DelMsg")
    Observable<DelMsg<Integer>> DelMsg(@QueryMap Map<String, Object> map);

    @POST("UploadMsg")
    @FormUrlEncoded
    Observable<DelMsg<Integer>> UploadMsg(@FieldMap Map<String, Object> map);

    @POST("UploadMsg")
    @FormUrlEncoded
    Observable<DelMsg<Integer>> UploadMsg(@Body RequestBody body);
}
