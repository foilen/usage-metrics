/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foilen.smalltools.restapi.model.ApiError;
import com.foilen.smalltools.restapi.model.FormResult;
import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.smalltools.tools.JsonTools;
import com.foilen.usagemetrics.central.service.EntitlementService;
import com.foilen.usagemetrics.central.service.UsageResourceService;
import com.foilen.usagemetrics.common.api.form.ResourcesAddForm;

@Controller
@RequestMapping("resource")
public class ResourceController extends AbstractBasics {

    @Autowired
    private EntitlementService entitlementService;
    @Autowired
    private UsageResourceService usageResourceService;

    @PostMapping("/")
    public FormResult addResources(@RequestBody ResourcesAddForm form) {
        FormResult result = new FormResult();
        if (!entitlementService.hostCanAddResources(form.getAuthUser(), form.getAuthKey())) {
            result.setError(new ApiError("The host does not have the right key"));
            return result;
        }

        logger.info("Hostname: {} -> Usage Resources: {}", form.getAuthUser(), JsonTools.compactPrintWithoutNulls(form.getUsageResources()));
        usageResourceService.addUsageResource(form.getAuthUser(), form.getUsageResources());
        return result;
    }

}
