package com.macro.dev.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public void globalUserDetails(final AuthenticationManagerBuilder auth) throws Exception {
        // @formatter:off
	auth.inMemoryAuthentication()
	  .withUser("john").password("123").roles("USER").and()
	  .withUser("tom").password("111").roles("ADMIN").and()
	  .withUser("user1").password("pass").roles("USER");
    }// @formatter:on

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        // @formatter:off
		http.httpBasic()
            .and().authorizeRequests().antMatchers("/login").permitAll()
            .antMatchers("/bower_components/**").permitAll()
            .antMatchers("/api/**").permitAll()
            .antMatchers("/file_manager/**").permitAll()
            .antMatchers("/gulp-tasks/**").permitAll()
            .antMatchers("/assets/**").permitAll()
            .antMatchers("/data/**").permitAll()
            .antMatchers("/kendoui/**").permitAll()
            .antMatchers("/package.json").permitAll()
            .antMatchers("/font.css").permitAll()
            .antMatchers("/service/send-mail").permitAll()
            .antMatchers("/index.html", "/user","/").permitAll()
            .antMatchers("/oauth/token/revokeById/**").permitAll()
            .antMatchers("/tokens/**").permitAll()
            .anyRequest().authenticated()
            .and().formLogin().permitAll()
            .and().csrf().disable();
		// @formatter:on
    }

}
