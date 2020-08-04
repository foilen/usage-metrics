/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foilen.smalltools.restapi.model.FormResult;
import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.smalltools.tools.JsonTools;
import com.foilen.usagemetrics.api.form.ResourcesAddForm;
import com.foilen.usagemetrics.central.security.ApiUsersUserDetailsService;
import com.foilen.usagemetrics.central.service.UsageResourceService;

@RestController
@RequestMapping("resource")
@Secured(ApiUsersUserDetailsService.ROLE_HOST)
public class ResourceController extends AbstractBasics {

    @Autowired
    private UsageResourceService usageResourceService;

    @PostMapping("/")
    public FormResult addResources(@AuthenticationPrincipal User user, @RequestBody ResourcesAddForm form) {
        logger.info("Hostname: {} -> Usage Resources: {}", user.getUsername(), JsonTools.compactPrintWithoutNulls(form.getUsageResources()));
        usageResourceService.addUsageResource(user.getUsername(), form.getUsageResources());
        return new FormResult();
    }

}
