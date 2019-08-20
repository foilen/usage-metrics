/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.foilen.usagemetrics.central.CentralApp;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

@Configuration
public class MongoSpringConfig {

    @Bean
    public MongoClient mongoClient() {
        return new MongoClient(mongoClientUri());
    }

    @Bean
    public MongoClientURI mongoClientUri() {
        return new MongoClientURI(CentralApp.getCentralConfig().getMongoUri());
    }

}
