package com.restfulapi.quartz.jobs;


import com.restfulapi.quartz.handler.ApplicationContextHandler;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by q on 2017/8/26.
 */

public class ScheduleControll implements Job {
    private static final Logger log= LoggerFactory.getLogger(ScheduleControll.class);


    private SimpleDateFormat dateFormat() {
        return new SimpleDateFormat("HH:mm:ss");
    }
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println(" ");
        log.debug("定时任务开始,time:{} ",dateFormat().format(new Date()));
        /**
         * ApplicationContextHandler 提取普通类
         */
        ScheduleJob scheduleJob = (ScheduleJob) ApplicationContextHandler.getBean(ScheduleJob.class);
        try {
            scheduleJob.job1();
        } catch (Exception e) {
            log.error("空调定时任务失败"+e.getMessage());
        }
        try {
            scheduleJob.job2();
        } catch (Exception e) {
            log.error("除湿机定时任务失败"+e.getMessage());
        }

    }
}

