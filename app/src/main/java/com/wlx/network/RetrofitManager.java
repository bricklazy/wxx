package com.wlx.network;

import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wlx.utils.Base64;
import com.wlx.utils.Constants;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    public static final int DEFAULT_TIME_OUT = 20;
    private static final String TAG = "RetrofitManager";
    private static RetrofitManager mRetrofitManager;
    private Retrofit.Builder mRetrofitBuilder;
    private Retrofit mRetrofit;

    private RetrofitManager(){
        initRetrofit();
    }

    public static synchronized RetrofitManager getInstance(){

        if (mRetrofitManager == null){
            mRetrofitManager = new RetrofitManager();
        }
        return mRetrofitManager;
    }

    private void initRetrofit() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new RspCheckInterceptor());

        HttpLoggingInterceptor LoginInterceptor = new HttpLoggingInterceptor();
        LoginInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(LoginInterceptor);

        builder.proxy(Proxy.NO_PROXY);
        builder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
//        builder.sslSocketFactory(SSLSocketFactoryUtils.createSSLSocketFactory(), SSLSocketFactoryUtils.createTrustAllManager());
        builder.hostnameVerifier((hostname, session) -> {
            //强行返回true 即验证成功
            return true;
        });

        OkHttpClient client = builder.build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return false;
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .create();

        mRetrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client);
    }

    public <T> T createReq(Class<T> reqServer){
        return createReq(null, reqServer);
    }

    /**
     *
     * @param baseUrl
     * @param reqServer
     * @param <T>
     * @return
     */
    public <T> T createReq(String baseUrl, Class<T> reqServer){
        mRetrofitBuilder.baseUrl(baseUrl == null ? Constants.BASE_URL : baseUrl);
        mRetrofit = mRetrofitBuilder.build();
        return mRetrofit.create(reqServer);
    }

    public static RequestBody builderRequestBody(JSONObject jsonObject){
        if(jsonObject==null){
            return null;
        }

        Log.d(TAG, jsonObject.toString());
        return RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
    }

    public static RequestBody builderRequestBody(JSONArray jsonArray){
        if(jsonArray==null){
            return null;
        }

        Log.d(TAG, jsonArray.toString());
        return RequestBody.create(MediaType.parse("application/json"), jsonArray.toString());
    }

    public static RequestBody builderRequestBodyEncrypt(int type, JSONObject jsonObject){
        if(jsonObject==null){
            return null;
        }

        Log.d(TAG,"原数据："+jsonObject.toString());

        if(type==0) {
            Log.d(TAG, "加密数据："+Base64.encode(jsonObject.toString().getBytes()));
            return RequestBody.create(MediaType.parse("application/json"), Base64.encode(jsonObject.toString().getBytes()));
        }else{
            return RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        }
    }

}