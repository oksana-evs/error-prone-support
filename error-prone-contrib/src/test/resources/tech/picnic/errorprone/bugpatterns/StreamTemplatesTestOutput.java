package tech.picnic.errorprone.bugpatterns;

import static java.util.Comparator.comparingInt;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.reverseOrder;
import static java.util.function.Predicate.not;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

final class StreamTemplatesTest implements RefasterTemplateTestCase {
  @Override
  public ImmutableSet<?> elidedTypesAndStaticImports() {
    return ImmutableSet.of(
        Objects.class, Streams.class, (Runnable) () -> not(null), (Runnable) () -> reverseOrder());
  }

  Stream<String> testEmptyStream() {
    return Stream.empty();
  }

  ImmutableSet<Stream<String>> testStreamOfNullable() {
    return ImmutableSet.of(Stream.ofNullable("a"), Stream.ofNullable("b"));
  }

  Stream<Integer> testConcatOneStream() {
    return Stream.of(1);
  }

  Stream<Integer> testConcatTwoStreams() {
    return Stream.concat(Stream.of(1), Stream.of(2));
  }

  Stream<Integer> testFilterOuterStreamAfterFlatMap() {
    return Stream.of("foo").flatMap(v -> Stream.of(v.length())).filter(len -> len > 0);
  }

  Stream<Integer> testMapOuterStreamAfterFlatMap() {
    return Stream.of("foo").flatMap(v -> Stream.of(v.length())).map(len -> len * 0);
  }

  Stream<Integer> testFlatMapOuterStreamAfterFlatMap() {
    return Stream.of("foo").flatMap(v -> Stream.of(v.length())).flatMap(Stream::of);
  }

  ImmutableSet<Optional<Integer>> testStreamMapFirst() {
    return ImmutableSet.of(
        Stream.of("foo").findFirst().map(s -> s.length()),
        Stream.of("bar").findFirst().map(String::length));
  }

  ImmutableSet<Boolean> testStreamIsEmpty() {
    return ImmutableSet.of(
        Stream.of(1).findAny().isEmpty(),
        Stream.of(2).findAny().isEmpty(),
        Stream.of(3).findAny().isEmpty(),
        Stream.of(4).findAny().isEmpty());
  }

  ImmutableSet<Boolean> testStreamIsNotEmpty() {
    return ImmutableSet.of(
        Stream.of(1).findAny().isPresent(),
        Stream.of(2).findAny().isPresent(),
        Stream.of(3).findAny().isPresent(),
        Stream.of(4).findAny().isPresent());
  }

  ImmutableSet<Optional<String>> testStreamMin() {
    return ImmutableSet.of(
        Stream.of("foo").min(comparingInt(String::length)),
        Stream.of("bar").min(comparingInt(String::length)));
  }

  ImmutableSet<Optional<String>> testStreamMinNaturalOrder() {
    return ImmutableSet.of(
        Stream.of("foo").min(naturalOrder()), Stream.of("bar").min(naturalOrder()));
  }

  ImmutableSet<Optional<String>> testStreamMax() {
    return ImmutableSet.of(
        Stream.of("foo").max(comparingInt(String::length)),
        Stream.of("bar").max(comparingInt(String::length)));
  }

  ImmutableSet<Optional<String>> testStreamMaxNaturalOrder() {
    return ImmutableSet.of(
        Stream.of("foo").max(naturalOrder()), Stream.of("bar").max(naturalOrder()));
  }

  ImmutableSet<Boolean> testStreamNoneMatch() {
    Predicate<String> pred = String::isBlank;
    return ImmutableSet.of(
        Stream.of("foo").noneMatch(s -> s.length() > 1),
        Stream.of("bar").noneMatch(String::isBlank),
        Stream.of("baz").noneMatch(pred),
        Stream.of("qux").noneMatch(String::isEmpty));
  }

  ImmutableSet<Boolean> testStreamNoneMatch2() {
    return ImmutableSet.of(
        Stream.of("foo").noneMatch(s -> s.isBlank()), Stream.of(Boolean.TRUE).noneMatch(b -> b));
  }

  ImmutableSet<Boolean> testStreamAnyMatch() {
    return ImmutableSet.of(
        Stream.of("foo").anyMatch(s -> s.length() > 1), Stream.of("bar").anyMatch(String::isEmpty));
  }

  ImmutableSet<Boolean> testStreamAllMatch() {
    Predicate<String> pred = String::isBlank;
    return ImmutableSet.of(
        Stream.of("foo").allMatch(String::isBlank),
        Stream.of("bar").allMatch(pred),
        Stream.of("baz").allMatch(s -> s.length() > 1),
        Stream.of("qux").allMatch(pred),
        Stream.of("quux").allMatch(String::isEmpty),
        Stream.of("quuz").allMatch(pred),
        Stream.of(Boolean.TRUE).allMatch(b -> b),
        Stream.of(Boolean.TRUE).allMatch(b -> b),
        Stream.of(Boolean.TRUE).allMatch(b -> b));
  }

  ImmutableSet<Boolean> testStreamAllMatch2() {
    return ImmutableSet.of(
        Stream.of("foo").allMatch(s -> s.isBlank()),
        Stream.of("bar").allMatch(s -> s.isEmpty()),
        Stream.of("baz").allMatch(s -> s.isBlank()));
  }
}