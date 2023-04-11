package com.increff.pos.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/user/**").permitAll();
        http.authorizeRequests().antMatchers("/api/users/**").permitAll();
        http.authorizeRequests().antMatchers("/api/orders/**").hasAnyAuthority("operator", "supervisor");
        http.authorizeRequests().antMatchers("/api/reports/**").hasAnyAuthority("operator", "supervisor");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/**").hasAnyAuthority("operator", "supervisor");
        http.authorizeRequests().antMatchers("/api/**").hasAnyAuthority("supervisor");
        http.authorizeRequests().anyRequest().permitAll();
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security",
                "/swagger-ui.html", "/webjars/**");
    }

}
