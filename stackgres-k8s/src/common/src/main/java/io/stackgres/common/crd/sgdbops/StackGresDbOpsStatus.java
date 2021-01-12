/*
 * Copyright (C) 2019 OnGres, Inc.
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package io.stackgres.common.crd.sgdbops;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.stackgres.common.StackGresUtil;
import io.stackgres.common.validation.FieldReference;
import io.stackgres.common.validation.FieldReference.ReferencedField;

@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@RegisterForReflection
public class StackGresDbOpsStatus implements KubernetesResource {

  private static final long serialVersionUID = 1L;

  @JsonProperty("conditions")
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  @Valid
  private List<StackGresDbOpsCondition> conditions = new ArrayList<>();

  @JsonProperty("opRetries")
  private Integer opRetries;

  @JsonProperty("opStarted")
  private String opStarted;

  @JsonProperty("benchmark")
  private StackGresDbOpsBenchmarkStatus benchmark;

  @JsonProperty("majorVersionUpgrade")
  private StackGresDbOpsMajorVersionUpgradeStatus majorVersionUpgrade;

  @ReferencedField("opStarted")
  interface OpStarted extends FieldReference { }

  @JsonIgnore
  @AssertTrue(message = "opStarted must be in ISO 8601 date format: `YYYY-MM-DDThh:mm:ss.ddZ`.",
      payload = OpStarted.class)
  public boolean isOpStartedValid() {
    try {
      if (opStarted != null) {
        Instant.parse(opStarted);
      }
      return true;
    } catch (DateTimeParseException ex) {
      return false;
    }
  }

  public List<StackGresDbOpsCondition> getConditions() {
    return conditions;
  }

  public void setConditions(List<StackGresDbOpsCondition> conditions) {
    this.conditions = conditions;
  }

  public Integer getOpRetries() {
    return opRetries;
  }

  public void setOpRetries(Integer opRetries) {
    this.opRetries = opRetries;
  }

  public String getOpStarted() {
    return opStarted;
  }

  public void setOpStarted(String opStarted) {
    this.opStarted = opStarted;
  }

  public StackGresDbOpsBenchmarkStatus getBenchmark() {
    return benchmark;
  }

  public void setBenchmark(StackGresDbOpsBenchmarkStatus benchmark) {
    this.benchmark = benchmark;
  }

  public StackGresDbOpsMajorVersionUpgradeStatus getMajorVersionUpgrade() {
    return majorVersionUpgrade;
  }

  public void setMajorVersionUpgrade(StackGresDbOpsMajorVersionUpgradeStatus majorVersionUpgrade) {
    this.majorVersionUpgrade = majorVersionUpgrade;
  }

  @Override
  public int hashCode() {
    return Objects.hash(benchmark, conditions, majorVersionUpgrade, opRetries, opStarted);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof StackGresDbOpsStatus)) {
      return false;
    }
    StackGresDbOpsStatus other = (StackGresDbOpsStatus) obj;
    return Objects.equals(benchmark, other.benchmark)
        && Objects.equals(conditions, other.conditions)
        && Objects.equals(majorVersionUpgrade, other.majorVersionUpgrade)
        && Objects.equals(opRetries, other.opRetries) && Objects.equals(opStarted, other.opStarted);
  }

  @Override
  public String toString() {
    return StackGresUtil.toPrettyYaml(this);
  }

}