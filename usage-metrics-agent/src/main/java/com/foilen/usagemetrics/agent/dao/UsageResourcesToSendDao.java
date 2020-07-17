/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.agent.dao;

import java.io.File;

import com.foilen.smalltools.db.AbstractSingleJsonFileDao;
import com.foilen.usagemetrics.agent.dao.model.UsageResourcesList;

public class UsageResourcesToSendDao extends AbstractSingleJsonFileDao<UsageResourcesList> {

    private File dbFile;
    private File stagingFile;

    public UsageResourcesToSendDao(File dbFile) {
        this.dbFile = dbFile;
        this.stagingFile = new File(dbFile.getAbsolutePath() + "_tmp");
    }

    @Override
    protected File getFinalFile() {
        return dbFile;
    }

    @Override
    protected File getStagingFile() {
        return stagingFile;
    }

    @Override
    protected Class<UsageResourcesList> getType() {
        return UsageResourcesList.class;
    }

}