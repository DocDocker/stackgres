/*
 * Copyright (C) 2019 OnGres, Inc.
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package io.stackgres.distributedlogs.common;

import static io.stackgres.operatorframework.resource.EventReason.Type.WARNING;

import io.stackgres.common.StackgresClusterContainers;
import io.stackgres.operatorframework.resource.EventReason;

public enum DistributedLogsEventReason implements EventReason {

  DISTRIBUTED_LOGS_CONTROLLER_ERROR(WARNING, "DistributedLogsControllerFailed");

  private final Type type;
  private final String reason;

  DistributedLogsEventReason(Type type, String reason) {
    this.type = type;
    this.reason = reason;
  }

  @Override
  public String component() {
    return StackgresClusterContainers.DISTRIBUTEDLOGS_CONTROLLER;
  }

  @Override
  public String reason() {
    return reason;
  }

  @Override
  public Type type() {
    return type;
  }

}
