package com.cos.jwt.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cos.jwt.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
//        super(authenticationManager);
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        System.out.println("JwtAuthenticationFilter attemptAuthentication() : 로그인 시도중");

        // username, password 확인
        try {
            StringBuffer sb = new StringBuffer();

//            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));

            BufferedReader br = request.getReader();
//
//            String line = null;
//            while((line = br.readLine()) != null) {
//                sb.append(line);
//            }

            // 회원정보 만들기
            ObjectMapper mapper = new ObjectMapper();
            User loginUser = mapper.readValue(br, User.class);

            // 로그인 시도 > 토큰 객체 만들기
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword());

            // 인증 시작
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            System.out.println("인증 통과~");

            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println(principalDetails.getUser().getUsername());

            // security session에 저장됨.
            return authentication;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        System.out.println("JwtAuthenticationFilter successfulAuthentication() : 인증이 완료되고 실행됨.");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        // 인증이 완료 되었으니 토큰을 생성한다.
//        String subject = "cos토큰 생성";
        String subject = "cos토큰";
        String secret = "cos";
        String jwtToken = JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 10))// 10초
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC256(secret));

        jwtToken = "Bearer " + jwtToken;

//        response.setHeader("Authorization", 11111+jwtToken);
//        response.setHeader("Authorization1", jwtToken);
//        response.setHeader(HttpHeaders.AUTHORIZATION, jwtToken);
        response.addHeader("Authorization", jwtToken);
        response.addHeader("Authorization1", jwtToken);

        // header에 값이 안 넘어간다.
//        super.successfulAuthentication(request, response, chain, authResult);
    }
}
