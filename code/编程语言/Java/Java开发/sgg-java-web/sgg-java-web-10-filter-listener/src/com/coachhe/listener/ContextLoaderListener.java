package com.coachhe.listener;

import com.coachhe.ioc.BeanFactory;
import com.coachhe.ioc.ClassPathXmlApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created with IntelliJ IDEA.
 * User: coachhe
 * Date: 2023/3/5
 * Time: 20:18
 * Description:
 * 监听上下文启动，在上下文启动的时候去创建IOC容器，然后将其保存在application作用域
 * 后面中央控制器再从application作用域中获取IOC容器
 */
@WebListener
public class ContextLoaderListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        BeanFactory beanFactory = new ClassPathXmlApplicationContext();
        ServletContext application = servletContextEvent.getServletContext();
        application.setAttribute("beanFactory", beanFactory);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
