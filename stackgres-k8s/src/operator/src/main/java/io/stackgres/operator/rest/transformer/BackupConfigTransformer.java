/*
 * Copyright (C) 2019 OnGres, Inc.
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package io.stackgres.operator.rest.transformer;

import javax.enterprise.context.ApplicationScoped;

import io.stackgres.operator.customresource.sgbackupconfig.StackGresBackupConfig;
import io.stackgres.operator.customresource.sgbackupconfig.StackGresBackupConfigSpec;
import io.stackgres.operator.rest.dto.SecretKeySelector;
import io.stackgres.operator.rest.dto.backupconfig.BackupConfigDto;
import io.stackgres.operator.rest.dto.backupconfig.BackupConfigSpec;
import io.stackgres.operator.rest.dto.storages.AwsCredentials;
import io.stackgres.operator.rest.dto.storages.AwsS3Storage;
import io.stackgres.operator.rest.dto.storages.AzureBlobStorage;
import io.stackgres.operator.rest.dto.storages.AzureBlobStorageCredentials;
import io.stackgres.operator.rest.dto.storages.BackupStorage;
import io.stackgres.operator.rest.dto.storages.GoogleCloudCredentials;
import io.stackgres.operator.rest.dto.storages.GoogleCloudStorage;
import io.stackgres.operator.rest.dto.storages.PgpConfiguration;

@ApplicationScoped
public class BackupConfigTransformer
    extends AbstractResourceTransformer<BackupConfigDto, StackGresBackupConfig> {

  @Override
  public StackGresBackupConfig toCustomResource(BackupConfigDto source) {
    StackGresBackupConfig transformation = new StackGresBackupConfig();
    transformation.setMetadata(getCustomResourceMetadata(source));
    transformation.setSpec(getCustomResourceSpec(source.getSpec()));
    return transformation;
  }

  @Override
  public BackupConfigDto toResource(StackGresBackupConfig source) {
    BackupConfigDto transformation = new BackupConfigDto();
    transformation.setMetadata(getResourceMetadata(source));
    transformation.setSpec(getResourceSpec(source.getSpec()));
    return transformation;
  }

  public StackGresBackupConfigSpec getCustomResourceSpec(BackupConfigSpec source) {
    StackGresBackupConfigSpec transformation = new StackGresBackupConfigSpec();
    transformation.setCompressionMethod(source.getCompressionMethod());
    transformation.setDiskRateLimit(source.getDiskRateLimit());
    transformation.setFullSchedule(source.getFullSchedule());
    transformation.setFullWindow(source.getFullWindow());
    transformation.setNetworkRateLimit(source.getNetworkRateLimit());
    transformation.setPgpConfiguration(
        getCustomResourcePgpConfiguration(source.getPgpConfiguration()));
    transformation.setRetention(source.getRetention());
    transformation.setStorage(
        getCustomResourceStorage(source.getStorage()));
    transformation.setTarSizeThreshold(source.getTarSizeThreshold());
    transformation.setUploadDiskConcurrency(source.getUploadDiskConcurrency());
    return transformation;
  }

  private io.stackgres.operator.customresource.storages.PgpConfiguration
      getCustomResourcePgpConfiguration(PgpConfiguration source) {
    if (source == null) {
      return null;
    }
    io.stackgres.operator.customresource.storages.PgpConfiguration transformation =
        new io.stackgres.operator.customresource.storages.PgpConfiguration();
    transformation.setKey(new io.fabric8.kubernetes.api.model.SecretKeySelector(
        source.getKey().getName(),
        source.getKey().getKey(),
        false));
    return transformation;
  }

  private io.stackgres.operator.customresource.storages.BackupStorage
      getCustomResourceStorage(BackupStorage source) {
    if (source == null) {
      return null;
    }
    io.stackgres.operator.customresource.storages.BackupStorage transformation =
        new io.stackgres.operator.customresource.storages.BackupStorage();
    transformation.setAzureblob(
        getCustomResourceAzureblobStorage(source.getAzureblob()));
    transformation.setGcs(
        getCustomResourceGcsStorage(source.getGcs()));
    transformation.setS3(
        getCustomResourceS3Storage(source.getS3()));
    transformation.setType(source.getType());
    return transformation;
  }

  private io.stackgres.operator.customresource.storages.AzureBlobStorage
      getCustomResourceAzureblobStorage(AzureBlobStorage source) {
    if (source == null) {
      return null;
    }
    io.stackgres.operator.customresource.storages.AzureBlobStorage transformation =
        new io.stackgres.operator.customresource.storages.AzureBlobStorage();
    transformation.setBufferSize(source.getBufferSize());
    transformation.setCredentials(
        getCustomResourceAzureblobStorageCredentials(source.getCredentials()));
    transformation.setMaxBuffers(source.getMaxBuffers());
    transformation.setPrefix(source.getPrefix());
    return transformation;
  }

  private io.stackgres.operator.customresource.storages.AzureBlobStorageCredentials
      getCustomResourceAzureblobStorageCredentials(
      AzureBlobStorageCredentials source) {
    if (source == null) {
      return null;
    }
    io.stackgres.operator.customresource.storages.AzureBlobStorageCredentials
        transformation =
        new io.stackgres.operator.customresource.storages.AzureBlobStorageCredentials();
    transformation.setAccessKey(
        new io.fabric8.kubernetes.api.model.SecretKeySelector(
            source.getAccessKey().getName(),
            source.getAccessKey().getKey(),
            false));
    transformation.setAccount(
        new io.fabric8.kubernetes.api.model.SecretKeySelector(
            source.getAccount().getName(),
            source.getAccount().getKey(),
            false));
    return transformation;
  }

  private io.stackgres.operator.customresource.storages.GoogleCloudStorage
      getCustomResourceGcsStorage(GoogleCloudStorage source) {
    if (source == null) {
      return null;
    }
    io.stackgres.operator.customresource.storages.GoogleCloudStorage
        transformation =
        new io.stackgres.operator.customresource.storages.GoogleCloudStorage();
    transformation.setCredentials(
        getCustomResourceGcsStorageCredentials(source.getCredentials()));
    transformation.setPrefix(source.getPrefix());
    return transformation;
  }

  private io.stackgres.operator.customresource.storages.GoogleCloudCredentials
      getCustomResourceGcsStorageCredentials(GoogleCloudCredentials source) {
    if (source == null) {
      return null;
    }
    io.stackgres.operator.customresource.storages.GoogleCloudCredentials
        transformation =
        new io.stackgres.operator.customresource.storages.GoogleCloudCredentials();
    transformation.setServiceAccountJsonKey(
        new io.fabric8.kubernetes.api.model.SecretKeySelector(
            source.getServiceAccountJsonKey().getName(),
            source.getServiceAccountJsonKey().getKey(),
            false));
    return transformation;
  }

  private io.stackgres.operator.customresource.storages.AwsS3Storage
      getCustomResourceS3Storage(AwsS3Storage source) {
    if (source == null) {
      return null;
    }
    io.stackgres.operator.customresource.storages.AwsS3Storage transformation =
        new io.stackgres.operator.customresource.storages.AwsS3Storage();
    transformation.setCredentials(
        getCustomResourceAwsCredentials(source.getCredentials()));
    transformation.setCseKmsId(source.getCseKmsId());
    transformation.setCseKmsRegion(source.getCseKmsRegion());
    transformation.setEndpoint(source.getEndpoint());
    transformation.setForcePathStyle(source.isForcePathStyle());
    transformation.setPrefix(source.getPrefix());
    transformation.setRegion(source.getRegion());
    transformation.setSse(source.getSse());
    transformation.setSseKmsId(source.getSseKmsId());
    transformation.setStorageClass(source.getStorageClass());
    return transformation;
  }

  private io.stackgres.operator.customresource.storages.AwsCredentials
      getCustomResourceAwsCredentials(AwsCredentials source) {
    if (source == null) {
      return null;
    }
    io.stackgres.operator.customresource.storages.AwsCredentials
        transformation =
        new io.stackgres.operator.customresource.storages.AwsCredentials();
    transformation.setAccessKey(
        new io.fabric8.kubernetes.api.model.SecretKeySelector(
            source.getAccessKey().getName(),
            source.getAccessKey().getKey(),
            false));
    transformation.setSecretKey(
        new io.fabric8.kubernetes.api.model.SecretKeySelector(
            source.getSecretKey().getName(),
            source.getSecretKey().getKey(),
            false));
    return transformation;
  }

  public BackupConfigSpec getResourceSpec(StackGresBackupConfigSpec source) {
    BackupConfigSpec transformation = new BackupConfigSpec();
    transformation.setCompressionMethod(source.getCompressionMethod());
    transformation.setDiskRateLimit(source.getDiskRateLimit());
    transformation.setFullSchedule(source.getFullSchedule());
    transformation.setFullWindow(source.getFullWindow());
    transformation.setNetworkRateLimit(source.getNetworkRateLimit());
    transformation.setPgpConfiguration(
        getResourcePgpConfiguration(source.getPgpConfiguration()));
    transformation.setRetention(source.getRetention());
    transformation.setStorage(
        getResourceStorage(source.getStorage()));
    transformation.setTarSizeThreshold(source.getTarSizeThreshold());
    transformation.setUploadDiskConcurrency(source.getUploadDiskConcurrency());
    return transformation;
  }

  private PgpConfiguration getResourcePgpConfiguration(
      io.stackgres.operator.customresource.storages.PgpConfiguration
      source) {
    if (source == null) {
      return null;
    }
    PgpConfiguration transformation = new PgpConfiguration();
    transformation.setKey(SecretKeySelector.create(
        source.getKey().getName(),
        source.getKey().getKey()));
    return transformation;
  }

  private BackupStorage getResourceStorage(
      io.stackgres.operator.customresource.storages.BackupStorage source) {
    if (source == null) {
      return null;
    }
    BackupStorage transformation = new BackupStorage();
    transformation.setAzureblob(
        getResourceAzureblobStorage(source.getAzureblob()));
    transformation.setGcs(
        getResourceGcsStorage(source.getGcs()));
    transformation.setS3(
        getResourceS3Storage(source.getS3()));
    transformation.setType(source.getType());
    return transformation;
  }

  private AzureBlobStorage getResourceAzureblobStorage(
      io.stackgres.operator.customresource.storages.AzureBlobStorage source) {
    if (source == null) {
      return null;
    }
    AzureBlobStorage transformation = new AzureBlobStorage();
    transformation.setBufferSize(source.getBufferSize());
    transformation.setCredentials(
        getResourceAzureblobStorageCredentials(source.getCredentials()));
    transformation.setMaxBuffers(source.getMaxBuffers());
    transformation.setPrefix(source.getPrefix());
    return transformation;
  }

  private AzureBlobStorageCredentials getResourceAzureblobStorageCredentials(
      io.stackgres.operator.customresource.storages.AzureBlobStorageCredentials source) {
    if (source == null) {
      return null;
    }
    AzureBlobStorageCredentials transformation =
        new AzureBlobStorageCredentials();
    transformation.setAccessKey(
        SecretKeySelector.create(
            source.getAccessKey().getName(),
            source.getAccessKey().getKey()));
    transformation.setAccount(
        SecretKeySelector.create(
            source.getAccount().getName(),
            source.getAccount().getKey()));
    return transformation;
  }

  private GoogleCloudStorage getResourceGcsStorage(
      io.stackgres.operator.customresource.storages.GoogleCloudStorage source) {
    if (source == null) {
      return null;
    }
    GoogleCloudStorage transformation = new GoogleCloudStorage();
    transformation.setCredentials(
        getResourceGcsStorageCredentials(source.getCredentials()));
    transformation.setPrefix(source.getPrefix());
    return transformation;
  }

  private GoogleCloudCredentials getResourceGcsStorageCredentials(
      io.stackgres.operator.customresource.storages.GoogleCloudCredentials source) {
    if (source == null) {
      return null;
    }
    GoogleCloudCredentials transformation =
        new GoogleCloudCredentials();
    transformation.setServiceAccountJsonKey(
        SecretKeySelector.create(
            source.getServiceAccountJsonKey().getName(),
            source.getServiceAccountJsonKey().getKey()));
    return transformation;
  }

  private AwsS3Storage getResourceS3Storage(
      io.stackgres.operator.customresource.storages.AwsS3Storage source) {
    if (source == null) {
      return null;
    }
    AwsS3Storage transformation = new AwsS3Storage();
    transformation.setCredentials(
        getResourceAwsCredentials(source.getCredentials()));
    transformation.setCseKmsId(source.getCseKmsId());
    transformation.setCseKmsRegion(source.getCseKmsRegion());
    transformation.setEndpoint(source.getEndpoint());
    transformation.setForcePathStyle(source.isForcePathStyle());
    transformation.setPrefix(source.getPrefix());
    transformation.setRegion(source.getRegion());
    transformation.setSse(source.getSse());
    transformation.setSseKmsId(source.getSseKmsId());
    transformation.setStorageClass(source.getStorageClass());
    return transformation;
  }

  private AwsCredentials getResourceAwsCredentials(
      io.stackgres.operator.customresource.storages.AwsCredentials source) {
    if (source == null) {
      return null;
    }
    AwsCredentials transformation = new AwsCredentials();
    transformation.setAccessKey(
        SecretKeySelector.create(
            source.getAccessKey().getName(),
            source.getAccessKey().getKey()));
    transformation.setSecretKey(
        SecretKeySelector.create(
            source.getSecretKey().getName(),
            source.getSecretKey().getKey()));
    return transformation;
  }

}