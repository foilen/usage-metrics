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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foilen.smalltools.restapi.model.FormResult;
import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.usagemetrics.api.form.OwnerMappingAddForm;
import com.foilen.usagemetrics.api.form.OwnerUsageResourceMappingOwnerMappingForm;
import com.foilen.usagemetrics.api.model.OwnerMappingResult;
import com.foilen.usagemetrics.api.model.OwnerMappingResults;
import com.foilen.usagemetrics.central.security.ApiUsersUserDetailsService;
import com.foilen.usagemetrics.central.service.OwnerMappingService;

@RestController
@RequestMapping("ownerMapping")
@Secured(ApiUsersUserDetailsService.ROLE_API_USER)
public class OwnerMappingController extends AbstractBasics {

    @Autowired
    private OwnerMappingService ownerMappingService;

    @PostMapping("/")
    public FormResult add(@RequestBody OwnerMappingAddForm form) {
        return ownerMappingService.add(form);
    }

    @PostMapping("/{ownerName}")
    public FormResult addOwnerMapping(@PathVariable String ownerName, @RequestBody OwnerUsageResourceMappingOwnerMappingForm form) {
        return ownerMappingService.addMapping(ownerName, form);
    }

    @DeleteMapping("/{ownerName}")
    public FormResult delete(@PathVariable String ownerName) {
        return ownerMappingService.delete(ownerName);
    }

    @DeleteMapping("/{ownerName}/{type}/{owner}")
    public FormResult deleteOwnerMapping(@PathVariable String ownerName, @PathVariable String type, @PathVariable String owner) {
        return ownerMappingService.deleteMapping(ownerName, type, owner);
    }

    @GetMapping("/")
    public OwnerMappingResults findAll() {
        return ownerMappingService.findAll();
    }

    @GetMapping("/{ownerName}")
    public OwnerMappingResult get(@PathVariable String ownerName) {
        return ownerMappingService.find(ownerName);
    }

}
