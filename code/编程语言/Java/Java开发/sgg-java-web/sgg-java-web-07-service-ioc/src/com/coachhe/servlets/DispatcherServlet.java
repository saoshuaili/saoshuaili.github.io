package com.coachhe.servlets;

import com.coachhe.io.BeanFactory;
import com.coachhe.io.ClassPathXmlApplicationContext;
import com.coachhe.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Created with IntelliJ IDEA.
 * User: coachhe
 * Date: 2023/1/31
 * Time: 18:46
 * Description:
 */
@WebServlet("*.do") // 拦截所有以.do结尾的请求
public class DispatcherServlet extends ViewBaseServlet {

    private BeanFactory beanFactory;

    public DispatcherServlet() {
    }

    public void init() throws ServletException {
        super.init();
        beanFactory = new ClassPathXmlApplicationContext();
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
        Object beanObj = beanFactory.getBean(servletPath);

        String operate = request.getParameter("operate");

        if (StringUtil.isEmpty(operate)) {
            operate = "index";
        }

        try {
            // 获取当前类中的所有方法
            Method[] methods = beanObj.getClass().getDeclaredMethods();
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
                    Object methodReturnValueObj = method.invoke(beanObj, parameterValues);

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
