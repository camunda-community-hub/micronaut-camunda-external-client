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
package info.novatec.micronaut.camunda.external.client.feature.test

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.camunda.bpm.client.ExternalTaskClient
import org.camunda.bpm.client.impl.ExternalTaskClientImpl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import javax.inject.Inject

/**
 * @author Martin Sawilla
 */
@MicronautTest
class ExternalTaskSubscriptionTest {

    @Inject
    lateinit var externalTaskClient: ExternalTaskClient

    @Test
    fun `test topic subscription`() {
        val client = externalTaskClient as ExternalTaskClientImpl
        val subscriptions = client.topicSubscriptionManager.subscriptions

        assertEquals(1, subscriptions.size)

        assertEquals("test-topic", subscriptions[0].topicName)
        assertEquals(19000, subscriptions[0].lockDuration)
        assertEquals(listOf("test-one", "test-two"), subscriptions[0].variableNames)
        assertEquals(true, subscriptions[0].isLocalVariables)
    }
}
