/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central.service;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foilen.smalltools.hash.HashSha256;
import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.usagemetrics.central.CentralApp;
import com.foilen.usagemetrics.central.dao.ApiUserDao;
import com.foilen.usagemetrics.central.dao.domain.ApiUser;
import com.foilen.usagemetrics.common.UsageMetricException;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Service
public class EntitlementServiceImpl extends AbstractBasics implements EntitlementService {

    @Autowired
    private ApiUserDao apiUserDao;

    private LoadingCache<String, String> keyByHostname = CacheBuilder.newBuilder() //
            .maximumSize(1000) //
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String hostname) throws Exception {
                    String hostKeySalt = CentralApp.getCentralConfig().getHostKeySalt();
                    return HashSha256.hashString(hostKeySalt + hostname);
                }
            });

    public void cacheClear() {
        keyByHostname.invalidateAll();
    }

    @Override
    public String deriveHostnameKey(String hostname) {
        String hostKeySalt = CentralApp.getCentralConfig().getHostKeySalt();
        if (hostKeySalt == null) {
            return null;
        }
        try {
            return keyByHostname.get(hostname);
        } catch (ExecutionException e) {
            throw new UsageMetricException(e);
        }
    }

    @Override
    public ApiUser getApiUser(String username) {
        return apiUserDao.findById(username).orElse(null);
    }

}
