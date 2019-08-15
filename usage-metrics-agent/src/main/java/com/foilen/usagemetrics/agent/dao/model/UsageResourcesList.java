/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.agent.dao.model;

import java.util.ArrayList;
import java.util.List;

import com.foilen.usagemetrics.common.model.UsageResource;

public class UsageResourcesList {

    private List<UsageResource> usageResources = new ArrayList<>();

    public List<UsageResource> getUsageResources() {
        return usageResources;
    }

    public void setUsageResources(List<UsageResource> usageResources) {
        this.usageResources = usageResources;
    }

}
