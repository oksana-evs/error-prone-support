package tech.picnic.errorprone.bugpatterns;

import com.google.common.collect.ImmutableSet;
import java.time.Duration;
import java.util.Optional;
import java.util.function.Supplier;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;

final class ReactorTemplatesTest implements RefasterTemplateTestCase {
  ImmutableSet<Mono<Integer>> testMonoFromOptional() {
    return ImmutableSet.of(
        Mono.defer(() -> Mono.justOrEmpty(Optional.of(1))),
        Mono.defer(() -> Mono.justOrEmpty(Optional.of(2))));
  }

  Mono<Void> testMonoDeferredError() {
    return Mono.error(() -> new IllegalStateException());
  }

  Flux<Void> testFluxDeferredError() {
    return Flux.error(() -> new IllegalStateException());
  }

  ImmutableSet<Mono<Void>> testMonoErrorSupplier() {
    return ImmutableSet.of(
        Mono.error(((Supplier<RuntimeException>) null)),
        Mono.error(((Supplier<RuntimeException>) null)));
  }

  ImmutableSet<Flux<Void>> testFluxErrorSupplier() {
    return ImmutableSet.of(
        Flux.error(((Supplier<RuntimeException>) null)),
        Flux.error(((Supplier<RuntimeException>) null)));
  }

  Mono<String> testMonoThenReturn() {
    return Mono.empty().thenReturn("foo");
  }

  ImmutableSet<PublisherProbe<Void>> testPublisherProbeEmpty() {
    return ImmutableSet.of(PublisherProbe.empty(), PublisherProbe.empty());
  }

  StepVerifier.Step<Integer> testStepVerifierStepExpectNextEmpty() {
    return StepVerifier.create(Mono.just(0));
  }

  ImmutableSet<StepVerifier.Step<String>> testStepVerifierStepExpectNext() {
    return ImmutableSet.of(
        StepVerifier.create(Mono.just("foo")).expectNext("bar"),
        StepVerifier.create(Mono.just("baz")).expectNext("qux"));
  }

  Duration testStepVerifierLastStepVerifyComplete() {
    return StepVerifier.create(Mono.empty()).verifyComplete();
  }

  Duration testStepVerifierLastStepVerifyError() {
    return StepVerifier.create(Mono.empty()).verifyError();
  }

  Duration testStepVerifierLastStepVerifyErrorClass() {
    return StepVerifier.create(Mono.empty()).verifyError(IllegalArgumentException.class);
  }

  Duration testStepVerifierLastStepVerifyErrorMatches() {
    return StepVerifier.create(Mono.empty())
        .verifyErrorMatches(IllegalArgumentException.class::equals);
  }

  Duration testStepVerifierLastStepVerifyErrorSatisfies() {
    return StepVerifier.create(Mono.empty()).verifyErrorSatisfies(t -> {});
  }

  Duration testStepVerifierLastStepVerifyErrorMessage() {
    return StepVerifier.create(Mono.empty()).verifyErrorMessage("foo");
  }

  Duration testStepVerifierLastStepVerifyTimeout() {
    return StepVerifier.create(Mono.empty()).verifyTimeout(Duration.ZERO);
  }

  Scheduler testBoundedElasticScheduler() {
    return Schedulers.boundedElastic();
  }
}