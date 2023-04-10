package de.kieselbrecher.socketapi.packet;

import java.io.Serializable;

/**
 * An abstract class representing a packet that can be sent between the server and clients.
 * Each packet has a type that determines what kind of data it contains.
 */
public abstract class Packet implements Serializable {

    private final PacketType type;

    /**
     * Creates a new Packet object with the specified type.
     * @param type the type of the packet
     */
    public Packet(PacketType type) {
        this.type = type;
    }

    /**
     * Returns the specific type of the packet.
     * @return the type of the packet
     */
    public PacketType getType() {
        return type;
    }
}
