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

📨 _Please [contact](#contact) us!_

---

Do you want to try it out? Please jump to the [Getting Started](#getting-started) section.

Do you want to contribute to our open source project? Please read the [Contribution Guidelines](CONTRIBUTING.md) and [contact us](#contact).

Micronaut + Camunda = :heart:

[![Release](https://img.shields.io/github/v/release/NovatecConsulting/micronaut-camunda-external-client.svg)](https://github.com/NovatecConsulting/micronaut-camunda-external-client/releases)
[![License](https://img.shields.io/:license-apache-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Continuous Integration](https://github.com/NovatecConsulting/micronaut-camunda-external-client/workflows/Continuous%20Integration/badge.svg)](https://github.com/NovatecConsulting/micronaut-camunda-external-client/actions)
[![GitHub Discussions](https://img.shields.io/badge/Forum-GitHub_Discussions-blue)](https://github.com/NovatecConsulting/micronaut-camunda-external-client/discussions)

# Table of Contents

* ✨ [Features](#features)
* 🚀 [Getting Started](#getting-started)
  * [Supported JDKs](#supported-jdks)
  * [Dependency Management](#dependency-management)
  * [Configuration](#configuration)
* 🏆 [Advanced Topics](#advanced-topics)
  * [Customize the External Task Client](#customize-the-external-task-client)
* 📚 [Releases](#releases)
* 📨 [Contact](#contact)

# ✨Features
* Camunda external client can be integrated by simply adding a dependency to your project.
* A worker can subscribe to multiple topics.
* The worker's external task client can be configured with [properties](#configuration) and [programmatically](#customize-the-external-task-client).

# 🚀Getting Started

This section describes what needs to be done to use `micronaut-camunda-external-client-feature` in a Micronaut project.

Here are some example applications:
* [Simple Example](/micronaut-camunda-external-client-example). Remember that you need to start the [camunda application](/micronaut-camunda-external-client-example/micronaut-camunda-bpm-application) first.

## Supported JDKs

We officially support the following JDKs:
* JDK 8 (LTS)
* JDK 11 (LTS)
* JDK 15 (the latest version supported by Micronaut)

## Dependency Management

The Camunda integration works with both Gradle and Maven, but we recommend using Gradle because it has better Micronaut Support.

<details>
<summary>Click to show Gradle configuration</summary>

1. Optional: Create an empty Micronaut project using [Micronaut Launch](https://micronaut.io/launch) or alternatively with the CLI: `mn create-app my-example`.
2. Add the dependency to the build.gradle:
```groovy
implementation("info.novatec:micronaut-camunda-external-client-feature:0.1.0")
```
</details>

<details>
<summary>Click to show Maven configuration</summary>

1. Optional: Create an empty Micronaut using [Micronaut Launch](https://micronaut.io/launch) or alternatively with the CLI:  `mn create-app my-example --build=maven`.
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

## Creating a client
The minimal configuration requires you to provide a handler for a specific topic and a configuration that points to the
Camunda REST API. You can register multiple handlers in this way for different topics. To register a handler you just 
need to add the annotation `ExternalTaskSubscription` and specify the topic to listen to. On start of the application the external task 
client will automatically connect to the specified Camunda REST API and start fetching tasks.

Example configuration in application.yml
```yaml
camunda.external-client:
  base-url: the URL of your Camunda REST API
```
Example handler:
```java 
import info.novatec.micronaut.camunda.external.client.feature.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;

import javax.inject.Singleton;

@Singleton
@ExternalTaskSubscription(topicName = "your topic name")
public class ExampleHandler implements ExternalTaskHandler {

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        // Put your business logic here
        
        externalTaskService.complete(externalTask);
    }
}
```

## ExternalTaskSubscription Annotation
The annotation accepts the following properties:

| Property                    | Default | Description                                                                  |
|-----------------------------|---------|------------------------------------------------------------------------------|
| topicName                   |         | Mandatory, The topic name the client subscribes to.                          |
| lockDuration                | 20000   | Lock duration in milliseconds to lock external tasks. Must be greater than zero. |
| variables                   |         | The name of the variables that are supposed to be retrieved.                 |
| localVariables              | false   | Whether or not variables from greater scope than the external task should be fetched. false means all variables visible in the scope of the external task will be fetched, true means only local variables (to the scope of the external task) will be fetched. |
| businessKey                 |         | A business key to filter for external tasks that are supposed to be fetched and locked. |
| processDefinitionId         |         | A process definition id to filter for external tasks that are supposed to be fetched and locked. |
| processDefinitionIdIn       |         | Process definition ids to filter for external tasks that are supposed to be fetched and locked. |
| processDefinitionKey        |         | A process definition key to filter for external tasks that are supposed to be fetched and locked. |
| processDefinitionKeyIn      |         | Process definition keys to filter for external tasks that are supposed to be fetched and locked. |
| processDefinitionVersionTag |         |                                                                              |
| withoutTenantId             | false   | Filter for external tasks without tenant.                                    |
| tenantIdIn                  |         | Tenant ids to filter for external tasks that are supposed to be fetched and locked. |
| includeExtensionProperties  | false   | Whether or not to include custom extension properties for fetched external tasks. true means all extensionProperties defined in the external task activity will be provided. false means custom extension properties are not available within the external-task-client |

## Configuration

You may use the following properties (typically in application.yml) to configure the external task client.

| Prefix                | Property         | Default               | Description                                       |
|-----------------------|------------------|-----------------------|---------------------------------------------------|
| camunda.external-client | .base-url      |                       | Mandatory, Base url of the Camunda Platform REST API. |
|                       | .worker-id       | Generated out of hostname + 128 Bit UUID | A custom worker id the Workflow Engine is aware of. |
|                       | .max-Tasks       | 10                    | Maximum amount of tasks that will be fetched with each request. |
|                       | .use-priority    | true                  | Specifies whether tasks should be fetched based on their priority or arbitrarily. |
|                       | .default-serialization-format | application/json | Specifies the serialization format that is used to serialize objects when no specific format is requested. |
|                       | .date-format     |                       | Specifies the date format to de-/serialize date variables. |
|                       | .async-response-timeout |                | Asynchronous response (long polling) is enabled if a timeout is given. Specifies the maximum waiting time for the response of fetched and locked external tasks. The response is performed immediately, if external tasks are available in the moment of the request. Unless a timeout is given, fetch and lock responses are synchronous. |
|                       | .lock-duration   | 20000 (milliseconds)  | Lock duration in milliseconds to lock external tasks. Must be greater than zero. This gets overridden by the lock duration configured on a topic subscription |
|                       | .disable-auto-fetching | false          | Disables immediate fetching for external tasks after creating the client. To start fetching ExternalTaskClient.start() must be called. |
|                       | .disable-backoff-strategy | false        | Disables the client-side backoff strategy. On invocation, the configuration option backoffStrategy is ignored. Please bear in mind that disabling the client-side backoff can lead to heavy load situations on engine side. To avoid this, please specify an appropriate async-response-timeout(long). |


# 🏆Advanced Topics

## Customize the External Task Client
With the following bean it is possible to customize the external task client:
```java
import info.novatec.micronaut.camunda.external.client.feature.ExternalClientCustomizer;
import io.micronaut.context.annotation.Replaces;
import org.camunda.bpm.client.ExternalTaskClientBuilder;

import javax.inject.Singleton;

@Singleton
@Replaces(ExternalClientCustomizer.class)
public class MyExternalClientCustomizer implements ExternalClientCustomizer {

    @Override
    public void customize(ExternalTaskClientBuilder builder) {
        // Do your customization here e.g.:
        builder.asyncResponseTimeout(1000);
    }
}
```
You can use this e.g. to implement custom backoff strategies. Important: the properties set within your customizer have 
higher priority than the properties set in your configuration file.

# 📚Releases

The list of [releases](/releases) contains a detailed changelog.

We use [Semantic Versioning](https://semver.org/) which does allow incompatible changes before release 1.0.0, but we try to minimize them.

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

We love listening to your feedback, and of course also discussing the project roadmap and possible use cases with you!

You can reach us:
* [GitHub Discussions](https://github.com/NovatecConsulting/micronaut-camunda-external-client/discussions)
* [mailto:micronaut-camunda@novatec-gmbh.de](mailto:micronaut-camunda@novatec-gmbh.de)
