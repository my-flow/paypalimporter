# PayPal Importer [![Build Status](https://travis-ci.org/my-flow/paypalimporter.svg?branch=master)](https://travis-ci.org/my-flow/paypalimporter)

PayPal Importer for Moneydance is an extension for [Moneydance®]
(http://www.moneydance.com) that imports transactions from PayPal into 
Moneydance. For more information on where to download and how to install the 
extension please visit the [project page]
(http://my-flow.github.io/paypalimporter/).

## Build Prerequisites
* Java Development Kit, version 6
* [Apache Ant™](http://ant.apache.org), version 1.7 or newer

## Building the extension
1. `git clone git@github.com:my-flow/paypalimporter.git` creates a copy of the 
repository.
2. `ant all` compiles the extension (and signs it if an applicable key 
pair is found).

## Signing the extension
1. `ant genkeys` generates a passphrase-protected key pair.
2. `ant sign` signs the extension.

## Running the extension
After the build process has succeeded, the resulting extension file 
`dist/paypalimporter.mxt` can be added to Moneydance®.

## Documentation
* [PayPal Importer API](http://my-flow.github.io/paypalimporter/docs/api/)
* [CheckStyle Audit]
(http://my-flow.github.io/paypalimporter/docs/checkstyle-report/)
* [JMockit Coverage Report]
(http://my-flow.github.io/paypalimporter/docs/coverage-report/)
* [PMD report]
(http://my-flow.github.io/paypalimporter/docs/pmd-report/pmd-report.html)

## License
Copyright (C) 2013-2014 [Florian J. Breunig](http://www.my-flow.com).
All rights reserved.
