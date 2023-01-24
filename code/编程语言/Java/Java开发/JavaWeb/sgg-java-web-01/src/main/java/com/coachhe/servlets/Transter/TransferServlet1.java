package com.coachhe.servlets.Transter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: CoachHe
 * @Date: 2023/1/25 01:25
 * 演示服务器内部转发以及客户端重定向
 */
public class TransferServlet1 extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("transfer servlet1");
        // 服务器端内部转发
        request.getRequestDispatcher("TransferServlet2").forward(request, response);

    }
}
