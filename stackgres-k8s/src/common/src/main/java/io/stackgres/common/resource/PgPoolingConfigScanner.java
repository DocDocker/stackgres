/*
 * Copyright (C) 2019 OnGres, Inc.
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package io.stackgres.common.resource;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.stackgres.common.CdiUtil;
import io.stackgres.common.KubernetesClientFactory;
import io.stackgres.common.crd.sgpooling.StackGresPoolingConfig;
import io.stackgres.common.crd.sgpooling.StackGresPoolingConfigDefinition;
import io.stackgres.common.crd.sgpooling.StackGresPoolingConfigDoneable;
import io.stackgres.common.crd.sgpooling.StackGresPoolingConfigList;

@ApplicationScoped
public class PgPoolingConfigScanner extends AbstractCustomResourceScanner
    <StackGresPoolingConfig, StackGresPoolingConfigList, StackGresPoolingConfigDoneable> {

  @Inject
  public PgPoolingConfigScanner(KubernetesClientFactory clientFactory) {
    super(clientFactory, StackGresPoolingConfigDefinition.CONTEXT,
        StackGresPoolingConfig.class, StackGresPoolingConfigList.class,
        StackGresPoolingConfigDoneable.class);
  }

  public PgPoolingConfigScanner() {
    super(null, null, null, null, null);
    CdiUtil.checkPublicNoArgsConstructorIsCalledToCreateProxy();
  }

}

