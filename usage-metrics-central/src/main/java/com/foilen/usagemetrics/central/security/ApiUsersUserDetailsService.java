/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central.security;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.usagemetrics.central.dao.domain.ApiUser;
import com.foilen.usagemetrics.central.service.EntitlementService;

public class ApiUsersUserDetailsService extends AbstractBasics implements UserDetailsService {

    private static final String API_USER = "API_USER";
    private static final String HOST = "HOST";

    public static final String ROLE_API_USER = "ROLE_" + API_USER;
    public static final String ROLE_HOST = "ROLE_" + HOST;

    private EntitlementService entitlementService;

    public ApiUsersUserDetailsService(EntitlementService entitlementService) {
        this.entitlementService = entitlementService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        ApiUser apiUser = entitlementService.getApiUser(username);
        if (apiUser == null) {
            // Give a host user
            return new User(username, entitlementService.deriveHostnameKey(username), Collections.singletonList(new SimpleGrantedAuthority(HOST)));
        }

        // Api user
        return new User(username, apiUser.getAuthPasswordHash(), Collections.singletonList(new SimpleGrantedAuthority(API_USER)));

    }

}
