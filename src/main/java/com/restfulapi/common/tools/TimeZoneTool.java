package com.restfulapi.common.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeZoneTool {
    public static final Logger log = LoggerFactory.getLogger(TimeZoneTool.class);
    public static final SimpleDateFormat DATEDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat DATEY = new SimpleDateFormat("yyyy");
    private static SimpleDateFormat DATEM = new SimpleDateFormat("MM");
    private static SimpleDateFormat DATED = new SimpleDateFormat("dd");
    private static SimpleDateFormat DATEH = new SimpleDateFormat("HH");
    private static SimpleDateFormat DATEmm = new SimpleDateFormat("mm");
    private static SimpleDateFormat DATEss = new SimpleDateFormat("ss");

    public static String formatTimeAfterEight(String time) throws Exception {
        Calendar calendar = Calendar.getInstance();
        Date date = DATEDF.parse(time);
        calendar.set(Calendar.YEAR, Integer.valueOf(DATEY.format(date)));
        calendar.set(Calendar.MONTH, Integer.valueOf(DATEM.format(date)) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(DATED.format(date)));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(DATEH.format(date)) + 8);
        calendar.set(Calendar.MINUTE, Integer.valueOf(DATEmm.format(date)));
        calendar.set(Calendar.SECOND, Integer.valueOf(DATEss.format(date)));
        return DATEDF.format(calendar.getTime());
    }

    public static String formatTimeBeforeEight(String time) throws Exception {
        Calendar calendar = Calendar.getInstance();
        Date date = DATEDF.parse(time);
        calendar.set(Calendar.YEAR, Integer.valueOf(DATEY.format(date)));
        calendar.set(Calendar.MONTH, Integer.valueOf(DATEM.format(date)) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(DATED.format(date)));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(DATEH.format(date)) - 8);
        calendar.set(Calendar.MINUTE, Integer.valueOf(DATEmm.format(date)));
        calendar.set(Calendar.SECOND, Integer.valueOf(DATEss.format(date)));
        return DATEDF.format(calendar.getTime());
    }
}
