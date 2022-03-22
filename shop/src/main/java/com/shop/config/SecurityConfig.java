package com.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity  //WebSecurityConfigureAdapter를 상속받는 클래스에 @EnableWebSesurity 어노테이션을 선언하면
                    //SpringSecurityFileChain이 자동으로 포함됩ㄴ다.
                    //WebSecurityConfigureAdapter를 상속받아 메소드 오버라이딩을 통해 보안 설정을 커스터마이징 할 수 있다.

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MemberService memberService;

    @Override
    protected void configure(HttpSecurity http) throws Exception{   //http 요청에 대한 보안을 설정한다.
                                                                    //페이지 권한 설정, 로그인 페이지 설정, 로그아웃 메소드 등에 대한 설정을 작성한다.
        http.formLogin()
                .loginPage("/members/login")    //로그인 페이지 url 설정
                .defaultSuccessUrl("/")         //로그인 성공시 이동할 url 설정
                .usernameParameter("email")     //로그인 시 사용할 파라미터 이름으로 email을 지정
                .failureUrl("/members/login/error") //로그인 실패시 이동할 url 설정
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) //로그아웃 url 설정
                .logoutSuccessUrl("/")  //로그아웃 성공 시 이동할 url
                ;

        // http.authorizeRequests()
        //                .mvcMatchers("/", "/members/**", "/item/**", "/images/**").permitAll()
        //                .mvcMatchers("/admin/**").hasRole("ADMIN")
        //                .anyRequest().authenticated()
        //        ;

        http.exceptionHandling() 
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()) //인증되지 않은 사용자가 리소스에 접근하였을 때 수행되는 핸들러를 등록
        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**"); //static 디렉터리의 하위 파일은 인증을 무시한다.
    }

    @Bean
    public PasswordEncoder passwordEncoder(){   //비밀번호를 데이터베이스에 그대로 저장했을 경우, 데이터베이스가 해킹당하면 고객의 정보가 그대로 노출된다.
        return new BCryptPasswordEncoder();     //이를 위해, BCryptPasswordEncoder의 해시 함수를 이용하여, 비밀번호를 암호화하여 저장한다. Bean으로 등록하여 사용했다.
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
        throws Exception{   //Spring Security에서 인증은 AuthenticationManagerBuilder가 AuthenticationManager을 생성한다.
        auth.userDetailsService(memberService)
                .passwordEncoder(passwordEncoder());    //userDetailService를 구현하고 있는 객체로 memberService를 지정해주며, 비밀번호 암호화를 위해 passwordEncoder를 지정한다.
    }
}
