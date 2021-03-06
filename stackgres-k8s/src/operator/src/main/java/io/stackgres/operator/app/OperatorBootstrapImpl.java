/*
 * Copyright (C) 2019 OnGres, Inc.
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package io.stackgres.operator.app;

import java.net.SocketTimeoutException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.internal.KubernetesDeserializer;
import io.stackgres.common.KubernetesClientFactory;
import io.stackgres.common.crd.sgbackup.StackGresBackup;
import io.stackgres.common.crd.sgbackup.StackGresBackupDefinition;
import io.stackgres.common.crd.sgbackupconfig.StackGresBackupConfig;
import io.stackgres.common.crd.sgbackupconfig.StackGresBackupConfigDefinition;
import io.stackgres.common.crd.sgcluster.StackGresCluster;
import io.stackgres.common.crd.sgcluster.StackGresClusterDefinition;
import io.stackgres.common.crd.sgdbops.StackGresDbOps;
import io.stackgres.common.crd.sgdbops.StackGresDbOpsDefinition;
import io.stackgres.common.crd.sgdistributedlogs.StackGresDistributedLogs;
import io.stackgres.common.crd.sgdistributedlogs.StackGresDistributedLogsDefinition;
import io.stackgres.common.crd.sgpgconfig.StackGresPostgresConfig;
import io.stackgres.common.crd.sgpgconfig.StackGresPostgresConfigDefinition;
import io.stackgres.common.crd.sgpooling.StackGresPoolingConfig;
import io.stackgres.common.crd.sgpooling.StackGresPoolingConfigDefinition;
import io.stackgres.common.crd.sgprofile.StackGresProfile;
import io.stackgres.common.crd.sgprofile.StackGresProfileDefinition;
import io.stackgres.operator.common.StackGresClusterContext;
import io.stackgres.operator.common.StackGresDistributedLogsContext;
import io.stackgres.operator.initialization.InitializationQueue;
import io.stackgres.operatorframework.resource.ResourceHandlerSelector;
import io.stackgres.operatorframework.resource.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class OperatorBootstrapImpl implements OperatorBootstrap {

  private static final Logger LOGGER = LoggerFactory.getLogger(OperatorBootstrapImpl.class);

  private final KubernetesClientFactory kubeClient;
  private final ResourceHandlerSelector<StackGresClusterContext> handlerSelector;
  private final ResourceHandlerSelector<StackGresDistributedLogsContext>
      distributedLogsHandlerSelector;
  private final InitializationQueue initializationQueue;

  @Inject
  public OperatorBootstrapImpl(
      KubernetesClientFactory kubeClient,
      ResourceHandlerSelector<StackGresClusterContext> handlerSelector,
      ResourceHandlerSelector<StackGresDistributedLogsContext> distributedLogsHandlerSelector,
      InitializationQueue initializationQueue) {
    this.kubeClient = kubeClient;
    this.handlerSelector = handlerSelector;
    this.distributedLogsHandlerSelector = distributedLogsHandlerSelector;
    this.initializationQueue = initializationQueue;
  }

  @Override
  public void bootstrap() {

    try (KubernetesClient client = kubeClient.create()) {
      if (client.getVersion() != null) {
        LOGGER.info("Kubernetes version: {}", client.getVersion().getGitVersion());
      }
      LOGGER.info("URL of this Kubernetes cluster: {}", client.getMasterUrl());
      if (!hasCustomResource(client, StackGresClusterDefinition.NAME)
          || !hasCustomResource(client, StackGresProfileDefinition.NAME)
          || !hasCustomResource(client, StackGresPostgresConfigDefinition.NAME)
          || !hasCustomResource(client, StackGresPoolingConfigDefinition.NAME)
          || !hasCustomResource(client, StackGresBackupConfigDefinition.NAME)
          || !hasCustomResource(client, StackGresBackupDefinition.NAME)
          || !hasCustomResource(client, StackGresDistributedLogsDefinition.NAME)
          || !hasCustomResource(client, StackGresDbOpsDefinition.NAME)) {
        throw new RuntimeException("Some required CRDs does not exists");
      }

      registerResources();
      initializationQueue.start();

    } catch (KubernetesClientException e) {
      if (e.getCause() instanceof SocketTimeoutException) {
        LOGGER.error("Kubernetes cluster is not reachable, check your connection.");
      }
      throw e;
    }

  }

  private void registerResources() {
    KubernetesDeserializer.registerCustomKind(StackGresClusterDefinition.APIVERSION,
        StackGresClusterDefinition.KIND, StackGresCluster.class);

    KubernetesDeserializer.registerCustomKind(StackGresPostgresConfigDefinition.APIVERSION,
        StackGresPostgresConfigDefinition.KIND, StackGresPostgresConfig.class);

    KubernetesDeserializer.registerCustomKind(StackGresPoolingConfigDefinition.APIVERSION,
        StackGresPoolingConfigDefinition.KIND, StackGresPoolingConfig.class);

    KubernetesDeserializer.registerCustomKind(StackGresProfileDefinition.APIVERSION,
        StackGresProfileDefinition.KIND, StackGresProfile.class);

    KubernetesDeserializer.registerCustomKind(StackGresBackupConfigDefinition.APIVERSION,
        StackGresBackupConfigDefinition.KIND, StackGresBackupConfig.class);

    KubernetesDeserializer.registerCustomKind(StackGresBackupDefinition.APIVERSION,
        StackGresBackupDefinition.KIND, StackGresBackup.class);

    KubernetesDeserializer.registerCustomKind(StackGresDbOpsDefinition.APIVERSION,
        StackGresDbOpsDefinition.KIND, StackGresDbOps.class);

    KubernetesDeserializer.registerCustomKind(StackGresDistributedLogsDefinition.APIVERSION,
        StackGresDistributedLogsDefinition.KIND, StackGresDistributedLogs.class);

    handlerSelector.registerKinds();
    distributedLogsHandlerSelector.registerKinds();
  }

  private boolean hasCustomResource(KubernetesClient client, String crdName) {
    if (!ResourceUtil.getCustomResource(client, crdName).isPresent()) {
      LOGGER.error("CRD not found, please create it first: {}", crdName);
      return false;
    }
    return true;
  }

}
