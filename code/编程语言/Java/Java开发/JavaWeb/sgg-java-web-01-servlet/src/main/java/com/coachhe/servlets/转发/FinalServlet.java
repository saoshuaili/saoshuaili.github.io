package com.coachhe.servlets.转发;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: CoachHe
 * @Date: 2023/1/25 01:40
 * 最终处理的 Servlet，用来观察效果，表明真实被调用了
 */
public class FinalServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("transger servlet2...");
    }
}
