package com.encore.ordering.securities;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //특정권한을 가진사람만 접근할 수 있도록
public class SecurityConfig {

    private final JwtAuthFilter authFilter;

    public SecurityConfig(JwtAuthFilter authFilter) {
        this.authFilter = authFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                /*csrf와 xss와의 차이점? 금융권쪽 단골면접질문
                mvc와 같이(restapi는 구조적으로 덜 취약)쿠키와 세션을 사용했을 때 csrf공격 발생가능이 높아진다
                * example.com로그인 -> 링크가 들어있는 이메일을 클릭 -> example.com과 똑같은 화면이 나옴 ->
                * 토큰과 세션id값을 탈취당함 -
                * 화면마다 토큰을 넣어주어야한다*/
                .csrf().disable()
                .cors().and() //cors 활성화
                .httpBasic().disable()
                .authorizeRequests()
                .antMatchers("/member/create","/doLogin","/items","/item/image/**")
                /*로그아웃은 왜 필요가 없을까?
                * <세션방식>
                * doLogin -> 성공시 세션storage에 sessionId저장 + 세선id전달
                * 이후 API요청시 검증 : 쿠키의 sessionId와 storage에 SessionId를 비교
                * doLogout -> 세선storage에서 sessionId삭제
                * <토큰방식>
                * doLogin ->서버에서 저장안함 + 성공시 사용자에게 토큰ID전달.
                * 이후 API요청시 검증 : 사용자가 보내온 토큰을 적합한 토큰인지 서버에서 검사
                * => 비밀번호 원리와 동일(?), 최초 토큰 보낼때 1234를 서버에 보낸다 서버에서 암호화 툴로 abcd로암호화가 됨 ->
                * 사용자의 두번째 요청, 1234와 abcd를 같이 보낼 시 진짜 사용자인지 검증이 가능한가? (서버에는 실제 저장값이 하나도 없어도 검증이 가능)
                * 페이로더 + 헤더?
                * 진짜 비밀번호가 1234일 경우 ? 우선 1234가 고유의 값은 아님 -> api요청시마다 db 조회 필요? -> 기타정보(이메일)+암호화 ->
                * 이메일과 기타정보 암호화툴을 전부 알고있으면...? => 시크릿키가 필요(예:hello)
                *
                * <정리>
                * String token= sha256(사용자의 info+secretKey문자열)을 사용자에게 전달 -> 사용자가 API요청시 token만 서버에 전달하면서 aPI요청
                * ->
                 * */
                    .permitAll()
                .anyRequest().authenticated()
                .and()
//              /*세션을 사용하지 않겠다는 메서드 추가*/
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterAfter(authFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
