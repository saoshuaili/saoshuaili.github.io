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
@WebFilter("*.do")
public class Filter01 implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        System.out.println("A");

        // 放行
        filterChain.doFilter(servletRequest, servletResponse);

        System.out.println("A2");

    }

    @Override
    public void destroy() {

    }
}
