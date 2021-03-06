/*
 * Copyright (C) 2019 OnGres, Inc.
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package io.stackgres.common.crd.storages;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.quarkus.runtime.annotations.RegisterForReflection;

@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@RegisterForReflection
public class GoogleCloudCredentials {

  @JsonProperty("fetchCredentialsFromMetadataService")
  private boolean fetchCredentialsFromMetadataService;

  @JsonProperty("secretKeySelectors")
  @Valid
  private GoogleCloudSecretKeySelector secretKeySelectors;

  @JsonIgnore
  @AssertTrue(message = "The secretKeySelectors is required if fetchCredentialsFromMetadataService"
      + " is false")
  public boolean isSecretKeySelectorsSetIfFetchCredentialsFromMetadataServiceiSFalse() {
    return secretKeySelectors != null || fetchCredentialsFromMetadataService;
  }

  public boolean isFetchCredentialsFromMetadataService() {
    return fetchCredentialsFromMetadataService;
  }

  public void setFetchCredentialsFromMetadataService(boolean fetchCredentialsFromMetadataService) {
    this.fetchCredentialsFromMetadataService = fetchCredentialsFromMetadataService;
  }

  public GoogleCloudSecretKeySelector getSecretKeySelectors() {
    return secretKeySelectors;
  }

  public void setSecretKeySelectors(GoogleCloudSecretKeySelector secretKeySelectors) {
    this.secretKeySelectors = secretKeySelectors;
  }

  @Override
  public int hashCode() {
    return Objects.hash(fetchCredentialsFromMetadataService, secretKeySelectors);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof GoogleCloudCredentials)) {
      return false;
    }
    GoogleCloudCredentials other = (GoogleCloudCredentials) obj;
    return fetchCredentialsFromMetadataService == other.fetchCredentialsFromMetadataService
        && Objects.equals(secretKeySelectors, other.secretKeySelectors);
  }
}
