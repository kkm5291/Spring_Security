package com.cos.login_security.controller;


import com.cos.login_security.model.User;
import com.cos.login_security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // View를 리턴하겠다
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @GetMapping({"", "/"})
    public String index() {
        return "index";
    }

    @GetMapping("/user")
    public String user() {
        return "user";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public String manager() {
        return "manager";
    }

    @GetMapping("/loginForm") // SecurityConfig 파일을 작성하면 스프링시큐리티의 자동 login 페이지 사용 불가능
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm") // 회원가입 홈페이지로 이동
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join") // 회원가입이 되는 진행
    public String join(User user) {
        System.out.println(user);
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user); // 회원가입 완료 그러나 비밀번호가 1234 => 시큐리티로 로그인 불가능, 이유는 패스워드 암호화 안됐기 때문

        return "redirect:/loginForm";
    }

    @GetMapping("/info")
    @Secured("ROLE_ADMIN") // 특정 메서드에 간단하게 권한 설정을 주고싶을 때 사용하는 방법임.
    public @ResponseBody String info() {
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // 여러개 권한을 설정해주고 싶을 때 (만약 글로벌로 권한을 주고 싶을 경우엔 그냥 securityConfig에다가 거삼)
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "데이터정보";
    }
}
