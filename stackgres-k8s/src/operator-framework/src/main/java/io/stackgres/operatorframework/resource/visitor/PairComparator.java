/*
 * Copyright (C) 2019 OnGres, Inc.
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */

package io.stackgres.operatorframework.resource.visitor;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jooq.lambda.tuple.Tuple;

class PairComparator<T> extends PairVisitor<T, Boolean> {

  PairComparator(T left, T right) {
    super(left, right);
  }

  @Override
  public Boolean result() {
    return true;
  }

  @Override
  public PairVisitor<T, Boolean> transformRight(UnaryOperator<T> rightTransformer) {
    return new PairComparator<>(left, rightTransformer.apply(right));
  }

  @Override
  public PairVisitor<T, Boolean> transformLeft(UnaryOperator<T> leftTransformer) {
    return new PairComparator<>(leftTransformer.apply(left), right);
  }

  @Override
  public PairVisitor<T, Boolean> visit() {
    if (left == null || right == null) {
      return lastResult(left == null && right == null);
    }
    return this;
  }

  @Override
  public <O> PairVisitor<T, Boolean> visit(Function<T, O> getter) {
    return returnResult(equalsAny(getter));
  }

  @Override
  public <O> PairVisitor<T, Boolean> visit(Function<T, O> getter,
      BiConsumer<T, O> setter) {
    return returnResult(equalsAny(getter));
  }

  @Override
  public <O> PairVisitor<T, Boolean> visit(Function<T, O> getter,
      BiConsumer<T, O> setter, O defaultValue) {
    return returnResult(equalsAnyWithDefault(getter, t -> defaultValue));
  }

  @Override
  public <O> PairVisitor<T, Boolean> visitUsingDefaultFrom(Function<T, O> getter,
      BiConsumer<T, O> setter, Function<T, O> defaultGetter) {
    return returnResult(equalsAnyWithDefault(getter, defaultGetter));
  }

  @Override
  public <O> PairVisitor<T, Boolean> visitTransformed(
      Function<T, O> getter, BiConsumer<T, O> setter,
      BinaryOperator<O> leftTransformer, BinaryOperator<O> rightTransformer) {
    return returnResult(equalsAnyTransformed(getter, leftTransformer, rightTransformer));
  }

  @Override
  public <O, S> PairVisitor<T, Boolean> visitWith(Function<T, O> getter,
      BiConsumer<T, O> setter,
      UnaryOperator<PairVisitor<O, S>> subVisitor) {
    return returnResult(equalsAnyWith(getter,
        pv -> subVisitor.apply(pv.as()).resultAs()));
  }

  @Override
  public <O, S> PairVisitor<T, Boolean> visitWithUsingDefaultFrom(Function<T, O> getter,
      BiConsumer<T, O> setter,
      UnaryOperator<PairVisitor<O, S>> subVisitor,
      Supplier<O> defaultValue) {
    return returnResult(equalsAnyWithUsingDefaultFrom(getter,
        pv -> subVisitor.apply(pv.as()).resultAs(),
        defaultValue));
  }

  <O> boolean equalsAny(Function<T, O> getter) {
    return equals(getter.apply(left), getter.apply(right));
  }

  <O> boolean equalsAnyWithDefault(Function<T, O> getter, Function<T, O> defaultGetter) {
    return equals(
        Optional.ofNullable(getter.apply(left)).orElseGet(() -> defaultGetter.apply(left)),
        Optional.ofNullable(getter.apply(right)).orElseGet(() -> defaultGetter.apply(right)));
  }

  <O> boolean equalsAnyTransformed(Function<T, O> getter,
      BinaryOperator<O> leftTransformer, BinaryOperator<O> rightTransformer) {
    O leftValue = getter.apply(left);
    O rightValue = getter.apply(right);
    return equals(leftTransformer.apply(leftValue, rightValue),
        rightTransformer.apply(leftValue, rightValue));
  }

  <O> boolean equalsAnyWith(Function<T, O> getter,
      Predicate<PairVisitor<O, Boolean>> tester) {
    return tester.test(get(getter));
  }

  <O> boolean equalsAnyWithUsingDefaultFrom(Function<T, O> getter,
      Predicate<PairVisitor<O, Boolean>> tester, Supplier<O> defaultValue) {
    return tester.test(getOrDefault(getter, defaultValue));
  }

  <O> PairComparator<O> get(Function<T, O> getter) {
    return new PairComparator<>(getter.apply(left), getter.apply(right));
  }

  <O> PairComparator<O> getOrDefault(Function<T, O> getter, Supplier<O> defaultValue) {
    return new PairComparator<>(
        getter.andThen(Optional::ofNullable)
            .andThen(o -> o.orElseGet(defaultValue)).apply(left),
        getter.andThen(Optional::ofNullable)
            .andThen(o -> o.orElseGet(defaultValue)).apply(right));
  }

  @Override
  public <E, O extends List<E>> PairVisitor<T, Boolean> visitList(
      Function<T, O> getter, BiConsumer<T, O> setter) {
    return returnResult(equalsList(getter));
  }

