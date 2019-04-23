package com.wlx.data;

import com.wlx.network.DelMsg;
import com.wlx.network.GetMsg;

import java.util.List;
import java.util.Map;
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
    Observable<DelMsg<Integer>> UploadMsg(@QueryMap Map<String, Object> map);
}