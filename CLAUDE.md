# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test Commands
- Build project: `./gradlew clean assemble`
- Run all tests: `./gradlew clean check test assemble`
- Generate code coverage: `./gradlew jacocoTestReport`
- Check code style: `./gradlew checkstyleMain`
- Check for bugs: `./gradlew spotbugsMain`
- Run static analysis: `./gradlew pmdMain`

## Code Style Guidelines
- Java 11 compatibility (release = 11)
- Line length: max 120 characters
- Use explicit imports (no star imports)
- Import order: com.*, java.*, javax.*, other
- Whitespace: 4-space indentation, no tabs
- Javadoc required for all packages, classes and public methods
- Naming: camelCase for methods/variables, PascalCase for classes
- Always use braces for control structures
- Final parameters and immutable objects preferred
- Use proper exception handling with specific exceptions
- Follow Design for Extension principles
- Implement interfaces rather than relying on concrete implementations
- Use immutability where appropriate

## Checkstyle Rules
- NoFinalizer: Do not use finalize methods
- SuperFinalize: Always call super.finalize() in finalize methods if needed
- DeclarationOrder: Maintain proper order of class members:
  - Static variables
  - Instance variables
  - Constructors
  - Methods
- HiddenField: Avoid parameter names that hide instance fields
- NewlineAtEndOfFile: All files must end with a newline
- ImportOrder: Follow required import order (com.*, java.*, javax.*, other)
- NoTrailingWhitespace: Remove all trailing whitespace

## PMD Rules
- EmptyFinalizer: Avoid empty finalizer methods
- ExceptionSoftening: Avoid converting checked exceptions to unchecked exceptions without constraints
- AvoidThrowingRawExceptionTypes: Use specific exception types
- ConstructorThrows: Be careful with exceptions in constructors to prevent finalizer attacks
- ImmutableField: Prefer final fields where possible

## SpotBugs Rules
- ME_ENUM_FIELD_SETTER: Avoid unconditionally setting enum fields in public methods
- MS_EXPOSE_REP: Avoid exposing internal representation by returning mutable objects
- EXS_EXCEPTION_SOFTENING_NO_CONSTRAINTS: Don't convert checked exceptions to unchecked without constraints
- CT_CONSTRUCTOR_THROW: Avoid throwing exceptions in constructors
- OCP_OVERLY_CONCRETE_PARAMETER: Use interface types rather than concrete implementations for parameters

When committing changes, use the project's standards for commit messages and follow code quality guidelines enforced by Checkstyle, PMD, and SpotBugs.
