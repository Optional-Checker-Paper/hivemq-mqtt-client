package org.mqttbee.mqtt5.message.auth;

import org.mqttbee.annotations.NotNull;
import org.mqttbee.annotations.Nullable;
import org.mqttbee.api.mqtt5.message.auth.Mqtt5Auth;
import org.mqttbee.api.mqtt5.message.auth.Mqtt5AuthReasonCode;
import org.mqttbee.mqtt5.codec.encoder.Mqtt5MessageEncoder;
import org.mqttbee.mqtt5.message.Mqtt5Message.Mqtt5MessageWithReasonCode;
import org.mqttbee.mqtt5.message.Mqtt5UTF8StringImpl;
import org.mqttbee.mqtt5.message.Mqtt5UserPropertiesImpl;
import org.mqttbee.util.ByteBufferUtil;

import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author Silvio Giebl
 */
public class Mqtt5AuthImpl extends Mqtt5MessageWithReasonCode<Mqtt5AuthImpl, Mqtt5AuthReasonCode> implements Mqtt5Auth {

    private final Mqtt5UTF8StringImpl method;
    private final ByteBuffer data;

    public Mqtt5AuthImpl(
            @NotNull final Mqtt5AuthReasonCode reasonCode, @NotNull final Mqtt5UTF8StringImpl method,
            @Nullable final ByteBuffer data, @Nullable final Mqtt5UTF8StringImpl reasonString,
            @NotNull final Mqtt5UserPropertiesImpl userProperties,
            @NotNull final Function<Mqtt5AuthImpl, ? extends Mqtt5MessageEncoder<Mqtt5AuthImpl>> encoderProvider) {

        super(reasonCode, reasonString, userProperties, encoderProvider);
        this.method = method;
        this.data = data;
    }

    @NotNull
    @Override
    public Mqtt5UTF8StringImpl getMethod() {
        return method;
    }

    @NotNull
    @Override
    public Optional<ByteBuffer> getData() {
        return ByteBufferUtil.optionalReadOnly(data);
    }

    @Nullable
    public ByteBuffer getRawData() {
        return data;
    }

    @Override
    protected Mqtt5AuthImpl getCodable() {
        return this;
    }

}
