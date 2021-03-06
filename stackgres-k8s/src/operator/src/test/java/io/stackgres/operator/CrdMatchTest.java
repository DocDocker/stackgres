/*
 * Copyright (C) 2019 OnGres, Inc.
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package io.stackgres.operator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import io.fabric8.kubernetes.client.CustomResource;
import io.stackgres.common.StackGresProperty;
import io.stackgres.common.YamlMapperProvider;
import io.stackgres.testutil.CrdUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

public class CrdMatchTest {

  private static final String CRD_POD_VERSION = StackGresProperty.CRD_VERSION.getString();

  private static final String CRD_POD_GROUP = StackGresProperty.CRD_GROUP.getString();

  private static File[] crdFiles;

  private static Map<String, CustomResourceDefinition> definitionsByKind;

  @BeforeAll
  static void beforeAll() throws Exception {

    File crdFolder = CrdUtils.getCrdsFolder();

    crdFiles = crdFolder.listFiles(file -> file.getName().endsWith(".yaml"));

    List<CustomResourceDefinition> customResourceDefinitions = getCustomResourceDefinitions();

    definitionsByKind = customResourceDefinitions
        .stream()
        .collect(Collectors.toMap(CustomResourceDefinition::getKind, Function.identity()));

  }

  private static List<CustomResourceDefinition> getCustomResourceDefinitions() throws IOException {

    ClassLoader LOADER = Thread.currentThread().getContextClassLoader();

    ClassPath CLASSPATH_SCANNER = ClassPath.from(LOADER);

    ImmutableSet<ClassPath.ClassInfo> clazzes = CLASSPATH_SCANNER
        .getTopLevelClassesRecursive("io.stackgres.common.crd");

    return clazzes.stream()
        .map(classInfo -> classInfo.load())
        .filter(CustomResource.class::isAssignableFrom)
        .filter(clazz -> {
          try {
            Class.forName(clazz.getName() + "Definition");
            return true;
          } catch (ClassNotFoundException ex) {
            return false;
          }
        })
        .map(clazz -> {
          try {
            return Class.forName(clazz.getName() + "Definition");
          } catch (ClassNotFoundException e) {
            throw new AssertionFailedError("Custom Resources "
                + clazz.getName() + " definition is no accessible");
          }
        })
        .filter(clazz -> clazz.isEnum())
        .map(resurceDefinition -> {
          CustomResourceDefinition crd = new CustomResourceDefinition();
          try {
            crd.setKind((String) resurceDefinition.getField("KIND").get(null));
            crd.setSingular((String) resurceDefinition.getField("SINGULAR").get(null));
            crd.setPlural((String) resurceDefinition.getField("PLURAL").get(null));
            crd.setName((String) resurceDefinition.getField("NAME").get(null));
            return crd;
          } catch (Exception e) {
            throw new AssertionFailedError("Class "
                + resurceDefinition.getName() + "is not properly defined", e);
          }
        })
        .collect(Collectors.toList());
  }

  private static void withEveryYaml(Consumer<JsonNode> crdDefinition) throws IOException {
    YAMLMapper yamlMapper = new YamlMapperProvider().yamlMapper();
    for (File crd : crdFiles) {
      JsonNode crdTree = yamlMapper.readTree(crd);
      crdDefinition.accept(crdTree);
    }
  }

  @Test
  void crdVersion_ShouldMatchConfiguredVersion() throws IOException {

    withEveryYaml((crdTree) -> {
      JsonNode crdInstallVersions = crdTree.get("spec").get("versions");

      for (JsonNode crdInstallVersion : crdInstallVersions) {
        assertEquals(CRD_POD_VERSION, crdInstallVersion.get("name").asText());

      }
    });

  }

  @Test
  void crdVersion_ShouldMatchConfiguredGroup() throws IOException {

    withEveryYaml((crdTree) -> {
      String yamlGroup = crdTree.get("spec").get("group").asText();

      assertEquals(CRD_POD_GROUP, yamlGroup);

    });

  }

  @Test
  void customResourcesYamlSingular_shouldMatchWithSingularInJavaDefinition() throws IOException {

    withEveryYaml((crdTree) -> {

      JsonNode crdNames = crdTree.get("spec").get("names");

      CustomResourceDefinition definition = getDefinition(crdTree);

      String declaredSingular = crdNames.get("singular").asText();

      assertEquals(definition.getSingular(), declaredSingular);

    });

  }

  @Test
  void CustomResourcesYamlDefinitionsPlural_ShouldMatchWithPluralInJavaDefinition() throws IOException {

    withEveryYaml((crdTree) -> {

      JsonNode crdNames = crdTree.get("spec").get("names");

      CustomResourceDefinition definition = getDefinition(crdTree);

      String declaredPlural = crdNames.get("plural").asText();

      assertEquals(definition.getPlural(), declaredPlural);

    });

  }

  @Test
  void CustomResourcesYamlMetadataName_ShouldMatchWithNameInJavaDefinition() throws IOException {

    withEveryYaml((crdTree) -> {

      JsonNode metadataName = crdTree.get("metadata").get("name");

      CustomResourceDefinition definition = getDefinition(crdTree);

      assertEquals(definition.getName(), metadataName.asText());

    });

  }

  private static CustomResourceDefinition getDefinition(JsonNode crdTree) {

    String declaredKind = crdTree.get("spec").get("names").get("kind").asText();

    return Optional.ofNullable(definitionsByKind.get(declaredKind))
        .orElseThrow(() -> new AssertionFailedError("Custom Resource definition "
            + declaredKind + " does not exists. Available kinds: " + definitionsByKind.keySet()));

  }

  private static class CustomResourceDefinition {

    private String kind;

    private String plural;

    private String singular;

    private String name;

    public String getKind() {
      return kind;
    }

    public void setKind(String kind) {
      this.kind = kind;
    }

    public String getPlural() {
      return plural;
    }

    public void setPlural(String plural) {
      this.plural = plural;
    }

    public String getSingular() {
      return singular;
    }

    public void setSingular(String singular) {
      this.singular = singular;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

  }

}
