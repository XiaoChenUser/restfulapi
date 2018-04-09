package com.restfulapi.quartz.jobs;

import com.restfulapi.common.tools.TimeZoneTool;
import com.restfulapi.service.AirTaskService;
import com.restfulapi.service.DehumidifierTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by q on 2017/8/28.
 */
@Component
public class ScheduleJob {
    private static final Logger log = LoggerFactory.getLogger(ScheduleJob.class);
    public static final String PREFIX_REDIS_NEWESTSTATUS = "G-N-*";
    public static final SimpleDateFormat DATETT =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
    public static final SimpleDateFormat DATEFF =new SimpleDateFormat("yyyyMMddHHmmss");
    @Autowired
    AirTaskService airTaskService;
    @Autowired
    DehumidifierTaskService dehumidifierTaskService;

    public String getCalDate(Integer integer)throws Exception{
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, integer);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        return DATEFF.format(cal.getTime());
    }
    public void job1() throws Exception {
        String startTime = TimeZoneTool.formatTimeBeforeEight(airTaskService.getCalDate(-1));
        String endTime = TimeZoneTool.formatTimeBeforeEight(airTaskService.getCalDate(0));
        log.debug("startTime {},endTime {}",startTime,endTime);
        airTaskService.scanRealData(startTime,endTime);
        System.out.println();
        log.debug("空调定时任务执行成功");
    }

    public void job2() throws Exception {
        String startTime = TimeZoneTool.formatTimeBeforeEight(airTaskService.getCalDate(-1));
        String endTime = TimeZoneTool.formatTimeBeforeEight(airTaskService.getCalDate(0));
        log.debug("startTime {},endTime {}",startTime,endTime);
        dehumidifierTaskService.dehumidifierTask(startTime,endTime);
        System.out.println();
        log.debug("除湿机定时任务执行成功");
    }

}
