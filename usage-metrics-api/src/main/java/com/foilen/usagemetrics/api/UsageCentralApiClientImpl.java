/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.api;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;

import com.foilen.smalltools.restapi.model.FormResult;
import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.usagemetrics.api.form.OwnerMappingAddForm;
import com.foilen.usagemetrics.api.form.OwnerUsageResourceMappingOwnerMappingForm;
import com.foilen.usagemetrics.api.form.ReportShowForm;
import com.foilen.usagemetrics.api.form.ResourcesAddForm;
import com.foilen.usagemetrics.api.model.OwnerMappingResult;
import com.foilen.usagemetrics.api.model.OwnerMappingResults;
import com.foilen.usagemetrics.api.model.ReportShowResult;
import com.foilen.usagemetrics.api.model.UsageResource;

public class UsageCentralApiClientImpl extends AbstractBasics implements UsageCentralApiClient {

    private String centralUri;

    private RestTemplate restTemplate = new RestTemplate();

    public UsageCentralApiClientImpl(String centralUri, String authUser, String authKey) {
        this.centralUri = centralUri;

        if (this.centralUri.endsWith("/")) {
            this.centralUri = this.centralUri.substring(0, this.centralUri.length() - 1);
        }

        this.restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(authUser, authKey));

    }

    @Override
    public FormResult ownerMappingAdd(String ownerName) {
        OwnerMappingAddForm form = new OwnerMappingAddForm() //
                .setOwnerName(ownerName);

        return restTemplate.postForObject(centralUri + "/ownerMapping/", form, FormResult.class);
    }

    @Override
    public FormResult ownerMappingAddMapping(String ownerName, String type, String owner) {
        OwnerUsageResourceMappingOwnerMappingForm form = new OwnerUsageResourceMappingOwnerMappingForm() //
                .setType(type) //
                .setOwner(owner);

        return restTemplate.postForObject(centralUri + "/ownerMapping/{ownerName}", form, FormResult.class, Collections.singletonMap("ownerName", ownerName));
    }

    @Override
    public FormResult ownerMappingDelete(String ownerName) {
        ResponseEntity<FormResult> responseEntity = restTemplate.exchange(centralUri + "/ownerMapping/{ownerName}", HttpMethod.DELETE, null, FormResult.class,
                Collections.singletonMap("ownerName", ownerName));
        return responseEntity.getBody();
    }

    @Override
    public OwnerMappingResult ownerMappingFind(String ownerName) {
        return restTemplate.getForObject(centralUri + "/ownerMapping/{ownerName}", OwnerMappingResult.class, Collections.singletonMap("ownerName", ownerName));
    }

    @Override
    public OwnerMappingResults ownerMappingFindAll() {
        return restTemplate.getForObject(centralUri + "/ownerMapping/", OwnerMappingResults.class);
    }

    @Override
    public FormResult ownerMappingRemoveMapping(String ownerName, String type, String owner) {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("ownerName", ownerName);
        uriVariables.put("type", type);
        uriVariables.put("owner", owner);
        ResponseEntity<FormResult> responseEntity = restTemplate.exchange(centralUri + "/ownerMapping/{ownerName}/{type}/{owner}", HttpMethod.DELETE, null, FormResult.class, uriVariables);
        return responseEntity.getBody();
    }

    @Override
    public ReportShowResult reportShow(Date forDate) {
        ReportShowForm form = new ReportShowForm() //
                .setForDate(forDate);
        return restTemplate.postForObject(centralUri + "/report/showReport", form, ReportShowResult.class);
    }

    @Override
    public FormResult resourceAdd(List<UsageResource> usageResources) {
        ResourcesAddForm form = new ResourcesAddForm() //
                .setUsageResources(usageResources);
        return restTemplate.postForObject(centralUri + "/resource/", form, FormResult.class);
    }

}
