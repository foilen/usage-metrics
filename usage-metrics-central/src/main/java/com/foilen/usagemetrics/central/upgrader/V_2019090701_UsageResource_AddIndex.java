/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central.upgrader;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.smalltools.upgrader.tasks.UpgradeTask;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;

@Component
public class V_2019090701_UsageResource_AddIndex extends AbstractBasics implements UpgradeTask {

    @Autowired
    private MongoClient mongoClient;

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Override
    public void execute() {
        logger.info("Add the index");

        String databaseName = new ConnectionString(mongoUri).getDatabase();
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        MongoCollection<Document> collection = database.getCollection("usageResource");
        collection.createIndex(Indexes.ascending("hostname", "usageResourceType", "batchId"));
    }

    @Override
    public String useTracker() {
        return UpgradeTask.DEFAULT_TRACKER;
    }

}
