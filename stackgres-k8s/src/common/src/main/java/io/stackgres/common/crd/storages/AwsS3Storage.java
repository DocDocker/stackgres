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

@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@RegisterForReflection
public class AwsS3Storage implements PrefixedStorage {

  @JsonProperty("bucket")
  @NotNull(message = "The bucket is required")
  private String bucket;

  @JsonProperty("path")
  private String path;

  @JsonProperty("awsCredentials")
  @NotNull(message = "The credentials is required")
  @Valid
  private AwsCredentials awsCredentials;

  @JsonProperty("region")
  private String region;

  @JsonProperty("storageClass")
  private String storageClass;

  @Override
  public String getSchema() {
    return "s3";
  }

  @Override
  public String getBucket() {
    return bucket;
  }

  @Override
  public void setBucket(String bucket) {
    this.bucket = bucket;
  }

  @Override
  public String getPath() {
    return path;
  }

  @Override
  public void setPath(String path) {
    this.path = path;
  }

  public AwsCredentials getAwsCredentials() {
    return awsCredentials;
  }

  public void setAwsCredentials(AwsCredentials awsCredentials) {
    this.awsCredentials = awsCredentials;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public String getStorageClass() {
    return storageClass;
  }

  public void setStorageClass(String storageClass) {
    this.storageClass = storageClass;
  }

  @Override
  public int hashCode() {
    return Objects.hash(awsCredentials, bucket,
        region, storageClass);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof AwsS3Storage)) {
      return false;
    }
    AwsS3Storage other = (AwsS3Storage) obj;
    return Objects.equals(awsCredentials, other.awsCredentials)
        && Objects.equals(bucket, other.bucket) && Objects.equals(path, other.path)
        && Objects.equals(region, other.region)
        && Objects.equals(storageClass, other.storageClass);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .omitNullValues()
        .add("bucket", bucket)
        .add("path", path)
        .add("credentials", awsCredentials)
        .add("region", region)
        .add("storageClass", storageClass)
        .toString();
  }

}