  @Override
  public <E, O extends List<E>, S> PairVisitor<T, Boolean> visitListWith(
      Function<T, O> getter, BiConsumer<T, O> setter,
      UnaryOperator<PairVisitor<E, S>> subVisitor) {
    return returnResult(equalsList(getter, pv -> subVisitor.apply(pv.as()).resultAs()));
  }

  <E, O extends List<E>> boolean equalsList(
      Function<T, O> getter) {
    O leftList = getter.apply(left);
    O rightList = getter.apply(right);
    if (leftList == null || leftList.isEmpty()) {
      return rightList == null || rightList.isEmpty();
    }
    if (rightList == null
        || leftList.size() != rightList.size()) {
      return false;
    }
    ListIterator<E> leftListIterator = leftList.listIterator();
    ListIterator<E> rightListIterator = rightList.listIterator();
    while (leftListIterator.hasNext() && rightListIterator.hasNext()) {
      if (!equals(leftListIterator.next(), rightListIterator.next())) {
        return false;
      }
    }
    return true;
  }

  <E, O extends List<E>> boolean equalsList(
      Function<T, O> getter, Predicate<PairVisitor<E, Boolean>> tester) {
    O leftList = getter.apply(left);
    O rightList = getter.apply(right);
    if (leftList == null || leftList.isEmpty()) {
      return rightList == null || rightList.isEmpty();
    }
    if (rightList == null
        || leftList.size() != rightList.size()) {
      return false;
    }
    ListIterator<E> leftListIterator = leftList.listIterator();
    ListIterator<E> rightListIterator = rightList.listIterator();
    while (leftListIterator.hasNext() && rightListIterator.hasNext()) {
      if (!tester.test(new PairComparator<>(
          leftListIterator.next(), rightListIterator.next()))) {
        return false;
      }
    }
    return true;
  }

  @Override
  public <K, V, O extends Map<K, V>> PairVisitor<T, Boolean> visitMap(
      Function<T, O> getter) {
    return returnResult(equalsMap(getter, (l, r) -> l, (l, r) -> r, false));
  }

  @Override
  public <K, V, O extends Map<K, V>> PairVisitor<T, Boolean> visitMap(
      Function<T, O> getter, BiConsumer<T, O> setter) {
    return visitMap(getter);
  }

  @Override
  public <K, V, O extends Map<K, V>> PairVisitor<T, Boolean> visitMapKeys(
      Function<T, O> getter) {
    return returnResult(equalsMap(getter, (l, r) -> l, (l, r) -> r, true));
  }

  @Override
  public <K, V, O extends Map<K, V>> PairVisitor<T, Boolean> visitMapKeys(
      Function<T, O> getter, BiConsumer<T, O> setter) {
    return visitMapKeys(getter);
  }

  @Override
  public <K, V, O extends Map<K, V>> PairVisitor<T, Boolean> visitMapTransformed(
      Function<T, O> getter, BiConsumer<T, O> setter,
      BinaryOperator<Entry<K, V>> leftTransformer,
      BinaryOperator<Entry<K, V>> rightTransformer) {
    return returnResult(equalsMap(getter,
        leftTransformer, rightTransformer, false));
  }

  <K, V, O extends Map<K, V>> boolean equalsMap(
      Function<T, O> getter,
      BinaryOperator<Entry<K, V>> leftTransformer,
      BinaryOperator<Entry<K, V>> rightTransformer,
      boolean onlyKeys) {
    Map<K, V> leftMap = Optional.ofNullable(getter.apply(left))
        .map(map -> (Map<K, V>) map)
        .orElseGet(() -> new HashMap<K, V>(0));
    Map<K, V> rightMap = Optional.ofNullable(getter.apply(right))
        .map(map -> (Map<K, V>) map)
        .orElseGet(() -> new HashMap<K, V>(0));
    return Stream.concat(leftMap.keySet().stream(), rightMap.keySet().stream())
        .collect(Collectors.groupingBy(key -> key))
        .keySet()
        .stream()
        .map(key -> Tuple.tuple(
            leftMap.entrySet().stream()
                .filter(e -> e.getKey().equals(key))
                .findAny().orElse(null),
            rightMap.entrySet().stream()
                .filter(e -> e.getKey().equals(key))
                .findAny().orElse(null)))
        .map(t -> Tuple.tuple(
            Optional.ofNullable(leftTransformer.apply(t.v1, t.v2))
                .map(e -> onlyKeys ? e.getKey() : e.getValue()).orElse(null),
            Optional.ofNullable(rightTransformer.apply(t.v1, t.v2))
                .map(e -> onlyKeys ? e.getKey() : e.getValue()).orElse(null)))
        .allMatch(t -> equals(t.v1, t.v2));
  }

  private boolean equals(Object left, Object right) {
    return Objects.equals(left, right);
  }

  private PairVisitor<T, Boolean> returnResult(boolean equals) {
    if (equals) {
      return this;
    }
    return lastResult(false);
  }

}
