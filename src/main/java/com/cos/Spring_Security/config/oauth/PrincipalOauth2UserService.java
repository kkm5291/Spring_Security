package com.cos.Spring_Security.config.oauth;

import com.cos.Spring_Security.config.auth.PrincipalDetails;
import com.cos.Spring_Security.model.User;
import com.cos.Spring_Security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;

    // 이곳에서 구글 로그인의 후처리가 작업이 됨
    // 구글로 부터 받은 userRequest 데이터에 대한 후처리 되는 함수
    // 회원 프로필을 받아올 수 있음
    // 함수 종료 시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("getAccessToken : " + userRequest.getAccessToken());
        System.out.println("getAdditionalParameters : " + userRequest.getAdditionalParameters());
        System.out.println("getClientRegistration : " + userRequest.getClientRegistration()); // registrationId로 어떤 OAuth로 로그인 했는지 확인 가능


        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인을 완료 -> code를 리턴(OAuth-Client라이브러리) -> AccessToken요청
        // 위에 과정 까지가 userRequest 정보 -> loadUser 함수 호출 -> 구글에서 회원 프로필 받아옴
        System.out.println("getAttributes : " + oAuth2User.getAttributes()); // 사실상 얘만 필요함!


        //회원가입 강제로 진행시키기
        String provider = userRequest.getClientRegistration().getClientId(); // google
        String providerId = oAuth2User.getAttribute("sub"); // google의 provider Id
        String username = provider + "_" + providerId; // google_sub가 될것임 (유저네임 충돌되지 않음)
        String password = bCryptPasswordEncoder.encode("겟인데어");
        String email = oAuth2User.getAttribute("email");
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username);

        if (userEntity == null) {
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        }

        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}
