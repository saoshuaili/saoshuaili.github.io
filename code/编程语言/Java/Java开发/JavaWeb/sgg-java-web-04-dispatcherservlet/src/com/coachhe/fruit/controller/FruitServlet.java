package com.coachhe.fruit.controller;

import com.coachhe.fruit.dao.FruitDAO;
import com.coachhe.fruit.dao.impl.FruitDAOImpl;
import com.coachhe.fruit.pojo.Fruit;
import com.coachhe.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author CoachHe
 * @date 2023/1/31 00:59
 **/
public class FruitServlet extends ViewBaseServlet {

    private FruitDAO fruitDAO = new FruitDAOImpl();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 设置编码
        request.setCharacterEncoding("utf-8");

        String operator = request.getParameter("operate");

        if (StringUtil.isEmpty(operator)) {
            operator = "index";
        }

        // 获取当前类中的所有方法
        Method[] methods = this.getClass().getDeclaredMethods();
        for (Method m : methods) {
            // 获取方法名
            String methodName = m.getName();
            if (operator.equals(methodName)) {
                // 找到和operate同名的方法，那么通过反射调用它
                try {
                    m.invoke(this, request, response);
                    return;
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        throw new RuntimeException("operate值非法");

        // 原始处理方法，但是switch case很可能会很多，因此换为上面所给出的反射方法
//        switch (operator) {
//            case "index":
//                index(request, response);
//                break;
//            case "add":
//                add(request, response);
//                break;
//            case "edit":
//                edit(request, response);
//                break;
//            case "del":
//                del(request, response);
//                break;
//            case "update":
//                update(request, response);
//                break;
//            default:
//                throw new RuntimeException("operate值非法");
//        }
    }

    private void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int pageNo = 1;

        String pageNoStr = request.getParameter("pageNo");
        if (StringUtil.isNotEmpty(pageNoStr)) {
            pageNo = Integer.parseInt(pageNoStr);
        }

        // 默认查询第一页的数据
        List<Fruit> fruitList = fruitDAO.getFruitList(pageNo);

        // 保存至 session 作用域
        HttpSession session = request.getSession();
        // 保存水果列表
        session.setAttribute("fruitList", fruitList);
        // 保存当前页面
        session.setAttribute("pageNo", pageNo);

        super.processTemplate("index", request, response);
    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

        response.sendRedirect("fruit.do");
    }

    private void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fidStr = request.getParameter("fid");
        if (StringUtil.isNotEmpty(fidStr)) {
            int fid = Integer.parseInt(fidStr);
            Fruit fruit = fruitDAO.getFruitByFid(fid);
            request.setAttribute("fruit", fruit);
            super.processTemplate("edit", request, response);
        }
    }

    private void del(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fidStr = request.getParameter("fid");
        if (StringUtil.isNotEmpty(fidStr)) {
            int fid = Integer.parseInt(fidStr);
            fruitDAO.delFruitByFid(fid);
            System.out.println("delete successfully");
        }

        response.sendRedirect("fruit.do");
    }

    private void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        response.sendRedirect("fruit.do");
    }


}
