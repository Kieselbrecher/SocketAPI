package de.kieselbrecher.socketapi.server;

import de.kieselbrecher.socketapi.packet.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;

/**
 * The {@code ClientHandler} class is responsible for handling incoming and outgoing communication
 * between the server and a client. It extends the {@code Thread} class and is run as a separate
 * thread to allow for multiple clients to be served simultaneously.
 */
public class ClientHandler extends Thread {

    private final Server server;
    private final Socket socket;
    private final UUID clientId;

    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    /**
     * Constructs a new {@code ClientHandler} object with the given {@code Server}, {@code Socket},
     * and client ID. The input and output streams are also initialized and started in a new thread.
     * @param server   the server that the client is connected to
     * @param socket   the socket that the client is connected through
     * @param clientId the unique ID of the client
     * @throws RuntimeException if there is an error opening the input or output streams
     */
    public ClientHandler(Server server, Socket socket, UUID clientId) {
        this.server = server;
        this.socket = socket;
        this.clientId = clientId;
        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.start();
    }

    /**
     * Continuously reads incoming data from the client and forwards it to the server's
     * broadcast method in the form of a packet.
     */
    @Override
    public void run() {
        try {
            while (true) {
                Object input = in.readObject();
                if(input instanceof Packet packet) {
                    server.broadcastPacket(clientId, packet);
                }
            }
        } catch (IOException ignored) {
            close();
        } catch (ClassNotFoundException exception) {
            throw new RuntimeException("Class not found. The specified class object could not be found by the ClassLoader. " +
                    "Please ensure that the class exists and is available on both the server and client sides.", exception);
        }
    }

    /**
     * Continuously reads incoming data from the client and forwards it to the server's
     * broadcast method in the form of a packet.
     */
    public void sendPacket(Packet packet) {
        try {
            out.writeObject(packet);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Closes the input and output streams, the client's socket, and removes the client from the
     * server's list of connected clients.
     *
     * @throws RuntimeException if there is an error closing any of the streams or the socket
     */
    public void close() {
        try {
            in.close();
            out.close();
            if(socket != null && !socket.isClosed())
                socket.close();
            server.removeClient(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the unique ID of the client.
     *
     * @return the client ID
     */
    public UUID getClientId() {
        return clientId;
    }
}