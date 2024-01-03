# PayPal Importer
[![GitHub release](https://img.shields.io/github/v/release/my-flow/paypalimporter)](https://github.com/my-flow/paypalimporter/releases/latest)

PayPal Importer is a [Moneydance](http://www.moneydance.com) extension that
imports PayPal transactions into Moneydance. Please see the
[project page](https://www.my-flow.com/paypalimporter/) for more information
on where to download and how to install the extension.

## Build Prerequisites
Java Development Kit, version 17

## Building the extension
1. `git clone git@github.com:my-flow/paypalimporter.git` creates a copy of the
repository.
2. `./gradlew assemble` produces the distributable.

## Signing the extension
1. `./gradlew genKeys` generates a passphrase-protected key pair.
2. `./gradlew sign` signs the extension.

## Running the extension
After the build is finished, the generated MXT file located in the 
`core/build/distributions/` directory can be added to Moneydance.

## Project Structure
The project consists of 2 gradle sub-projects:
- The **core** sub-project contains the audited source code.
- The **support** sub-project contains the test infrastructure (stubs and
mock-ups) and the test cases. It depends on the core project.

## Documentation
* [Changelog](CHANGELOG.md)
* [SonarCloud](https://sonarcloud.io/project/overview?id=my-flow_paypalimporter)

## License
Copyright (C) [Florian J. Breunig](http://www.my-flow.com).
All rights reserved.
