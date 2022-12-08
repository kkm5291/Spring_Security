package com.cos.login_security.config.auth;

import com.cos.login_security.model.User;
import com.cos.login_security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessingUrl("/login");
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어있는 loadUserByUsername 함수가 실행
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    //시큐리티 session = Authentication = UserDetails
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // String username 이라는 애는 LoginForm에 있는 로그인 아이디랑 name 이 같아야 함
        // 만약 username을 바꿔주고 싶으면 SecurityConfig에서 .loginPage 뒤에 .usernameParameter("username2") 이런식으로 추가해줘야 변경할 수 있음

        System.out.println("username : " +username);
        User userEntity = userRepository.findByUsername(username);
        if (username != null) {
            return new PrincipalDetails(userEntity); // 이 리턴된 값이 Authentication으로 쏙 들어가버렷! Authentication 은 Session으로 쏙 들어가버렷
        }
        return null;
    }
}
