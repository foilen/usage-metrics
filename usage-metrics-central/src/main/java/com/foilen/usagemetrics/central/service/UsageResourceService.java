/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central.service;

import java.util.List;

import com.foilen.usagemetrics.common.api.model.UsageResource;

public interface UsageResourceService {

    void addUsageResource(String hostname, List<UsageResource> usageResources);

}