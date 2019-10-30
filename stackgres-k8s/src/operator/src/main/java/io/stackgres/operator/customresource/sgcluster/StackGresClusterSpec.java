/*
 * Copyright (C) 2019 OnGres, Inc.
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package io.stackgres.operator.customresource.sgcluster;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;

import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.quarkus.runtime.annotations.RegisterForReflection;

@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@RegisterForReflection
public class StackGresClusterSpec implements KubernetesResource {

  private static final long serialVersionUID = -5276087851826599719L;

  @JsonProperty("instances")
  @Min(1)
  private int instances;

  @JsonProperty("pg_version")
  private String postgresVersion;

  @JsonProperty("pg_config")
  @NotNull
  private String postgresConfig;

  @JsonProperty("postgres_exporter_version")
  @NotNull
  private String postgresExporterVersion;

  @JsonProperty("resource_profile")
  @NotNull
  private String resourceProfile;

  @JsonProperty("connection_pooling_config")
  @NotNull
  private String connectionPoolingConfig;

  @JsonProperty("volume_size")
  @NotNull
  private String volumeSize;

  @JsonProperty("storage_class")
  @NotNull
  private String storageClass;

  @JsonProperty("prometheus_autobind")
  private Boolean prometheusAutobind;

  @JsonProperty("sidecars")
  private List<String> sidecars;

  public int getInstances() {
    return instances;
  }

  public void setInstances(int instances) {
    this.instances = instances;
  }

  public String getPostgresVersion() {
    return postgresVersion;
  }

  public void setPostgresVersion(String postgresVersion) {
    this.postgresVersion = postgresVersion;
  }

  public String getPostgresConfig() {
    return postgresConfig;
  }

  public void setPostgresConfig(String postgresConfig) {
    this.postgresConfig = postgresConfig;
  }

  public String getPostgresExporterVersion() {
    return postgresExporterVersion;
  }

  public void setPostgresExporterVersion(String postgresExporterVersion) {
    this.postgresExporterVersion = postgresExporterVersion;
  }

  public String getResourceProfile() {
    return resourceProfile;
  }

  public void setResourceProfile(String resourceProfile) {
    this.resourceProfile = resourceProfile;
  }

  public String getConnectionPoolingConfig() {
    return connectionPoolingConfig;
  }

  public void setConnectionPoolingConfig(String connectionPoolingConfig) {
    this.connectionPoolingConfig = connectionPoolingConfig;
  }

  public String getVolumeSize() {
    return volumeSize;
  }

  public void setVolumeSize(String volumeSize) {
    this.volumeSize = volumeSize;
  }

  public String getStorageClass() {
    return storageClass;
  }

  public void setStorageClass(String storageClass) {
    this.storageClass = storageClass;
  }

  public Boolean getPrometheusAutobind() {
    return prometheusAutobind;
  }

  public void setPrometheusAutobind(Boolean prometheusAutobind) {
    this.prometheusAutobind = prometheusAutobind;
  }

  public List<String> getSidecars() {
    return sidecars;
  }

  public void setSidecars(List<String> sidecars) {
    this.sidecars = sidecars;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .omitNullValues()
        .add("instances", instances)
        .add("pg_version", postgresVersion)
        .add("pg_config", postgresConfig)
        .add("resource_profile", resourceProfile)
        .add("connection_pooling_config", connectionPoolingConfig)
        .add("volume_size", volumeSize)
        .add("storage_class", storageClass)
        .add("sidecars", sidecars)
        .toString();
  }

}