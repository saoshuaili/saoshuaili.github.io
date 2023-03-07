package com.coachhe.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: coachhe
 * Date: 2023/2/19
 * Time: 19:53
 * Description:
 */
@WebServlet("/demo01.do")
public class Demo01Servlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("demo01 service....");
        request.getRequestDispatcher("succ.html").forward(request, response);
    }
}
