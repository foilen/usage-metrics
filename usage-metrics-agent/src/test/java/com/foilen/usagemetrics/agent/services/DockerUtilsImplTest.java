/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.agent.services;

import java.util.List;

import org.junit.Test;

import com.foilen.smalltools.test.asserts.AssertTools;
import com.foilen.smalltools.tools.JsonTools;
import com.foilen.smalltools.tools.ResourceTools;
import com.foilen.usagemetrics.agent.services.model.DockerPs;

public class DockerUtilsImplTest {

    private void assertDockerPs(String expectedList, String actualOutput) {
        List<DockerPs> actual = new DockerUtilsImpl().convertToDockerPs(ResourceTools.getResourceAsString(actualOutput, this.getClass()));
        List<DockerPs> expected = JsonTools.readFromResourceAsList(expectedList, DockerPs.class, this.getClass());

        AssertTools.assertJsonComparison(expected, actual);
    }

    @Test
    public void testConvertToDockerPs() {
        assertDockerPs("DockerUtilsImplTest-testConvertToDockerPs-nothing-expected.json", "DockerUtilsImplTest-testConvertToDockerPs-nothing.txt");
        assertDockerPs("DockerUtilsImplTest-testConvertToDockerPs-some-expected.json", "DockerUtilsImplTest-testConvertToDockerPs-some.txt");
        assertDockerPs("DockerUtilsImplTest-testConvertToDockerPs-incompleteStream-expected.json", "DockerUtilsImplTest-testConvertToDockerPs-incompleteStream.txt");
        assertDockerPs("DockerUtilsImplTest-testConvertToDockerPs-sizeWithExponent-expected.json", "DockerUtilsImplTest-testConvertToDockerPs-sizeWithExponent.txt");
    }

}
