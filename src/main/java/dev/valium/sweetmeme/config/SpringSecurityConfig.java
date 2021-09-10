package dev.valium.sweetmeme.config;

import dev.valium.sweetmeme.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final MemberService memberService;
    private final DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // TODO 임시 permitAll() 나중에 경로 설정하기
                .mvcMatchers("/", "/login", "/sign-up", "/user/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/members/*", "sections/*").permitAll()
                .anyRequest().authenticated();

        http.formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
                .loginPage("/login")
                .permitAll();

        http.rememberMe()
                .rememberMeParameter("remember-me")
                .userDetailsService(memberService)
                .tokenRepository(tokenRepository());

        http.logout()
                .logoutSuccessUrl("/");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .mvcMatchers("/node_modules/**", "/sections/**") // 추가필터
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public PersistentTokenRepository tokenRepository() {
        // TODO JdbcTokenRepositoryImpl.CREATE_TABLE_SQL의 SQL문에 맞는 Entity 생성

        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);

        return jdbcTokenRepository;
    }
}
