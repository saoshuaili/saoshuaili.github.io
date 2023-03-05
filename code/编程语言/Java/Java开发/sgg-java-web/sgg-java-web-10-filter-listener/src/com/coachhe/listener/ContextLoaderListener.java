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
        // 1. 获取上下文对象
        ServletContext application = servletContextEvent.getServletContext();
        // 2. 获取上下文初始化参数
        String contextConfigLocation = application.getInitParameter("contextConfigLocation");
        // 3. 创建ioc容器
        BeanFactory beanFactory = new ClassPathXmlApplicationContext(contextConfigLocation);
        // 4. 将ioc容器保存到application作用域
        application.setAttribute("beanFactory", beanFactory);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
