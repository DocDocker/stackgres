/*
 * Copyright (C) 2019 OnGres, Inc.
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package io.stackgres.operator.validation.cluster;

import java.util.Optional;

import io.stackgres.operator.services.KubernetesCustomResourceFinder;
import io.stackgres.operator.utils.JsonUtil;
import io.stackgres.operator.validation.Operation;
import io.stackgres.operator.validation.StackgresClusterReview;
import io.stackgres.operator.validation.ValidationFailed;
import io.stackgres.sidecars.pgbouncer.customresources.StackGresPgbouncerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class PgBouncerValidatorTest {

  private PgBouncerValidator validator;

  @Mock()
  private KubernetesCustomResourceFinder<StackGresPgbouncerConfig> configFinder;

  private StackGresPgbouncerConfig pgbouncerConfig;

  @BeforeEach
  void setUp(){
    validator = new PgBouncerValidator(configFinder);

    pgbouncerConfig = JsonUtil.readFromJson("pgbouncer_config/default.json", StackGresPgbouncerConfig.class);

  }

  @Test
  void givenValidStackgresPoolingOnCreation_shouldNotFail() throws ValidationFailed {

    final StackgresClusterReview review = JsonUtil
        .readFromJson("cluster_allow_requests/valid_creation.json", StackgresClusterReview.class);

    String poolingConfig = review.getRequest().getObject().getSpec().getConnectionPoolingConfig();
    String namespace = review.getRequest().getObject().getMetadata().getNamespace();
    when(configFinder.findByNameAndNamespace(poolingConfig, namespace))
        .thenReturn(Optional.of(pgbouncerConfig));

    validator.validate(review);

    verify(configFinder).findByNameAndNamespace(eq(poolingConfig), eq(namespace));

  }

  @Test
  void giveInvalidStackgresPoolingOnCreation_shouldFail() {

    final StackgresClusterReview review = JsonUtil
        .readFromJson("cluster_allow_requests/valid_creation.json", StackgresClusterReview.class);

    String poolingConfig = review.getRequest().getObject().getSpec().getConnectionPoolingConfig();
    String namespace = review.getRequest().getObject().getMetadata().getNamespace();

    when(configFinder.findByNameAndNamespace(poolingConfig, namespace))
        .thenReturn(Optional.empty());

    ValidationFailed ex = assertThrows(ValidationFailed.class, () -> {
      validator.validate(review);
    });

    String resultMessage = ex.getMessage();

    assertEquals("Pooling config " + poolingConfig + " not found", resultMessage);

  }

  @Test
  void giveAnAttemptToUpdateToAnUnknownPoolingConfig_shouldFail() {

    final StackgresClusterReview review = JsonUtil
        .readFromJson("cluster_allow_requests/connectionpooling_config_update.json", StackgresClusterReview.class);

    String poolingConfig = review.getRequest().getObject().getSpec().getConnectionPoolingConfig();

    String namespace = review.getRequest().getObject().getMetadata().getNamespace();

    when(configFinder.findByNameAndNamespace(poolingConfig, namespace))
        .thenReturn(Optional.empty());

    ValidationFailed ex = assertThrows(ValidationFailed.class, () -> {
      validator.validate(review);
    });

    String resultMessage = ex.getMessage();

    assertEquals("Cannot update to pooling config " + poolingConfig
        + " because it doesn't exists", resultMessage);

    verify(configFinder).findByNameAndNamespace(eq(poolingConfig), eq(namespace));

  }

  @Test
  void giveAnAttemptToUpdateToAnKnownPooling_shouldNotFail() throws ValidationFailed {

    final StackgresClusterReview review = JsonUtil
        .readFromJson("cluster_allow_requests/connectionpooling_config_update.json", StackgresClusterReview.class);

    String poolingConfig = review.getRequest().getObject().getSpec().getConnectionPoolingConfig();

    String namespace = review.getRequest().getObject().getMetadata().getNamespace();

    when(configFinder.findByNameAndNamespace(poolingConfig, namespace))
        .thenReturn(Optional.of(pgbouncerConfig));

    validator.validate(review);

    verify(configFinder).findByNameAndNamespace(eq(poolingConfig), eq(namespace));

  }

  @Test
  void giveAnAttemptToDelete_shouldNotFail() throws ValidationFailed {

    final StackgresClusterReview review = JsonUtil
        .readFromJson("cluster_allow_requests/connectionpooling_config_update.json", StackgresClusterReview.class);
    review.getRequest().setOperation(Operation.DELETE);

    String poolingConfig = review.getRequest().getObject().getSpec().getConnectionPoolingConfig();

    String namespace = review.getRequest().getObject().getMetadata().getNamespace();

    validator.validate(review);

    verify(configFinder, never()).findByNameAndNamespace(eq(poolingConfig), eq(namespace));

  }

}