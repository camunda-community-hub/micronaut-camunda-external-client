/*
 * Copyright 2021 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.novatec.micronaut.camunda.external.client.feature;

import io.micronaut.context.BeanContext;
import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.core.annotation.Order;
import io.micronaut.inject.BeanDefinition;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Singleton;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.topic.TopicSubscription;
import org.camunda.bpm.client.topic.TopicSubscriptionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * @author Martin Sawilla
 * @author Tobias Schäfer
 *
 * Allows to configure an external task worker with the {@link ExternalTaskSubscription} annotation. This allows to easily build
 * external workers for multiple topics.
 */
@Singleton
public class ExternalWorkerSubscriptionCreator implements AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(ExternalWorkerSubscriptionCreator.class);

    protected final BeanContext beanContext;
    protected final ExternalTaskClient externalTaskClient;
    protected Configuration configuration;

    protected Collection<TopicSubscription> topicSubscriptions = Collections.synchronizedCollection(new ArrayList<>());

    public ExternalWorkerSubscriptionCreator(BeanContext beanContext,
                                             ExternalTaskClient externalTaskClient,
                                             Configuration configuration) {
        this.beanContext = beanContext;
        this.externalTaskClient = externalTaskClient;

        this.configuration = configuration;
    }

    @PreDestroy
    @Override
    public void close() {
        log.info("Closing {} topic subscriptions", topicSubscriptions.size());
        topicSubscriptions.forEach(TopicSubscription::close);
    }

    @Order(-90) //Start after process engine with REST-interface which has order -100
    @EventListener
    public void onEvent(ServerStartupEvent event) {
        beanContext.getBeanDefinitions(ExternalTaskHandler.class).forEach(this::registerExternalTaskHandler);
    }

    protected void registerExternalTaskHandler(BeanDefinition<ExternalTaskHandler> beanDefinition) {
        ExternalTaskHandler externalTaskHandler = beanContext.getBean(beanDefinition);
        AnnotationValue<ExternalTaskSubscription> annotationValue = beanDefinition.getAnnotation(ExternalTaskSubscription.class);

        if (annotationValue != null) {
            //noinspection OptionalGetWithoutIsPresent
            String topicName = annotationValue.stringValue("topicName").get();

            TopicSubscriptionBuilder builder = createTopicSubscription(externalTaskHandler, externalTaskClient, topicName, annotationValue);

            Map<String, Configuration.Subscription> subscriptions = configuration.getSubscriptions();
            if (subscriptions != null && subscriptions.containsKey(topicName)) {
                Configuration.Subscription subscription = subscriptions.get(topicName);
                if (subscription != null) {
                    overrideTopicSubscriptionWithConfigurationProperties(subscription, builder, topicName);
                }
            }

            topicSubscriptions.add(builder.open());
            log.info("External task client '{}' subscribed to topic '{}'", externalTaskHandler.getClass().getSimpleName(), topicName);

        } else {
            log.warn("Skipping subscription. Could not find annotation ExternalTaskSubscription on class {}", beanDefinition.getName());
        }
    }

    protected TopicSubscriptionBuilder createTopicSubscription(ExternalTaskHandler externalTaskHandler, ExternalTaskClient client, String topicName, AnnotationValue<ExternalTaskSubscription> annotationValue) {

        TopicSubscriptionBuilder builder = client.subscribe(topicName);

        builder.handler(externalTaskHandler);

        annotationValue.longValue("lockDuration").ifPresent(builder::lockDuration);

        annotationValue.get("variables", String[].class).ifPresent(it -> {
            if (!it[0].equals("")) {
                builder.variables(it);
            }
        });

        annotationValue.booleanValue("localVariables").ifPresent(builder::localVariables);

        annotationValue.stringValue("businessKey").ifPresent(builder::businessKey);

        annotationValue.stringValue("processDefinitionId").ifPresent(builder::processDefinitionId);

        annotationValue.get("processDefinitionIdIn", String[].class).ifPresent(it -> {
            if (!it[0].equals("")) {
                builder.processDefinitionIdIn(it);
            }
        });

        annotationValue.stringValue("processDefinitionKey").ifPresent(builder::processDefinitionKey);

        annotationValue.get("processDefinitionKeyIn", String[].class).ifPresent(it -> {
            if (!it[0].equals("")) {
                builder.processDefinitionKeyIn(it);
            }
        });

        annotationValue.stringValue("processDefinitionVersionTag").ifPresent(builder::processDefinitionVersionTag);

        annotationValue.booleanValue("withoutTenantId").ifPresent(it -> {
            if (it) {
                builder.withoutTenantId();
            }
        });

        annotationValue.get("tenantIdIn", String[].class).ifPresent(it -> {
            if (!it[0].equals("")) {
                builder.tenantIdIn(it);
            }
        });

        annotationValue.booleanValue("includeExtensionProperties").ifPresent(builder::includeExtensionProperties);

        return builder;
    }

    protected void overrideTopicSubscriptionWithConfigurationProperties(Configuration.Subscription subscription, TopicSubscriptionBuilder builder, String topicName) {
        log.info("External configuration for topic {} found.", topicName);

        if (subscription.lockDuration() != null) {
            builder.lockDuration(subscription.lockDuration());
        }

        if (subscription.variables() != null) {
            builder.variables(subscription.variables().toArray(String[]::new));
        }

        if (subscription.localVariables() != null) {
            builder.localVariables(subscription.localVariables());
        }

        if (subscription.businessKey() != null) {
            builder.businessKey((subscription.businessKey()));
        }

        if (subscription.processDefinitionId() != null) {
            builder.processDefinitionId(subscription.processDefinitionId());
        }

        if (subscription.processDefinitionIdIn() != null) {
            builder.processDefinitionIdIn(subscription.processDefinitionIdIn().toArray(String[]::new));
        }

        if (subscription.processDefinitionKey() != null) {
            builder.processDefinitionKey(subscription.processDefinitionKey());
        }

        if (subscription.processDefinitionKeyIn() != null) {
            builder.processDefinitionKeyIn(subscription.processDefinitionKeyIn().toArray(String[]::new));
        }

        if (subscription.processDefinitionVersionTag() != null) {
            builder.processDefinitionVersionTag(subscription.processDefinitionVersionTag());
        }

        if (subscription.withoutTenantId() != null && subscription.withoutTenantId()) {
            builder.withoutTenantId();
        }

        if (subscription.tenantIdIn() != null) {
            builder.tenantIdIn(subscription.tenantIdIn().toArray(String[]::new));
        }

        if (subscription.includeExtensionProperties() != null) {
            builder.includeExtensionProperties(subscription.includeExtensionProperties());
        }
    }
}
