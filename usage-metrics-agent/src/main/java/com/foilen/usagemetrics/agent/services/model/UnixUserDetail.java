/*
    Usage Metrics
    https://github.com/foilen/usage-metrics
    Copyright (c) 2019-2020 Foilen (http://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.usagemetrics.agent.services.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.ComparisonChain;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UnixUserDetail implements Comparable<UnixUserDetail> {

    private Long id;
    private Long gid;
    private String name;
    private String gecos;
    private String homeFolder;
    private String shell;
    private String hashedPassword;
    private Set<String> sudos = new HashSet<>();

    public UnixUserDetail() {
    }

    public UnixUserDetail(Long id, Long gid, String name, String homeFolder, String shell) {
        this.id = id;
        this.gid = gid;
        this.name = name;
        this.homeFolder = homeFolder;
        this.shell = shell;
    }

    @Override
    public int compareTo(UnixUserDetail o) {
        return ComparisonChain.start() //
                .compare(id, o.id) //
                .compare(gid, o.gid) //
                .compare(name, o.name) //
                .result();
    }

    public String getGecos() {
        return gecos;
    }

    public Long getGid() {
        return gid;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getHomeFolder() {
        return homeFolder;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getShell() {
        return shell;
    }

    public Set<String> getSudos() {
        return sudos;
    }

    public void setGecos(String gecos) {
        this.gecos = gecos;
    }

    public void setGid(Long gid) {
        this.gid = gid;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void setHomeFolder(String homeFolder) {
        this.homeFolder = homeFolder;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShell(String shell) {
        this.shell = shell;
    }

    public void setSudos(Set<String> sudos) {
        this.sudos = sudos;
    }

    /**
     * Get the line in /etc/passwd file.
     *
     * @return the line
     */
    public String toPasswd() {
        return name + ":x:" + id + ":" + id + "::" + homeFolder + ":" + shell;
    }

    /**
     * Get the line in /etc/shadow file.
     *
     * @return the line
     */
    public String toShadow() {
        return name + ":" + hashedPassword + ":0:0:99999:7:::";
    }

    /**
     * Complete sudo file.
     *
     * @return all the lines
     */
    public String toSudoFile() {
        StringBuilder builder = new StringBuilder();
        for (String command : sudos) {
            builder.append(name);
            builder.append("  ALL = NOPASSWD: ");
            builder.append(command.replaceAll("\\:", "\\\\:"));
            builder.append("\n");
        }
        return builder.toString();
    }

}
