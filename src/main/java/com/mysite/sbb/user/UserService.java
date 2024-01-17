package com.mysite.sbb.user;

import com.mysite.sbb.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser create(String username, String email, String password){
        //User의 비밀번호는 보안을 위해 반드시 암호화하여 저장해야 한다.
        // 그러므로 스프링 시큐리티의 BCryptPasswordEncoder 클래스를 사용하여 암호화하여 비밀번호를 저장
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        /*
        * BCryptPasswordEncoder 객체를 직접 new로 생성하는 방식보다는
        * PasswordEncoder 객체를 빈으로 등록해서 사용하는 것이 좋다.
        * 왜냐하면 암호화 방식을 변경하면 BCryptPasswordEncoder를
        * 사용한 모든 프로그램을 일일이 찾아다니며 수정해야 하기 때문이다.
        * */
        return this.userRepository.save(SiteUser.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build());
    }

    public SiteUser getUser(String username) {
        /*
        * principal 객체를 사용하면 이제 로그인한 사용자명을 알 수 있으므로
        * 사용자명으로 SiteUser 객체를 조회할 수 있다.
        * 먼저 SiteUser를 조회할 수 있는 getUser 메서드를 UserService에 추가
        * */
        Optional<SiteUser> siteUser = this.userRepository.findByusername(username);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }
}
