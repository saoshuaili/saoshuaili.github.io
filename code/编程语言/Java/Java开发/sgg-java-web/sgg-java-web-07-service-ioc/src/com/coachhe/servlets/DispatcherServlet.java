package com.coachhe.servlets;

import com.coachhe.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
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

    private Map<String, Object> beanMap = new HashMap<>();

    public DispatcherServlet() {
    }

    public void init() throws ServletException {
        super.init();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("applicationContext.xml");
            // 1. 创建DocumentBuilderFactory
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            // 2. 创建DocumentBuilder对象
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            // 3. 创建Document对象
            Document document = documentBuilder.parse(inputStream);
            // 4. 获取所有的bean节点
            NodeList beanNodeList = document.getElementsByTagName("bean");

            for (int i = 0; i < beanNodeList.getLength(); i++) {
                Node beanNode = beanNodeList.item(i);
                if (beanNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element beanElement = (Element) beanNode;
                    String beanId = beanElement.getAttribute("id");
                    String className = beanElement.getAttribute("class");
                    // 创建bean实例
                    Object beanObj = Class.forName(className).newInstance();
                    // 讲bean实例对象保存到map容器中
                    beanMap.put(beanId, beanObj);
                    // 到目前为止，此处需要注意的是，bean和bean之间的依赖关系还没有设置
                }
            }

            // 5. 组装bean之间的依赖关系
            for (int i = 0; i < beanNodeList.getLength(); i++) {
                Node beanNode = beanNodeList.item(i);
                if (beanNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element beanElement = (Element) beanNode;
                    NodeList beanChildNodes = beanElement.getChildNodes();
                    for (int j = 0; j < beanChildNodes.getLength(); j++) {
                        Node beanChildNode = beanChildNodes.item(j);
                        if (beanChildNode.getNodeType() == Node.ELEMENT_NODE && beanChildNode.getNodeName().equals("property")) {
                            Element propertyElement = (Element) beanChildNode;
                            String propertyName = propertyElement.getAttribute("name");
                            String propertyRef = propertyElement.getAttribute("ref");
                            // 1. 找到propertyRef对应的示例
                            Object refObj = beanMap.get(propertyRef);
                            // 2. 将refObj设置到当前bean对应的实例的property属性上去
                            Object beanObj = beanMap.get(propertyRef);
                            Class beanClazz = beanObj.getClass();
                            Field propertyField = beanClazz.getDeclaredField(propertyName);
                            propertyField.setAccessible(true);
                            propertyField.set(beanObj, refObj);
                        }
                    }

                    String beanId = beanElement.getAttribute("id");

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
        Object controllerBeanObj = beanMap.get(servletPath);

        String operate = request.getParameter("operate");

        if (StringUtil.isEmpty(operate)) {
            operate = "index";
        }

        try {
            // 获取当前类中的所有方法
            Method[] methods = controllerBeanObj.getClass().getDeclaredMethods();
            for (Method method : methods) {
                // 获取方法名
                String methodName = method.getName();
                // 找到和operate同名的方法，那么通过反射调用它
                if (operate.equals(methodName)) {
                    //1-1.获取当前方法的参数，返回参数数组
                    Parameter[] parameters = method.getParameters();
                    //1-2.parameterValues 用来承载参数的值
                    Object[] parameterValues = new Object[parameters.length];
                    for (int i = 0; i < parameters.length; i++) {
                        Parameter parameter = parameters[i];
                        String parameterName = parameter.getName();
                        //如果参数名是request,response,session 那么就不是通过请求中获取参数的方式了
                        if ("request".equals(parameterName)) {
                            parameterValues[i] = request;
                        } else if ("response".equals(parameterName)) {
                            parameterValues[i] = response;
                        } else if ("session".equals(parameterName)) {
                            parameterValues[i] = request.getSession();
                        } else {
                            //从请求中获取参数值
                            String parameterValue = request.getParameter(parameterName);
                            String typeName = parameter.getType().getName();

                            Object parameterObj = parameterValue;

                            if (parameterObj != null) {
                                // 因为我们获取的是字符串，但我们需要的是Integer
                                if ("java.lang.Integer".equals(typeName)) {
                                    parameterObj = Integer.parseInt(parameterValue);
                                }
                            }

                            parameterValues[i] = parameterObj;
                        }
                    }

                    // Controller组件中的方法调用
                    method.setAccessible(true);
                    Object methodReturnValueObj = method.invoke(controllerBeanObj, parameterValues);

                    // 视图处理
                    String methodReturnValueStr = (String) methodReturnValueObj;
                    if (methodReturnValueStr.startsWith("redirect:")) { //例如redirect: fruit.do
                        String redirectStr = methodReturnValueStr.substring("redirect:".length());
                        response.sendRedirect(redirectStr);
                    } else { // 比如edit
                        super.processTemplate(methodReturnValueStr, request, response);
                    }

                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}


// 常见错误
// arguement mismatch
