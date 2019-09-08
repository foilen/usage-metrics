/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.common.api.model;

import com.foilen.smalltools.restapi.model.AbstractApiBase;

public class ReportUsage extends AbstractApiBase {

    private String hostname;

    private String usageResourceOwner;
    private String usageResourceType;
    private String details;

    private long size;

    public String getDetails() {
        return details;
    }

    public String getHostname() {
        return hostname;
    }

    public long getSize() {
        return size;
    }

    public String getUsageResourceOwner() {
        return usageResourceOwner;
    }

    public String getUsageResourceType() {
        return usageResourceType;
    }

    public ReportUsage setDetails(String details) {
        this.details = details;
        return this;
    }

    public ReportUsage setHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    public ReportUsage setSize(long size) {
        this.size = size;
        return this;
    }

    public ReportUsage setUsageResourceOwner(String usageResourceOwner) {
        this.usageResourceOwner = usageResourceOwner;
        return this;
    }

    public ReportUsage setUsageResourceType(String usageResourceType) {
        this.usageResourceType = usageResourceType;
        return this;
    }

}
