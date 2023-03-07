package com.coachhe.servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

/**
 * Created with IntelliJ IDEA.
 * User: coachhe
 * Date: 2023/2/2
 * Time: 13:23
 * Description:
 */
@WebServlet(urlPatterns = "/api2", initParams = {
        @WebInitParam(name = "name", value = "coachhe"),
        @WebInitParam(name = "age", value = "27")
})
public class ApiServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        ServletConfig servletConfig = getServletConfig();
        String name = servletConfig.getInitParameter("name");
        System.out.println("init value = " + name);

        ServletContext servletContext = getServletContext();
        String contextConfigLocation = servletContext.getInitParameter("contextConfigLocation");
        System.out.println("contextConfigLocation = " + contextConfigLocation);
    }
}

// Servlet生命周期： 实例化，初始化，服务，销毁
