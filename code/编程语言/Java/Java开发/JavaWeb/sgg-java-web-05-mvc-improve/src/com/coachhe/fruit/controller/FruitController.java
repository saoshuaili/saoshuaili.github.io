package com.coachhe.fruit.controller;

import com.coachhe.fruit.dao.FruitDAO;
import com.coachhe.fruit.dao.impl.FruitDAOImpl;
import com.coachhe.fruit.pojo.Fruit;
import com.coachhe.servlets.ViewBaseServlet;
import com.coachhe.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * @author CoachHe
 * @date 2023/1/31 00:59
 **/
public class FruitController {

    private FruitDAO fruitDAO = new FruitDAOImpl();

    private String index(String oper, String keyword, Integer pageNo, HttpServletRequest request) {

        // 默认查询第一页的数据
        List<Fruit> fruitList = fruitDAO.getFruitList(pageNo);

        // 保存至 session 作用域
        HttpSession session = request.getSession();
        // 保存水果列表
        session.setAttribute("fruitList", fruitList);
        // 保存当前页面
        session.setAttribute("pageNo", pageNo);

//        super.processTemplate("index", request, response);
        return "redirect:fruit.do";
    }

    private String add(String fname, Integer price, Integer fcount, String remark) {

        Fruit fruit = new Fruit(0, fname, price, fcount, remark);

        fruitDAO.addFruit(fruit);

//        response.sendRedirect("fruit.do");
        return "redirect:fruit.do";
    }

    private String edit(Integer fid, HttpServletRequest request) {
        String fidStr = request.getParameter("fid");
        if (StringUtil.isNotEmpty(fidStr)) {
            Fruit fruit = fruitDAO.getFruitByFid(fid);
            request.setAttribute("fruit", fruit);
//            super.processTemplate("edit", request, response);
            return "edit";
        }
        return "error";
    }

    private String del(HttpServletRequest request) {
        String fidStr = request.getParameter("fid");
        if (StringUtil.isNotEmpty(fidStr)) {
            int fid = Integer.parseInt(fidStr);
            fruitDAO.delFruitByFid(fid);
            System.out.println("delete successfully");
        }

//        response.sendRedirect("fruit.do");
        return "redirect:fruit.do";
    }

    private String update(Integer fid, String fname, Integer price, Integer fcount, String remark) {

        // 3.执行更新
        fruitDAO.updateFruit(new Fruit(fid, fname, price, fcount, remark));
        System.out.println("update successfully");

        return "redirect:fruit.do";
    }


}
