/*
 * Copyright 2018 The MQTT Bee project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.mqttbee.mqtt.message.subscribe.mqtt3;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mqttbee.api.mqtt.datatypes.MqttQos;
import org.mqttbee.api.mqtt.datatypes.MqttTopicFilter;
import org.mqttbee.api.mqtt.mqtt3.message.subscribe.Mqtt3Subscription;
import org.mqttbee.api.mqtt.mqtt3.message.subscribe.Mqtt3SubscriptionBuilder;
import org.mqttbee.api.mqtt.mqtt3.message.subscribe.Mqtt3SubscriptionBuilderBase;
import org.mqttbee.mqtt.datatypes.MqttTopicFilterImpl;
import org.mqttbee.mqtt.util.MqttBuilderUtil;

import java.util.function.Function;

/**
 * @author Silvio Giebl
 */
// @formatter:off
public abstract class Mqtt3SubscriptionBuilderImpl<
            B extends Mqtt3SubscriptionBuilderBase<B, C>,
            C extends B>
        implements Mqtt3SubscriptionBuilderBase<B, C>,
                   Mqtt3SubscriptionBuilderBase.Complete<B, C> {
// @formatter:on

    private @Nullable MqttTopicFilterImpl topicFilter;
    private @NotNull MqttQos qos = Mqtt3Subscription.DEFAULT_QOS;

    abstract @NotNull C self();

    @Override
    public @NotNull C topicFilter(final @NotNull String topicFilter) {
        this.topicFilter = MqttBuilderUtil.topicFilter(topicFilter);
        return self();
    }

    @Override
    public @NotNull C topicFilter(final @NotNull MqttTopicFilter topicFilter) {
        this.topicFilter = MqttBuilderUtil.topicFilter(topicFilter);
        return self();
    }

    @Override
    public @NotNull C qos(final @NotNull MqttQos qos) {
        this.qos = Preconditions.checkNotNull(qos, "QoS must not be null.");
        return self();
    }

    public @NotNull Mqtt3Subscription build() {
        Preconditions.checkNotNull(topicFilter, "Topic filter must not be null.");
        return Mqtt3SubscriptionView.of(topicFilter, qos);
    }

    // @formatter:off
    public static class Impl
            extends Mqtt3SubscriptionBuilderImpl<
                        Mqtt3SubscriptionBuilder,
                        Mqtt3SubscriptionBuilder.Complete>
            implements Mqtt3SubscriptionBuilder,
                       Mqtt3SubscriptionBuilder.Complete {
    // @formatter:on

        @Override
        @NotNull Mqtt3SubscriptionBuilder.Complete self() {
            return this;
        }
    }

    // @formatter:off
    public static class NestedImpl<P>
            extends Mqtt3SubscriptionBuilderImpl<
                        Mqtt3SubscriptionBuilder.Nested<P>,
                        Mqtt3SubscriptionBuilder.Nested.Complete<P>>
            implements Mqtt3SubscriptionBuilder.Nested<P>,
                       Mqtt3SubscriptionBuilder.Nested.Complete<P> {
    // @formatter:on

        private final @NotNull Function<? super Mqtt3Subscription, P> parentConsumer;

        public NestedImpl(final @NotNull Function<? super Mqtt3Subscription, P> parentConsumer) {
            this.parentConsumer = parentConsumer;
        }

        @Override
        @NotNull Mqtt3SubscriptionBuilder.Nested.Complete<P> self() {
            return this;
        }

        @Override
        public @NotNull P applySubscription() {
            return parentConsumer.apply(build());
        }
    }
}
