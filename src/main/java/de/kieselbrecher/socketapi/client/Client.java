package de.kieselbrecher.socketapi.client;

import de.kieselbrecher.socketapi.channel.ChannelHandler;
import de.kieselbrecher.socketapi.packet.packets.MessagePacket;

import java.io.IOException;
import java.net.Socket;

/**
 * A class representing a client in a client-server communication.
 * The client connects to a server and can register channels and send messages to them.
 */
public class Client {

    private final Socket socket;
    private final ClientOutputHandler outputHandler;
    private final ClientInputHandler inputHandler;

    /**
     * Constructs a new client and connects it to the server with the specified hostname and port.
     * @param hostname the hostname of the server to connect to
     * @param port the port number of the server to connect to
     * @throws RuntimeException if failed to connect to the server
     */
    public Client(String hostname, int port) {
        try {
            this.socket = new Socket(hostname, port);
            this.outputHandler = new ClientOutputHandler(socket);
            this.inputHandler = new ClientInputHandler(this, socket);
        } catch (IOException exception) {
            throw new RuntimeException("Failed to connect to the server with hostname '" + hostname + "' and port '" + port + "'", exception);
        }
    }

    /**
     * Registers a channel with the specified name and handler to receive messages on.
     * @param channel the name of the channel to register
     * @param handler the handler to be called when a message is received on the channel
     */
    public void registerChannel(String channel, ChannelHandler handler) {
        inputHandler.addChannel(channel, handler);
    }

    /**
     * Sends a message to the specified channel.
     * @param channel the name of the channel to send the message to
     * @param message the message to send
     */
    public void sendMessage(String channel, String message) {
        outputHandler.sendPacket(new MessagePacket(channel, message));
    }

    /**
     * Closes the connection to the server.
     * @throws RuntimeException if an error occurs while closing the connection
     */
    public void close() {
        try {
            outputHandler.close();
            inputHandler.close();
            socket.close();
        } catch (IOException exception) {
            throw new RuntimeException("An error occurred while closing the connection to the server", exception);
        }
    }
}