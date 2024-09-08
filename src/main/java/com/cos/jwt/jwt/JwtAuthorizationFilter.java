package com.cos.jwt.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cos.jwt.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.cos.jwt.repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        System.out.println("인증이 필요한 페이지 요청");

        // 인증이 된 사용자가 아니면 그냥 filterChain실행
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authorization == null || !authorization.startsWith("Bearer")) {
            System.out.println("인증 안 된 사용자!");
            chain.doFilter(request, response);
            return;
        }

        // 토큰이 있는 사용가 인증이 유효한지 확인
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).replace("Bearer ", "");
        String secret = "cos";

        // 누가 보낸건지 확인(내가 만든 건지 확인)
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secret)).build()
                .verify(token);

        String username = decodedJWT.getClaim("username").as(String.class);
//        String username = decodedJWT.getClaim("username").asString();

        if(username != null) {

            System.out.println("인증 완료 후, 권한 토큰 생성중.......");

            // session에 권한정보를 넣어준다.
            User user = userRepository.findByUsername(username);

            PrincipalDetails principalDetails = new PrincipalDetails(user);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            chain.doFilter(request, response);
        }
    }
}
