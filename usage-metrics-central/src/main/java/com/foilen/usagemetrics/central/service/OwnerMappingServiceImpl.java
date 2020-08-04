/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.central.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foilen.smalltools.restapi.model.ApiError;
import com.foilen.smalltools.restapi.model.FormResult;
import com.foilen.smalltools.restapi.services.FormValidationTools;
import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.smalltools.tools.CollectionsTools;
import com.foilen.smalltools.tools.JsonTools;
import com.foilen.usagemetrics.api.form.OwnerMappingAddForm;
import com.foilen.usagemetrics.api.form.OwnerUsageResourceMappingOwnerMappingForm;
import com.foilen.usagemetrics.api.model.OwnerMappingResult;
import com.foilen.usagemetrics.api.model.OwnerMappingResults;
import com.foilen.usagemetrics.api.model.OwnerUsageResourceMapping;
import com.foilen.usagemetrics.central.dao.OwnerMappingDao;
import com.foilen.usagemetrics.central.dao.domain.OwnerMapping;

@Service
@Transactional
public class OwnerMappingServiceImpl extends AbstractBasics implements OwnerMappingService {

    @Autowired
    private OwnerMappingDao ownerMappingDao;

    @Override
    public FormResult add(OwnerMappingAddForm form) {

        FormResult result = new FormResult();

        // Validations
        FormValidationTools.validateMandatory(result, "ownerName", form.getOwnerName());
        if (!result.isSuccess()) {
            return result;
        }

        // Check that it doesn't already exists
        Optional<OwnerMapping> existing = ownerMappingDao.findById(form.getOwnerName());
        if (existing.isPresent()) {
            result.getGlobalWarnings().add("Already exists");
            return result;
        }

        // Create
        ownerMappingDao.save(JsonTools.clone(form, OwnerMapping.class));
        return result;

    }

    @Override
    public FormResult addMapping(String ownerName, OwnerUsageResourceMappingOwnerMappingForm form) {

        FormResult result = new FormResult();

        // Validations
        FormValidationTools.validateMandatory(result, "ownerName", ownerName);
        FormValidationTools.validateMandatory(result, "type", form.getType());
        FormValidationTools.validateMandatory(result, "owner", form.getOwner());
        if (!result.isSuccess()) {
            return result;
        }

        // Check that it doesn't already exists
        Optional<OwnerMapping> existing = ownerMappingDao.findById(ownerName);
        if (existing.isEmpty()) {
            CollectionsTools.getOrCreateEmptyArrayList(result.getValidationErrorsByField(), "ownerName", String.class).add("Does not exist");
        }
        if (!result.isSuccess()) {
            return result;
        }

        OwnerMapping ownerMapping = existing.get();
        if (!ownerMapping.getOwnerUsageResourceMappings().add(new OwnerUsageResourceMapping(form.getType(), form.getOwner()))) {
            result.getGlobalWarnings().add("Already exists");
            return result;
        }

        // Update
        ownerMappingDao.save(ownerMapping);
        return result;

    }

    @Override
    public FormResult delete(String ownerName) {

        FormResult result = new FormResult();

        // Validations
        FormValidationTools.validateMandatory(result, "ownerName", ownerName);
        if (!result.isSuccess()) {
            return result;
        }

        // Check that it doesn't already exists
        Optional<OwnerMapping> existing = ownerMappingDao.findById(ownerName);
        if (!existing.isPresent()) {
            result.getGlobalWarnings().add("Already deleted");
            return result;
        }

        // Delete
        ownerMappingDao.deleteById(ownerName);
        return result;

    }

    @Override
    public FormResult deleteMapping(String ownerName, String type, String owner) {

        FormResult result = new FormResult();

        // Validations
        FormValidationTools.validateMandatory(result, "ownerName", ownerName);
        FormValidationTools.validateMandatory(result, "type", type);
        FormValidationTools.validateMandatory(result, "owner", owner);
        if (!result.isSuccess()) {
            return result;
        }

        // Check that it doesn't already exists
        Optional<OwnerMapping> existing = ownerMappingDao.findById(ownerName);
        if (existing.isEmpty()) {
            CollectionsTools.getOrCreateEmptyArrayList(result.getValidationErrorsByField(), "ownerName", String.class).add("Does not exist");
        }
        if (!result.isSuccess()) {
            return result;
        }

        OwnerMapping ownerMapping = existing.get();
        if (!ownerMapping.getOwnerUsageResourceMappings().remove(new OwnerUsageResourceMapping(type, owner))) {
            result.getGlobalWarnings().add("Already deleted");
            return result;
        }

        // Update
        ownerMappingDao.save(ownerMapping);
        return result;

    }

    @Override
    public OwnerMappingResult find(String ownerName) {

        OwnerMappingResult ownerMappingResult = new OwnerMappingResult();

        Optional<OwnerMapping> ownerMappingO = ownerMappingDao.findById(ownerName);
        if (ownerMappingO.isPresent()) {
            ownerMappingResult.setItem(JsonTools.clone(ownerMappingO.get(), com.foilen.usagemetrics.api.model.OwnerMapping.class));
        } else {
            ownerMappingResult.setError(new ApiError("Does not exist"));
        }

        return ownerMappingResult;

    }

    @Override
    public OwnerMappingResults findAll() {
        OwnerMappingResults results = new OwnerMappingResults();
        List<OwnerMapping> ownerMappings = ownerMappingDao.findAll(Sort.by("ownerName"));
        results.setItems(ownerMappings.stream().map(it -> JsonTools.clone(it, com.foilen.usagemetrics.api.model.OwnerMapping.class)).collect(Collectors.toList()));
        return results;
    }

}
