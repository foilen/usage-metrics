/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.api.form;

import java.util.ArrayList;
import java.util.List;

import com.foilen.smalltools.restapi.model.AbstractApiBase;
import com.foilen.usagemetrics.api.model.UsageResource;

public class ResourcesAddForm extends AbstractApiBase {

    private List<UsageResource> usageResources = new ArrayList<>();

    public List<UsageResource> getUsageResources() {
        return usageResources;
    }

    public ResourcesAddForm setUsageResources(List<UsageResource> usageResources) {
        this.usageResources = usageResources;
        return this;
    }

}
