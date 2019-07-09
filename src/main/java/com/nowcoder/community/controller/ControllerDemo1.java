package com.nowcoder.community.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 使用两个SpringMVC的注解来注解类
 * Controller,RequestMapping
 * 方法能被浏览器访问的前提是，它被RequestMapping所注解
 * 并且使用注解ResponseBody进行声明
 * 处理浏览器的请求分2个方面：1、基于请求的数据2、基于响应的数据
 * 怎么接受请求数据，怎么返回响应数据
 * GET请求 POST请求
 */
@Controller
@RequestMapping("")
public class ControllerDemo1 {

    @RequestMapping("/hello")
    @ResponseBody
    public String demoHello() {
        return "Hello World!!!";
    }


    /**
     * HttpServletRequest request, HttpServletResponse response,获得请求对象,响应对象，SpringMVC对他们进行了封装
     * 利用对象处理请求
     * 自动调用http
     */
    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response) {
        //获取请求数据
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        /**
         * Enumeration<String>迭代器，使用while遍历
         */
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + " : " + value);
        }
        //获取参数 在路径后面加?name=lzq 加&带更多的参数
        System.out.println(request.getParameter("code"));

        /**
         * 返回响应数据
         * 设置返回数据的类型:text/html;charset=utf-8 :网页类型文本 字符集utf-8
         * 要想使用response向浏览器响应，就是使用response封装的输出流向浏览器输出
         * response.getwriter()获取输出流
         */
        response.setContentType("text/html;charset=utf-8");
        try(
                PrintWriter writer = response.getWriter();
        ) {
            writer.write("<h1>cj<h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * GET请求的处理：一般用于浏览器向服务器获取某些数据，默认的请求
     * /students?current=1&limit=20;传两参数给服务器，一般目的是向服务器获取基于参数的特定数据
     * RequestMapping（）声明请求路径,同时method = RequestMethod.GET，声明请求方式
     * 强制只能处理GET请求，请求方式很多，容易产生漏洞
     * 通过RequestParam对参数的注入做更详细的声明
     * required = false可以不传，defaultValue = "1"设置默认值
     * 通过路径传参有2种方式：1、路径后面加?参数名=参数值
     * 2、/students/{id} 使用PathVariable路径变量注解
     */
    @RequestMapping(path = "/students",method = RequestMethod.GET)
    @ResponseBody
    public String getStudents(
            @RequestParam(name = "current",required = false,defaultValue = "1") int current,
            @RequestParam(name = "limit",required = false,defaultValue = "1") int limit){
        System.out.println(current);
        System.out.println(limit);
        return "student" + current + limit;
    }
    @RequestMapping(path = "/students/{id}",method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id ) {
        System.out.println(id);
        return "A Student";
    }

    /**
     * POST请求一般用于向服务器提交数据
     * GET POST可以解决大部分问题
     * 浏览器要向服务器提交数据，首先浏览器要打开一个带有表单的网页
     * resources/static存放静态资源，图片，css文件，静态网页
     * resources/templates当中存放的是动态模板
     * 1、在resources/static下新建静态网页用于存放表单
     * 参数的名字与表单中数据的名字一致就自动获取
     * 对thymeleaf
     * 使用application.properties关闭thymeleaf缓存
     * 表单要提交给谁，用什么样的方式提交
     * <form method = "post" action="community/demo1/student">
     */
    @RequestMapping(path = "/student",method = RequestMethod.POST)
    @ResponseBody
    public String postStudent(String name, int id) {
        System.out.println(name +" : "+ id);
        return "success";
    }
    /**
     * 响应html数据，响应JSON数据，异步请求
     * 把JAVA对象返回给浏览器，浏览器使用的是JS，所以希望得到JS对象
     * JSON是一种具有特定格式的字符串，实现JAVA和JS兼容
     * JAVA对象->JSON->JS对象
     * 不加ResponseBody注解，Spring以为要返回一个html
     * 加了ResponseBody注解，就知道是返回一个字符串
     */
    @RequestMapping(path = "/emp",method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getemp() {
        Map<String, Object> emp = new HashMap<>();
        emp.put("姓名","常菊");
        emp.put("道歉","对不起");
        return emp;
    }

}
