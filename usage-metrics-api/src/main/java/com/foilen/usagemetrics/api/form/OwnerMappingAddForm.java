/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.api.form;

import com.foilen.smalltools.restapi.model.AbstractApiBase;

public class OwnerMappingAddForm extends AbstractApiBase {

    private String ownerName;

    public String getOwnerName() {
        return ownerName;
    }

    public OwnerMappingAddForm setOwnerName(String ownerName) {
        this.ownerName = ownerName;
        return this;
    }

}
