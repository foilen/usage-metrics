/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.agent.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.smalltools.tools.CollectionsTools;
import com.foilen.smalltools.tools.DirectoryTools;
import com.foilen.smalltools.tools.FileTools;
import com.foilen.usagemetrics.agent.services.model.UnixUserDetail;
import com.foilen.usagemetrics.common.UsageMetricException;

public class UnixUsersAndGroupsUtilsImpl extends AbstractBasics implements UnixUsersAndGroupsUtils {

    private String hostFs = "/";

    private String passwdFile = "/etc/passwd";
    private String shadowFile = "/etc/shadow";
    private String rootDirectory = "/";
    private String etcDirectory = "/etc/";
    private String sudoDirectory = "/etc/sudoers.d/";

    public UnixUsersAndGroupsUtilsImpl() {
    }

    public UnixUsersAndGroupsUtilsImpl(String hostFs) {
        this.hostFs = hostFs;
    }

    public String getEtcDirectory() {
        return etcDirectory;
    }

    public String getPasswdFile() {
        return passwdFile;
    }

    public String getRootDirectory() {
        return rootDirectory;
    }

    public String getShadowFile() {
        return shadowFile;
    }

    public String getSudoDirectory() {
        return sudoDirectory;
    }

    private void parseUserPasswd(String[] parts, UnixUserDetail unixUserDetail) {
        int i = 0;
        unixUserDetail.setName(parts[i++]);
        ++i;
        unixUserDetail.setId(Long.valueOf(parts[i++]));
        unixUserDetail.setGid(Long.valueOf(parts[i++]));
        unixUserDetail.setGecos(parts[i++]);
        unixUserDetail.setHomeFolder(parts[i++]);
        if (parts.length >= 7) {
            unixUserDetail.setShell(parts[i++]);
        }
    }

    public UnixUsersAndGroupsUtilsImpl setEtcDirectory(String etcDirectory) {
        this.etcDirectory = DirectoryTools.pathTrailingSlash(etcDirectory);
        return this;
    }

    public UnixUsersAndGroupsUtilsImpl setPasswdFile(String passwdFile) {
        this.passwdFile = passwdFile;
        return this;
    }

    public UnixUsersAndGroupsUtilsImpl setRootDirectory(String rootDirectory) {
        this.rootDirectory = DirectoryTools.pathTrailingSlash(rootDirectory);
        return this;
    }

    public UnixUsersAndGroupsUtilsImpl setShadowFile(String shadowFile) {
        this.shadowFile = shadowFile;
        return this;
    }

    public UnixUsersAndGroupsUtilsImpl setSudoDirectory(String sudoDirectory) {
        this.sudoDirectory = DirectoryTools.pathTrailingSlash(sudoDirectory);
        return this;
    }

    @Override
    public List<UnixUserDetail> userGetAll() {

        Map<String, UnixUserDetail> detailsByUsername = new HashMap<>();

        // Read the passwd file
        for (String line : FileTools.readFileLinesIteration(hostFs + passwdFile)) {
            String parts[] = line.split(":");
            if (parts.length < 6) {
                throw new UsageMetricException("[USER PASSWD GET] The entry [" + line + "] is invalid in the passwd file");
            }
            UnixUserDetail unixUserDetails = CollectionsTools.getOrCreateEmpty(detailsByUsername, parts[0], UnixUserDetail.class);
            parseUserPasswd(parts, unixUserDetails);
        }

        return detailsByUsername.values().stream() //
                .filter(it -> it.getId() != null) // Must have an ID
                .sorted((a, b) -> Long.compare(a.getId(), b.getId())) // Sort by ID
                .collect(Collectors.toList());
    }

}
