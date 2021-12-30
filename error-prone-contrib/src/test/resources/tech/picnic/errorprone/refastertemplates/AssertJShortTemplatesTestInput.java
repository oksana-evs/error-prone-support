package tech.picnic.errorprone.refastertemplates;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;
import static org.assertj.core.data.Percentage.withPercentage;

import com.google.common.collect.ImmutableSet;
import org.assertj.core.api.AbstractShortAssert;
import tech.picnic.errorprone.annotations.Template;
import tech.picnic.errorprone.annotations.TemplateCollection;
import tech.picnic.errorprone.refastertemplates.AssertJShortTemplates.AbstractShortAssertIsEqualTo;
import tech.picnic.errorprone.refastertemplates.AssertJShortTemplates.AbstractShortAssertIsNotEqualTo;
import tech.picnic.errorprone.refastertemplates.AssertJShortTemplates.AbstractShortAssertIsNotZero;
import tech.picnic.errorprone.refastertemplates.AssertJShortTemplates.AbstractShortAssertIsOne;
import tech.picnic.errorprone.refastertemplates.AssertJShortTemplates.AbstractShortAssertIsZero;

@TemplateCollection(AssertJShortTemplates.class)
final class AssertJShortTemplatesTest implements RefasterTemplateTestCase {
  @Override
  public ImmutableSet<?> elidedTypesAndStaticImports() {
    return ImmutableSet.of(offset(0), withPercentage(0));
  }

  @Template(AbstractShortAssertIsEqualTo.class)
  ImmutableSet<AbstractShortAssert<?>> testAbstractShortAssertIsEqualTo() {
    return ImmutableSet.of(
        assertThat((short) 0).isCloseTo((short) 1, offset((short) 0)),
        assertThat((short) 0).isCloseTo(Short.valueOf((short) 1), offset((short) 0)),
        assertThat((short) 0).isCloseTo((short) 1, withPercentage(0)),
        assertThat((short) 0).isCloseTo(Short.valueOf((short) 1), withPercentage(0)),
        assertThat((short) 0).isEqualTo(Short.valueOf((short) 1)));
  }

  @Template(AbstractShortAssertIsNotEqualTo.class)
  ImmutableSet<AbstractShortAssert<?>> testAbstractShortAssertIsNotEqualTo() {
    return ImmutableSet.of(
        assertThat((short) 0).isNotCloseTo((short) 1, offset((short) 0)),
        assertThat((short) 0).isNotCloseTo(Short.valueOf((short) 1), offset((short) 0)),
        assertThat((short) 0).isNotCloseTo((short) 1, withPercentage(0)),
        assertThat((short) 0).isNotCloseTo(Short.valueOf((short) 1), withPercentage(0)),
        assertThat((short) 0).isNotEqualTo(Short.valueOf((short) 1)));
  }

  @Template(AbstractShortAssertIsZero.class)
  AbstractShortAssert<?> testAbstractShortAssertIsZero() {
    return assertThat((short) 0).isZero();
  }

  @Template(AbstractShortAssertIsNotZero.class)
  AbstractShortAssert<?> testAbstractShortAssertIsNotZero() {
    return assertThat((short) 0).isNotZero();
  }

  @Template(AbstractShortAssertIsOne.class)
  AbstractShortAssert<?> testAbstractShortAssertIsOne() {
    return assertThat((short) 0).isOne();
  }
}