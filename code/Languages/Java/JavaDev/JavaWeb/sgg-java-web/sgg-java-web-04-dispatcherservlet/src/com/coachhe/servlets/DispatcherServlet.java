package com.coachhe.servlets;

import com.coachhe.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: coachhe
 * Date: 2023/1/31
 * Time: 18:46
 * Description:
 */
@WebServlet("*.do") // 拦截所有以.do结尾的请求
public class DispatcherServlet extends ViewBaseServlet {

    private Map<String, Object> beanClassMap = new HashMap<>();

    public DispatcherServlet() {
    }

    public void init() throws ServletException {
        super.init();
        try {
            InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("applicationContext.xml");
            // 1. 创建DocumentBuilderFactory
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            // 2. 创建DocumentBuilder对象
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            // 3. 创建Document对象
            Document document = documentBuilder.parse(resourceAsStream);
            // 4. 获取所有的bean节点
            NodeList beanNodeList = document.getElementsByTagName("bean");

            for (int i = 0; i < beanNodeList.getLength(); i++) {
                Node beanNode = beanNodeList.item(i);
                if (beanNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element beanElement = (Element) beanNode;
                    String beanId = beanElement.getAttribute("id");
                    String className = beanElement.getAttribute("class");
                    Object beanObj = Class.forName(className).newInstance();
                    beanClassMap.put(beanId, beanObj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置编码
        request.setCharacterEncoding("utf-8");

        // 假设url是： http://localhost:8080/mvc/hello.do
        // 那么servletPath是： /hello.do
        String servletPath = request.getServletPath();
        // 通过/hello.do得到hello
        servletPath = servletPath.substring(1);
        int lastDotIndex = servletPath.lastIndexOf(".do");
        servletPath = servletPath.substring(0, lastDotIndex);
        // 通过hello找到helloController
        Object controllerBeanObj = beanClassMap.get(servletPath);

        String operator = request.getParameter("operate");

        if (StringUtil.isEmpty(operator)) {
            operator = "index";
        }

        // 获取当前类中的所有方法
        Method[] methods = controllerBeanObj.getClass().getDeclaredMethods();
        for (Method m : methods) {
            // 获取方法名
            String methodName = m.getName();
            if (operator.equals(methodName)) {
                // 找到和operate同名的方法，那么通过反射调用它
                try {
                    m.setAccessible(true);
                    m.invoke(controllerBeanObj, request, response);
                    return;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }


    }
}
