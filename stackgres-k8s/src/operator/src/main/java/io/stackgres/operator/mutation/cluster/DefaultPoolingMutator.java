/*
 * Copyright (C) 2019 OnGres, Inc.
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package io.stackgres.operator.mutation.cluster;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jsonpatch.JsonPatchOperation;
import com.google.common.collect.ImmutableList;
import io.stackgres.common.CdiUtil;
import io.stackgres.common.crd.sgcluster.StackGresCluster;
import io.stackgres.common.crd.sgpooling.StackGresPoolingConfig;
import io.stackgres.common.resource.CustomResourceFinder;
import io.stackgres.common.resource.CustomResourceScheduler;
import io.stackgres.operator.common.StackGresClusterReview;
import io.stackgres.operator.initialization.DefaultCustomResourceFactory;
import io.stackgres.operatorframework.admissionwebhook.Operation;

@ApplicationScoped
public class DefaultPoolingMutator
    extends AbstractDefaultResourceMutator<StackGresPoolingConfig> {

  @Inject
  public DefaultPoolingMutator(
      DefaultCustomResourceFactory<StackGresPoolingConfig> resourceFactory,
      CustomResourceFinder<StackGresPoolingConfig> finder,
      CustomResourceScheduler<StackGresPoolingConfig> scheduler) {
    super(resourceFactory, finder, scheduler);
  }

  public DefaultPoolingMutator() {
    super(null, null, null);
    CdiUtil.checkPublicNoArgsConstructorIsCalledToCreateProxy();
  }

  @Override
  public List<JsonPatchOperation> mutate(StackGresClusterReview review) {

    if (review.getRequest().getOperation() == Operation.CREATE) {

      ImmutableList.Builder<JsonPatchOperation> operations = ImmutableList.builder();
      operations.addAll(ClusterConfigurationMutator.ensureConfigurationNode(review));
      operations.addAll(super.mutate(review));
      return operations.build();
    }

    return ImmutableList.of();
  }

  @Override
  protected String getTargetPropertyValue(StackGresCluster targetCluster) {
    return targetCluster.getSpec().getConfiguration().getConnectionPoolingConfig();
  }

  @Override
  protected JsonPointer getTargetPointer() throws NoSuchFieldException {
    return getTargetPointer("connectionPoolingConfig");
  }

  @Override
  public JsonPointer getTargetPointer(String field) throws NoSuchFieldException {
    return ClusterConfigurationMutator.getTargetPointer(field);
  }
}
