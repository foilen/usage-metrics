/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiSecurityConfig {

    @Bean
    public ApiWebSecurityRepositoryConfigurer apiWebSecurityRepositoryConfigurer() {
        return new ApiWebSecurityRepositoryConfigurer();
    }

}
