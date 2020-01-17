/*
 * Copyright (C) 2019 OnGres, Inc.
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package io.stackgres.operator.sidecars.pgbouncer.customresources;

import io.stackgres.operator.common.StackGresUtil;

public enum StackGresPgbouncerConfigDefinition {

  ;

  public static final String KIND = "StackGresConnectionPoolingConfig";
  public static final String SINGULAR = "sgconnectionpoolingconfig";
  public static final String PLURAL = "sgconnectionpoolingconfigs";
  public static final String NAME = PLURAL + "." + StackGresUtil.CRD_GROUP;
  public static final String APIVERSION = StackGresUtil.CRD_GROUP + "/" + StackGresUtil.CRD_VERSION;

}