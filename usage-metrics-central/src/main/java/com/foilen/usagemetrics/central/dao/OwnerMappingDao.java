/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.foilen.usagemetrics.central.dao.domain.OwnerMapping;

@Repository
public interface OwnerMappingDao extends MongoRepository<OwnerMapping, String> {

}