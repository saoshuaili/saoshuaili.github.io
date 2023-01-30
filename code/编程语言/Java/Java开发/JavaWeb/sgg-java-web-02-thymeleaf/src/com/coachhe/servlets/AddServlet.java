package com.coachhe.servlets;

import com.coachhe.fruit.dao.FruitDAO;
import com.coachhe.fruit.dao.impl.FruitDAOImpl;
import com.coachhe.fruit.pojo.Fruit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: coachhe
 * Date: 2023/1/30
 * Time: 20:07
 * Description:
 */
@WebServlet("/add.do")
public class AddServlet extends ViewBaseServlet{

    private FruitDAO fruitDAO = new FruitDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置编码
        request.setCharacterEncoding("utf-8");

        // 获取参数
        String fname = request.getParameter("fname");
        String priceStr = request.getParameter("price");
        String fcountStr = request.getParameter("fcount");
        int price = Integer.parseInt(priceStr);
        int fcount = Integer.parseInt(fcountStr);
        String remark = request.getParameter("remark");

        Fruit fruit = new Fruit(0, fname, price, fcount, remark);

        fruitDAO.addFruit(fruit);

        System.out.println("新增成功");

        response.sendRedirect("/index");
    }
}
