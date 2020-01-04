# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com),
and this project does *not* adhere to Semantic Versioning.

## [v8] - 2019-10-07
### Added
- Support for dark mode.
- [JaCoCo Java Code Coverage Library](https://www.jacoco.org/jacoco/).
- This changelog file.

### Changed
- Introduce [Dagger](https://dagger.dev) for dependency injection.
- Replace [FindBugs](http://findbugs.sourceforge.net) by [SpotBugs](https://spotbugs.github.io).

### Fixed
- Crash in Moneydance 2019 when importing into new account.
- Remember designated account ([#13]).


## [v7] - 2018-05-12
### Added
- Compile-time data to manifest file.

### Removed
- Google Analytics tracker.


## [v6] - 2016-06-06
### Changed
- Show wizard immediately after installation of extension.
- Build automation system from Apache Ant to Gradle.
- Replace Google Analytics tracking component.

### Fixed
- Creation of duplicate transactions ([#8]).
- Remember API username and signature in Moneydance 2015.7 ([#9]).


## [v5] - 2015-02-18
### Added
- Continuous integration testing on [Travis CI](https://travis-ci.org/my-flow/paypalimporter).
- Prerequisite: Moneydance 2015 or later.
- Prerequisite: Java 7 or later versions.

### Removed
- Compatibility with Moneydance 2014 and earlier versions.
- Compatibility with Java 6 and earlier versions.


## [v4] - 2014-11-14
### Added
- Tracking of unexpected exceptions.
- Human-readable error message for login failures.


## [v3] - 2013-12-13
### Added
- Support for proxy settings.


## [v2] - 2013-10-11
### Added
- Support for downloading an unlimited amount of PayPal transactions ([#4]).
- Temporary caching of API password.
- Compatibility with Moneydance 2008 and Moneydance 2010.

### Changed
- Warning message if PayPal account has multiple currencies ([#2]).

### Fixed
- Issue where password would be stored even if the checkbox was not checked.


## [v1] - 2013-08-03
### Added
- First version based on [Apache Ant](https://ant.apache.org)’s build automation system.
- Compatibility with Moneydance 2007.
- Compatibility with Java 1.3 and later versions.


[#13]: https://github.com/my-flow/paypalimporter/issues/13
[#9]: https://github.com/my-flow/paypalimporter/issues/9
[#8]: https://github.com/my-flow/paypalimporter/issues/8
[#4]: https://github.com/my-flow/paypalimporter/issues/4
[#2]: https://github.com/my-flow/paypalimporter/issues/2

[v8]: https://github.com/my-flow/paypalimporter/compare/v7...v8
[v7]: https://github.com/my-flow/paypalimporter/compare/v6...v7
[v6]: https://github.com/my-flow/paypalimporter/compare/v5...v6
[v5]: https://github.com/my-flow/paypalimporter/compare/v4...v5
[v4]: https://github.com/my-flow/paypalimporter/compare/v3...v4
[v3]: https://github.com/my-flow/paypalimporter/compare/v2...v3
[v2]: https://github.com/my-flow/paypalimporter/compare/v1...v2
[v1]: https://github.com/my-flow/paypalimporter/commits/v1
