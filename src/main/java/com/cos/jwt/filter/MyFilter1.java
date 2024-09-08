package com.cos.jwt.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class MyFilter1 implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("필터1");

//        servletResponse.setCharacterEncoding("UTF-8");
//        servletResponse.setContentType("text/html;charset=utf-8");
//        PrintWriter pw = servletResponse.getWriter();
//        pw.println("안녕~하하");

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
