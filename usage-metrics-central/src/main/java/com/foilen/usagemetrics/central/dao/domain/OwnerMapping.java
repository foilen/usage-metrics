/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central.dao.domain;

import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.foilen.usagemetrics.api.model.OwnerUsageResourceMapping;

@Document
public class OwnerMapping {

    @Id
    private String ownerName;
    private String comment;

    private SortedSet<OwnerUsageResourceMapping> ownerUsageResourceMappings = new TreeSet<>();

    public OwnerMapping addOwnerUsageResourceMappings(OwnerUsageResourceMapping ownerUsageResourceMapping) {
        ownerUsageResourceMappings.add(ownerUsageResourceMapping);
        return this;
    }

    public String getComment() {
        return comment;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public SortedSet<OwnerUsageResourceMapping> getOwnerUsageResourceMappings() {
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

    public OwnerMapping setOwnerUsageResourceMappings(SortedSet<OwnerUsageResourceMapping> ownerUsageResourceMappings) {
        this.ownerUsageResourceMappings = ownerUsageResourceMappings;
        return this;
    }

}
