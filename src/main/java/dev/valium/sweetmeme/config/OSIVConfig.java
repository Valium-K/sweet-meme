package dev.valium.sweetmeme.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@Configuration
public class OSIVConfig {

//    @Bean
//    public FilterRegistrationBean registerOpenEntityManagerInViewFilterBean() {
//        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//        OpenEntityManagerInViewFilter filter = new OpenEntityManagerInViewFilter();
//
//        registrationBean.setFilter(filter);
//
//        // TODO url pattern 정의 필요
//        registrationBean.addUrlPatterns("/*");
//        //registrationBean.setOrder(5);
//
//        return registrationBean;
//    }
}
