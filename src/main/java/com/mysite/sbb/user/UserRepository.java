package com.mysite.sbb.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser,Long> {
    Optional<SiteUser> findByusername(String username);
    //뒤에서 생성할 UserSecurityService는 사용자 ID를 조회하는 기능이 필요하므로
    //다음과 같이 사용자 ID로 SiteUser 엔티티를 조회하는 findByUsername 메서드를 User 리포지터리에 추가
}
