package tech.picnic.errorprone.refastertemplates;

import com.google.errorprone.refaster.Refaster;
import com.google.errorprone.refaster.annotation.AfterTemplate;
import com.google.errorprone.refaster.annotation.AlsoNegation;
import com.google.errorprone.refaster.annotation.BeforeTemplate;
import com.google.errorprone.refaster.annotation.NoAutoboxing;
import java.util.Objects;
import java.util.function.Predicate;

/** Refaster templates related to expressions dealing with (in)equalities. */
final class EqualityTemplates {
  private EqualityTemplates() {}

  /** Prefer primitive/reference-based quality for primitives and enums. */
  static final class PrimitiveOrReferenceEquality {
    @NoAutoboxing
    @BeforeTemplate
    boolean before(boolean a, boolean b) {
      return Objects.equals(a, b);
    }

    @NoAutoboxing
    @BeforeTemplate
    boolean before(long a, long b) {
      return Objects.equals(a, b);
    }

    @NoAutoboxing
    @BeforeTemplate
    boolean before(double a, double b) {
      return Objects.equals(a, b);
    }

    /**
     * Enums can be compared by reference. It is safe to do so even in the face of refactorings,
     * because if the type is ever converted to a non-enum, then Error-Prone will complain about any
     * remaining reference-based equality checks.
     */
    // XXX: This Refaster rule is the topic of https://github.com/google/error-prone/issues/559. We
    // work around the issue by selecting the "largest replacements". See RefasterCheck.
    @BeforeTemplate
    <T extends Enum<T>> boolean before(T a, T b) {
      return Refaster.anyOf(a.equals(b), Objects.equals(a, b));
    }

    @AlsoNegation
    @AfterTemplate
    boolean after(boolean a, boolean b) {
      return a == b;
    }
  }

  /** Prefer {@link Object#equals(Object)} over the equivalent lambda function. */
  // XXX: As it stands, this rule is a special case of what `MethodReferenceUsageCheck` tries to
  // achieve. If/when `MethodReferenceUsageCheck` becomes production ready, we should simply drop
  // this check.
  // XXX: Alternatively, rule should be replaced with a plugin which also identifies cases where
  // the arguments are swapped but simplification is possible anyway, by virtue of `v` being
  // non-null.
  static final class EqualsPredicate<T> {
    @BeforeTemplate
    @SuppressWarnings("NoFunctionalReturnType")
    Predicate<T> before(T v) {
      return e -> v.equals(e);
    }

    @AfterTemplate
    @SuppressWarnings("NoFunctionalReturnType")
    Predicate<T> after(T v) {
      return v::equals;
    }
  }

  /** Avoid double negations; this is not Javascript. */
  static final class DoubleNegation {
    @BeforeTemplate
    boolean before(boolean b) {
      return !!b;
    }

    @AfterTemplate
    boolean after(boolean b) {
      return b;
    }
  }

  /**
   * Don't negate an equality test or use the ternary operator to compare two booleans; directly
   * test for inequality instead.
   */
  static final class Negation {
    @BeforeTemplate
    boolean before(boolean a, boolean b) {
      return Refaster.anyOf(!(a == b), a ? !b : b);
    }

    @BeforeTemplate
    boolean before(long a, long b) {
      return !(a == b);
    }

    @BeforeTemplate
    boolean before(double a, double b) {
      return !(a == b);
    }

    @BeforeTemplate
    boolean before(Object a, Object b) {
      return !(a == b);
    }

    @AfterTemplate
    boolean after(boolean a, boolean b) {
      return a != b;
    }
  }

  /**
   * Don't negate an inequality test or use the ternary operator to compare two booleans; directly
   * test for equality instead.
   */
  static final class IndirectDoubleNegation {
    @BeforeTemplate
    boolean before(boolean a, boolean b) {
      return Refaster.anyOf(!(a != b), a ? b : !b);
    }

    @BeforeTemplate
    boolean before(long a, long b) {
      return !(a != b);
    }

    @BeforeTemplate
    boolean before(double a, double b) {
      return !(a != b);
    }

    @BeforeTemplate
    boolean before(Object a, Object b) {
      return !(a != b);
    }

    @AfterTemplate
    boolean after(boolean a, boolean b) {
      return a == b;
    }
  }
}