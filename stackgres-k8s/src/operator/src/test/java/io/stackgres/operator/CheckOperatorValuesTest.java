/*
 * Copyright (C) 2019 OnGres, Inc.
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package io.stackgres.operator;

import java.net.URL;
import java.nio.file.Paths;
import java.util.Properties;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import io.stackgres.common.OperatorProperty;
import io.stackgres.common.StackGresProperty;
import io.stackgres.operator.app.StackGresOperatorApp;
import io.stackgres.operator.common.StackGresComponents;

import org.jooq.lambda.Seq;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(CheckOperatorValuesExtension.class)
public class CheckOperatorValuesTest {

  @Test
  public void checkOperatorValues() throws Exception {
    ObjectMapper objectMapper = new YAMLMapper();
    JsonNode operatorConfig = objectMapper.readTree(
        Paths.get("../../install/helm/stackgres-operator/values.yaml").toFile());
    final String imageTag;
    if (StackGresProperty.OPERATOR_VERSION.getString().endsWith("-SNAPSHOT")) {
      imageTag = "development(-[^-]+)?-jvm";
    } else {
      imageTag = StackGresProperty.OPERATOR_VERSION.getString() + "-jvm";
    }
    Assert.assertTrue(operatorConfig.get("operator").get("image").get("tag").asText()
        + " should match " + imageTag,
        operatorConfig.get("operator").get("image").get("tag").asText()
            .matches(imageTag));
    Assert.assertEquals(OperatorProperty.PROMETHEUS_AUTOBIND.getString(),
        operatorConfig.get("prometheus").get("allowAutobind").asText());
  }

  @Test
  public void checkComponentVersions() throws Exception {
    ObjectMapper objectMapper = new YAMLMapper();
    JsonNode versions = objectMapper.readTree(
        new URL("https://stackgres.io/downloads/stackgres-k8s/stackgres/components/"
            + StackGresProperty.CONTAINER_BUILD.getString() + "/versions.yaml"));
    Properties properties = new Properties();
    properties.load(StackGresOperatorApp.class.getResourceAsStream("/versions.properties"));
    Assert.assertArrayEquals(
        Seq.seq((ArrayNode) versions.get("components").get("postgresql").get("versions"))
        .map(JsonNode::asText)
        .toArray(),
        StackGresComponents.getAsArray("postgresql"));
    Assert.assertEquals(
        versions.get("components").get("patroni").get("versions").get(0).asText(),
        StackGresComponents.get("patroni"));
    Assert.assertEquals(
        versions.get("components").get("pgbouncer").get("versions").get(0).asText(),
        StackGresComponents.get("pgbouncer"));
    Assert.assertEquals(
        versions.get("components").get("postgres_exporter").get("versions").get(0).asText(),
        StackGresComponents.get("postgres_exporter"));
    Assert.assertEquals(
        versions.get("components").get("envoy").get("versions").get(0).asText(),
        StackGresComponents.get("envoy"));
    Assert.assertEquals(
        versions.get("components").get("fluentbit").get("versions").get(0).asText(),
        StackGresComponents.get("fluentbit"));
    Assert.assertEquals(
        versions.get("components").get("fluentd").get("versions").get(0).asText(),
        StackGresComponents.get("fluentd"));
  }

}
