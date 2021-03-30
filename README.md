# micronaut-camunda-external-client

This open source project allows you to easily integrate [Camunda](https://camunda.com/products/bpmn-engine) 's [External Task Clients](https://docs.camunda.org/manual/latest/user-guide/process-engine/external-tasks/) into [Micronaut](https://micronaut.io) projects.

Micronaut is known for its efficient use of resources. With this integration you can easily implement an external client which to process external tasks. If you use GraalVM you have startup times of about 20 to 30ms!

The integration is preconfigured with sensible defaults, so that you can get started with minimal configuration: simply add a dependency in your Micronaut project!

If you also want to run the Camunda Workflow Engine on Micronaut then have a look at the open source project [micronaut-camunda-bpm](https://github.com/NovatecConsulting/micronaut-camunda-bpm).

---
_We're not aware of all installations of our Open Source project. However, we love_
* _listening to your feedback,_
* _discussing possible use cases with you,_
* _aligning the roadmap to your needs!_

_📨  Please [contact](#contact) us!_

---

Do you want to try it out? Please jump to the [Getting Started](#getting-started) section.

Do you want to contribute to our open source project? Please read the [Contribution Guidelines](CONTRIBUTING.md) and [contact us](#contact).

Micronaut + Camunda = :heart:

[![Release](https://img.shields.io/github/v/release/NovatecConsulting/micronaut-camunda-external-client.svg)](https://github.com/NovatecConsulting/micronaut-camunda-external-client/releases)
[![License](https://img.shields.io/:license-apache-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Continuous Integration](https://github.com/NovatecConsulting/micronaut-camunda-external-client/workflows/Continuous%20Integration/badge.svg)](https://github.com/NovatecConsulting/micronaut-camunda-external-client/actions)
[![GitHub Discussions](https://img.shields.io/badge/Forum-GitHub_Discussions-blue)](https://github.com/NovatecConsulting/micronaut-camunda-external-client/discussions)

# Table of Contents

* [✨ Features](#features)
* [🚀 Getting Started](#getting-started)
  * [Supported JDKs](#supported-jdks)
  * [Dependency Management](#dependency-management)
  * [Configuration](#configuration)
* [🏆 Advanced Topics](#advanced-topics)
* [📚 Releases](#releases)
* [📨 Contact](#contact)

# ✨Features
* TODO

# 🚀Getting Started

This section describes what needs to be done to use `micronaut-camunda-external-client-feature` in a Micronaut project.

Here are some example applications:
* TODO

## Supported JDKs

We officially support the following JDKs:
* JDK 8 (LTS)
* JDK 11 (LTS)
* JDK 15 (latest version supported by Micronaut)

## Dependency Management

The Camunda integration works with both Gradle and Maven, but we recommend using Gradle because it has better Micronaut Support.

<details>
<summary>Click to show Gradle configuration</summary>

1. Optional: Create an empty Micronaut project using [Micronaut Launch](https://launch.micronaut.io) or alternatively with the CLI: `mn create-app my-example`.
2. Add the dependency to the build.gradle:
```groovy
implementation("info.novatec:micronaut-camunda-external-client-feature:0.1.0")
```
</details>

<details>
<summary>Click to show Maven configuration</summary>

1. Optional: Create an empty Micronaut using [Micronaut Launch](https://launch.micronaut.io) or alternatively with the CLI:  `mn create-app my-example --build=maven`.
2. Add the dependency to the pom.xml:
```xml
<dependency>
  <groupId>info.novatec</groupId>
  <artifactId>micronaut-camunda-external-client-feature</artifactId>
  <version>0.1.0</version>
</dependency>
```
</details>

Note: The module `micronaut-camunda-external-client-feature` includes the dependency `org.camunda.bpm:camunda-external-task-client` which will be resolved transitively.

## Configuration

TODO

# 🏆Advanced Topics

TODO

# 📚Releases

The list of [releases](/releases) contains a detailed changelog.

We use [Semantic Versioning](https://semver.org/) which does allow incompatible changes before release 1.0.0 but we try to minimize them.

The following compatibility matrix shows the officially supported Micronaut and Camunda versions for each release.
Other combinations might also work but have not been tested. It will probably work with Camunda 7.9.0 and newer.

| Release |Micronaut | Camunda |
|--------|-------|--------|
| 0.1.0 | 2.4.1 | 7.14.0 |

<details>
<summary>Click to see older releases</summary>

| Release |Micronaut | Camunda |
|--------|-------|--------|
</details>

Download of Releases:
* [GitHub Artifacts](https://github.com/NovatecConsulting/micronaut-camunda-external-client/releases)
* [Maven Central Artifacts](https://search.maven.org/artifact/info.novatec/micronaut-camunda-external-client-feature)

# 📨Contact

This open source project is being developed by [Novatec Consulting GmbH](https://www.novatec-gmbh.de/en/) with the support of the open source community.

If you have any questions or ideas feel free to create an [issue](https://github.com/NovatecConsulting/micronaut-camunda-external-client/issues) or contact us via GitHub Discussions or mail.

We love listening to your feedback. And of course also discussing the project roadmap and possible use cases with you!

You can reach us:
* [GitHub Discussions](https://github.com/NovatecConsulting/micronaut-camunda-external-client/discussions)
* [mailto:micronaut-camunda@novatec-gmbh.de](mailto:micronaut-camunda@novatec-gmbh.de)
