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
- Java 9 compatibility (release = 9)
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
- Use dependency injection (Dagger) for all components

When committing changes, use the project's standards for commit messages and follow code quality guidelines enforced by Checkstyle, PMD, and SpotBugs.
