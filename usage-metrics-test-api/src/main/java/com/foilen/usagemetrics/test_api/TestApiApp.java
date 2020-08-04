/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.test_api;

import com.foilen.smalltools.restapi.exception.ApiExceptionTools;
import com.foilen.smalltools.restapi.model.FormResult;
import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.smalltools.tools.JsonTools;
import com.foilen.usagemetrics.api.UsageCentralApiClient;
import com.foilen.usagemetrics.api.UsageCentralApiClientImpl;
import com.foilen.usagemetrics.api.model.OwnerMapping;
import com.foilen.usagemetrics.api.model.OwnerMappingResult;
import com.foilen.usagemetrics.api.model.OwnerMappingResults;

public class TestApiApp extends AbstractBasics {

    private static final String OWNER = "testing_api";

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

        if (args.length != 3) {
            System.out.println("Provide the central Uri, the username and the password");
            System.out.println("E.g: http://127.0.0.1:8080 test test");
            System.exit(1);
        }

        UsageCentralApiClient usageCentralApiClient = new UsageCentralApiClientImpl(args[0], args[1], args[2]);

        listOwnerMapping(usageCentralApiClient);

        createOwnerMapping(usageCentralApiClient);

        createOwnerMapping(usageCentralApiClient, "localDisk", "alpha");
        createOwnerMapping(usageCentralApiClient, "localDisk", "beta");
        createOwnerMapping(usageCentralApiClient, "docker", "alpha");
        createOwnerMapping(usageCentralApiClient, "docker", "beta");

        listOwnerMapping(usageCentralApiClient);

        removeOwnerMapping(usageCentralApiClient, "localDisk", "beta");
        listOwnerMapping(usageCentralApiClient);

        showOwnerMapping(usageCentralApiClient);

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

}
