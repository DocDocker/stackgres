/*
 * Copyright (C) 2019 OnGres, Inc.
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package io.stackgres.common.crd.sgdbops;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.quarkus.runtime.annotations.RegisterForReflection;

@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@RegisterForReflection
public abstract class StackGresDbOpsVacuumConfig {

  @JsonProperty("full")
  protected Boolean full;

  @JsonProperty("freeze")
  protected Boolean freeze;

  @JsonProperty("analyze")
  protected Boolean analyze;

  @JsonProperty("disablePageSkipping")
  protected Boolean disablePageSkipping;

  public Boolean getFull() {
    return full;
  }

  public void setFull(Boolean full) {
    this.full = full;
  }

  public Boolean getFreeze() {
    return freeze;
  }

  public void setFreeze(Boolean freeze) {
    this.freeze = freeze;
  }

  public Boolean getAnalyze() {
    return analyze;
  }

  public void setAnalyze(Boolean analyze) {
    this.analyze = analyze;
  }

  public Boolean getDisablePageSkipping() {
    return disablePageSkipping;
  }

  public void setDisablePageSkipping(Boolean disablePageSkipping) {
    this.disablePageSkipping = disablePageSkipping;
  }

}
