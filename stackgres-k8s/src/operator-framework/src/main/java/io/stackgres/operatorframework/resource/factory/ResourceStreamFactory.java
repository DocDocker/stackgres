/*
 * Copyright (C) 2019 OnGres, Inc.
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package io.stackgres.operatorframework.resource.factory;

import io.fabric8.kubernetes.api.model.HasMetadata;

@FunctionalInterface
public interface ResourceStreamFactory<T extends HasMetadata, C>
    extends SubResourceStreamFactory<T, C> {

}
