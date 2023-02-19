package com.coachhe.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: coachhe
 * Date: 2023/2/19
 * Time: 19:58
 * Description:
 */
//@WebFilter("/demo01.do")
public class Demo01Filter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        System.out.println("helloA");

        // 放行
        filterChain.doFilter(servletRequest, servletResponse);

        System.out.println("helloA2");

    }

    @Override
    public void destroy() {

    }
}
