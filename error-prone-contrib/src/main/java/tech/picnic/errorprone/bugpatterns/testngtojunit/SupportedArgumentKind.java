package tech.picnic.errorprone.bugpatterns.testngtojunit;

import static java.util.Arrays.stream;

import java.util.Optional;
import tech.picnic.errorprone.bugpatterns.testngtojunit.migrators.argument.DataProviderArgumentMigrator;
import tech.picnic.errorprone.bugpatterns.testngtojunit.migrators.argument.DescriptionArgumentMigrator;
import tech.picnic.errorprone.bugpatterns.testngtojunit.migrators.argument.ExpectedExceptionsArgumentMigrator;
import tech.picnic.errorprone.bugpatterns.testngtojunit.migrators.argument.PriorityArgumentMigrator;

/** The annotation argument kinds that are supported by the TestNG -> JUnit migration. */
enum SupportedArgumentKind {
  PRIORITY("priority", new PriorityArgumentMigrator()),
  DESCRIPTION("description", new DescriptionArgumentMigrator()),
  DATAPROVIDER("dataProvider", new DataProviderArgumentMigrator()),
  EXPECTED_EXCEPTIONS("expectedExceptions", new ExpectedExceptionsArgumentMigrator());

  private final String name;

  private final ArgumentMigrator argumentMigrator;

  SupportedArgumentKind(String name, ArgumentMigrator argumentMigrator) {
    this.name = name;
    this.argumentMigrator = argumentMigrator;
  }

  ArgumentMigrator getArgumentMigrator() {
    return argumentMigrator;
  }

  static Optional<SupportedArgumentKind> fromString(String argument) {
    return stream(values()).filter(value -> value.name.equals(argument)).findFirst();
  }
}
