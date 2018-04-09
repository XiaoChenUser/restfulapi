package com.restfulapi.quartz;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * Created by q on 2017/8/26.
 */
@Configuration
public class QuartzListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    public QuartzManager quartzManager;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            quartzManager.scheduleJobs();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(){
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        return schedulerFactoryBean;
    }

}
