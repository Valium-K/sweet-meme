package dev.valium.sweetmeme.infra.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

@Configuration
public class OSIVConfig {

    @Bean
    public FilterRegistrationBean registerOpenEntityManagerInViewFilterBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        OpenEntityManagerInViewFilter filter = new OpenEntityManagerInViewFilter();

        registrationBean.setFilter(filter);

        // TODO url pattern 정의 필요
        registrationBean.addUrlPatterns("/**");
        //registrationBean.setOrder(5);

        return registrationBean;
    }
}
