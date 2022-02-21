# Kotlin Annotation Processor Demo

[![Build Status](https://travis-ci.org/toolisticon/aptk-kotlin-demo.svg?branch=master)](https://travis-ci.org/toolisticon/aptk-kotlin-demo)
[![codecov](https://codecov.io/gh/toolisticon/aptk-kotlin-demo/branch/master/graph/badge.svg)](https://codecov.io/gh/toolisticon/aptk-kotlin-demo)

# About

This demo shows how to build annotation processors in kotlin using the toolisticon annotation processor stack

The stack contains:

- [APTK](https://github.com/toolisticon/aptk): The annotation processor toolkit is a library that helps you to develop annotation processors.
- [CUTE](https://github.com/toolisticon/cute): A compile time unit testing framework for testing annotation processors by compiling Java test source files or by supporting you to develop unit tests for you annotation processor related code. (Will soon also support Kotlin files)

The demo is the kotlin port of [SPIAP](https://github.com/toolisticon/SPI-Annotation-Processor) project written in Java, just to demonstrate how the same processor could look like in kotlin.

One remark: The Kotlin code isn't perfect since I have just started to learn Kotlin. It will be updated from  time to time.

Not the whole project has been moved to kotlin yet. This will be done in the near future
# Contributing

We welcome any kind of suggestions and pull requests.

## Building the demo

The SPI-Annotation-Processor is built using Maven (at least version 3.0.0).
A simple import of the pom in your IDE should get you up and running. 
To build the demo on the commandline, just run `./mvnw` or `./mvnw clean install`

## Requirements

The likelihood of a pull request being used rises with the following properties:

- You have used a feature branch.
- You have included a test that demonstrates the functionality added or fixed.
- You adhered to the [code conventions](http://www.oracle.com/technetwork/java/javase/documentation/codeconvtoc-136057.html).

## Contributions

- (2022) Tobias Stamann (Holisticon AG)

# License

This project is released under the revised [MIT License](LICENSE).

This project includes and repackages the [Annotation-Processor-Toolkit](https://github.com/holisticon/annotation-processor-toolkit) released under the  [MIT License](/3rdPartyLicenses/annotation-processor-toolkit/LICENSE.txt).
