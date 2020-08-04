/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.collect.ComparisonChain;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_EMPTY, content = Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public class OwnerUsageResourceMapping implements Comparable<OwnerUsageResourceMapping> {

    private String type;
    private String owner;

    public OwnerUsageResourceMapping() {
    }

    public OwnerUsageResourceMapping(String type, String owner) {
        this.type = type;
        this.owner = owner;
    }

    @Override
    public int compareTo(OwnerUsageResourceMapping o) {
        return ComparisonChain.start() //
                .compare(type, o.type) //
                .compare(owner, o.owner) //
                .result();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        OwnerUsageResourceMapping other = (OwnerUsageResourceMapping) obj;
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        if (owner == null) {
            if (other.owner != null) {
                return false;
            }
        } else if (!owner.equals(other.owner)) {
            return false;
        }
        return true;
    }

    public String getOwner() {
        return owner;
    }

    public String getType() {
        return type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((owner == null) ? 0 : owner.hashCode());
        return result;
    }

    public OwnerUsageResourceMapping setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public OwnerUsageResourceMapping setType(String type) {
        this.type = type;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OwnerUsageResourceMapping [type=");
        builder.append(type);
        builder.append(", owner=");
        builder.append(owner);
        builder.append("]");
        return builder.toString();
    }

}
