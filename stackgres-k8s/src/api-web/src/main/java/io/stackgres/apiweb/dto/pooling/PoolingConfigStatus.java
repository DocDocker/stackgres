/*
 * Copyright (C) 2019 OnGres, Inc.
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package io.stackgres.apiweb.dto.pooling;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import io.quarkus.runtime.annotations.RegisterForReflection;

@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@RegisterForReflection
public class PoolingConfigStatus {

  @JsonProperty("clusters")
  @NotNull(message = "clusters is required")
  private List<String> clusters;

  @JsonProperty("pgBouncer")
  @NotNull(message = "pgBouncer is required")
  @Valid
  private PoolingConfigPgBouncerStatus pgBouncer;

  public List<String> getClusters() {
    return clusters;
  }

  public void setClusters(List<String> clusters) {
    this.clusters = clusters;
  }

  public PoolingConfigPgBouncerStatus getPgBouncer() {
    return pgBouncer;
  }

  public void setPgBouncer(PoolingConfigPgBouncerStatus pgBouncer) {
    this.pgBouncer = pgBouncer;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .omitNullValues()
        .add("clusters", clusters)
        .add("pgBouncer", pgBouncer)
        .toString();
  }

}
