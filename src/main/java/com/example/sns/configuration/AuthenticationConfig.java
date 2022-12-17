package com.example.sns.configuration;

import com.example.sns.configuration.filter.JwtTokenFilter;
import com.example.sns.exception.CustomAuthenticationEntryPoint;
import com.example.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthenticationConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    @Value("${jwt.secret-key}")
    private String key;



    //정적화면(favicon 등 과 같은) 프론트 화면은 필터 적용하고 싶지 않을 때는 web.ignoring()를 사용한다.
    @Override
    public void configure(WebSecurity web) throws Exception {
        //ignoring 을 첫번째로 만나게 된다. 정규 표현식에 해당하는 표현들은 인증절차를 건너뛴다.
        //정규식은 /api 가 아닌것들은 인증 없이 통과시킨다 로 해석할 수 있다.
        web.ignoring().regexMatchers("^(?!/api/).*");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/*/users/join", "/api/*/users/login").permitAll() //이 경우 허가
                .antMatchers("/api/**").authenticated() //이 경우 인증절차
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션 사용 안함
                .and()
                //매번 요청때마다 필터를 사용하여 들어온 토큰이 어떤 유저를 가르키는지 체크하는 로직 사용
                //addFilterBefore: UsernamePasswordAuthenticationFilter 이전에 JwtTokenFilter 적용하겠다.
                .addFilterBefore(new JwtTokenFilter(key, userService), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling() //에러가 발생하면 entryPoint 로 보내줘야 한다.
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());

    }
}
