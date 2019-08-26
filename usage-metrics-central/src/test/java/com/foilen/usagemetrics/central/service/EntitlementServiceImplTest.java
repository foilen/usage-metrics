/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.foilen.usagemetrics.central.AbstractSpringTests;
import com.foilen.usagemetrics.central.CentralApp;

public class EntitlementServiceImplTest extends AbstractSpringTests {

    @Autowired
    private EntitlementServiceImpl entitlementService;

    @Test
    public void testHostCanAddResources_withoutSalt() {
        entitlementService.cacheClear();
        CentralApp.getCentralConfig().setHostKeySalt(null);
        Assert.assertFalse(entitlementService.hostCanAddResources("h1.example.com", "22f606813cd8b680aed4282cdf4fe357b435ddd2777dc5619c658de56461f886"));
        Assert.assertFalse(entitlementService.hostCanAddResources("h1.example.com", "000606813cd8b680aed4282cdf4fe357b435ddd2777dc5619c658de56461f000"));
        Assert.assertTrue(entitlementService.hostCanAddResources("h1.example.com", null));
        Assert.assertFalse(entitlementService.hostCanAddResources("h2.example.com", "ebfef80e56ac1f076573a7f670cdaaf3ebc50164ebad9f5c4cbf952eb0b37c5e"));
        Assert.assertTrue(entitlementService.hostCanAddResources("h2.example.com", null));
    }

    @Test
    public void testHostCanAddResources_withSalt() {
        entitlementService.cacheClear();
        CentralApp.getCentralConfig().setHostKeySalt("37D5C");
        Assert.assertTrue(entitlementService.hostCanAddResources("h1.example.com", "22f606813cd8b680aed4282cdf4fe357b435ddd2777dc5619c658de56461f886"));
        Assert.assertFalse(entitlementService.hostCanAddResources("h1.example.com", "000606813cd8b680aed4282cdf4fe357b435ddd2777dc5619c658de56461f000"));
        Assert.assertFalse(entitlementService.hostCanAddResources("h1.example.com", null));
        Assert.assertTrue(entitlementService.hostCanAddResources("h2.example.com", "ebfef80e56ac1f076573a7f670cdaaf3ebc50164ebad9f5c4cbf952eb0b37c5e"));
    }

}
