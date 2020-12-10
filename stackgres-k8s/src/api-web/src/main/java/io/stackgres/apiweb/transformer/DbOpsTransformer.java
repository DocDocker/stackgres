/*
 * Copyright (C) 2019 OnGres, Inc.
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package io.stackgres.apiweb.transformer;

import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import io.stackgres.apiweb.dto.dbops.DbOpsBenchmark;
import io.stackgres.apiweb.dto.dbops.DbOpsBenchmarkStatus;
import io.stackgres.apiweb.dto.dbops.DbOpsCondition;
import io.stackgres.apiweb.dto.dbops.DbOpsDto;
import io.stackgres.apiweb.dto.dbops.DbOpsPgbench;
import io.stackgres.apiweb.dto.dbops.DbOpsPgbenchStatus;
import io.stackgres.apiweb.dto.dbops.DbOpsSpec;
import io.stackgres.apiweb.dto.dbops.DbOpsStatus;
import io.stackgres.common.crd.sgdbops.StackGresDbOps;
import io.stackgres.common.crd.sgdbops.StackGresDbOpsBenchmark;
import io.stackgres.common.crd.sgdbops.StackGresDbOpsBenchmarkStatus;
import io.stackgres.common.crd.sgdbops.StackGresDbOpsCondition;
import io.stackgres.common.crd.sgdbops.StackGresDbOpsPgbench;
import io.stackgres.common.crd.sgdbops.StackGresDbOpsPgbenchStatus;
import io.stackgres.common.crd.sgdbops.StackGresDbOpsSpec;
import io.stackgres.common.crd.sgdbops.StackGresDbOpsStatus;

@ApplicationScoped
public class DbOpsTransformer
    extends AbstractResourceTransformer<DbOpsDto, StackGresDbOps> {

  @Override
  public StackGresDbOps toCustomResource(DbOpsDto source,
      StackGresDbOps original) {
    StackGresDbOps transformation = Optional.ofNullable(original)
        .orElseGet(StackGresDbOps::new);
    transformation.setMetadata(getCustomResourceMetadata(source, original));
    transformation.setSpec(getCustomResourceSpec(source.getSpec()));
    transformation.setStatus(getCustomResourceStatus(source.getStatus()));
    return transformation;
  }

  @Override
  public DbOpsDto toDto(StackGresDbOps source) {
    DbOpsDto transformation = new DbOpsDto();
    transformation.setMetadata(getDtoMetadata(source));
    transformation.setSpec(getResourceSpec(source.getSpec()));
    transformation.setStatus(getResourceStatus(source.getStatus()));
    return transformation;
  }

  private StackGresDbOpsSpec getCustomResourceSpec(DbOpsSpec source) {
    if (source == null) {
      return null;
    }
    StackGresDbOpsSpec transformation = new StackGresDbOpsSpec();
    transformation.setSgCluster(source.getSgCluster());
    transformation.setOp(source.getOp());
    transformation.setRunAt(source.getRunAt());
    transformation.setTimeout(source.getTimeout());
    transformation.setMaxRetries(source.getMaxRetries());
    transformation.setBenchmark(getCustomResourceBenchmark(source.getBenchmark()));
    return transformation;
  }

  private StackGresDbOpsBenchmark getCustomResourceBenchmark(
      DbOpsBenchmark source) {
    if (source == null) {
      return null;
    }
    StackGresDbOpsBenchmark transformation =
        new StackGresDbOpsBenchmark();
    transformation.setType(source.getType());
    transformation.setPgbench(getCustomResourcePgbench(source.getPgbench()));
    transformation.setConnectionType(source.getConnectionType());
    return transformation;
  }

  private StackGresDbOpsPgbench getCustomResourcePgbench(
      DbOpsPgbench source) {
    if (source == null) {
      return null;
    }
    StackGresDbOpsPgbench transformation =
        new StackGresDbOpsPgbench();
    transformation.setDatabaseSize(source.getDatabaseSize());
    transformation.setDuration(source.getDuration());
    transformation.setUsePreparedStatements(source.getUsePreparedStatements());
    transformation.setConcurrentClients(source.getConcurrentClients());
    transformation.setThreads(source.getThreads());
    return transformation;
  }

  private StackGresDbOpsStatus getCustomResourceStatus(DbOpsStatus source) {
    if (source == null) {
      return null;
    }
    StackGresDbOpsStatus transformation = new StackGresDbOpsStatus();
    transformation.setConditions(source.getConditions().stream()
        .map(this::getCustomResourceCondition).collect(Collectors.toList()));
    transformation.setOpRetries(source.getOpRetries());
    transformation.setOpStarted(source.getOpStarted());
    transformation.setBenchmark(getCustomResourceBenchmarkStatus(source.getBenchmark()));
    return transformation;
  }

  private StackGresDbOpsCondition getCustomResourceCondition(DbOpsCondition source) {
    if (source == null) {
      return null;
    }
    StackGresDbOpsCondition transformation = new StackGresDbOpsCondition();
    transformation.setType(source.getType());
    transformation.setStatus(source.getStatus());
    transformation.setLastTransitionTime(source.getLastTransitionTime());
    transformation.setReason(source.getReason());
    transformation.setMessage(source.getMessage());
    return transformation;
  }

  private StackGresDbOpsBenchmarkStatus getCustomResourceBenchmarkStatus(
      DbOpsBenchmarkStatus source) {
    if (source == null) {
      return null;
    }
    StackGresDbOpsBenchmarkStatus transformation =
        new StackGresDbOpsBenchmarkStatus();
    transformation.setPgbench(getCustomResourcePgbenchStatus(source.getPgbench()));
    return transformation;
  }

  private StackGresDbOpsPgbenchStatus getCustomResourcePgbenchStatus(
      DbOpsPgbenchStatus source) {
    if (source == null) {
      return null;
    }
    StackGresDbOpsPgbenchStatus transformation =
        new StackGresDbOpsPgbenchStatus();
    transformation.setScaleFactor(source.getScaleFactor());
    transformation.setTransactionsProcessed(source.getTransactionsProcessed());
    transformation.setLatencyAverage(source.getLatencyAverage());
    transformation.setLatencyStddev(source.getLatencyStddev());
    transformation.setTpsIncludingConnectionsEstablishing(
        source.getTpsIncludingConnectionsEstablishing());
    transformation.setTpsExcludingConnectionsEstablishing(
        source.getTpsExcludingConnectionsEstablishing());
    return transformation;
  }

  private DbOpsSpec getResourceSpec(StackGresDbOpsSpec source) {
    DbOpsSpec transformation = new DbOpsSpec();
    transformation.setSgCluster(source.getSgCluster());
    transformation.setOp(source.getOp());
    transformation.setRunAt(source.getRunAt());
    transformation.setTimeout(source.getTimeout());
    transformation.setMaxRetries(source.getMaxRetries());
    transformation.setBenchmark(
        getResourceBenchmark(source.getBenchmark()));
    return transformation;
  }

  private DbOpsBenchmark getResourceBenchmark(
      StackGresDbOpsBenchmark source) {
    if (source == null) {
      return null;
    }
    DbOpsBenchmark transformation = new DbOpsBenchmark();
    transformation.setType(source.getType());
    transformation.setPgbench(getResourcePgbench(source.getPgbench()));
    transformation.setConnectionType(source.getConnectionType());
    return transformation;
  }

  private DbOpsPgbench getResourcePgbench(
      StackGresDbOpsPgbench source) {
    if (source == null) {
      return null;
    }
    DbOpsPgbench transformation = new DbOpsPgbench();
    transformation.setDatabaseSize(source.getDatabaseSize());
    transformation.setDuration(source.getDuration());
    transformation.setUsePreparedStatements(source.getUsePreparedStatements());
    transformation.setConcurrentClients(source.getConcurrentClients());
    transformation.setThreads(source.getThreads());
    return transformation;
  }

  private DbOpsStatus getResourceStatus(StackGresDbOpsStatus source) {
    if (source == null) {
      return null;
    }
    DbOpsStatus transformation = new DbOpsStatus();
    transformation.setConditions(source.getConditions().stream()
        .map(this::getResourceCondition).collect(Collectors.toList()));
    transformation.setOpRetries(source.getOpRetries());
    transformation.setOpStarted(source.getOpStarted());
    transformation.setBenchmark(getResourceBenchmarkStatus(source.getBenchmark()));
    return transformation;
  }

  private DbOpsCondition getResourceCondition(StackGresDbOpsCondition source) {
    if (source == null) {
      return null;
    }
    DbOpsCondition transformation = new DbOpsCondition();
    transformation.setType(source.getType());
    transformation.setStatus(source.getStatus());
    transformation.setLastTransitionTime(source.getLastTransitionTime());
    transformation.setReason(source.getReason());
    transformation.setMessage(source.getMessage());
    return transformation;
  }

  private DbOpsBenchmarkStatus getResourceBenchmarkStatus(
      StackGresDbOpsBenchmarkStatus source) {
    if (source == null) {
      return null;
    }
    DbOpsBenchmarkStatus transformation = new DbOpsBenchmarkStatus();
    transformation.setPgbench(getResourcePgbenchStatus(source.getPgbench()));
    return transformation;
  }

  private DbOpsPgbenchStatus getResourcePgbenchStatus(
      StackGresDbOpsPgbenchStatus source) {
    if (source == null) {
      return null;
    }
    DbOpsPgbenchStatus transformation = new DbOpsPgbenchStatus();
    transformation.setScaleFactor(source.getScaleFactor());
    transformation.setTransactionsProcessed(source.getTransactionsProcessed());
    transformation.setLatencyAverage(source.getLatencyAverage());
    transformation.setLatencyStddev(source.getLatencyStddev());
    transformation.setTpsIncludingConnectionsEstablishing(
        source.getTpsIncludingConnectionsEstablishing());
    transformation.setTpsExcludingConnectionsEstablishing(
        source.getTpsExcludingConnectionsEstablishing());
    return transformation;
  }

}
