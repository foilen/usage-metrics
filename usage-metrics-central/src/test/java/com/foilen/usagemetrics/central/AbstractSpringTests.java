/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central;

import java.util.Date;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.foilen.usagemetrics.api.model.UsageResource;
import com.foilen.usagemetrics.central.dao.OwnerMappingDao;
import com.foilen.usagemetrics.central.dao.UsageResourceDao;
import com.foilen.usagemetrics.central.dao.domain.UsageResourceExtended;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("JUNIT")
public abstract class AbstractSpringTests {

    @Autowired
    protected OwnerMappingDao ownerMappingDao;
    @Autowired
    protected UsageResourceDao usageResourceDao;

    public AbstractSpringTests() {
        System.setProperty("spring.data.mongodb.uri", "mongodb://127.0.0.1:27017/usage-unit-tests");
    }

    @Before
    public void clearDb() {
        ownerMappingDao.deleteAll();
        usageResourceDao.deleteAll();
    }

    protected UsageResource ur(String batchId, long time, String usageResourceType, String details, long amount) {
        return new UsageResource() //
                .setBatchId(batchId) //
                .setTimestamp(new Date(time)) //
                .setUsageResourceType(usageResourceType) //
                .setOwner(details) //
                .setDetails(details) //
                .setSize(amount) //
        ;
    }

    protected UsageResourceExtended urx(String hostname, String batchId, long startTime, long endTime, String usageResourceType, String details, long amount) {

        UsageResourceExtended urx = new UsageResourceExtended();

        urx.setHostname(hostname) //
                .setEndTimestamp(new Date(endTime));

        urx.setBatchId(batchId) //
                .setTimestamp(new Date(startTime)) //
                .setUsageResourceType(usageResourceType) //
                .setOwner(details) //
                .setDetails(details) //
                .setSize(amount);

        return urx;
    }

}
