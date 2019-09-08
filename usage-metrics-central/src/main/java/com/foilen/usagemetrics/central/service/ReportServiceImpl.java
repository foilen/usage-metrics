/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foilen.smalltools.restapi.model.ApiError;
import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.smalltools.tools.CollectionsTools;
import com.foilen.usagemetrics.central.dao.OwnerMappingDao;
import com.foilen.usagemetrics.central.dao.UsageResourceDao;
import com.foilen.usagemetrics.central.dao.domain.OwnerMapping;
import com.foilen.usagemetrics.central.dao.domain.model.OwnerUsageResourceMapping;
import com.foilen.usagemetrics.common.api.model.ReportForOwner;
import com.foilen.usagemetrics.common.api.model.ReportShowResult;

@Service
public class ReportServiceImpl extends AbstractBasics implements ReportService {

    private static final String NO_OWNER = "NO_OWNER";

    @Autowired
    private OwnerMappingDao ownerMappingDao;
    @Autowired
    private UsageResourceDao usageResourceDao;

    @Override
    public ReportShowResult getReport(Date forDate) {
        ReportShowResult result = new ReportShowResult();

        // Get the mapping
        Map<OwnerUsageResourceMapping, String> ownerByMapping = new HashMap<>();
        for (OwnerMapping ownerMapping : ownerMappingDao.findAll()) {
            String ownerName = ownerMapping.getOwnerName();
            logger.info("Owner: {}", ownerName);

            for (OwnerUsageResourceMapping ownerUsageResourceMapping : ownerMapping.getOwnerUsageResourceMappings()) {
                logger.info("Owner: {} : {}", ownerName, ownerUsageResourceMapping);
                String previousOwnerName = ownerByMapping.get(ownerUsageResourceMapping);
                if (previousOwnerName != null) {
                    result.setError(new ApiError( //
                            "Resource " + ownerUsageResourceMapping.getType() + "/" + ownerUsageResourceMapping.getOwner() + //
                                    " is owned by more than one owner: " + previousOwnerName + " and " + ownerName));
                    return result;
                }

                ownerByMapping.put(ownerUsageResourceMapping, ownerName);
            }

        }

        // Process the usage
        usageResourceDao.findAtTime(forDate).forEachRemaining(ur -> {
            logger.info("Processing: {}", ur);
            OwnerUsageResourceMapping mapping = new OwnerUsageResourceMapping(ur.getUsageResourceType(), ur.getOwner());
            String owner = ownerByMapping.get(mapping);
            if (owner == null) {
                owner = NO_OWNER;
            }
            logger.info("Owner is: {}", owner);

            CollectionsTools.getOrCreateEmpty(result.getReportByOwner(), owner, ReportForOwner.class).addUsage(ur.getHostname(), ur);

        });

        return result;
    }

}
