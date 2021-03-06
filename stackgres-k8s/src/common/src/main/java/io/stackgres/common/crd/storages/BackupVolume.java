/*
 * Copyright (C) 2019 OnGres, Inc.
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package io.stackgres.common.crd.storages;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import io.quarkus.runtime.annotations.RegisterForReflection;

@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@RegisterForReflection
public class BackupVolume {

  @JsonProperty("size")
  @NotNull(message = "The volume size is required")
  private String size;

  @JsonProperty("writeManyStorageClass")
  private String writeManyStorageClass;

  public String getSize() {
    return size;
  }

  public void setSize(String size) {
    this.size = size;
  }

  public String getWriteManyStorageClass() {
    return writeManyStorageClass;
  }

  public void setWriteManyStorageClass(String writeManyStorageClass) {
    this.writeManyStorageClass = writeManyStorageClass;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .omitNullValues()
        .add("size", size)
        .add("writeManyStorageClass", writeManyStorageClass)
        .toString();
  }

}
