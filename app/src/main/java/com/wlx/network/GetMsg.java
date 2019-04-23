package com.wlx.network;

public class GetMsg<T> {

    /**
     * count : 9
     * rCode : 0
     * rows :
     */

    private int count;
    private int rCode;
    private T rows;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getRCode() {
        return rCode;
    }

    public void setRCode(int rCode) {
        this.rCode = rCode;
    }

    public T getRows() {
        return rows;
    }

    public void setRows(T rows) {
        this.rows = rows;
    }
}
