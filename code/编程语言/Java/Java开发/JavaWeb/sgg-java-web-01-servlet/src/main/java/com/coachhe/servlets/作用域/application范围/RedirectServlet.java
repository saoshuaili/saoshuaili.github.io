package com.coachhe.servlets.作用域.application范围;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author CoachHe
 * @date 2023/1/29 23:13
 * 重定向的Servlet
 **/
@WebServlet("/redirectApplicationServlet")
public class RedirectServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 获取session保存作用域保存的数据，key为uname
        Object unameObj = request.getServletContext().getAttribute("uname");
        System.out.println("uname = " + unameObj);
    }
}
