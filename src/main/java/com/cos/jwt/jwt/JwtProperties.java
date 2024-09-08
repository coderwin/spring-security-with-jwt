package com.cos.jwt.jwt;

public interface JwtProperties {

    String SECRET = "cos";
    String JWT_PREFIX = "Bearer ";
    long EXPIRE_TIME = 1000 * 60 * 10L;// 10분


}
