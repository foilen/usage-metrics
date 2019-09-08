/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central.service;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.foilen.smalltools.test.asserts.AssertTools;
import com.foilen.usagemetrics.central.AbstractSpringTests;
import com.foilen.usagemetrics.central.dao.domain.OwnerMapping;
import com.foilen.usagemetrics.central.dao.domain.model.OwnerUsageResourceMapping;

public class ReportServiceImplTest extends AbstractSpringTests {

    @Autowired
    private ReportService reportService;

    private void createData() {
        usageResourceDao.save(urx("h1", null, 0, 10000, "DiskUsage", "aaa", 10));
        usageResourceDao.save(urx("h1", null, 0, 10000, "Docker", "aaa", 100));
        usageResourceDao.save(urx("h1", null, 0, 10000, "DiskUsage", "bbb", 20));
        usageResourceDao.save(urx("h2", null, 0, 10000, "DiskUsage", "ccc", 30));

        usageResourceDao.save(urx("h1", null, 10001, 20000, "DiskUsage", "aaa", 50));
        usageResourceDao.save(urx("h1", null, 10001, 20000, "DiskUsage", "bbb", 60));
        usageResourceDao.save(urx("h2", null, 10001, 20000, "DiskUsage", "ccc", 70));
    }

    @Test
    public void testGetReport_2owners_t1() {

        createData();

        OwnerMapping o1 = new OwnerMapping().setOwnerName("o1");
        o1.getOwnerUsageResourceMappings().add(new OwnerUsageResourceMapping("DiskUsage", "aaa"));
        o1.getOwnerUsageResourceMappings().add(new OwnerUsageResourceMapping("DiskUsage", "bbb"));
        ownerMappingDao.save(o1);
        OwnerMapping o2 = new OwnerMapping().setOwnerName("o2");
        o2.getOwnerUsageResourceMappings().add(new OwnerUsageResourceMapping("Docker", "aaa"));
        ownerMappingDao.save(o2);

        AssertTools.assertJsonComparison("testGetReport_2owners_t1.json", getClass(), reportService.getReport(new Date(1000)));
    }

    @Test
    public void testGetReport_2owners_t2() {

        createData();

        OwnerMapping o1 = new OwnerMapping().setOwnerName("o1");
        o1.getOwnerUsageResourceMappings().add(new OwnerUsageResourceMapping("DiskUsage", "aaa"));
        o1.getOwnerUsageResourceMappings().add(new OwnerUsageResourceMapping("DiskUsage", "bbb"));
        ownerMappingDao.save(o1);
        OwnerMapping o2 = new OwnerMapping().setOwnerName("o2");
        o2.getOwnerUsageResourceMappings().add(new OwnerUsageResourceMapping("Docker", "aaa"));
        ownerMappingDao.save(o2);

        AssertTools.assertJsonComparison("testGetReport_2owners_t2.json", getClass(), reportService.getReport(new Date(10100)));
    }

}
