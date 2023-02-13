package tech.picnic.errorprone.bugpatterns.testngtojunit.migrators.argument;

import com.google.errorprone.VisitorState;
import com.google.errorprone.annotations.Immutable;
import com.google.errorprone.fixes.SuggestedFix;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodTree;
import java.util.Optional;
import org.testng.annotations.Test;
import tech.picnic.errorprone.bugpatterns.testngtojunit.ArgumentMigrator;
import tech.picnic.errorprone.bugpatterns.testngtojunit.Migrator;
import tech.picnic.errorprone.bugpatterns.testngtojunit.TestNGMetadata;
import tech.picnic.errorprone.bugpatterns.util.SourceCode;

/** An {@link Migrator} that migrates the {@link Test#description()} argument. */
@Immutable
public class DescriptionArgumentMigrator implements ArgumentMigrator {
  @Override
  public Optional<SuggestedFix> createFix(
      ClassTree classTree, MethodTree methodTree, ExpressionTree dataValue, VisitorState state) {
    return Optional.of(
        SuggestedFix.builder()
            .addImport("org.junit.jupiter.api.DisplayName")
            .merge(
                SuggestedFix.prefixWith(
                    methodTree,
                    String.format("@DisplayName(%s)\n", SourceCode.treeToString(dataValue, state))))
            .build());
  }

  @Override
  public boolean canFix(
      TestNGMetadata metadata,
      TestNGMetadata.AnnotationMetadata annotation,
      MethodTree methodTree,
      VisitorState state) {
    return annotation.getArguments().containsKey("description");
  }
}