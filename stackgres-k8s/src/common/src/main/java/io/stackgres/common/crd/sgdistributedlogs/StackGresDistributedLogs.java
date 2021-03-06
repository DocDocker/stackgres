/*
 * Copyright (C) 2019 OnGres, Inc.
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package io.stackgres.common.crd.sgdistributedlogs;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import io.fabric8.kubernetes.client.CustomResource;
import io.quarkus.runtime.annotations.RegisterForReflection;

@JsonInclude(JsonInclude.Include.NON_NULL)
@RegisterForReflection
public class StackGresDistributedLogs extends CustomResource {

  private static final long serialVersionUID = 1L;

  @JsonProperty("spec")
  @NotNull(message = "The specification is required")
  @Valid
  private StackGresDistributedLogsSpec spec;

  @JsonProperty("status")
  @Valid
  private StackGresDistributedLogsStatus status;

  public StackGresDistributedLogs() {
    super(StackGresDistributedLogsDefinition.KIND);
  }

  public StackGresDistributedLogsSpec getSpec() {
    return spec;
  }

  public void setSpec(StackGresDistributedLogsSpec spec) {
    this.spec = spec;
  }

  public StackGresDistributedLogsStatus getStatus() {
    return status;
  }

  public void setStatus(StackGresDistributedLogsStatus status) {
    this.status = status;
  }

  @Override
  public int hashCode() {
    return Objects.hash(spec, status);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof StackGresDistributedLogs)) {
      return false;
    }
    StackGresDistributedLogs other = (StackGresDistributedLogs) obj;
    return Objects.equals(spec, other.spec) && Objects.equals(status, other.status);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .omitNullValues()
        .add("apiVersion", getApiVersion())
        .add("kind", getKind())
        .add("metadata", getMetadata())
        .add("spec", spec)
        .add("status", status)
        .toString();
  }

}
