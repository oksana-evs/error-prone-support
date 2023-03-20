package tech.picnic.errorprone.testngjunit;

import static com.google.errorprone.BugPattern.SeverityLevel.ERROR;

import com.google.errorprone.BugPattern;
import com.google.errorprone.CompilationTestHelper;
import com.google.errorprone.VisitorState;
import com.google.errorprone.bugpatterns.BugChecker;
import com.google.errorprone.bugpatterns.BugChecker.AnnotationTreeMatcher;
import com.google.errorprone.bugpatterns.BugChecker.MethodTreeMatcher;
import com.google.errorprone.matchers.Description;
import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.MethodTree;
import org.junit.jupiter.api.Test;

final class TestNGMatchersTest {
  @Test
  void matches() {
    CompilationTestHelper.newInstance(TestNGMatchersTestChecker.class, getClass())
        .addSourceLines(
            "A.java",
            "import org.testng.annotations.DataProvider;",
            "import org.testng.annotations.Test;",
            "",
            "// BUG: Diagnostic contains: TestNG annotation",
            "@Test",
            "class A {",
            "  // BUG: Diagnostic contains: TestNG annotation",
            "  @Test",
            "  void basic() {}",
            "",
            "  // BUG: Diagnostic contains: TestNG annotation",
            "  @Test(dataProvider = \"dataProviderTestCases\")",
            "  void withDataProvider() {}",
            "",
            "  @DataProvider",
            "  // BUG: Diagnostic contains: TestNG value factory method",
            "  private static Object[][] dataProviderTestCases() {",
            "    return new Object[][] {};",
            "  }",
            "",
            "  @org.junit.jupiter.api.Test",
            "  void junitTest() {}",
            "}")
        .doTest();
  }

  /**
   * A {@link com.google.errorprone.BugPattern} used to report TestNG annotations as errors for
   * testing purposes.
   */
  @BugPattern(summary = "Interacts with `TestNGMatchers` for testing purposes", severity = ERROR)
  public static final class TestNGMatchersTestChecker extends BugChecker
      implements MethodTreeMatcher, AnnotationTreeMatcher {
    private static final long serialVersionUID = 1L;

    @Override
    public Description matchAnnotation(AnnotationTree annotationTree, VisitorState visitorState) {
      return TestNGMatchers.TESTNG_TEST_ANNOTATION.matches(annotationTree, visitorState)
          ? buildDescription(annotationTree).setMessage("TestNG annotation").build()
          : Description.NO_MATCH;
    }

    @Override
    public Description matchMethod(MethodTree methodTree, VisitorState visitorState) {
      return TestNGMatchers.TESTNG_VALUE_FACTORY_METHOD.matches(methodTree, visitorState)
          ? buildDescription(methodTree).setMessage("TestNG value factory method").build()
          : Description.NO_MATCH;
    }
  }
}
