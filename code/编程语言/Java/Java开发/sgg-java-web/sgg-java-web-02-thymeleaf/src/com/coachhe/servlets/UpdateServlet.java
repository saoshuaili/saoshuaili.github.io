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
 * @author CoachHe
 * @date 2023/1/30 01:35
 **/
@WebServlet("/update.do")
public class UpdateServlet extends ViewBaseServlet{

    private FruitDAO fruitDAO = new FruitDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1.设置编码
        request.setCharacterEncoding("utf-8");

        // 2.获取参数
        String fidStr = request.getParameter("fid");
        int fid = Integer.parseInt(fidStr);
        String fname = request.getParameter("fname");
        String priceStr = request.getParameter("price");
        String fcountStr = request.getParameter("fcount");
        int price = Integer.parseInt(priceStr);
        int fcount = Integer.parseInt(fcountStr);
        String remark = request.getParameter("remark");

        // 3.执行更新
        fruitDAO.updateFruit(new Fruit(fid, fname, price, fcount, remark));
        System.out.println("update successfully");

        // 4. 资源跳转
//        super.processTemplate("index", request, response);
        // 此处需要重定向，目的是重新给IndexServlet发请求，然后覆盖到session中
        response.sendRedirect("index");
    }


}
