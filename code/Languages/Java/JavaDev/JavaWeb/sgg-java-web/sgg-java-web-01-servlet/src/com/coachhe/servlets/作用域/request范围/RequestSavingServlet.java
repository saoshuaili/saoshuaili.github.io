package com.coachhe.servlets.作用域.request范围;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author CoachHe
 * @date 2023/1/29 23:10
 * 一次请求（request）范围内保存数据
 **/
@WebServlet("/requestServlet")
public class RequestSavingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 向request保存作用域保存数据
        request.setAttribute("uname", "coachhe");
        // 2. 客户端重定向,第二次请求无法获取上一次请求信息
        response.sendRedirect("redirectRequestServlet");
        // 3. 服务端转发，属于同一次请求，可以感知属性
//        request.getRequestDispatcher("redirectRequestServlet").forward(request, response);
    }
}
