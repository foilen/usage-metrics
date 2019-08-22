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

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.foilen.usagemetrics.central.AbstractSpringTests;
import com.google.common.base.Joiner;

public class UsageResourceDaoImplTest extends AbstractSpringTests {

    @Autowired
    private UsageResourceDao usageResourceDao;

    private void assertBatches(List<String> actual, String... expected) {
        Joiner joiner = Joiner.on('\n');

        Assert.assertEquals(joiner.join(expected), joiner.join(actual));
    }

    @Test
    public void testFindBatchesBetweenTime() {

        // Create some data
        usageResourceDao.save(urx("h1", "b1", 10, 20, "DiskSpace", "a", 100));
        usageResourceDao.save(urx("h1", "b1", 11, 20, "DiskSpace", "b", 101));
        usageResourceDao.save(urx("h1", "b2", 20, 30, "DiskSpace", "a", 200));

        // Useless data
        usageResourceDao.save(urx("h1", "b1", 10, 20, "Docker", "a", 888));
        usageResourceDao.save(urx("h2", "b1", 10, 20, "DiskSpace", "a", 999));
        usageResourceDao.save(urx("h2", "b2", 20, 100, "DiskSpace", "a", 999));

        assertBatches(usageResourceDao.findBatchesBetweenTime(new Date(0), new Date(9), "h1", "DiskSpace")); // Outside
        assertBatches(usageResourceDao.findBatchesBetweenTime(new Date(9), new Date(12), "h1", "DiskSpace"), "b1"); // to inside
        assertBatches(usageResourceDao.findBatchesBetweenTime(new Date(13), new Date(18), "h1", "DiskSpace"), "b1");// from and to inside
        assertBatches(usageResourceDao.findBatchesBetweenTime(new Date(0), new Date(29), "h1", "DiskSpace"), "b1", "b2"); // block inside
        assertBatches(usageResourceDao.findBatchesBetweenTime(new Date(0), new Date(30), "h1", "DiskSpace"), "b1", "b2"); // block inside
        assertBatches(usageResourceDao.findBatchesBetweenTime(new Date(0), new Date(40), "h1", "DiskSpace"), "b1", "b2");// block inside
        assertBatches(usageResourceDao.findBatchesBetweenTime(new Date(15), new Date(25), "h1", "DiskSpace"), "b1", "b2");// to inside first ; from inside second
        assertBatches(usageResourceDao.findBatchesBetweenTime(new Date(25), new Date(35), "h1", "DiskSpace"), "b2");// from inside

    }

}
