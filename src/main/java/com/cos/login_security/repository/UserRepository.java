package com.cos.login_security.repository;

import com.cos.login_security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// CRUD 함수를 JpaRepository가 들고 있음.
// @Repository 라는 어노테이션이 없어도 IoC가 됨, 이유는 JpaRepository를 상속했기 때문에
public interface UserRepository extends JpaRepository<User, Integer> {
    //findBy규칙 -> Username문법
    // select* from user where username = 1?
    public User findByUsername(String username); // Jpa Query methods 검색해서 공부해볼 것.
}
