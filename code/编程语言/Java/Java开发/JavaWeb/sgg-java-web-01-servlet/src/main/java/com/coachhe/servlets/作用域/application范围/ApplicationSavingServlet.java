package com.coachhe.servlets.作用域.application范围;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author CoachHe
 * @date 2023/1/29 23:10
 * 整个应用程序范围(application)内保存数据
 **/
@WebServlet("/applicationServlet")
public class ApplicationSavingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 向application保存作用域保存数据
        // ServletContext: Servlet上下文
        ServletContext servletContext = request.getServletContext();
        servletContext.setAttribute("uname", "coachhe");
        // 2. 客户端重定向,第二次请求无法获取上一次请求信息
        response.sendRedirect("redirectSessionServlet");
    }
}
