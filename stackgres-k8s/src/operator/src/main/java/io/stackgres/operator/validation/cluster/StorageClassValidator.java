/*
 * Copyright (C) 2019 OnGres, Inc.
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package io.stackgres.operator.validation.cluster;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.fabric8.kubernetes.api.model.storage.StorageClass;
import io.stackgres.common.ErrorType;
import io.stackgres.common.crd.sgcluster.StackGresCluster;
import io.stackgres.common.resource.ResourceFinder;
import io.stackgres.operator.common.StackGresClusterReview;
import io.stackgres.operator.validation.ValidationType;
import io.stackgres.operatorframework.admissionwebhook.validating.ValidationFailed;

@Singleton
@ValidationType(ErrorType.INVALID_STORAGE_CLASS)
public class StorageClassValidator implements ClusterValidator {

  private final ResourceFinder<StorageClass> finder;

  @Inject
  public StorageClassValidator(ResourceFinder<StorageClass> finder) {
    this.finder = finder;
  }

  @Override
  public void validate(StackGresClusterReview review) throws ValidationFailed {

    StackGresCluster cluster = review.getRequest().getObject();
    String storageClass = cluster.getSpec().getPod().getPersistentVolume().getStorageClass();

    switch (review.getRequest().getOperation()) {
      case CREATE:
        checkIfStorageClassExist(storageClass, "Storage class "
            + storageClass + " not found");
        break;
      case UPDATE:
        checkIfStorageClassExist(storageClass, "Cannot update to storage class "
            + storageClass + " because it doesn't exists");
        break;
      default:
    }

  }

  private void checkIfStorageClassExist(String storageClass, String onError)
      throws ValidationFailed {
    if (storageClass != null && !storageClass.isEmpty()
        && !finder.findByName(storageClass).isPresent()) {
      fail(onError);
    }
  }
}
