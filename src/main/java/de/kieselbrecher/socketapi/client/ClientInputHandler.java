package de.kieselbrecher.socketapi.client;

import de.kieselbrecher.socketapi.channel.Channel;
import de.kieselbrecher.socketapi.channel.ChannelHandler;
import de.kieselbrecher.socketapi.packet.Packet;
import de.kieselbrecher.socketapi.packet.packets.MessagePacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class handles incoming packets from the server and dispatches them to the appropriate channel handlers.
 * It runs in a separate thread and listens for incoming packets continuously.
 * When a packet is received, it checks its type and calls the appropriate method to handle the packet.
 */
public class ClientInputHandler extends Thread {

    private final Client client;
    private final ObjectInputStream in;
    private final List<Channel> channels;

    /**
     * Constructs a new ClientInputHandler object.
     * @param client the client associated with this handler
     * @param socket the socket used for communication with the server
     * @throws RuntimeException if the input stream cannot be opened
     */
    public ClientInputHandler(Client client, Socket socket) {
        this.client = client;
        try {
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException exception) {
            throw new RuntimeException("Failed to open input stream.", exception);
        }
        this.channels = new ArrayList<>();
        this.start();
    }

    /**
     * Runs the handler in a separate thread.
     * Listens for incoming packets continuously, and handles them when received.
     * If an IOException is caught while listening for packets, it means that the connection to the server is closed.
     * In that case, the associated client is closed as well.
     * @throws RuntimeException if a ClassNotFoundException is thrown while reading a packet object
     */
    @Override
    public void run() {
        try {
            while(true) {
                Object input = in.readObject();
                if(input instanceof Packet packet)
                    handlePacket(packet);
            }
        } catch (IOException ignored) {
            client.close();
        } catch (ClassNotFoundException exception) {
            throw new RuntimeException("Class not found. The specified class object could not be found by the ClassLoader. " +
                    "Please ensure that the class exists and is available on both the server and client sides.", exception);
        }
    }

    /**
     * Gets the channel object associated with the given channel name.
     * @param channel the name of the channel to retrieve
     * @return the channel object if found, otherwise null
     */
    private Channel getChannel(String channel) {
        for(Channel c : channels) {
            if(c.getName().equals(channel))
                return c;
        }
        return null;
    }

    /**
     * Handles a packet object received from the server.
     * If the packet is a message packet, it dispatches the message to the appropriate channel handlers.
     * @param packet the packet object to handle
     */
    private void handlePacket(Packet packet) {
        if(packet instanceof MessagePacket message && Objects.nonNull(getChannel(message.getChannel()))) {
            Channel channel = getChannel(message.getChannel());
            channel.getHandlers().forEach(handler -> {
                handler.onResult(message.getMessage());
            });
        }
    }

    /**
     * Adds a new channel to the list of channels associated with this handler.
     * If a channel with the given name already exists, it adds the handler to the existing channel.
     * If a channel with the given name does not exist, it creates a new channel with the given name and adds the handler to it.
     * @param channel the name of the channel to add
     * @param handler the channel handler to add
     */
    public void addChannel(String channel, ChannelHandler handler) {
        for(Channel c : channels) {
            if(c.getName().equals(channel)) {
                c.getHandlers().add(handler);
                return;
            }
        }
        channels.add(new Channel(channel));
        addChannel(channel, handler);
    }

    /**
     * Closes the input stream.
     *
     * @throws RuntimeException if an error occurs while closing the input stream.
     */
    public void close() {
        try {
            in.close();
        } catch (IOException exception) {
            throw new RuntimeException("An error occurred while closing the inout stream.", exception);
        }
    }
}