package dev.valium.sweetmeme.infra.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

import static dev.valium.sweetmeme.infra.config.FileConfig.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // TODO 임시 permitAll() 나중에 경로 설정하기
                .mvcMatchers("/*", "/user/**", "/post/*", "/login/forgot", "/robots.txt").permitAll()
                .mvcMatchers(HttpMethod.GET, SECTION_URL+"*", FILE_URL+"*", DOWNLOAD_URL+"*",
                        COMMENT_IMAGE_URL+"*", "/avatar/*", "/comment/slice/**",
                        "/reply/slice/**", "/post/slice/**", "/email/verify/check", "/login/forgot/check").permitAll()
                .anyRequest().authenticated();

        http.formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(new LoginSuccessHandler())
                .loginPage("/login")
                .permitAll();

        http.rememberMe()
                .rememberMeParameter("remember-me")
                .userDetailsService(userDetailsService)
                .tokenRepository(tokenRepository());

        http.logout()
                .logoutUrl("/member/logout")
                .logoutSuccessUrl("/");

        // https://stackoverflow.com/questions/52982246/spring-thymeleaf-throws-cannot-create-a-session-after-the-response-has-been-com
        // 타임리프가 자동으로 hidden csrf를 생성하는데, 문제는 세션이 만들어지기 전에 이러한 행동을 취한다면,
        // Cannot create a session after the response has been committed 에러로 이어진다.
        // 해결법은 특정 페이지의 csrf를 제거하거나 아래 설정을 추가하는 것이다.
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .mvcMatchers("/node_modules/**") // 추가필터
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public PersistentTokenRepository tokenRepository() {

        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);

        return jdbcTokenRepository;
    }
}