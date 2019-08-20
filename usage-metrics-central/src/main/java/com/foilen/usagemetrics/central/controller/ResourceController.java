/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foilen.smalltools.restapi.model.FormResult;
import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.smalltools.tools.JsonTools;
import com.foilen.usagemetrics.common.api.form.ResourcesAddForm;

@Controller
@RequestMapping("resource")
public class ResourceController extends AbstractBasics {

    @PostMapping("/")
    public FormResult addResources(@RequestBody ResourcesAddForm form) {
        // TODO Check AUTH
        FormResult result = new FormResult();
        logger.info("{}", JsonTools.compactPrintWithoutNulls(form));
        // TODO Store in MongoDB
        return result;
    }

}
