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
                .mvcMatchers("/*", "/user/**", "/post/*", "/login/forgot").permitAll()
                .mvcMatchers(HttpMethod.GET, SECTION_URL+"*", FILE_URL+"*", DOWNLOAD_URL+"*",
                        COMMENT_IMAGE_URL+"*", "/avatar/*", "/comment/slice/**",
                        "/reply/slice/**", "/post/slice/**", "/email/verify/check").permitAll()
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