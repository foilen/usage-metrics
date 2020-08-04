/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.foilen.smalltools.test.asserts.AssertTools;
import com.foilen.usagemetrics.api.model.OwnerUsageResourceMapping;
import com.foilen.usagemetrics.central.AbstractSpringTests;
import com.foilen.usagemetrics.central.dao.domain.OwnerMapping;

public class OwnerMappingServiceImplTest extends AbstractSpringTests {

    @Autowired
    private OwnerMappingService ownerMappingService;

    private void createData() {
        ownerMappingDao.save(new OwnerMapping().setOwnerName("infra").setComment("All the infrastructure stuff") //
                .addOwnerUsageResourceMappings(new OwnerUsageResourceMapping("DiskUsage", "infra_aaa")) //
                .addOwnerUsageResourceMappings(new OwnerUsageResourceMapping("Docker", "infra_aaa")) //
        );
        ownerMappingDao.save(new OwnerMapping().setOwnerName("alpha") //
                .addOwnerUsageResourceMappings(new OwnerUsageResourceMapping("DiskUsage", "bbb")) //
                .addOwnerUsageResourceMappings(new OwnerUsageResourceMapping("Docker", "bbb")) //
                .addOwnerUsageResourceMappings(new OwnerUsageResourceMapping("DiskUsage", "ccc")) //
                .addOwnerUsageResourceMappings(new OwnerUsageResourceMapping("Docker", "ccc")) //
        );
    }

    @Test
    public void testFindAll() {
        createData();

        AssertTools.assertJsonComparisonWithoutNulls("OwnerMappingServiceImplTest-testFindAll.json", getClass(), ownerMappingService.findAll());
    }

}
