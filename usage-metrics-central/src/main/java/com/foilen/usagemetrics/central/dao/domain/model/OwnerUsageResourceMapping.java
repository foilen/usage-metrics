/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central.dao.domain.model;

public class OwnerUsageResourceMapping {

    private String type;
    private String owner;

    public OwnerUsageResourceMapping() {
    }

    public OwnerUsageResourceMapping(String type, String owner) {
        this.type = type;
        this.owner = owner;
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
        if (owner == null) {
            if (other.owner != null) {
                return false;
            }
        } else if (!owner.equals(other.owner)) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
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
        result = prime * result + ((owner == null) ? 0 : owner.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
