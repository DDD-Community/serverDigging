package com.example.digging.adapter.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("swagger-ui.html");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .authorizeRequests() //HttpServletRequest 접근 제한 설정
                .antMatchers("/api/user").permitAll()
                .anyRequest().authenticated(); //모두 인증되어야 한다

    }


}
