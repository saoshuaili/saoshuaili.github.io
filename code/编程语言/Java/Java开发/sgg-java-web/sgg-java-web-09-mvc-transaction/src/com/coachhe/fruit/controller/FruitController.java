package com.coachhe.fruit.controller;

import com.coachhe.fruit.pojo.Fruit;
import com.coachhe.fruit.service.FruitService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author CoachHe
 * @date 2023/1/31 00:59
 **/
public class FruitController {

    private FruitService fruitService = null;

    private String index(String oper, String keyword, Integer pageNo, HttpServletRequest request) {
        if (pageNo == null) {
            pageNo = 1;
        }
        // 默认查询第一页的数据
        List<Fruit> fruitList = fruitService.getFruitList(keyword, pageNo);
        // 保存至 session 作用域
        HttpSession session = request.getSession();
        // 保存水果列表
        session.setAttribute("fruitList", fruitList);
        // 保存当前页面
        session.setAttribute("pageNo", pageNo);
        return "index";
    }

    private String add(String fname, Integer price, Integer fcount, String remark) {
        Fruit fruit = new Fruit(0, fname, price, fcount, remark);
        fruitService.addFruit(fruit);
        return "redirect:fruit.do";
    }

    private String edit(Integer fid, HttpServletRequest request) {
        if (fid != null) {
            Fruit fruit = fruitService.getFruitByFid(fid);
            request.setAttribute("fruit", fruit);
            return "edit";
        }
        return "error";
    }

    private String del(Integer fid) {
        if (fid != null) {
            fruitService.delFruitByFid(fid);
            System.out.println("delete successfully");
            return "redirect:fruit.do";
        }
        return "error";
    }

    private String update(Integer fid, String fname, Integer price, Integer fcount, String remark) {
        // 执行更新
        fruitService.updateFruit(new Fruit(fid, fname, price, fcount, remark));
        return "redirect:fruit.do";
    }


}
