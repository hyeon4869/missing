package gradu.gradu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //CORS를 활성화 시켜 http
    //csrf 공격도 막음
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .authorizeRequests()
//                .antMatchers("/auth/login", "/auth/signup").permitAll() // 로그인, 회원가입 경로는 인증 없이 허용
//                .anyRequest().authenticated(); // 그 외의 모든 경로는 인증이 필요
                .anyRequest().permitAll(); //모든 요청에 대해 인증 없이 사용

    }
}
