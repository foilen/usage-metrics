/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.common.api.model;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.foilen.smalltools.restapi.model.AbstractApiBase;

public class UsageResource extends AbstractApiBase {

    private String owner;
    private String usageResourceType;
    private Date timestamp = new Date();
    private long size;

    private String details;

    private Map<String, String> extra = new TreeMap<>();

    public UsageResource() {
    }

    public String getDetails() {
        return details;
    }

    public Map<String, String> getExtra() {
        return extra;
    }

    public String getOwner() {
        return owner;
    }

    public long getSize() {
        return size;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getUsageResourceType() {
        return usageResourceType;
    }

    public UsageResource setDetails(String details) {
        this.details = details;
        return this;
    }

    public UsageResource setExtra(Map<String, String> extra) {
        this.extra = extra;
        return this;
    }

    public UsageResource setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public UsageResource setSize(long size) {
        this.size = size;
        return this;
    }

    public UsageResource setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public UsageResource setUsageResourceType(String usageResourceType) {
        this.usageResourceType = usageResourceType;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UsageResource [owner=");
        builder.append(owner);
        builder.append(", usageResourceType=");
        builder.append(usageResourceType);
        builder.append(", timestamp=");
        builder.append(timestamp);
        builder.append(", size=");
        builder.append(size);
        builder.append(", details=");
        builder.append(details);
        builder.append(", extra=");
        builder.append(extra);
        builder.append("]");
        return builder.toString();
    }

}
