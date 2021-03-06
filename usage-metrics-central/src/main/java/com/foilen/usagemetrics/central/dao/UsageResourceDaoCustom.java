/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.util.CloseableIterator;

import com.foilen.usagemetrics.central.dao.domain.UsageResourceExtended;

public interface UsageResourceDaoCustom {

    boolean existsFutureEntry(String hostname, String usageResourceType, Date timestamp, String ignoreBatchId);

    CloseableIterator<UsageResourceExtended> findAtTime(Date timestamp);

    UsageResourceExtended findAtTime(Date timestamp, String hostname, String usageResourceType, String details);

    List<String> findBatchesBetweenTime(Date from, Date to, String hostname, String usageResourceType);
}