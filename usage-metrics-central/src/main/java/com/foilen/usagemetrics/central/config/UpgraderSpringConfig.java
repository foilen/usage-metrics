/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.foilen.smalltools.upgrader.UpgraderTools;
import com.foilen.smalltools.upgrader.tasks.UpgradeTask;
import com.foilen.smalltools.upgrader.trackers.UpgraderTracker;
import com.foilen.usagemetrics.central.upgrader.MongoDb4UpgraderTracker;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;

@Configuration
public class UpgraderSpringConfig implements WebMvcConfigurer {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Autowired
    private MongoClient mongoClient;

    @Bean
    public UpgraderTracker mongodbUpgraderTracker() {
        ConnectionString connectionString = new ConnectionString(mongoUri);
        return new MongoDb4UpgraderTracker(mongoClient, connectionString.getDatabase());
    }

    @Bean
    public UpgraderTools upgraderTools(List<UpgradeTask> tasks) {
        UpgraderTools upgraderTools = new UpgraderTools(tasks);
        upgraderTools.setDefaultUpgraderTracker(mongodbUpgraderTracker());
        return upgraderTools;
    }

}
