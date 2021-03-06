/*
 * Copyright (C) 2019 OnGres, Inc.
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package io.stackgres.operator.mutation.pgconfig;

import javax.enterprise.context.ApplicationScoped;

import io.stackgres.common.crd.sgpgconfig.StackGresPostgresConfig;
import io.stackgres.operator.common.PgConfigReview;
import io.stackgres.operator.mutation.AbstractAnnotationMutator;

@ApplicationScoped
public class PgConfigAnnotationMutator
    extends AbstractAnnotationMutator<StackGresPostgresConfig, PgConfigReview>
    implements PgConfigMutator {
}
