/*
 * Copyright (C) 2019 OnGres, Inc.
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package io.stackgres.operatorframework.resource;

import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.stackgres.operatorframework.resource.factory.SubResourceStreamFactory;
import org.jooq.lambda.Seq;

public class ResourceGenerator<T extends KubernetesResource, C> {

  private final C context;
  private final Seq<KubernetesResource> seq;

  private ResourceGenerator(C context) {
    this.context = context;
    this.seq = Seq.empty();
  }

  private ResourceGenerator(C context, Seq<KubernetesResource> seq) {
    this.context = context;
    this.seq = seq;
  }

  public static <C> ResourceGenerator<KubernetesResource, C> with(C context) {
    return new ResourceGenerator<>(context);
  }

  public <H extends T> ResourceGenerator<H, C> of(Class<H> resourceClass) {
    return new ResourceGenerator<>(context);
  }

  public <H extends T, F extends SubResourceStreamFactory<H, ? super C>>
      ResourceGenerator<H, C> append(F resourceSeqFactory) {
    return new ResourceGenerator<>(
        context, seq.append(resourceSeqFactory.streamResources(context)));
  }

  @SuppressWarnings("unchecked")
  public Seq<T> stream() {
    return seq.map(resource -> (T) resource);
  }

}
