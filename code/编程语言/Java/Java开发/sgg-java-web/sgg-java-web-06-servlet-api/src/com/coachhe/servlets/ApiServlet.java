package com.coachhe.servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Created with IntelliJ IDEA.
 * User: coachhe
 * Date: 2023/2/2
 * Time: 13:23
 * Description:
 */
public class ApiServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        ServletConfig servletConfig = getServletConfig();
        String name = servletConfig.getInitParameter("name");
        System.out.println("init value = " + name);
    }
}

// Servlet生命周期： 实例化，初始化，服务，销毁
