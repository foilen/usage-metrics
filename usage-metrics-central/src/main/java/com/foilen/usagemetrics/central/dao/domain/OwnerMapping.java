/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central.dao.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.foilen.usagemetrics.central.dao.domain.model.OwnerUsageResourceMapping;

@Document
public class OwnerMapping {

    @Id
    private String ownerName;
    private String comment;

    private List<OwnerUsageResourceMapping> ownerUsageResourceMappings = new ArrayList<>();

    public String getComment() {
        return comment;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public List<OwnerUsageResourceMapping> getOwnerUsageResourceMappings() {
        return ownerUsageResourceMappings;
    }

    public OwnerMapping setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public OwnerMapping setOwnerName(String ownerName) {
        this.ownerName = ownerName;
        return this;
    }

    public OwnerMapping setOwnerUsageResourceMappings(List<OwnerUsageResourceMapping> ownerUsageResourceMappings) {
        this.ownerUsageResourceMappings = ownerUsageResourceMappings;
        return this;
    }

}
