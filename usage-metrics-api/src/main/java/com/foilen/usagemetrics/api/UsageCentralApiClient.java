/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.api;

import java.util.Date;
import java.util.List;

import com.foilen.smalltools.restapi.model.FormResult;
import com.foilen.usagemetrics.api.model.OwnerMappingResult;
import com.foilen.usagemetrics.api.model.OwnerMappingResults;
import com.foilen.usagemetrics.api.model.ReportShowResult;
import com.foilen.usagemetrics.api.model.UsageResource;

public interface UsageCentralApiClient {

    FormResult ownerMappingAdd(String ownerName);

    FormResult ownerMappingAddMapping(String ownerName, String type, String owner);

    FormResult ownerMappingDelete(String ownerName);

    OwnerMappingResult ownerMappingFind(String ownerName);

    /**
     * Get the list of owner mapping.
     *
     * @return the owner mapping
     */
    OwnerMappingResults ownerMappingFindAll();

    FormResult ownerMappingRemoveMapping(String ownerName, String type, String owner);

    /**
     * Get the usage report at the specified time.
     *
     * @param forDate
     *            (optional) the date to check for (default: now)
     * @return the report
     */
    ReportShowResult reportShow(Date forDate);

    /**
     * Used to add resource usages by the Agents.
     *
     * @param usageResources
     *            the usage resources to add
     * @return the result
     */
    FormResult resourceAdd(List<UsageResource> usageResources);

}