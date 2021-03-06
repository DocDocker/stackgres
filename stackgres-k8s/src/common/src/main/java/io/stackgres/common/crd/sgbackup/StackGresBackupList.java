/*
 * Copyright (C) 2019 OnGres, Inc.
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package io.stackgres.common.crd.sgbackup;

import io.fabric8.kubernetes.client.CustomResourceList;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class StackGresBackupList extends CustomResourceList<StackGresBackup> {

  private static final long serialVersionUID = -1519749838799557685L;

}
