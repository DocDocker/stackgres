/*
 * Copyright (C) 2019 OnGres, Inc.
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package io.stackgres.apiweb.rest;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.common.collect.ImmutableList;
import io.fabric8.kubernetes.api.model.storage.StorageClass;
import io.quarkus.security.Authenticated;
import io.stackgres.common.resource.ResourceScanner;

@Path("/stackgres/storageclass")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StorageClassResource {

  private ResourceScanner<StorageClass> storageClassScanner;

  @GET
  @Authenticated
  public List<String> get() {
    return storageClassScanner.findResources().stream()
        .map(sc -> sc.getMetadata().getName())
        .collect(ImmutableList.toImmutableList());
  }

  @Inject
  public void setStorageClassScanner(ResourceScanner<StorageClass> storageClassScanner) {
    this.storageClassScanner = storageClassScanner;
  }
}
