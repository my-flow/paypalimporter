# PayPal Importer [![Build Status](https://travis-ci.org/my-flow/paypalimporter.svg?branch=master)](https://travis-ci.org/my-flow/paypalimporter)

PayPal Importer for Moneydance is an extension for
[Moneydance](http://www.moneydance.com) that imports transactions from PayPal
into Moneydance. For more information on where to download and how to install
the extension please visit the
[project page](http://my-flow.github.io/paypalimporter/).

## Build Prerequisites
Java Development Kit, version 7

## Building the extension
1. `git clone git@github.com:my-flow/paypalimporter.git` creates a copy of the 
repository.
2. `./gradlew assemble` produces the distributable.

## Signing the extension
1. `./gradlew genKeys` generates a passphrase-protected key pair.
2. `./gradlew sign` signs the extension.

## Running the extension
After the build process has succeeded, the resulting extension file 
`core/build/distributions/paypalimporter.mxt` can be added to Moneydance.

## Project Structure
The project consists of 2 gradle sub-projects:
- The **core** sub-project contains the source code which must be audited.
- The **support** sub-project contains the test infrastructure (stubs and
mock-ups) and the test cases. It depends from the core project.

## Documentation
* [PayPal Importer API](http://my-flow.github.io/paypalimporter/docs/api/)
* [JMockit Coverage Report](http://my-flow.github.io/paypalimporter/docs/coverage-report/)

## License
Copyright (C) 2013-2016 [Florian J. Breunig](http://www.my-flow.com).
All rights reserved.
