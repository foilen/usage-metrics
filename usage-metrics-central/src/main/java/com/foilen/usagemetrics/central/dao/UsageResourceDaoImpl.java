/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central.dao;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.util.CloseableIterator;
import org.springframework.stereotype.Service;

import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.usagemetrics.central.dao.domain.UsageResourceExtended;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

@Service
public class UsageResourceDaoImpl extends AbstractBasics implements UsageResourceDaoCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public boolean existsFutureEntry(String hostname, String usageResourceType, Date timestamp, String ignoreBatchId) {
        return mongoTemplate.exists(new Query(Criteria //
                .where("hostname").is(hostname) //
                .and("usageResourceType").is(usageResourceType) //
                .and("timestamp").gt(timestamp) //
                .and("batchId").ne(ignoreBatchId) //
        ), UsageResourceExtended.class);
    }

    @Override
    public CloseableIterator<UsageResourceExtended> findAtTime(Date timestamp) {
        return mongoTemplate.stream(new Query(Criteria //
                .where("timestamp").lte(timestamp).and("endTimestamp").gt(timestamp) //
        ), //
                UsageResourceExtended.class);
    }

    @Override
    public UsageResourceExtended findAtTime(Date timestamp, String hostname, String usageResourceType, String details) {
        return mongoTemplate.findOne(new Query(Criteria //
                .where("hostname").is(hostname) //
                .and("usageResourceType").is(usageResourceType) //
                .and("details").is(details) //
                .and("timestamp").lte(timestamp).and("endTimestamp").gt(timestamp) //
        ), //
                UsageResourceExtended.class);
    }

    @Override
    public List<String> findBatchesBetweenTime(Date from, Date to, String hostname, String usageResourceType) {

        MongoCollection<Document> collection = mongoTemplate.getCollection(mongoTemplate.getCollectionName(UsageResourceExtended.class));

        return StreamSupport.stream(collection //
                .distinct("batchId", Filters.and( //
                        Filters.eq("hostname", hostname), //
                        Filters.eq("usageResourceType", usageResourceType), //
                        Filters.or( //
                                Filters.and(Filters.lte("timestamp", from), Filters.gte("endTimestamp", from)), // from is between
                                Filters.and(Filters.lte("timestamp", to), Filters.gte("endTimestamp", to)), // to is between
                                Filters.and(Filters.gte("timestamp", from), Filters.lte("endTimestamp", to)) // block is between
                        ) //
                ), String.class) //
                .spliterator(), false) //
                .collect(Collectors.toList());
    }

}
