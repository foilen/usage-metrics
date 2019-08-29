/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.foilen.usagemetrics.central.dao.domain.UsageResourceExtended;

// TODO Add indexes
@Repository
public interface UsageResourceDao extends MongoRepository<UsageResourceExtended, String>, UsageResourceDaoCustom {

    List<UsageResourceExtended> findAllByHostnameAndUsageResourceType(String hostname, String usageResourceType);

    List<UsageResourceExtended> findByHostnameAndUsageResourceTypeAndBatchIdIn(String hostname, String usageResourceType, List<String> batchIds);

}