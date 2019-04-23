package com.wlx.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/12/26.
 */

public class DateUtil {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
    public static final String YYYY_MM_DD_hh_mm_ss_sss = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String YYYY_MM_DD_hh_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM = "yyyy-MM";
    public static final String MM_DD_WW = "MM/dd EEE";

    /**
     *
     * @param pattern
     * @param time
     * @return 日期
     */
    public static String dateToString(String pattern, long time){
        simpleDateFormat.applyPattern(pattern);
        return simpleDateFormat.format(time);
    }

}
