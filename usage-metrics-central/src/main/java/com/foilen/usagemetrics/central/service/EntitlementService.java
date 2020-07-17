/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central.service;

public interface EntitlementService {

    boolean hostCanAddResources(String authUser, String authKey);

    boolean reportCanShow(String authUser, String authKey);

}
