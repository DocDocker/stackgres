/*
 * Copyright (C) 2019 OnGres, Inc.
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package io.stackgres.common.crd.storages;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.stackgres.common.crd.SecretKeySelector;

@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@RegisterForReflection
public class AzureBlobSecretKeySelector {

  @JsonProperty("storageAccount")
  @NotNull(message = "The account is required")
  private SecretKeySelector account;

  @JsonProperty("accessKey")
  @NotNull(message = "The accessKey is required")
  @Valid
  private SecretKeySelector accessKey;

  public SecretKeySelector getAccount() {
    return account;
  }

  public void setAccount(SecretKeySelector account) {
    this.account = account;
  }

  public SecretKeySelector getAccessKey() {
    return accessKey;
  }

  public void setAccessKey(SecretKeySelector accessKey) {
    this.accessKey = accessKey;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AzureBlobSecretKeySelector that = (AzureBlobSecretKeySelector) o;
    return Objects.equals(account, that.account)
        && Objects.equals(accessKey, that.accessKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(account, accessKey);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("account", account)
        .add("accessKey", accessKey)
        .toString();
  }
}
