/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.agent.grabber;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mariadb.jdbc.MariaDbDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.smalltools.tools.ResourceTools;
import com.foilen.smalltools.tuple.Tuple2;
import com.foilen.usagemetrics.agent.DatabaseInfo;
import com.foilen.usagemetrics.api.model.UsageResource;
import com.foilen.usagemetrics.common.UsageMetricException;

public class JamesGrabber extends AbstractBasics implements Grabber {

    private static final String USER_AND_SIZE_SQL = ResourceTools.getResourceAsString("JamesGrabber-user_and_size.sql", JamesGrabber.class);
    private static final String EMAILS_COUNT_SQL = ResourceTools.getResourceAsString("JamesGrabber-emails_count.sql", JamesGrabber.class);

    private static final String RESOURCE_TYPE = "james";

    private JdbcTemplate jdbcTemplate;

    private long lastCount;

    public JamesGrabber(DatabaseInfo databaseInfo) {
        MariaDbDataSource dataSource = new MariaDbDataSource(databaseInfo.getHost(), databaseInfo.getPort(), databaseInfo.getDbName());
        try {
            dataSource.setUserName(databaseInfo.getDbUser());
            dataSource.setPassword(databaseInfo.getDbPassword());
        } catch (SQLException e) {
            throw new UsageMetricException("Could not connect to James database", e);
        }
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<UsageResource> grab() {

        lastCount = jdbcTemplate.queryForObject(EMAILS_COUNT_SQL, Long.class);

        List<UsageResource> usageResources = new ArrayList<>();

        List<Tuple2<String, Long>> userAndSizes = jdbcTemplate.query(USER_AND_SIZE_SQL, (rs, rowNum) -> new Tuple2<>(rs.getString(1), rs.getLong(2)));
        for (Tuple2<String, Long> userAndSize : userAndSizes) {

            String user = userAndSize.getA();
            String owner = user.split("@")[1];
            Long size = userAndSize.getB();

            logger.debug("Got user: {} ; for owner: {} ; with size {}", user, owner, size);

            UsageResource usageResource = new UsageResource() //
                    .setUsageResourceType(RESOURCE_TYPE) //
                    .setOwner(owner) //
                    .setDetails(user) //
                    .setSize(size);
            usageResources.add(usageResource);
        }
        return usageResources;
    }

    @Override
    public boolean shouldRun() {
        long currentCount = jdbcTemplate.queryForObject(EMAILS_COUNT_SQL, Long.class);
        return Math.abs(currentCount - lastCount) >= 1000;
    }

}
