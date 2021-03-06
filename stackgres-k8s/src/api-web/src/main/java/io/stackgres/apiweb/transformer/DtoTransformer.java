/*
 * Copyright (C) 2019 OnGres, Inc.
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package io.stackgres.apiweb.transformer;

import io.fabric8.kubernetes.client.CustomResource;
import io.stackgres.apiweb.dto.ResourceDto;

public interface DtoTransformer<T extends ResourceDto, R extends CustomResource> {

  T toDto(R customResource);

}
