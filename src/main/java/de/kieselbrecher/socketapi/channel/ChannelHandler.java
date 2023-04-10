package de.kieselbrecher.socketapi.channel;

/**
 * A functional interface for handling messages received from a specific channel.
 */
public interface ChannelHandler {

    /**
     * Invoked when a new message is received from the channel this handler is registered to.
     * @param message the message content
     */
    void onResult(String message);
}
