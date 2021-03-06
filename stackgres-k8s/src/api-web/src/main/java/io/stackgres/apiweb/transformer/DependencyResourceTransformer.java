/*
 * Copyright (C) 2019 OnGres, Inc.
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package io.stackgres.apiweb.transformer;

import java.util.List;

import io.fabric8.kubernetes.client.CustomResource;
import io.stackgres.apiweb.dto.ResourceDto;
import org.jetbrains.annotations.Nullable;

public interface DependencyResourceTransformer<T extends ResourceDto, R extends CustomResource> {

  R toCustomResource(T resource, @Nullable R originalResource);

  T toResource(R customResource, List<String> clusters);

}
