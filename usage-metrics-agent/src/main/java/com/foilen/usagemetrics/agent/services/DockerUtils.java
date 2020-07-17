/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.agent.services;

import java.util.List;

import com.foilen.usagemetrics.agent.services.model.DockerPs;

public interface DockerUtils {

    List<DockerPs> containerPsFindAll();

}
