# micronaut-camunda-external-client

[![Release](https://img.shields.io/github/v/release/camunda-community-hub/micronaut-camunda-external-client.svg)](https://github.com/camunda-community-hub/micronaut-camunda-external-client/releases)
[![License](https://img.shields.io/:license-apache-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Continuous Integration](https://github.com/camunda-community-hub/micronaut-camunda-external-client/workflows/Continuous%20Integration/badge.svg)](https://github.com/camunda-community-hub/micronaut-camunda-external-client/actions)
[![GitHub Discussions](https://img.shields.io/badge/Forum-GitHub_Discussions-blue)](https://github.com/camunda-community-hub/micronaut-camunda-external-client/discussions)

[![](https://img.shields.io/badge/Community%20Extension-An%20open%20source%20community%20maintained%20project-FF4700)](https://github.com/camunda-community-hub/community)
![Compatible with: Camunda Platform 7](https://img.shields.io/badge/Compatible%20with-Camunda%20Platform%207-26d07c)
[![](https://img.shields.io/badge/Lifecycle-Stable-brightgreen)](https://github.com/Camunda-Community-Hub/community/blob/main/extension-lifecycle.md#stable-)


This open source project allows you to easily integrate [Camunda](https://camunda.com/products/bpmn-engine) 's [External Task Clients](https://docs.camunda.org/manual/latest/user-guide/process-engine/external-tasks/) into [Micronaut Framework](https://micronaut.io) projects.

The Micronaut Framework is known for its efficient use of resources. With this integration you can easily implement an external client which to process external tasks. If you use GraalVM you have startup times of about 35ms!

The integration is preconfigured with sensible defaults, so that you can get started with minimal configuration: simply add a dependency in your Micronaut project!

If you also want to run the Camunda Workflow Engine on a Micronaut application, have a look at the open source project [micronaut-camunda-bpm](https://github.com/camunda-community-hub/micronaut-camunda-bpm).

---
_We're not aware of all installations of our Open Source project. However, we love_
* _listening to your feedback,_
* _discussing possible use cases with you,_
* _aligning the roadmap to your needs!_

📨 _Please [contact](#contact) us!_

---

Do you want to try it out? Please jump to the [Getting Started](#getting-started) section.

Do you want to contribute to our open source project? Please read the [Contribution Guidelines](CONTRIBUTING.md) and [contact us](#contact).

Micronaut Framework + Camunda = :heart:

# Table of Contents

* ✨ [Features](#features)
* 🚀 [Getting Started](#getting-started)
  * [Dependency Management](#dependency-management)
  * [Creating a Client](#creating-a-client)
  * [ExternalTaskSubscription Annotation](#externaltasksubscription-annotation)
  * [Configuration](#configuration)
  * [Examples](#examples)
  * [Supported JDKs](#supported-jdks)
* 🏆 [Advanced Topics](#advanced-topics)
  * [Customize the External Task Client](#customize-the-external-task-client)
  * [GraalVM](#graalvm)
* 📚 [Releases](#releases)
* 📆 [Publications](#publications)
* 📨 [Contact](#contact)

# ✨Features
* Camunda external client can be integrated by simply adding a dependency to your project.
* A worker can subscribe to multiple topics.
* The worker's external task client can be configured with [properties](#configuration) and [programmatically](#customize-the-external-task-client).

# 🚀Getting Started

This section describes what needs to be done to use `micronaut-camunda-external-client-feature` in a Micronaut project.

## Dependency Management

The Camunda External Worker integration works with both Gradle and Maven, but we recommend using Gradle because it has better Micronaut Support.

You have the following options to integrate the Camunda External Worker integration:
* Create a new Micronaut project using [Micronaut Launch](https://micronaut.io/launch?name=externalworker&features=camunda-external-worker) and check that the "camunda-external-worker" feature is selected.
* Manually add the dependency to a Micronaut project:
  <details>
  <summary>Click to show Gradle configuration</summary>

  Add the dependency to the build.gradle file:
  ```groovy
  implementation("info.novatec:micronaut-camunda-external-client-feature:3.4.0")
  ```
  </details>

  <details>
  <summary>Click to show Maven configuration</summary>

  Add the dependency to the pom.xml file:
  ```xml
  <dependency>
    <groupId>info.novatec</groupId>
    <artifactId>micronaut-camunda-external-client-feature</artifactId>
    <version>3.4.0</version>
  </dependency>
  ```
  </details>

Note: The module `micronaut-camunda-external-client-feature` includes the dependency `org.camunda.bpm:camunda-external-task-client` which will be resolved transitively.

## Creating a Client
The minimal configuration requires you to provide a handler for a specific topic and a configuration that points to the
Camunda REST API. You can register multiple handlers in this way for different topics. To register a handler you just 
need to add the annotation `ExternalTaskSubscription` and specify the topic to listen to. On start of the application the external task 
client will automatically connect to the specified Camunda REST API and start fetching tasks.

Example configuration in application.yml
```yaml
camunda.external-client:
  base-url: http://localhost:8080/engine-rest
```
Example handler:
```java 
import info.novatec.micronaut.camunda.external.client.feature.ExternalTaskSubscription;
import jakarta.inject.Inject;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;

@Singleton
@ExternalTaskSubscription(topicName = "my-topic")
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
| topicName                   |         | The mandatory topic name the client subscribes to.                          |
| lockDuration                | 20000   | Lock duration in milliseconds to lock external tasks. Must be greater than zero. |
| variables                   |         | The name of the variables that are supposed to be retrieved.                 |
| localVariables              | false   | Whether or not variables from greater scope than the external task should be fetched. false means all variables visible in the scope of the external task will be fetched, true means only local variables (to the scope of the external task) will be fetched. |
| businessKey                 |         | A business key to filter for external tasks that are supposed to be fetched and locked. |
| processDefinitionId         |         | A process definition id to filter for external tasks that are supposed to be fetched and locked. |
| processDefinitionIdIn       |         | Process definition ids to filter for external tasks that are supposed to be fetched and locked. |
| processDefinitionKey        |         | A process definition key to filter for external tasks that are supposed to be fetched and locked. |
| processDefinitionKeyIn      |         | Process definition keys to filter for external tasks that are supposed to be fetched and locked. |
| processDefinitionVersionTag |         | Process definition version tag to filter for external tasks that are supposed to be fetched and locked. |
| withoutTenantId             | false   | Filter for external tasks without tenant.                                    |
| tenantIdIn                  |         | Tenant ids to filter for external tasks that are supposed to be fetched and locked. |
| includeExtensionProperties  | false   | Whether or not to include custom extension properties for fetched external tasks. true means all extensionProperties defined in the external task activity will be provided. false means custom extension properties are not available within the external-task-client |

## Configuration

You may use the following properties (typically in application.yml) to configure the external task client.

| Prefix                | Property         | Default               | Description                                       |
|-----------------------|------------------|-----------------------|---------------------------------------------------|
| camunda.external-client | .base-url      |                       | Mandatory base url of the Camunda Platform REST API. |
|                       | .worker-id       | Generated out of hostname + 128 Bit UUID | A custom worker id the Workflow Engine is aware of. |
|                       | .max-tasks       | 10                    | Maximum amount of tasks that will be fetched with each request. |
|                       | .use-priority    | true                  | Specifies whether tasks should be fetched based on their priority or arbitrarily. |
|                       | .default-serialization-format | application/json | Specifies the serialization format that is used to serialize objects when no specific format is requested. |
|                       | .date-format     |                       | Specifies the date format to de-/serialize date variables. |
|                       | .async-response-timeout |                | Asynchronous response (long polling) is enabled if a timeout is given. Specifies the maximum waiting time for the response of fetched and locked external tasks. The response is performed immediately, if external tasks are available in the moment of the request. Unless a timeout is given, fetch and lock responses are synchronous. |
|                       | .lock-duration   | 20000 (milliseconds)  | Lock duration in milliseconds to lock external tasks. Must be greater than zero. This gets overridden by the lock duration configured on a topic subscription |
|                       | .disable-auto-fetching | false          | Disables immediate fetching for external tasks after creating the client. To start fetching ExternalTaskClient.start() must be called. |
|                       | .disable-backoff-strategy | false        | Disables the client-side backoff strategy. On invocation, the configuration option backoffStrategy is ignored. Please bear in mind that disabling the client-side backoff can lead to heavy load situations on engine side. To avoid this, please specify an appropriate long async-response-timeout. |

You can also configure the subscriptions via configuration with the same properties as the annotation. You can then reference the configuration with the annotation by using the topic name, e.g.: 
> Important: If you set `withOutTenantId = true` in the annotation on your handler, you cannot overwrite this property afterwards.

```yaml
camunda:
  external-client:
    subscriptions:
      my-topic:
        lock-duration: 29000
        variables:
          - variable-one
          - variable-two
        local-variables: true
```

```java
@Singleton
@ExternalTaskSubscription(topicName = "my-topic")
public class SimpleHandler implements ExternalTaskHandler {
    ...
}
```

## Examples

Here are some example applications:
* [Calculation](https://github.com/tobiasschaefer/micronaut-camunda-external-client-example-java-gradle) where the server with a BPMN process creates calculations which will be executed by the external task client.
* [Internal example application](/micronaut-camunda-external-client-example) used during development. Remember that you need to start the [Camunda Process Application](/micronaut-camunda-server-example) first.

## Supported JDKs

The current releases are built with Micronaut 4 and therefore we officially support the following JDKs:
* JDK 17 (LTS)

The lastest release supporting Micronaut 3 is 2.13.0 which supports the following JDKs:
* JDK 8 (LTS)
* JDK 11 (LTS)
* JDK 17 (LTS)

# 🏆Advanced Topics

## Customize the External Task Client

With the following bean it is possible to customize the external task client, e.g. to implement custom backoff strategies or register a client request interceptor.

```java
import info.novatec.micronaut.camunda.external.client.feature.ExternalClientCustomizer;
import io.micronaut.context.annotation.Replaces;
import jakarta.inject.Inject;
import org.camunda.bpm.client.ExternalTaskClientBuilder;
import org.camunda.bpm.client.backoff.BackoffStrategy;
import org.camunda.bpm.client.interceptor.ClientRequestInterceptor;

@Singleton
@Replaces(ExternalClientCustomizer.class)
public class MyExternalClientCustomizer implements ExternalClientCustomizer {

    @Override
    public void customize(ExternalTaskClientBuilder builder) {
        // Do your customization here e.g.:
        BackoffStrategy backoffStrategy = ...
        ClientRequestInterceptor interceptor = ...

        builder.backoffStrategy(backoffStrategy)
                .addInterceptor(interceptor);
    }
}
```

Important: the values set within your customizer have higher priority than the properties set in your configuration file.

## GraalVM

With [GraalVM](https://www.graalvm.org/) you can reduce start-up time and memory usage even more! For example, on a developer environment the start-up time will drop to about 35ms!

The following instructions are based on macOS - other operating systems will probably be similar. Feel free to create a pull request with updated instructions for other operating systems.

### Initial Setup

Install the `gu` executable to be able to install `native-image` based on instructions: https://www.graalvm.org/docs/getting-started/macos/ which links to https://github.com/graalvm/graalvm-ce-builds/releases

```
tar -xvf graalvm-ce-java17-darwin-amd64-21.3.0.tar.gz
sudo mv graalvm-ce-java17-21.3.0 /Library/Java/JavaVirtualMachines
/usr/libexec/java_home -V
sudo xattr -r -d com.apple.quarantine /Library/Java/JavaVirtualMachines/graalvm-ce-java17-21.3.0
export PATH=/Library/Java/JavaVirtualMachines/graalvm-ce-java17-21.3.0/Contents/Home/bin:$PATH
gu install native-image
native-image --version
```

### Install GraalVM

Install GraalVM using [SDKMAN!](https://sdkman.io/):

```
curl -s "https://get.sdkman.io" | bash
sdk install java 21.3.0.r17-grl
```

### Initialize Environment

```
sdk use java 21.3.0.r17-grl
export PATH=/Library/Java/JavaVirtualMachines/graalvm-ce-java17-21.3.0/Contents/Home/bin:$PATH
export JAVA_HOME=/Library/Java/JavaVirtualMachines/graalvm-ce-java17-21.3.0/Contents/Home
```

### Create Reflection Configuration

```
cd micronaut-camunda-external-client-example
../gradlew clean build
mkdir -p src/main/resources/META-INF/native-image
java -agentlib:native-image-agent=config-output-dir=src/main/resources/META-INF/native-image -jar build/libs/micronaut-camunda-external-client-example-0.0.1-SNAPSHOT-all.jar
```

Start the server and cancel the client with `Ctrl-C` once you see that the client is running when it repeatedly logs `Completed external task`.

### Build Image

The generated `reflect-config.json` misses three entries (why?) which we add manually:
```json
{
    "name":"org.camunda.bpm.client.variable.impl.format.json.JacksonJsonDataFormatProvider",
    "queryAllPublicMethods":true,
    "methods":[{"name":"<init>","parameterTypes":[] }]
},
{
    "name":"org.camunda.bpm.client.variable.impl.format.serializable.SerializableDataFormatProvider",
    "queryAllPublicMethods":true,
    "methods":[{"name":"<init>","parameterTypes":[] }]
},
{
    "name":"org.camunda.bpm.client.variable.impl.format.xml.DomXmlDataFormatProvider",
    "queryAllPublicMethods":true,
    "methods":[{"name":"<init>","parameterTypes":[] }]
},
```

Now build the native image - note: this will take a few minutes:

`../gradlew clean nativeCompile`

### Start Native Client

You can then start the external client (Note: Server must be running):

`build/native/nativeCompile/micronaut-camunda-external-client-example`

The application will be up and processing the first tasks in about 35ms (!):

```
INFO  io.micronaut.runtime.Micronaut - Startup completed in 33ms. Server Running: http://localhost:8888
INFO  i.n.m.c.e.c.example.SimpleHandler - Completed external task: 47*2=94
INFO  i.n.m.c.e.c.example.SimpleHandler - Completed external task: 22*2=44
INFO  i.n.m.c.e.c.example.SimpleHandler - Completed external task: 48*2=96
```

# 📚Releases

The list of [releases](https://github.com/camunda-community-hub/micronaut-camunda-external-client/releases) contains a detailed changelog.

We use [Semantic Versioning](https://semver.org/).

The following compatibility matrix shows the officially supported Micronaut Framework and Camunda versions for each release.
Other combinations might also work but have not been tested. The current release of the external client will probably work with a server running on Camunda 7.9.0 and newer.

| Release | Micronaut Framework | Camunda |
|---------|---------------------|---------|
| 3.4.0   | 4.0.1               | 7.23.0  |

<details>
<summary>Click to see older releases</summary>

| Release |Micronaut Framework | Camunda       |
|--------|-------|---------------|
| 3.3.1   | 4.0.1               | 7.22.0  |
| 3.2.0   | 4.0.1               | 7.21.0  |
| 3.1.0   | 4.0.1               | 7.20.0  |
| 3.0.0   | 4.0.0               | 7.19.0  |
| 2.13.0  | 3.9.4               | 7.19.0  |
| 2.12.0  | 3.9.0               | 7.19.0  |
| 2.11.0  | 3.8.9               | 7.19.0  |
| 2.10.0  | 3.8.0               | 7.18.0  |
| 2.9.0   | 3.7.1               | 7.18.0  |
| 2.8.0   | 3.6.1               | 7.17.0  |
| 2.7.1   | 3.5.2               | 7.17.0  |
| 2.7.0   | 3.5.2               | 7.18.0-alpha2 |
| 2.6.0   | 3.4.1               | 7.17.0        |
| 2.5.0   | 3.4.0               | 7.16.0        |
| 2.4.1   | 3.3.4               | 7.16.0        |
| 2.4.0   | 3.3.4               | 7.16.0        |
| 2.3.0  | 3.3.0    | 7.16.0        |
| 2.2.0  | 3.2.0  | 7.16.0        |
| 2.1.0  | 3.1.0  | 7.16.0        |
| 2.0.0  | 3.0.0  | 7.15.0        |
| 1.0.1  | 2.5.12 | 7.15.0        |
| 1.0.0  | 2.5.9 | 7.15.0        |
| 0.4.0  | 2.5.3 | 7.15.0        |
| 0.3.0  | 2.5.1 | 7.15.0        |
| 0.2.0  | 2.4.2 | 7.15.0        |
| 0.1.0  | 2.4.2 | 7.14.0        |
</details>

Download of Releases:
* [GitHub Artifacts](https://github.com/camunda-community-hub/micronaut-camunda-external-client/releases)
* [Maven Central Artifacts](https://search.maven.org/artifact/info.novatec/micronaut-camunda-external-client-feature)

# 📆Publications

* 2021-11: [Automating Processes with Camunda and Micronaut](https://www.youtube.com/watch?app=desktop&v=PwgrAb2z0YU&t=17m45s))  
  Recording of the Novatec Summit by Tobias Schäfer (45 Minutes)
* 2021-07: [Automate any Process on Micronaut](https://camunda.com/blog/2021/07/automate-any-process-on-micronaut/)  
  Blogpost by Tobias Schäfer
* 2021-04: [The Camunda External Client for Micronaut](https://www.novatec-gmbh.de/en/blog/the-camunda-external-client-for-micronaut/)  
  by Tobias Schäfer and Martin Sawilla

# 📨Contact

If you have any questions or ideas feel free to create an [issue](https://github.com/camunda-community-hub/micronaut-camunda-external-client/issues) or contact us via [GitHub Discussions](https://github.com/camunda-community-hub/micronaut-camunda-external-client/discussions).

We love listening to your feedback, and of course also discussing the project roadmap and possible use cases with you!

This open source project is being developed by [envite consulting GmbH](https://envite.de/) and [Novatec Consulting GmbH](https://www.novatec-gmbh.de/en/) with the support of the open source community.

---
![envite consulting GmbH](envite-black.png#gh-light-mode-only)
![envite consulting GmbH](envite-white.png#gh-dark-mode-only)
---
![Novatec Consulting GmbH](novatec.jpeg)
---
