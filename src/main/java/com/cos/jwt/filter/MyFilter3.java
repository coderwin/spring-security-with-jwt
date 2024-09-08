package com.cos.jwt.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter3 implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("필터3");

//        servletResponse.setCharacterEncoding("UTF-8");
//        servletResponse.setContentType("text/html;charset=utf-8");
//        PrintWriter pw = servletResponse.getWriter();

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        String method = req.getMethod();
        if(method.equals("POST")) {
            System.out.println("post 요청됨");

            String headerAuth = req.getHeader("Authorization");
            System.out.println(headerAuth);
            if(headerAuth.equals("cos")) {

                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                PrintWriter writer = res.getWriter();
                writer.println("인증 안 됨!!!!");
            }
        }

//        pw.println("안녕~하하");
//        filterChain.doFilter(servletRequest, servletResponse);
    }
}
