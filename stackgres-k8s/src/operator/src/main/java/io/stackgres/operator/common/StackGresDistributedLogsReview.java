/*
 * Copyright (C) 2019 OnGres, Inc.
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package io.stackgres.operator.common;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.stackgres.common.crd.sgdistributedlogs.StackGresDistributedLogs;
import io.stackgres.operatorframework.admissionwebhook.AdmissionReview;

@RegisterForReflection
public class StackGresDistributedLogsReview extends AdmissionReview<StackGresDistributedLogs> {

  private static final long serialVersionUID = 1L;

}
