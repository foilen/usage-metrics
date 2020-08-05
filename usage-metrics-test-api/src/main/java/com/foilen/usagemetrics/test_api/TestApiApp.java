/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.test_api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.foilen.smalltools.restapi.exception.ApiExceptionTools;
import com.foilen.smalltools.restapi.model.FormResult;
import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.smalltools.tools.JsonTools;
import com.foilen.smalltools.tools.SecureRandomTools;
import com.foilen.smalltools.tools.StringTools;
import com.foilen.usagemetrics.api.UsageCentralApiClient;
import com.foilen.usagemetrics.api.UsageCentralApiClientImpl;
import com.foilen.usagemetrics.api.model.OwnerMapping;
import com.foilen.usagemetrics.api.model.OwnerMappingResult;
import com.foilen.usagemetrics.api.model.OwnerMappingResults;
import com.foilen.usagemetrics.api.model.ReportForOwner;
import com.foilen.usagemetrics.api.model.ReportShowResult;
import com.foilen.usagemetrics.api.model.UsageResource;

public class TestApiApp extends AbstractBasics {

    private static final String OWNER = "testing_api";

    private static void addResources(UsageCentralApiClient usageCentralApiClient) {

        System.out.println("===[ Add resources ]===");

        String batchId = SecureRandomTools.randomHexString(4);
        List<UsageResource> usageResources = new ArrayList<UsageResource>();
        usageResources.add(new UsageResource().setBatchId(batchId).setUsageResourceType("localDisk").setOwner("alpha").setDetails("/home/alpha").setSize(1000));
        usageResources.add(new UsageResource().setBatchId(batchId).setUsageResourceType("localDisk").setOwner("beta").setDetails("/home/beta").setSize(2000));
        usageResources.add(new UsageResource().setBatchId(batchId).setUsageResourceType("localDisk").setOwner("cesar").setDetails("/home/cesar").setSize(3000));

        FormResult formResult = usageCentralApiClient.resourceAdd(usageResources);
        ApiExceptionTools.throwIfFailure("Add resources", formResult);

        System.out.println("Success: " + JsonTools.compactPrintWithoutNulls(formResult));

    }

    private static void createOwnerMapping(UsageCentralApiClient usageCentralApiClient) {

        System.out.println("===[ Owner Mapping Create ]===");

        FormResult formResult = usageCentralApiClient.ownerMappingAdd(OWNER);
        ApiExceptionTools.throwIfFailure("Create Owner Mapping", formResult);

        System.out.println("Success: " + JsonTools.compactPrintWithoutNulls(formResult));
    }

    private static void createOwnerMapping(UsageCentralApiClient usageCentralApiClient, String type, String owner) {

        System.out.println("===[ Owner Mapping Add Mapping ]===");

        FormResult formResult = usageCentralApiClient.ownerMappingAddMapping(OWNER, type, owner);
        ApiExceptionTools.throwIfFailure("Add Owner Mapping", formResult);

        System.out.println("Success: " + JsonTools.compactPrintWithoutNulls(formResult));
    }

    private static void listOwnerMapping(UsageCentralApiClient usageCentralApiClient) {

        System.out.println("===[ Owner Mapping List ]===");

        OwnerMappingResults ownerMappingResults = usageCentralApiClient.ownerMappingFindAll();
        ApiExceptionTools.throwIfFailure("Get owner mapping list", ownerMappingResults);

        ownerMappingResults.getItems().forEach(owner -> {
            System.out.println(owner.getOwnerName() + " (" + owner.getComment() + ")");
            owner.getOwnerUsageResourceMappings().forEach(mapping -> {
                System.out.println("\t" + mapping.getType() + " / " + mapping.getOwner());
            });
        });
    }

    public static void main(String[] args) {

        if (args.length != 5) {
            System.out.println("Provide the central Uri, the API username, the API password, the hostname, the hostname password");
            System.out.println("E.g: http://127.0.0.1:8080 test test");
            System.exit(1);
        }

        UsageCentralApiClient apiClient = new UsageCentralApiClientImpl(args[0], args[1], args[2]);
        UsageCentralApiClient hostClient = new UsageCentralApiClientImpl(args[0], args[3], args[4]);

        listOwnerMapping(apiClient);

        createOwnerMapping(apiClient);

        createOwnerMapping(apiClient, "localDisk", "alpha");
        createOwnerMapping(apiClient, "localDisk", "beta");
        createOwnerMapping(apiClient, "docker", "alpha");
        createOwnerMapping(apiClient, "docker", "beta");

        listOwnerMapping(apiClient);

        showOwnerMapping(apiClient);

        addResources(hostClient);

        showReport(apiClient);
        removeOwnerMapping(apiClient, "localDisk", "beta");
        showReport(apiClient);

    }

    private static void removeOwnerMapping(UsageCentralApiClient usageCentralApiClient, String type, String owner) {

        System.out.println("===[ Owner Mapping Remove Mapping ]===");

        FormResult formResult = usageCentralApiClient.ownerMappingRemoveMapping(OWNER, type, owner);
        ApiExceptionTools.throwIfFailure("Remove Owner Mapping", formResult);

        System.out.println("Success: " + JsonTools.compactPrintWithoutNulls(formResult));
    }

    private static void showOwnerMapping(UsageCentralApiClient usageCentralApiClient) {

        System.out.println("===[ Owner Mapping Get ]===");

        OwnerMappingResult ownerMappingResult = usageCentralApiClient.ownerMappingFind(OWNER);
        ApiExceptionTools.throwIfFailure("Get owner mapping", ownerMappingResult);

        OwnerMapping owner = ownerMappingResult.getItem();
        System.out.println(owner.getOwnerName() + " (" + owner.getComment() + ")");
        owner.getOwnerUsageResourceMappings().forEach(mapping -> {
            System.out.println("\t" + mapping.getType() + " / " + mapping.getOwner());
        });
    }

    private static void showReport(UsageCentralApiClient usageCentralApiClient) {

        System.out.println("===[ Show Report ]===");

        ReportShowResult reportShowResult = usageCentralApiClient.reportShow(new Date());
        ApiExceptionTools.throwIfFailure("Show Report", reportShowResult);

        reportShowResult.getReportByOwner().entrySet().stream() //
                .sorted((a, b) -> StringTools.safeComparisonNullFirst(a.getKey(), b.getKey())) //
                .forEach(entry -> {
                    String owner = entry.getKey();
                    ReportForOwner reportForOwner = entry.getValue();
                    System.out.println(owner + " " + reportForOwner.getSize());
                    reportForOwner.getUsages().forEach(usage -> {
                        System.out.println("\t" + usage);
                    });
                });
    }

}
