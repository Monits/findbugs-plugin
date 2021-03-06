# Findbugs Plugin
Findbugs plugin set from Monits. Removing bugs before they happen by enforcing best practices.

[![Build Status](https://secure.travis-ci.org/Monits/findbugs-plugin.svg)](http://travis-ci.org/Monits/findbugs-plugin)
[![Coverage Status](https://coveralls.io/repos/Monits/findbugs-plugin/badge.svg)](https://coveralls.io/r/Monits/findbugs-plugin)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.monits/findbugs-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.monits/findbugs-plugin)
[![Download](https://api.bintray.com/packages/monits/monits-android/findbugs-plugin/images/download.svg) ](https://bintray.com/monits/monits-android/findbugs-plugin/_latestVersion)

# How to use with Maven

To use this plugin, please configure your findbugs-maven-plugin like below.

```xml
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>findbugs-maven-plugin</artifactId>
        <version>3.0.1</version>
        <configuration>
          <plugins>
            <plugin>
              <groupId>com.monits</groupId>
              <artifactId>findbugs-plugin</artifactId>
              <version>0.2.0</version>
            </plugin>
          </plugins>
        </configuration>
      </plugin>
```

We are both on **Central Repository** (formerly *Maven Central*) and **jcenter**.

# History

## 0.2.0
New detectors:
- added `InconsistentHashCodeEqualsDetector`: this new detector will check for
classes that uses distinct fields in the calculation of `hashCode` and `equals`.
A class with this bug breaks the "equal objects must have equal hashcodes" invariant
and/or may also generate hash collisions on objects that are unequal.
- Effective Java's item 10. `toString` should be overridden when a
class has inner state. The check will make sure if members have themselves any
state / are primitives to discard meaningless reports
(think of a Service with a reference to a DAO).
- Effective Java's item 8. Never override `equals` if defined by a super-class
other than `Object`. Doing so breaks the general `equals` contract by breaking
*symmetry*.
- added `UselessStringValueOfCallDetector` to detect useless `String.valueOf` calls
when the argument given is already a string.
- added `NonStaticPatternCompileDetector` to report methods that have locals `Pattern.compile`
with a harcoded, static or final local regex as a static final to avoid recompiling the regex.

## 0.1.1
- forked from [WorksApplication's original plugin](WorksApplications/findbugs-plugin).
Awesome plugin, but Findbugs 2 only.
- upgraded to Findbugs 3
- rewrote most error messages to be more specific
- fixed method detection for `UnknownNullnessDetector`
- rewrote all unit tests, and added several new ones. Great code coverage
- made `UnexpectedAccessDetector` ignore calls from classes that use JUnit's
annotations or extends `junit.framework.TestCase`, which being tests are
legit accesses.

## 0.0.3

- added `UnexpectedAccessDetector`
- added `UndocumentedSuppressFBWarningsDetector`
- upgraded JDK from 1.6 to 1.7

## 0.0.2

- added `BrokenImmutableClassDetector`
- added `LongIndexNameDetector`
- added `LongTableNameDetector`
- added `LongColumnNameDetector`
- added `UnknownNullnessDetector`
- added `UndocumentedIgnoreDetector`
- added `ImplicitLengthDetector`
- added `ImplicitNullnessDetector`
- added `ColumnDefinitionDetector`
- added `NullablePrimitiveDetector`

## 0.0.1

- First release

# Copyright and License

    Copyright 2015 Monits S.A.
    Copyright 2013 Works Applications. Co.,Ltd.
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
