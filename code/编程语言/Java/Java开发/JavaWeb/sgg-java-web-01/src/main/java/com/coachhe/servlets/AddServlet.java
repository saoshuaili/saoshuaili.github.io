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

//        // tomcat8 之前设置 GET 请求的编码方式
//        String fname = request.getParameter("fname");
//        // 1. 将字符串打散成字节数组
//        byte[] bytes = fname.getBytes("ISO-8859-1");
//        // 2. 将字节数组按照设定的编码重新组装成字符串
//        fname = new String(bytes, "UTF-8");

        // post方式下，设置编码，防止中文乱码(tomcat8之前和之后都这么处理）
        request.setCharacterEncoding("UTF-8");
        String fname = request.getParameter("fname");
        String priceStr = request.getParameter("price");
        Integer price = Integer.parseInt(priceStr);
        String fcountStr = request.getParameter("fcount");
        Integer fcount = Integer.parseInt(fcountStr);
        String remark = request.getParameter("remark");

        // get方式目前不需要设置编码（基于 tomcat8）

//        System.out.println("fname=" + fname);
//        System.out.println("price=" + price);
//        System.out.println("fcount=" + fcount);
//        System.out.println("remark=" + remark);

        FruitDAO fruitDAO = new FruitDAOImpl();
        boolean flag = fruitDAO.addFruit(new Fruit(0, fname, price, fcount, remark));


        System.out.println(flag ? "添加成功" : "添加失败");

    }
}
