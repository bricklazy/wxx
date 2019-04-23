package com.wlx.network;

import android.os.Build;
import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RspCheckInterceptor implements Interceptor{

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {

        Request request = chain.request();
        request = chain.request().newBuilder().addHeader("User-Agent", "Mozilla/5.0 (Linux; Android "+ Build.VERSION.RELEASE+"; "+ Build.MODEL+" Build/"+Build.BRAND+") AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Mobile Safari/537.36").build();
        Response response = chain.proceed(request);
//        try {
//            ResponseBody rspBody = response.body();
//            JSONObject jsonObject = new JSONObject(InterceptorUtils.getRspData(rspBody));
//            int status = jsonObject.getInt("code");
//            if (status < 200 || status >= 300){
//                throw new IOException(jsonObject.getString("msg"));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            throw new IOException("parase data error");
//        }catch (Exception e){
//            if (e instanceof IOException){
//                throw (IOException)e;
//            }
//        }

        return response;
    }
}