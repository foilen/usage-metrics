/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central.service;

import com.foilen.smalltools.restapi.model.FormResult;
import com.foilen.usagemetrics.api.form.OwnerMappingAddForm;
import com.foilen.usagemetrics.api.form.OwnerUsageResourceMappingOwnerMappingForm;
import com.foilen.usagemetrics.api.model.OwnerMappingResult;
import com.foilen.usagemetrics.api.model.OwnerMappingResults;

public interface OwnerMappingService {

    FormResult add(OwnerMappingAddForm form);

    FormResult addMapping(String ownerName, OwnerUsageResourceMappingOwnerMappingForm form);

    FormResult delete(String ownerName);

    FormResult deleteMapping(String ownerName, String type, String owner);

    OwnerMappingResult find(String ownerName);

    OwnerMappingResults findAll();

}
