/*
 * Copyright (c) 2016-2024 Chronicle Software Ltd
 */

package town.lost.oms.dto;

import net.openhft.chronicle.bytes.BytesIn;
import net.openhft.chronicle.bytes.BytesOut;
import net.openhft.chronicle.core.Jvm;
import net.openhft.chronicle.core.io.InvalidMarshallableException;
import net.openhft.chronicle.core.io.Validatable;
import net.openhft.chronicle.wire.*;
import net.openhft.chronicle.wire.converter.NanoTime;
import net.openhft.chronicle.wire.converter.ShortText;

/**
 * The {@code AbstractEvent} class represents a generic event in the system.
 *
 * <p>This class provides common fields such as sender, target, and sendingTime which can be extended
 * by more specific events. Fields sender and target are encoded as {@link ShortText} for efficient storage and transmission.
 *
 * <p>The class is generic, with the type parameter being a subclass of {@code AbstractEvent}. This allows
 * methods in this class to return an instance of the subclass, enabling method chaining in the subclass.
 *
 * <p>The encoding of the marshalled bytes can be controlled via system properties "bytesInBinary" and "pregeneratedMarshallable".
 */
public class AbstractEvent<E extends AbstractEvent<E>> extends SelfDescribingMarshallable implements Validatable {
    // Used to control the benchmark
    public static final boolean BYTES_IN_BINARY = Jvm.getBoolean("bytesInBinary", true);

    // Used to control the benchmark
    public static final boolean PREGENERATED_MARSHALLABLE = Jvm.getBoolean("pregeneratedMarshallable", true);
    private static final int MARSHALLABLE_VERSION = 1;

    // The sender of the event
    @ShortText
    private long sender;

    // The target of the event
    @ShortText
    private long target;

    // The sending time of the event, in nanoseconds.
    @NanoTime
    private long sendingTime;

    @Override
    public boolean usesSelfDescribingMessage() {
        return !BYTES_IN_BINARY;
    }

    /**
     * Get the sender of the event.
     *
     * @return The sender's value as a long.
     */
    public long sender() {
        return sender;
    }

    /**
     * Set the sender of the event.
     *
     * @param sender The sender's value to set, as a long.
     * @return This AbstractEvent instance, to facilitate method chaining.
     */
    @SuppressWarnings("unchecked")
    public E sender(long sender) {
        this.sender = sender;
        return (E) this;
    }

    /**
     * Get the target of the event.
     *
     * @return The target's value as a long.
     */
    public long target() {
        return target;
    }

    /**
     * Set the target of the event.
     *
     * @param target The target's value to set, as a long.
     * @return This AbstractEvent instance, to facilitate method chaining.
     */
    @SuppressWarnings("unchecked")
    public E target(long target) {
        this.target = target;
        return (E) this;
    }

    /**
     * Get the sending time of the event.
     *
     * @return The sending time as a long.
     */
    public long sendingTime() {
        return sendingTime;
    }

    /**
     * Set the sending time of the event.
     *
     * @param sendingTime The sending time to set, as a long.
     * @return This AbstractEvent instance, to facilitate method chaining.
     */
    @SuppressWarnings("unchecked")
    public E sendingTime(long sendingTime) {
        this.sendingTime = sendingTime;
        return (E) this;
    }

    @Override
    public void writeMarshallable(BytesOut<?> out) {
        if (PREGENERATED_MARSHALLABLE) {
            out.writeStopBit(MARSHALLABLE_VERSION);
            out.writeLong(sender);
            out.writeLong(target);
            out.writeLong(sendingTime);
        } else {
            super.writeMarshallable(out);
        }
    }

    @Override
    public void readMarshallable(BytesIn<?> in) {
        if (PREGENERATED_MARSHALLABLE) {
            int version = (int) in.readStopBit();
            if (version == MARSHALLABLE_VERSION) {
                sender = in.readLong();
                target = in.readLong();
                sendingTime = in.readLong();
            } else {
                throw new IllegalStateException("Unknown version " + version);
            }
        } else {
            super.readMarshallable(in);
        }
    }

    @Override
    public void writeMarshallable(WireOut out) {
        if (PREGENERATED_MARSHALLABLE) {
            out.write("sender").writeLong(ShortTextLongConverter.INSTANCE, sender);
            out.write("target").writeLong(ShortTextLongConverter.INSTANCE, target);
            out.write("sendingTime").writeLong(MicroTimestampLongConverter.INSTANCE, sendingTime);
        } else {
            super.writeMarshallable(out);
        }
    }

    @Override
    public void readMarshallable(WireIn in) {
        if (PREGENERATED_MARSHALLABLE) {
            sender = in.read("sender").readLong(ShortTextLongConverter.INSTANCE);
            target = in.read("target").readLong(ShortTextLongConverter.INSTANCE);
            sendingTime = in.read("sendingTime").readLong(MicroTimestampLongConverter.INSTANCE);
        } else {
            super.readMarshallable(in);
        }
    }

    @Override
    public void validate() throws InvalidMarshallableException {
        if (sender == 0)
            throw new InvalidMarshallableException("sender is required");
        if (target == 0)
            throw new InvalidMarshallableException("target is required");
        if (sendingTime == 0)
            throw new InvalidMarshallableException("sendingTime is required");
    }
}
