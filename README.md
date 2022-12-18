# PayPal Importer
[![GitHub release](https://img.shields.io/github/v/release/my-flow/paypalimporter)](https://github.com/my-flow/paypalimporter/releases/latest) [![Build Status](https://img.shields.io/travis/my-flow/paypalimporter/develop)](https://app.travis-ci.com/github/my-flow/paypalimporter) [![Quality Gate Status](https://img.shields.io/sonar/tech_debt/paypalimporter:core?server=https%3A%2F%2Fsonarcloud.io)](https://sonarcloud.io/component_measures?id=paypalimporter%3Acore&metric=sqale_debt_ratio)

PayPal Importer for Moneydance is an extension for
[Moneydance](http://www.moneydance.com) that imports transactions from PayPal
into Moneydance. For more information on where to download and how to install
the extension please visit the
[project page](https://www.my-flow.com/paypalimporter/).

## Build Prerequisites
Java Development Kit, version 11

## Building the extension
1. `git clone git@github.com:my-flow/paypalimporter.git` creates a copy of the
repository.
2. `./gradlew assemble` produces the distributable.

## Signing the extension
1. `./gradlew genKeys` generates a passphrase-protected key pair.
2. `./gradlew sign` signs the extension.

## Running the extension
After the build process has succeeded, the resulting extension file
`core/build/distributions/paypalimporter-v9.mxt` can be added to Moneydance.

## Project Structure
The project consists of 2 gradle sub-projects:
- The **core** sub-project contains the source code which must be audited.
- The **support** sub-project contains the test infrastructure (stubs and
mock-ups) and the test cases. It depends on the core project.

## Documentation
* [Changelog](CHANGELOG.md)
* [SonarCloud](https://sonarcloud.io/organizations/paypalimporter/)

## License
Copyright (C) [Florian J. Breunig](http://www.my-flow.com).
All rights reserved.
