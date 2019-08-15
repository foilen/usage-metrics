/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.agent.services;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import com.foilen.smalltools.test.asserts.AssertTools;
import com.foilen.smalltools.tools.ResourceTools;
import com.foilen.usagemetrics.agent.services.model.UnixUserDetail;
import com.google.common.io.Files;

class UnixUsersAndGroupsUtilsImplTest {

    private UnixUsersAndGroupsUtilsImpl unixUsersAndGroupsUtils = new UnixUsersAndGroupsUtilsImpl();

    @Before
    public void init() throws Exception {
        unixUsersAndGroupsUtils = new UnixUsersAndGroupsUtilsImpl();

        // Passwd file
        File passwdFile = File.createTempFile("passwd", null);
        ResourceTools.copyToFile("UnixUsersAndGroupsUtilsImplTest-passwd.txt", this.getClass(), passwdFile);
        unixUsersAndGroupsUtils.setPasswdFile(passwdFile.getAbsolutePath());

        // Shadow file
        File shadowFile = File.createTempFile("shadow", null);
        ResourceTools.copyToFile("UnixUsersAndGroupsUtilsImplTest-shadow.txt", this.getClass(), shadowFile);
        unixUsersAndGroupsUtils.setShadowFile(shadowFile.getAbsolutePath());

        // Sudo folder
        File sudoFolderFile = Files.createTempDir();
        unixUsersAndGroupsUtils.setSudoDirectory(sudoFolderFile.getAbsolutePath());
        ResourceTools.copyToFile("UnixUsersAndGroupsUtilsImplTest-sudo-ccloud-1.txt", this.getClass(), new File(unixUsersAndGroupsUtils.getSudoDirectory() + "ccloud-1"));
    }

    @Test
    public void testGetAllUsers() {

        List<UnixUserDetail> unixUserDetails = unixUsersAndGroupsUtils.userGetAll();

        Assert.assertEquals(11, unixUserDetails.size());

        // Check all names
        int i = 0;
        for (int y = 0; y < 6; ++y) {
            Assert.assertEquals("reserved-" + y, unixUserDetails.get(i++).getName());
        }
        for (int y = 0; y < 4; ++y) {
            Assert.assertEquals("ccloud-" + y, unixUserDetails.get(i++).getName());
        }

        // Check details
        UnixUserDetail ccloud1 = unixUserDetails.get(7);
        AssertTools.assertJsonComparison("UnixUsersAndGroupsUtilsImplTest-testGetAllUsers-ccloud1.json", getClass(), ccloud1);

        // Check null hashed password
        UnixUserDetail ccloud2 = unixUserDetails.get(8);
        Assert.assertNull(ccloud2.getHashedPassword());
        UnixUserDetail ccloud3 = unixUserDetails.get(8);
        Assert.assertNull(ccloud3.getHashedPassword());

    }

}
