package com.coachhe.servlets;


import com.coachhe.fruit.dao.FruitDAO;
import com.coachhe.fruit.dao.impl.FruitDAOImpl;
import com.coachhe.fruit.pojo.Fruit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: CoachHe
 * @Date: 2023-01-18 04:00:00
 */
public class AddServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String fname = request.getParameter("fname");
        String priceStr = request.getParameter("price");
        Integer price = Integer.parseInt(priceStr);
        String fcountStr = request.getParameter("fcount");
        Integer fcount = Integer.parseInt(fcountStr);
        String remark = request.getParameter("remark");

//        System.out.println("fname=" + fname);
//        System.out.println("price=" + price);
//        System.out.println("fcount=" + fcount);
//        System.out.println("remark=" + remark);

        FruitDAO fruitDAO = new FruitDAOImpl();
        boolean flag = fruitDAO.addFruit(new Fruit(0, fname, price, fcount, remark));


        System.out.println(flag ? "添加成功" : "添加失败");

    }
}
