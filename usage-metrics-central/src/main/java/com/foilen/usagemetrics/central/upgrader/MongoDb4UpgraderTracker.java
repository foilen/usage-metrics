/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central.upgrader;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import com.foilen.smalltools.tools.DateTools;
import com.foilen.smalltools.upgrader.trackers.UpgraderTracker;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.model.Filters;

/**
 * A tracker that stores the successfully executed tasks in a MongoDB Database.
 */
public class MongoDb4UpgraderTracker implements UpgraderTracker {

    private MongoClient mongoClient;
    private String collectionName = "upgraderTools";
    private String databaseName;

    public MongoDb4UpgraderTracker(MongoClient mongoClient, String databaseName) {
        this.mongoClient = mongoClient;
        this.databaseName = databaseName;
    }

    public MongoDb4UpgraderTracker(String uri, String databaseName) {
        mongoClient = MongoClients.create(uri);
        this.databaseName = databaseName;
    }

    @Override
    public void executionBegin(String taskSimpleName) {
    }

    @Override
    public void executionEnd(String taskSimpleName, boolean isSuccessful) {
        if (isSuccessful) {
            Date appliedDate = new Date();

            Map<String, Object> document = new HashMap<String, Object>();
            document.put("_id", taskSimpleName);
            document.put("appliedDate", appliedDate);
            document.put("appliedDateText", DateTools.formatFull(appliedDate));

            mongoClient.getDatabase(databaseName) //
                    .getCollection(collectionName) //
                    .insertOne(new Document(document));
        }
    }

    public String getCollectionName() {
        return collectionName;
    }

    public MongoDb4UpgraderTracker setCollectionName(String collectionName) {
        this.collectionName = collectionName;
        return this;
    }

    @Override
    public void trackerBegin() {
    }

    @Override
    public void trackerEnd() {
    }

    @Override
    public boolean wasExecutedSuccessfully(String taskSimpleName) {
        return mongoClient.getDatabase(databaseName) //
                .getCollection(collectionName) //
                .countDocuments(Filters.eq("_id", taskSimpleName)) > 0;
    }

}
