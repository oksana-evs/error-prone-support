package tech.picnic.errorprone.refastertemplates;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableSet;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.assertj.core.api.NumberAssert;
import tech.picnic.errorprone.annotations.Template;
import tech.picnic.errorprone.annotations.TemplateCollection;
import tech.picnic.errorprone.refastertemplates.AssertJNumberTemplates.NumberAssertIsNegative;
import tech.picnic.errorprone.refastertemplates.AssertJNumberTemplates.NumberAssertIsNotNegative;
import tech.picnic.errorprone.refastertemplates.AssertJNumberTemplates.NumberAssertIsNotPositive;
import tech.picnic.errorprone.refastertemplates.AssertJNumberTemplates.NumberAssertIsPositive;

@TemplateCollection(AssertJNumberTemplates.class)
final class AssertJNumberTemplatesTest implements RefasterTemplateTestCase {
  @Template(NumberAssertIsPositive.class)
  ImmutableSet<NumberAssert<?, ?>> testNumberAssertIsPositive() {
    return ImmutableSet.of(
        assertThat((byte) 0).isPositive(),
        assertThat((byte) 0).isPositive(),
        assertThat((short) 0).isPositive(),
        assertThat((short) 0).isPositive(),
        assertThat(0).isPositive(),
        assertThat(0).isPositive(),
        assertThat(0L).isPositive(),
        assertThat(0L).isPositive(),
        assertThat(0.0F).isPositive(),
        assertThat(0.0).isPositive(),
        assertThat(BigInteger.ZERO).isPositive(),
        assertThat(BigInteger.ZERO).isPositive(),
        assertThat(BigDecimal.ZERO).isPositive());
  }

  @Template(NumberAssertIsNotPositive.class)
  ImmutableSet<NumberAssert<?, ?>> testNumberAssertIsNotPositive() {
    return ImmutableSet.of(
        assertThat((byte) 0).isNotPositive(),
        assertThat((byte) 0).isNotPositive(),
        assertThat((short) 0).isNotPositive(),
        assertThat((short) 0).isNotPositive(),
        assertThat(0).isNotPositive(),
        assertThat(0).isNotPositive(),
        assertThat(0L).isNotPositive(),
        assertThat(0L).isNotPositive(),
        assertThat(0.0F).isNotPositive(),
        assertThat(0.0).isNotPositive(),
        assertThat(BigInteger.ZERO).isNotPositive(),
        assertThat(BigInteger.ZERO).isNotPositive(),
        assertThat(BigDecimal.ZERO).isNotPositive());
  }

  @Template(NumberAssertIsNegative.class)
  ImmutableSet<NumberAssert<?, ?>> testNumberAssertIsNegative() {
    return ImmutableSet.of(
        assertThat((byte) 0).isNegative(),
        assertThat((byte) 0).isNegative(),
        assertThat((short) 0).isNegative(),
        assertThat((short) 0).isNegative(),
        assertThat(0).isNegative(),
        assertThat(0).isNegative(),
        assertThat(0L).isNegative(),
        assertThat(0L).isNegative(),
        assertThat(0.0F).isNegative(),
        assertThat(0.0).isNegative(),
        assertThat(BigInteger.ZERO).isNegative(),
        assertThat(BigInteger.ZERO).isNegative(),
        assertThat(BigDecimal.ZERO).isNegative());
  }

  @Template(NumberAssertIsNotNegative.class)
  ImmutableSet<NumberAssert<?, ?>> testNumberAssertIsNotNegative() {
    return ImmutableSet.of(
        assertThat((byte) 0).isNotNegative(),
        assertThat((byte) 0).isNotNegative(),
        assertThat((short) 0).isNotNegative(),
        assertThat((short) 0).isNotNegative(),
        assertThat(0).isNotNegative(),
        assertThat(0).isNotNegative(),
        assertThat(0L).isNotNegative(),
        assertThat(0L).isNotNegative(),
        assertThat(0.0F).isNotNegative(),
        assertThat(0.0).isNotNegative(),
        assertThat(BigInteger.ZERO).isNotNegative(),
        assertThat(BigInteger.ZERO).isNotNegative(),
        assertThat(BigDecimal.ZERO).isNotNegative());
  }
}