/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.api.form;

import com.foilen.smalltools.restapi.model.AbstractApiBase;

public class OwnerUsageResourceMappingOwnerMappingForm extends AbstractApiBase {

    private String type;
    private String owner;

    public String getOwner() {
        return owner;
    }

    public String getType() {
        return type;
    }

    public OwnerUsageResourceMappingOwnerMappingForm setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public OwnerUsageResourceMappingOwnerMappingForm setType(String type) {
        this.type = type;
        return this;
    }

}
