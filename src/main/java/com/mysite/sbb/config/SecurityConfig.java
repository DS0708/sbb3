package com.mysite.sbb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;



@Configuration //@Configuration은 이 파일이 스프링의 환경 설정 파일임을 의미하는 애너테이션
@EnableWebSecurity //@EnableWebSecurity는 모든 요청 URL이 스프링 시큐리티의 제어를 받도록 만드는 애너테이션
public class SecurityConfig {
    /*
     * 빈이란?
     * 빈(bean)은 스프링에 의해 생성 또는 관리되는 객체를 의미한다.
     * 우리가 지금껏 만들어 왔던 컨트롤러, 서비스, 리포지터리 등도 모두 빈에 해당한다.
     * 또한 앞선 예처럼 @Bean 애너테이션을 통해 자바 코드 내에서 별도로 빈을 정의하고 등록할 수도 있다.
     * */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
            //다음은 인증되지 않은 모든 페이지의 요청을 허락한다는 의미이다. 따라서 로그인하지 않더라도 모든 페이지에 접근할 수 있도록 한다.
            .csrf((csrf) -> csrf
                .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))
            //스프링 시큐리티가 CSRF 처리 시 H2 콘솔은 예외로 처리할 수 있도록 다음과 같이 설정 파일을 수정
            .headers((headers) -> headers
                .addHeaderWriter(new XFrameOptionsHeaderWriter(
                        XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
            //이와 같이 URL 요청 시 X-Frame-Options 헤더를 DENY 대신 SAMEORIGIN으로 설정하여
            // 오류가 발생하지 않도록 했다. X-Frame-Options 헤더의 값으로 SAMEORIGIN을 설정하면
            // 프레임에 포함된 웹 페이지가 동일한 사이트에서 제공할 때에만 사용이 허락된다.
        ;

        return http.build();
    }
    /*
    내부적으로 SecurityFilterChain 클래스가 동작하여 모든 요청 URL에
    이 클래스가 필터로 적용되어 URL별로 특별한 설정을 할 수 있게 된다.
    스프링 시큐리티의 세부 설정은 @Bean 애너테이션을 통해
    SecurityFilterChain 빈을 생성하여 설정할 수 있다.
    */
}
