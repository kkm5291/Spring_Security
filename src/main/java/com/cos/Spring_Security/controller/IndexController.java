package com.cos.Spring_Security.controller;


import com.cos.Spring_Security.config.auth.PrincipalDetails;
import com.cos.Spring_Security.model.User;
import com.cos.Spring_Security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
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

    @GetMapping("/test/login")
    public @ResponseBody String testLogin(Authentication authentication,
                                          @AuthenticationPrincipal PrincipalDetails userDetails) { // @AuthenticationPrincipal이라는 어노테이션으로 세션 정보 접근 가능
        // 세션 정보에 접근할 수 있는 방법은 어노테이션을 활용하는 방법과
        // 35번째 코드처럼 형변환을 통해서 확인하는 방법이 있음
        System.out.println("/test/login ==================");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication : " + principalDetails.getUser());

        System.out.println("userDetails : " + userDetails.getUser());
        return "세션 정보 확인하기";
    }


    //oauth 유저 정보 얻기
    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOAuthLogin(Authentication authentication,
                                               @AuthenticationPrincipal OAuth2User oauth) { // @AuthenticationPrincipal이라는 어노테이션으로 세션 정보 접근 가능
        System.out.println("/test/oauth/login ==================");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication : " + oAuth2User.getAttributes());
        System.out.println("oauth2User : " + oauth.getAttributes());

        return "OAuth 세션 정보 확인하기";
    }


    @GetMapping({"", "/"})
    public String index() {
        return "index";
    }

    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
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
