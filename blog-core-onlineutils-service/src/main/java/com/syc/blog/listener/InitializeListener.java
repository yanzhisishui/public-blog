package com.syc.blog.listener;

import com.syc.blog.utils.IllegalWordsHelper;
import lombok.SneakyThrows;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class InitializeListener implements ServletContextListener {
    @SneakyThrows
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        //初始化敏感词库
        IllegalWordsHelper.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

}
