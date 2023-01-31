package com.coachhe.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: coachhe
 * Date: 2023/1/31
 * Time: 18:46
 * Description:
 */
@WebServlet("*.do") // 拦截所有以.do结尾的请求
public class DispatcherServlet extends ViewBaseServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置编码
        request.setCharacterEncoding("utf-8");

        // 假设url是： http://localhost:8080/mvc/hello.do
        // 那么servletPath是： /hello.do
        String servletPath = request.getServletPath();
        // 通过/hello.do得到hello
        servletPath = servletPath.substring(1);
        int lastDotIndex = servletPath.lastIndexOf(".do");
        servletPath = servletPath.substring(0, lastDotIndex);




    }
}
