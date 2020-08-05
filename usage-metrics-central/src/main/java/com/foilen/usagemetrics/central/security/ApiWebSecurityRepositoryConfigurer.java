/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.foilen.usagemetrics.central.service.EntitlementService;

public class ApiWebSecurityRepositoryConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    private EntitlementService entitlementService;
    @Autowired
    private HashSha256PasswordEncoder hashSha256PasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        ApiUsersUserDetailsService apiUsersUserDetailsService = new ApiUsersUserDetailsService(entitlementService, hashSha256PasswordEncoder);

        auth.userDetailsService(apiUsersUserDetailsService) //
                .passwordEncoder(hashSha256PasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**").authorizeRequests() //
                .anyRequest().fullyAuthenticated() //
                .and() //
                .httpBasic().realmName("Usage Metrics Central") //
                .and() //
                .csrf().disable() //
        ;
    }

}
