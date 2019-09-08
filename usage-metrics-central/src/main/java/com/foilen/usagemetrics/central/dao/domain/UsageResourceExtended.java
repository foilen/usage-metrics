/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central.dao.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.foilen.smalltools.tools.DateTools;
import com.foilen.usagemetrics.common.api.model.UsageResource;

@Document(collection = "usageResource")
public class UsageResourceExtended extends UsageResource {

    @Id
    private String id;

    private String hostname;

    private Date endTimestamp = DateTools.parseDateOnly("2999-12-31");

    public Date getEndTimestamp() {
        return endTimestamp;
    }

    public String getHostname() {
        return hostname;
    }

    public String getId() {
        return id;
    }

    public UsageResourceExtended setEndTimestamp(Date endTimestamp) {
        this.endTimestamp = endTimestamp;
        return this;
    }

    public UsageResourceExtended setHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    public UsageResourceExtended setId(String id) {
        this.id = id;
        return this;
    }

}
