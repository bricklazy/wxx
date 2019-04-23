package com.wlx.base;

public interface BaseCallBack<T> {
    void success(T t);
    void faild(String msg);
}
