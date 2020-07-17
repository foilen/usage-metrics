/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.foilen.smalltools.tools.ResourceTools;
import com.foilen.usagemetrics.api.model.UsageResource;
import com.foilen.usagemetrics.central.AbstractSpringTests;
import com.foilen.usagemetrics.central.dao.UsageResourceDao;
import com.foilen.usagemetrics.central.dao.domain.UsageResourceExtended;
import com.foilen.usagemetrics.common.UsageMetricException;

public class UsageResourceServiceImplTest extends AbstractSpringTests {

    @Autowired
    private UsageResourceDao usageResourceDao;
    @Autowired
    private UsageResourceService usageResourceService;

    private void assertResourceStories() {
        assertResourceStory("h1", "DiskSpace");
        assertResourceStory("h1", "Docker");
        assertResourceStory("h2", "DiskSpace");
    }

    private void assertResourceStoriesNearTime() {
        assertResourceStoryNearTime("h1", "DiskSpace");
        assertResourceStoryNearTime("h1", "Docker");
    }

    private void assertResourceStory(String hostname, String usageResourceType) {

        // Expected
        String expectedResourceName = "UsageResourceServiceImplTest-story-" + hostname + "-" + usageResourceType + ".txt";
        String expected = ResourceTools.getResourceAsString(expectedResourceName, getClass());
        StringBuilder actual = new StringBuilder();

        // Story
        List<UsageResourceExtended> usageResources = usageResourceDao.findAllByHostnameAndUsageResourceType(hostname, usageResourceType);
        usageResources.forEach(ur -> {
            actual.append(ur.getTimestamp().getTime()).append(" -> ").append(ur.getEndTimestamp().getTime()).append(" ");
            actual.append(ur.getDetails()).append(" ");
            actual.append(ur.getSize()).append("\n");
        });

        // Assert
        Assert.assertEquals(expected, actual.toString());
    }

    private void assertResourceStoryNearTime(String hostname, String usageResourceType) {

        // Expected
        String expectedResourceName = "UsageResourceServiceImplTest-story-near-time-" + hostname + "-" + usageResourceType + ".txt";
        String expected = ResourceTools.getResourceAsString(expectedResourceName, getClass());
        StringBuilder actual = new StringBuilder();

        // Story
        List<UsageResourceExtended> usageResources = usageResourceDao.findAllByHostnameAndUsageResourceType(hostname, usageResourceType);
        usageResources.forEach(ur -> {
            actual.append(ur.getTimestamp().getTime()).append(" -> ").append(ur.getEndTimestamp().getTime()).append(" ");
            actual.append(ur.getDetails()).append(" ");
            actual.append(ur.getSize()).append("\n");
        });

        // Assert
        Assert.assertEquals(expected, actual.toString());
    }

    @SafeVarargs
    private List<UsageResource> concat(List<UsageResource>... lists) {
        List<UsageResource> all = new ArrayList<>();
        for (List<UsageResource> list : lists) {
            all.addAll(list);
        }
        return all;
    }

    private List<UsageResource> t1h1() {
        return Arrays.asList( //
                ur("b1.1", 10, "DiskSpace", "a", 100), //
                ur("b1.1", 11, "DiskSpace", "b", 101), //
                ur("b1.2", 12, "Docker", "a", 4) //
        );
    }

    private List<UsageResource> t1h2() {
        return Arrays.asList( //
                ur("b1.1", 10, "DiskSpace", "a", 7) //
        );
    }

    private List<UsageResource> t2h1() {
        return Arrays.asList( //
                ur("b2.1", 20, "DiskSpace", "a", 200), //
                ur("b2.1", 21, "DiskSpace", "c", 203), //
                ur("b2.2", 22, "Docker", "a", 4) //
        );
    }

    private List<UsageResource> t2h1Part1() {
        return Arrays.asList( //
                ur("b2.1", 20, "DiskSpace", "a", 200), //
                ur("b2.2", 22, "Docker", "a", 4) //
        );
    }

    private List<UsageResource> t2h1Part2() {
        return Arrays.asList( //
                ur("b2.1", 21, "DiskSpace", "c", 203), //
                ur("b2.2", 22, "Docker", "a", 4) //
        );
    }

    private List<UsageResource> t2h2() {
        return Arrays.asList( //
                ur("b2.1", 20, "DiskSpace", "a", 8) //
        );
    }

    private List<UsageResource> t3h1() {
        return Arrays.asList( //
                ur("b3.1", 30, "DiskSpace", "a", 300), //
                ur("b3.1", 31, "DiskSpace", "b", 305), //
                ur("b3.2", 32, "Docker", "a", 6) //
        );
    }

    private List<UsageResource> t3h2() {
        return Arrays.asList( //
                ur("b3.1", 30, "DiskSpace", "a", 9) //
        );
    }

    @Test
    public void testAddResource_inOrder() {
        usageResourceService.addUsageResource("h1", t1h1());
        usageResourceService.addUsageResource("h2", t1h2());

        usageResourceService.addUsageResource("h1", t2h1());
        usageResourceService.addUsageResource("h2", t2h2());

        usageResourceService.addUsageResource("h1", t3h1());
        usageResourceService.addUsageResource("h2", t3h2());

        assertResourceStories();
    }

    @Test
    public void testAddResource_inOrder_multi() {
        usageResourceService.addUsageResource("h1", t1h1());
        usageResourceService.addUsageResource("h2", t1h2());

        usageResourceService.addUsageResource("h1", t2h1());
        usageResourceService.addUsageResource("h2", concat(t2h2(), t3h2()));

        usageResourceService.addUsageResource("h1", t3h1());

        assertResourceStories();
    }

    @Test
    public void testAddResource_inOrder_one_at_a_time() {

        List<UsageResource> all = concat(t1h1(), t2h1(), t3h1());

        all.forEach(usageResource -> {
            usageResourceService.addUsageResource("h1", Arrays.asList(usageResource));
        });

        assertResourceStoriesNearTime();
    }

    @Test
    public void testAddResource_inOrder_withReplay_1() {
        usageResourceService.addUsageResource("h1", t1h1());
        usageResourceService.addUsageResource("h2", t1h2());

        usageResourceService.addUsageResource("h1", t2h1());
        usageResourceService.addUsageResource("h2", t2h2());

        usageResourceService.addUsageResource("h1", concat(t2h1Part1(), t3h1()));

        usageResourceService.addUsageResource("h2", concat(t2h2(), t3h2()));

        assertResourceStories();
    }

    @Test
    public void testAddResource_inOrder_withReplay_2() {
        usageResourceService.addUsageResource("h1", t1h1());
        usageResourceService.addUsageResource("h2", t1h2());

        usageResourceService.addUsageResource("h1", t2h1());
        usageResourceService.addUsageResource("h2", t2h2());

        usageResourceService.addUsageResource("h1", concat(t2h1Part2(), t3h1()));
        usageResourceService.addUsageResource("h2", concat(t2h2(), t3h2()));

        assertResourceStories();
    }

    @Test
    public void testAddResource_inReverseOrder_notAllowed() {
        Assert.assertThrows("Insertion of older entries is not allowed", UsageMetricException.class, () -> {
            usageResourceService.addUsageResource("h1", t3h1());
            usageResourceService.addUsageResource("h1", t2h1());
        });
    }

}
