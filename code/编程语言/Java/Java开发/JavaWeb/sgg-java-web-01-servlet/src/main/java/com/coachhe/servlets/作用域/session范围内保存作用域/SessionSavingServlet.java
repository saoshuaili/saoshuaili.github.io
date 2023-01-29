package com.coachhe.servlets.作用域.session范围内保存作用域;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author CoachHe
 * @date 2023/1/29 23:10
 * 一次会话(session)范围内保存数据
 **/
@WebServlet("/sessionServlet")
public class SessionSavingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 向session保存作用域保存数据
        request.getSession().setAttribute("uname", "coachhe");
        // 2. 客户端重定向,第二次请求无法获取上一次请求信息
        response.sendRedirect("redirectSessionServlet");
    }
}
