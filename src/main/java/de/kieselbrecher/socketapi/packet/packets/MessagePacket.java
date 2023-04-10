package de.kieselbrecher.socketapi.packet.packets;

import de.kieselbrecher.socketapi.packet.Packet;
import de.kieselbrecher.socketapi.packet.PacketType;

import java.io.Serial;
import java.io.Serializable;

/**
 * A serializable class that represents a message packet to be sent over the network.
 * This packet is used to send a message to a specific channel on the server.
 */
public class MessagePacket extends Packet implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String channel;
    private String message;

    /**
     * Constructs a new MessagePacket object with the given channel and message.
     *
     * @param channel the name of the channel to which the message will be sent
     * @param message the message to be sent
     */
    public MessagePacket(String channel, String message) {
        super(PacketType.MESSAGE);
        this.channel = channel;
        this.message = message;
    }

    /**
     * Returns the name of the channel to which the message will be sent.
     *
     * @return the channel name
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Returns the message to be sent.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }
}
