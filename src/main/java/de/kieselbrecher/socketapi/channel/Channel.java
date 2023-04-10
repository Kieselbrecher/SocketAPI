package de.kieselbrecher.socketapi.channel;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a messaging channel that a client can subscribe to in order to receive messages.
 */
public class Channel {

    private final String name;
    private final List<ChannelHandler> handlers;

    /**
     * Creates a new channel with the given name and no handlers attached.
     *
     * @param channel the name of the channel
     */
    public Channel(String channel) {
        this.name = channel;
        this.handlers = new ArrayList<>();
    }

    /**
     * Returns the name of this channel.
     *
     * @return the name of this channel
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the list of handlers attached to this channel.
     *
     * @return the list of handlers attached to this channel
     */
    public List<ChannelHandler> getHandlers() {
        return handlers;
    }
}
