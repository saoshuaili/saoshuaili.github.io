package com.coachhe.filters;


import com.coachhe.trans.TransactionManager;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.sql.SQLException;

/**
 * @author CoachHe
 * @date 2023/2/21 00:34
 **/
@WebFilter("*.do")
public class OpenSessionInViewFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        try {
            TransactionManager.beginTrans();
            filterChain.doFilter(servletRequest, servletResponse);
            TransactionManager.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                TransactionManager.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
