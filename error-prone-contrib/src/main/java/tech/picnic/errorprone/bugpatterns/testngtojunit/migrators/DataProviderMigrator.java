package tech.picnic.errorprone.bugpatterns.testngtojunit.migrators;

import static com.sun.source.tree.Tree.Kind.NEW_ARRAY;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

import com.google.common.collect.ImmutableList;
import com.google.errorprone.VisitorState;
import com.google.errorprone.fixes.SuggestedFix;
import com.google.errorprone.util.ASTHelpers;
import com.google.errorprone.util.ErrorProneToken;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.NewArrayTree;
import com.sun.source.tree.ReturnTree;
import com.sun.tools.javac.parser.Tokens;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import tech.picnic.errorprone.bugpatterns.testngtojunit.Migrator;
import tech.picnic.errorprone.bugpatterns.util.SourceCode;

public class DataProviderMigrator implements Migrator<Void> {
  @Override
  public Optional<SuggestedFix> createFix(
      ClassTree classTree, MethodTree methodTree, Void dataValue, VisitorState state) {

    return migrateDataProvider(methodTree, classTree, state);
  }

  public boolean canFix(MethodTree methodTree) {
    Optional<ReturnTree> returnTree = getReturnTree(methodTree);
    return returnTree.isPresent()
        && returnTree.flatMap(DataProviderMigrator::getDataProviderReturnTree).isPresent();
  }

  private static Optional<SuggestedFix> migrateDataProvider(
      MethodTree methodTree, ClassTree classTree, VisitorState state) {
    Optional<ReturnTree> returnTree = getReturnTree(methodTree);
    if (returnTree.isEmpty()) {
      return Optional.empty();
    }

    return getDataProviderReturnTree(returnTree.orElseThrow())
        .map(
            dataProviderReturnTree ->
                SuggestedFix.builder()
                    .addStaticImport("org.junit.jupiter.params.provider.Arguments.arguments")
                    .addImport("java.util.stream.Stream")
                    .addImport("org.junit.jupiter.params.provider.Arguments")
                    .removeImport("org.testng.annotations.DataProvider")
                    .merge(SuggestedFix.delete(methodTree))
                    .merge(
                        SuggestedFix.postfixWith(
                            methodTree,
                            buildMethodSource(
                                classTree.getSimpleName().toString(),
                                methodTree.getName().toString(),
                                methodTree,
                                returnTree.orElseThrow(),
                                dataProviderReturnTree,
                                state)))
                    .build());
  }

  private static Optional<ReturnTree> getReturnTree(MethodTree methodTree) {
    return methodTree.getBody().getStatements().stream()
        .filter(ReturnTree.class::isInstance)
        .findFirst()
        .map(ReturnTree.class::cast);
  }

  private static Optional<NewArrayTree> getDataProviderReturnTree(ReturnTree returnTree) {
    if (returnTree.getExpression().getKind() != NEW_ARRAY
        || ((NewArrayTree) returnTree.getExpression()).getInitializers().isEmpty()) {
      return Optional.empty();
    }

    return Optional.of((NewArrayTree) returnTree.getExpression());
  }

  private static String buildMethodSource(
      String className,
      String name,
      MethodTree methodTree,
      ReturnTree returnTree,
      NewArrayTree newArrayTree,
      VisitorState state) {
    StringBuilder sourceBuilder =
        new StringBuilder()
            .append("  private static Stream<Arguments> ")
            .append(name)
            .append(" () ");

    if (!methodTree.getThrows().isEmpty()) {
      sourceBuilder
          .append(" throws ")
          .append(
              methodTree.getThrows().stream()
                  .filter(IdentifierTree.class::isInstance)
                  .map(IdentifierTree.class::cast)
                  .map(identifierTree -> identifierTree.getName().toString())
                  .collect(joining(", ")));
    }

    return sourceBuilder
        .append(" {\n")
        .append(extractMethodBodyWithoutReturnStatement(methodTree, returnTree, state))
        .append(" return ")
        .append(buildArgumentStream(className, newArrayTree, state))
        .append(";\n}")
        .toString();
  }

  private static String extractMethodBodyWithoutReturnStatement(
      MethodTree methodTree, ReturnTree returnTree, VisitorState state) {
    String body = SourceCode.treeToString(methodTree.getBody(), state);
    return body.substring(2, body.indexOf(SourceCode.treeToString(returnTree, state)) - 1);
  }

  private static String buildArgumentStream(
      String className, NewArrayTree newArrayTree, VisitorState state) {
    StringBuilder argumentsBuilder = new StringBuilder();

    int startPos = ASTHelpers.getStartPosition(newArrayTree);
    int endPos = state.getEndPosition(newArrayTree);
    Map<Integer, List<Tokens.Comment>> comments =
        state.getOffsetTokens(startPos, endPos).stream()
            .collect(
                toMap(ErrorProneToken::pos, ErrorProneToken::comments, (a, b) -> b, HashMap::new));
    argumentsBuilder.append(
        newArrayTree.getInitializers().stream()
            .map(
                expression ->
                    buildArguments(
                        expression,
                        comments.getOrDefault(
                            ASTHelpers.getStartPosition(expression), ImmutableList.of()),
                        state))
            .collect(joining(",")));

    // This regex expression replaces all instances of "this.getClass()" or "getClass()"
    // with the fully qualified class name to retain functionality in static context.
    return String.format("Stream.of(%s\n  )", argumentsBuilder)
        .replaceAll("((?<!\\b\\.)|(\\bthis\\.))(getClass\\(\\))", className + ".class");
  }

  private static String buildArguments(
      ExpressionTree expressionTree, List<Tokens.Comment> comments, VisitorState state) {
    if (expressionTree.getKind() == NEW_ARRAY) {
      return buildArgumentsFromArray(((NewArrayTree) expressionTree), comments, state);
    } else {
      return buildArgumentsFromExpression(expressionTree, comments, state);
    }
  }

  private static String buildArgumentsFromExpression(
      ExpressionTree expressionTree, List<Tokens.Comment> comments, VisitorState state) {
    return String.format(
        "\t\t%s\n\t\targuments(%s)",
        comments.stream().map(Tokens.Comment::getText).collect(joining("\n")),
        SourceCode.treeToString(expressionTree, state));
  }

  private static String buildArgumentsFromArray(
      NewArrayTree argumentArray, List<Tokens.Comment> comments, VisitorState state) {
    String argSource = SourceCode.treeToString(argumentArray, state);
    return String.format(
        "\t\t%s\n\t\targuments(%s)",
        comments.stream().map(Tokens.Comment::getText).collect(joining("\n")),
        argSource.substring(1, argSource.length() - 1));
  }
}