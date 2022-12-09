package com.cos.Spring_Security.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    // 이곳에서 구글 로그인의 후처리가 작업이 됨
    // 구글로 부터 받은 userRequest 데이터에 대한 후처리 되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("getAccessToken : " + userRequest.getAccessToken());
        System.out.println("getAdditionalParameters : " + userRequest.getAdditionalParameters());
        System.out.println("getClientRegistration : " + userRequest.getClientRegistration());
        System.out.println("getAttributes : " + super.loadUser(userRequest).getAttributes()); // 사실상 얘만 필요함!
        return super.loadUser(userRequest); // 얘만 필요해
    }
}
