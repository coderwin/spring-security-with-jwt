package com.cos.jwt.controller;

import com.cos.jwt.model.User;
import com.cos.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RestApiController {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @GetMapping("/home")
    public String home() {
        return "<h1>Home</h1>";
    }

    @PostMapping("/token")
    public String token() {
        return "<h1>token</h1>";
    }

    @PostMapping("/join")
    public String join(User user) {

        // 비밀번호 암호화
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        // role 설정
        user.setRoles("ROLE_USER");

        userRepository.save(user);

        return "회원가입 완료!";
    }

    @GetMapping("/api/v1/user")
    public String user() {
        return "<h1>User</h1>";
    }

    @GetMapping("/api/v1/manager")
    public String manager() {
        return "<h1>Manager</h1>";
    }

    @GetMapping("/api/v1/admin")
    public String admin() {
        return "<h1>Admin</h1>";
    }


}
