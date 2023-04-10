package de.kieselbrecher.socketapi.server;

import de.kieselbrecher.socketapi.packet.Packet;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A simple server that listens for incoming socket connections from clients
 * and handles sending and receiving packets to and from connected clients.
 */
public class Server extends Thread {

    private final ServerSocket server;
    private final List<ClientHandler> clients;

    /**
     * Creates a new Server instance that listens for incoming client connections
     * on the specified port.
     *
     * @param port The port on which to listen for incoming client connections.
     * @throws RuntimeException If the server fails to start for any reason.
     */
    public Server(int port) {
        try {
            this.server = new ServerSocket(port);
            this.clients = new ArrayList<>();
            System.out.println("Server started on port '" + port + "'");
        } catch (IOException exception) {
            throw new RuntimeException("Failed to start server at port '" + port + "'", exception);
        }
        this.start();
    }

    /**
     * The main loop of the server thread that listens for incoming client connections
     * and adds them to the list of connected clients.
     */
    @Override
    public void run() {
        try {
            while(true) {
                Socket socket = server.accept();
                ClientHandler handler = new ClientHandler(this, socket, generateUUID());
                clients.add(handler);
                System.out.println("Client connected!");
            }
        } catch (IOException ignored) {

        }
    }

    /**
     * Generates a unique UUID for a new client connection.
     *
     * @return A unique UUID that is not already assigned to a connected client.
     */
    private UUID generateUUID() {
        UUID uuid = UUID.randomUUID();
        for(ClientHandler handler : clients) {
            if(handler.getClientId().equals(uuid))
                generateUUID();
        }
        return uuid;
    }

    /**
     * Sends a packet to all connected clients except the sender.
     *
     * @param sender The UUID of the client that sent the packet.
     * @param packet The packet to be sent to all clients.
     */
    public void broadcastPacket(UUID sender, Packet packet) {
        for(ClientHandler handler : clients) {
            if(!handler.getClientId().equals(sender))
                handler.sendPacket(packet);
        }
    }

    /**
     * Sends a packet to a specific connected client.
     *
     * @param target The UUID of the client to receive the packet.
     * @param packet The packet to be sent to the client.
     */
    public void sendPacket(UUID target, Packet packet) {
        for(ClientHandler handler : clients) {
            if(handler.getClientId().equals(target))
                handler.sendPacket(packet);
        }
    }

    /**
     * Removes a client from the list of connected clients.
     *
     * @param handler The ClientHandler instance representing the client to remove.
     */
    public void removeClient(ClientHandler handler) {
        clients.remove(handler);
        System.out.println("Client Disconnected!");
    }

    /**
     * Closes the server and all client connections.
     *
     * @throws RuntimeException If an error occurs while closing the server or client connections.
     */
    public void close() {
        try {
            server.close();
            for(ClientHandler handler : clients) {
                handler.close();
            }
        } catch (IOException exception) {
            throw new RuntimeException("An error occurred while closing the server", exception);
        }
    }

    /**
     * Gets the current number of connected clients.
     *
     * @return The number of clients currently connected to the server.
     */
    public int getClientCount() {
        return clients.size();
    }
}