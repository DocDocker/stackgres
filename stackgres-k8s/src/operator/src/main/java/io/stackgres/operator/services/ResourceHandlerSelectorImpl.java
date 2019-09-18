/*
 * Copyright (C) 2019 OnGres, Inc.
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package io.stackgres.operator.services;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.stackgres.operator.common.KindLiteral;
import io.stackgres.operator.common.StackGresClusterConfig;

@ApplicationScoped
public class ResourceHandlerSelectorImpl implements ResourceHandlerSelector {

  private Instance<ResourceHandler> handlers;

  @Inject
  public ResourceHandlerSelectorImpl(@Any Instance<ResourceHandler> creators) {
    this.handlers = creators;
  }

  @Override
  public boolean equals(StackGresClusterConfig config, HasMetadata existingResource,
      HasMetadata requiredResource) {
    return selectResourceHandler(config, requiredResource)
        .equals(existingResource, requiredResource);
  }

  @Override
  public HasMetadata update(StackGresClusterConfig config, HasMetadata existingResource,
      HasMetadata requiredResource) {
    return selectResourceHandler(config, requiredResource)
        .update(existingResource, requiredResource);
  }

  @Override
  public boolean isManaged(StackGresClusterConfig config, HasMetadata existingResource) {
    return selectResourceHandler(config, existingResource).isManaged(config, existingResource);
  }

  @Override
  public HasMetadata create(KubernetesClient client, StackGresClusterConfig config,
      HasMetadata resource) {
    return selectResourceHandler(config, resource).create(client, resource);
  }

  @Override
  public HasMetadata patch(KubernetesClient client, StackGresClusterConfig config,
      HasMetadata resource) {
    return selectResourceHandler(config, resource).patch(client, resource);
  }

  @Override
  public boolean delete(KubernetesClient client, StackGresClusterConfig config,
      HasMetadata resource) {
    return selectResourceHandler(config, resource).delete(client, resource);
  }

  private ResourceHandler selectResourceHandler(StackGresClusterConfig config,
      HasMetadata resource) {
    Optional<ResourceHandler> customHandler = handlers.stream()
        .filter(handler -> handler.handleResource(config, resource)).findAny();

    if (customHandler.isPresent()) {
      return customHandler.get();
    }

    Instance<ResourceHandler> kindHandler = handlers
        .select(new KindLiteral(resource.getKind()));

    if (kindHandler.isResolvable()) {
      return kindHandler.get();
    }

    return handlers.select(DefaultResourceHandler.class).get();
  }

}
