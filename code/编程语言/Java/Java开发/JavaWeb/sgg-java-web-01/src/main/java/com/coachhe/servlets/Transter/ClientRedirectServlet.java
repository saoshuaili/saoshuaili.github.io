package com.coachhe.servlets.Transter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: CoachHe
 * @Date: 2023/1/25 01:51
 * 演示客户端重定向
 */
public class ClientRedirectServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("客户端重定向");
        // 服务器端内部转发
        response.sendRedirect("FinalServlet");
    }
}
