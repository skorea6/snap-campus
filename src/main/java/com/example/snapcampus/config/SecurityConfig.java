package com.example.snapcampus.config;

import com.example.snapcampus.common.handler.CustomAuthenticationFailureHandler;
import com.example.snapcampus.common.login.jwt.JwtAuthenticationFilter;
import com.example.snapcampus.common.login.jwt.JwtTokenProvider;
import com.example.snapcampus.dto.security.SnapPrincipal;
import com.example.snapcampus.repository.MemberRepository;
import com.example.snapcampus.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.Optional;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private static final String[] requestPermitStaticUrl = new String[]{
            "/css/**",
            "/images/**",
            "/fonts/**",
            "/js/**",
            "/scss/**",
            "/favicon.ico",
            "/member/login",
            "/member/login/**",
    };

    private static final String[] requestPermitAllUrl = new String[]{
            "/member/signup/**",
            "/member/signup",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspection) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(requestPermitStaticUrl)
                        .permitAll()
                        .requestMatchers(requestPermitAllUrl)
                        .anonymous()
                        .requestMatchers("/api/map/place/delete", "/api/event/delete").hasRole("ADMIN")
                        .requestMatchers("/member/**", "/api/map/place/add").hasRole("MEMBER")
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/member/login")
                        .loginProcessingUrl("/member/login/action")
                        .usernameParameter("userId")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/")
                        .failureHandler(customAuthenticationFailureHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)  // 세션 무효화
                        .deleteCookies("JSESSIONID")  // 쿠키 삭제
                        .permitAll()
                )
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider), // 먼저 실행 (앞에 있는 필터가 통과하면 뒤에 있는 필터는 검사하지 않음)
                        UsernamePasswordAuthenticationFilter.class
                )
//                .exceptionHandling(
//                        e -> e.accessDeniedHandler(new CustomAccessDeniedHandler())
//                )
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(MemberRepository memberRepository) {
        return username -> Optional.ofNullable(memberRepository.findByUserId(username))
                .map(SnapPrincipal::from)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다 - username: " + username));
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
