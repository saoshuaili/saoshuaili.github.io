package com.coachhe.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created with IntelliJ IDEA.
 * User: coachhe
 * Date: 2023/3/1
 * Time: 13:17
 * Description:
 */
@WebListener
public class MyServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("Servlet上下文初始化动作被我监听到了");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("Servlet上下文销毁动作被我监听到了");
    }
}
