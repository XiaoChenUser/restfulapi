package com.restfulapi.hbase.utils;

import com.google.gson.Gson;
import com.restfulapi.hbase.entity.AirConData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class StrUtils {

    public static String tmGen(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }
    public static boolean isValidStrVal(String str) {
        return str != null && str.length() > 0;
    }

    /**
     * input time string(yyyy-MM-dd HH-mm-ss) to timestamp.
     * @param time
     * @return
     */
    public static String dataToStamp(String time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        try {
            Date date = simpleDateFormat.parse(time);
            long ts = date.getTime();
            return String.valueOf(ts);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String airConToJson(AirConData airConData){
        if(airConData!=null){
            Gson gson = new Gson();
            return gson.toJson(airConData);
        }
        return null;
    }
}
