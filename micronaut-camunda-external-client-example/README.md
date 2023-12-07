# micronaut-camunda-external-client-example

> [!WARNING]
> The micronaut-camunda-client-example in this project does not currently work together with micronaut-camunda-server-example when started together in the IDE because the client uses Micronaut 4 while info.novatec:micronaut-camunda-bpm-feature is still on Micronaut 3. If the server is started based on commit 707275d it actually works, i.e. the client works but not in the example setup.

This example shows how to apply the external task pattern: the server with a BPMN process will create tasks which will be executed by an external task client.

First start the server, see [micronaut-camunda-server-example](/micronaut-camunda-server-example).

Start Client:

`./gradlew run -p micronaut-camunda-external-client-example`

This will output something like:

```
Completed external task: 83*2=166
Completed external task: 34*2=68
Completed external task: 55*2=110
Completed external task: 14*2=28
```
