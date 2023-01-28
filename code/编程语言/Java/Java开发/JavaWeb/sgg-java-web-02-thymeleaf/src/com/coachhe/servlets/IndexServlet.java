package com.coachhe.servlets;

import com.coachhe.fruit.dao.FruitDAO;
import com.coachhe.fruit.dao.impl.FruitDAOImpl;
import com.coachhe.fruit.pojo.Fruit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * @Author: CoachHe
 * @Date: 2023/1/25 12:32
 * 用来展示 thymeleaf
 * Servlet从 3.0 版本开始支持注解方式的注册
 */
@WebServlet("/index")
public class IndexServlet extends ViewBaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        FruitDAO fruitDAO = new FruitDAOImpl();
        List<Fruit> fruitList = fruitDAO.getFruitList();
        // 保存至 session 作用域
        HttpSession session = request.getSession();        session.setAttribute("fruitList", fruitList);
        // 此处的视图名称是 index
        // 那么 thymeleaf 会将这个逻辑视图名称对应到物理视图名称上去
        // 逻辑视图名称： index
        // 物理视图名称： view-prefix + 逻辑视图名称 + view-suffix
        // 所以真实的视图名称是： /index.html
        super.processTemplate("index", request, response);
    }
}
