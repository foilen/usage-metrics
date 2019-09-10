/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.api.model;

import java.util.ArrayList;
import java.util.List;

import com.foilen.smalltools.restapi.model.AbstractApiBase;

public class ReportForOwner extends AbstractApiBase {

    private long size;
    private List<ReportUsage> usages = new ArrayList<>();

    public ReportForOwner addUsage(String hostname, UsageResource usageResource) {
        size += usageResource.getSize();

        usages.add(new ReportUsage() //
                .setHostname(hostname) //
                .setUsageResourceOwner(usageResource.getOwner()) //
                .setUsageResourceType(usageResource.getUsageResourceType()) //
                .setDetails(usageResource.getDetails()) //
                .setSize(usageResource.getSize()));

        return this;
    }

    public long getSize() {
        return size;
    }

    public List<ReportUsage> getUsages() {
        return usages;
    }

    public ReportForOwner setSize(long size) {
        this.size = size;
        return this;
    }

    public ReportForOwner setUsages(List<ReportUsage> usages) {
        this.usages = usages;
        return this;
    }

}
