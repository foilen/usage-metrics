/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central.service;

import com.foilen.usagemetrics.central.dao.domain.ApiUser;

public interface EntitlementService {

    String deriveHostnameKey(String username);

    ApiUser getApiUser(String username);

}
