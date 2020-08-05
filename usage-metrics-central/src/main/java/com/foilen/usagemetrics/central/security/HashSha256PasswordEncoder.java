/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.foilen.smalltools.hash.HashSha256;
import com.foilen.smalltools.tools.StringTools;

@Component
public class HashSha256PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return HashSha256.hashString(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String expectedAuthKeyHash = HashSha256.hashString(rawPassword.toString());
        return StringTools.safeEquals(encodedPassword, expectedAuthKeyHash);
    }

}
