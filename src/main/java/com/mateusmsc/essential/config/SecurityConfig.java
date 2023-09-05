package com.mateusmsc.essential.config;

import com.mateusmsc.essential.service.DevDojoUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Log4j2
@RequiredArgsConstructor
//Todo
// remover o método deprecated
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final DevDojoUserDetailsService devDojoUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        log.info("Password encoded {}", passwordEncoder.encode("academy"));
//        auth.inMemoryAuthentication()
//                .withUser("mateus")
//                .password(passwordEncoder.encode("123"))
//                .roles("USER", "ADMIN")
//                .and()
//                .withUser("devdojo")
//                .password(passwordEncoder.encode("academy"))
//                .roles("USER");
        auth.userDetailsService(devDojoUserDetailsService)
                .passwordEncoder(passwordEncoder);
    }
}
