# Semver4k

**Semver4k** is a lightweight Kotlin Multiplatform library that helps you handling versions. It follows the rules of the [semantic versioning](http://semver.org) specification and provides several versioning modes: strict, NPM, CocoaPods...

This library is a fork of [semver4j](https://github.com/vdurmont/semver4j) completly rewritten in kotlin.

Supported Kotlin Multiplatform targets:
* jvm
* ios
* wasmJs
* (Additional targets can be added as needed)

## Installation

Add the dependency to your project:

#### Using maven

```xml
<dependency>
  <groupId>de.voize</groupId>
  <artifactId>semver4k</artifactId>
  <version>4.3.0</version>
</dependency>
```

#### Using gradle

```kotlin
implementation("de.voize:semver4k:4.3.0")
```

## Usage

### What is a version?

In Semver4k, a version looks like: `1.2.3-beta.4+sha899d8g79f87`.

- `1` is the major part (required)
- `2` is the minor part (required in strict mode)
- `3` is the patch part (required in strict mode)
- `beta` and `4` are the version suffixes (optional)
- `sha899d8g79f87` is the build information (optional)

### The `Semver` object

You can create a version by using one of the 2 constructors:

```kotlin
val sem1 = Semver("1.2.3-beta.4+sha899d8g79f87") // Defaults to STRICT mode
val sem2 = Semver("1.2.3-beta.4+sha899d8g79f87", SemverType.NPM) // Specify the mode
```

If the version is invalid, a `SemverException` will be thrown.  
You can access the different parts of the version using `getMajor()`, `getMinor()`, `getPatch()`, `getSuffixTokens()` or `getBuild()`.

| Type      | Mandatory           | Optional                    |
| --------- | ------------------- | --------------------------- |
| STRICT    | major, minor, patch | suffix, build               |
| LOOSE     | major               | minor, patch, suffix, build |
| NPM       | major               | minor, patch, suffix, build |
| COCOAPODS | major               | minor, patch, suffix, build |

### Is the version stable?

You can check if you're working with a stable version by using `Semver#isStable()`.

A version is stable if its major number is _strictly_ positive and it has no suffix.

Examples:

```kotlin
// TRUE
Semver("1.2.3").isStable()
Semver("1.2.3+sHa.0nSFGKjkjsdf").isStable()

// FALSE
Semver("0.1.2").isStable())
Semver("0.1.2+sHa.0nSFGKjkjsdf").isStable()
Semver("1.2.3-BETA.11+sHa.0nSFGKjkjsdf").isStable()
```

### Comparing the versions

- `isGreaterThan` returns true if the version is strictly greater than the other one.

```kotlin
val sem = Semver("1.2.3")
sem.isGreaterThan("1.2.2") // true
sem.isGreaterThan("1.2.4") // false
sem.isGreaterThan("1.2.3") // false
```

- `isLowerThan` returns true if the version is strictly lower than the other one.

```kotlin
val sem = Semver("1.2.3")
sem.isLowerThan("1.2.2") // false
sem.isLowerThan("1.2.4") // true
sem.isLowerThan("1.2.3") // false
```

- `isEqualTo` returns true if the versions are exactly the same.

```kotin
val sem = Semver("1.2.3+sha123456789")
sem.isEqualTo("1.2.3+sha123456789") // true
sem.isEqualTo("1.2.3+shaABCDEFGHI") // false
```

- `isEquivalentTo` returns true if the versions are the same (does not take the build information into account).

```kotlin
val sem = Semver("1.2.3+sha123456789")
sem.isEquivalentTo("1.2.3+sha123456789") // true
sem.isEquivalentTo("1.2.3+shaABCDEFGHI") // true
```

### Versions diffs

If you want to know what is the main difference between 2 versions, use the `diff` method. It will return a `VersionDiff` enum value among: `NONE`, `MAJOR`, `MINOR`, `PATCH`, `SUFFIX`, `BUILD`. It will always return the biggest difference.

```kotlin
val sem = Semver("1.2.3-beta.4+sha899d8g79f87")
sem.diff("1.2.3-beta.4+sha899d8g79f87") // NONE
sem.diff("2.3.4-alpha.5+sha32iddfu987") // MAJOR
sem.diff("1.3.4-alpha.5+sha32iddfu987") // MINOR
sem.diff("1.2.4-alpha.5+sha32iddfu987") // PATCH
sem.diff("1.2.3-alpha.5+sha32iddfu987") // SUFFIX
sem.diff("1.2.3-beta.4+sha32iddfu987") // BUILD
```

### Requirements

If you want to check if a version satisfies a requirement, use the `satisfies` method.

- In `STRICT` and `LOOSE` modes, the requirement can only be another version.
- In `NPM` mode, the requirement follows [NPM versioning rules](https://github.com/npm/node-semver).

```kotlin
// STRICT mode
val semStrict = Semver("1.2.3", SemverType.STRICT)
semStrict.satisfies("1.2.3") // true
semStrict.satisfies("1.2.2") // false
semStrict.satisfies("1.2.4") // false
semStrict.satisfies(">1.2.2") // SemverException, incompatible requirement for a STRICT mode

// NPM mode (those are just examples, check NPM documentation to see all the cases)
val semNPM =  Semver("1.2.3", SemverType.NPM)
semNPM.satisfies(">1.2.2") // true
semNPM.satisfies("1.1.1 || 1.2.3 - 2.0.0") // true
semNPM.satisfies("1.1.*") // false
semNPM.satisfies("~1.2.1") // true
semNPM.satisfies("^1.1.1") // true

// COCOAPODS mode (those are just examples, check CocoaPods documentation to see all the cases)
val semPOD =  Semver("1.2.3", SemverType.COCOAPODS)
semPOD.satisfies("> 1.2.2") // true
semPOD.satisfies("~> 1.2.1") // true
semPOD.satisfies("<= 1.1.1") // false

// IVY mode (those are just examples, check Ivy/gradle documentation to see all the cases)
val semIVY = Semver("1.2.3", SemverType.IVY)
semIVY.satisfies("1.2.+") // true
semIVY.satisfies("(,1.8.9]") // true
semIVY.satisfies("[0.2,1.4]") // true
```

### Modifying the version

The `Semver` object is immutable. However, it provides a set of methods that will help you create new versions:

- `withIncMajor()` and `withIncMajor(int increment)` returns a `Semver` object with the major part incremented
- `withIncMinor()` and `withIncMinor(int increment)` returns a `Semver` object with the minor part incremented
- `withIncPatch()` and `withIncPatch(int increment)` returns a `Semver` object with the patch part incremented
- `withClearedSuffix()` returns a `Semver` object with no suffix
- `withClearedBuild()` returns a `Semver` object with no build information

You can also use built-in versioning methods such as:

- `nextMajor()`: `1.2.3-beta.4+sha32iddfu987 => 2.0.0`
- `nextMinor()`: `1.2.3-beta.4+sha32iddfu987 => 1.3.0`
- `nextPatch()`: `1.2.3-beta.4+sha32iddfu987 => 1.2.4`
- `nextIncrement()`: `1.2.3-beta.4+sha32iddfu987 => 1.2.3-beta.5+sha32iddfu987`

## Contributing

Any pull request or bug report is welcome!  
If you have any suggestion about new features, you can open an issue.
