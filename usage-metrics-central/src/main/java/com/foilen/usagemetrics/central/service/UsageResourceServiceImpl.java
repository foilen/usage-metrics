/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central.service;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.smalltools.tools.CollectionsTools;
import com.foilen.smalltools.tools.DateTools;
import com.foilen.smalltools.tools.JsonTools;
import com.foilen.smalltools.tools.StringTools;
import com.foilen.smalltools.tuple.Tuple2;
import com.foilen.usagemetrics.central.dao.UsageResourceDao;
import com.foilen.usagemetrics.central.dao.domain.UsageResourceExtended;
import com.foilen.usagemetrics.common.UsageMetricException;
import com.foilen.usagemetrics.common.api.model.UsageResource;

@Service
public class UsageResourceServiceImpl extends AbstractBasics implements UsageResourceService {

    @Autowired
    private UsageResourceDao usageResourceDao;

    @Override
    public void addUsageResource(String hostname, List<UsageResource> usageResources) {

        logger.info("Adding {} usage resources from {}", usageResources.size(), hostname);

        // Split in batches and resource type
        Map<String, List<UsageResourceExtended>> ursByBatchAndTypeId = new HashMap<>();
        usageResources.forEach(ur -> {
            String batchAndTypeId = ur.getBatchId() + "-" + ur.getUsageResourceType();

            UsageResourceExtended urx = JsonTools.clone(ur, UsageResourceExtended.class);
            urx.setHostname(hostname);
            urx.setBatchId(batchAndTypeId);

            CollectionsTools.getOrCreateEmptyArrayList(ursByBatchAndTypeId, batchAndTypeId, UsageResourceExtended.class).add(urx);
        });

        // Sort the batches per time
        ursByBatchAndTypeId.values().forEach(usageResourceExtendeds -> {
            usageResourceExtendeds.sort((a, b) -> Long.compare(a.getTimestamp().getTime(), b.getTimestamp().getTime()));
        });

        ursByBatchAndTypeId.entrySet().forEach(e -> addUsageResourceBatch(e.getKey(), e.getValue()));

    }

    private void addUsageResourceBatch(String batchId, List<UsageResourceExtended> urxs) {

        // Get the parameters of that batch
        Tuple2<Date, Date> batchRange = new Tuple2<Date, Date>(urxs.get(0).getTimestamp(), urxs.get(urxs.size() - 1).getTimestamp());
        UsageResourceExtended any = urxs.get(0);
        String hostname = any.getHostname();
        String usageResourceType = any.getUsageResourceType();
        logger.info("[{}] This batch is between {} ({}) and {} ({}) for hostname {} and type {}", batchId, DateTools.formatFull(batchRange.getA()), batchRange.getA().getTime(),
                DateTools.formatFull(batchRange.getB()), batchRange.getB().getTime(), hostname, usageResourceType);

        // Check if there is newer entries for that hostname/resource
        if (usageResourceDao.existsFutureEntry(hostname, usageResourceType, batchRange.getA(), batchId)) {
            throw new UsageMetricException("Insertion of older entries is not allowed");
        }

        // Find the batches with entries that touches the same time range
        List<String> previousBatches = usageResourceDao.findBatchesBetweenTime(batchRange.getA(), batchRange.getB(), hostname, usageResourceType);
        Collections.sort(previousBatches);
        logger.info("[{}] Previous batches with resources in that timeframe {}", batchId, previousBatches);

        List<UsageResourceExtended> previousUrxsInBatches = usageResourceDao.findByHostnameAndUsageResourceTypeAndBatchIdIn(hostname, usageResourceType, previousBatches);

        Map<String, List<UsageResourceExtended>> previousUrxsByDetail = new HashMap<>();
        previousUrxsInBatches.forEach(urx -> CollectionsTools.getOrCreateEmptyArrayList(previousUrxsByDetail, urx.getDetails(), UsageResourceExtended.class).add(urx));

        // Check if some replayed
        if (previousBatches.contains(batchId)) {
            logger.info("[{}] Got some resources that are replayed. Will cleanup", batchId);
            previousUrxsInBatches.stream() //
                    .filter(urx -> StringTools.safeEquals(batchId, urx.getBatchId())) //
                    .forEach(urx -> {
                        String details = urx.getDetails();
                        logger.info("[{}] {} is already in the DB. Skip it", batchId, details);
                        urxs.removeIf(u -> StringTools.safeEquals(details, u.getDetails()));
                        previousUrxsByDetail.remove(details);

                        batchRange.setA(new Date(Long.min(batchRange.getA().getTime(), urx.getTimestamp().getTime())));
                        batchRange.setB(new Date(Long.max(batchRange.getB().getTime(), urx.getTimestamp().getTime())));
                    });

            logger.info("[{}] After the range adjustments, this batch is between {} ({}) and {} ({}) for hostname {} and type {}", batchId, DateTools.formatFull(batchRange.getA()),
                    batchRange.getA().getTime(), DateTools.formatFull(batchRange.getB()), batchRange.getB().getTime(), hostname, usageResourceType);
        }

        urxs.forEach(urx -> {

            logger.info("[{}] Processing {}", batchId, JsonTools.compactPrintWithoutNulls(urx));
            List<UsageResourceExtended> previousUrxs = previousUrxsByDetail.remove(urx.getDetails());
            if (!CollectionsTools.isNullOrEmpty(previousUrxs)) {
                // Take the latest date
                Date endTimestamp = previousUrxs.stream().map(u -> u.getEndTimestamp()).sorted(Collections.reverseOrder()).findFirst().get();
                urx.setEndTimestamp(endTimestamp);
                for (UsageResourceExtended u : previousUrxs) {
                    u.setEndTimestamp(urx.getTimestamp());
                    logger.info("[{}] Inserting after {}", batchId, JsonTools.compactPrintWithoutNulls(u));
                }
                previousUrxs = usageResourceDao.saveAll(previousUrxs);
            }
            urx = usageResourceDao.save(urx);

        });

        // Remove any extra details on previousUrxsByDetail
        previousUrxsByDetail.values().forEach(previousUrxs -> {
            previousUrxs.forEach(previousUrx -> {
                logger.info("[{}] The resource {} does not exist anymore. Mark endtime now {} ({})", batchId, previousUrx.getDetails(), DateTools.formatFull(batchRange.getB()),
                        batchRange.getB().getTime());
                previousUrx.setEndTimestamp(batchRange.getB());
            });
            usageResourceDao.saveAll(previousUrxs);
        });

    }

}
