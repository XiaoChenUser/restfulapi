package com.restfulapi.quartz.handler;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by q on 2017/8/28.
 */
@Component
public class ApplicationContextHandler implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    /**
     * 实现ApplicationContextAware接口的context注入函数
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHandler.applicationContext = applicationContext;
    }

    /**
     * 获取 applicationContext
     *
     * @return ApplicationContext
     */
    public static final ApplicationContext getApplicationContext() {
        checkApplicationContext();
        return applicationContext;
    }

    /**
     * 清除 applicationContext
     *
     * @return ApplicationContext
     */
    public static ApplicationContext cleanApplicationContext() {
        ApplicationContext ac = applicationContext;
        applicationContext = null;
        return ac;
    }

    /**
     * 根据名称从静态变量ApplicationContext中获取Bean
     *
     * @param name 此函数不检查name是否为空，后续调用会进行检查并抛出IllegalArgumentException异常
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name) {
        checkApplicationContext();
        return (T) applicationContext.getBean(name);
    }

    /**
     * 根据类型从静态变量ApplicationContext中获取Bean
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        return (T) applicationContext.getBean(clazz);
    }

    /**
     * 检查ApplicationContext是否已注入
     */
    public static final void checkApplicationContext() {
        if (null == ApplicationContextHandler.applicationContext) {
            throw new UnsupportedOperationException("ApplicationContext not ready!");
        }
    }
}
