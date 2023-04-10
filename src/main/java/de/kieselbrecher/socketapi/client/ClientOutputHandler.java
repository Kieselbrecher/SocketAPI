package de.kieselbrecher.socketapi.client;

import de.kieselbrecher.socketapi.packet.Packet;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Ein Handler, der ausgehende Pakete an den Server sendet.
 */
public class ClientOutputHandler {

    private final ObjectOutputStream out;

    /**
     * Erstellt einen neuen ClientOutputHandler, der Pakete an den Server sendet.
     * @param socket die Socket-Verbindung zum Server.
     */
    public ClientOutputHandler(Socket socket) {
        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException exception) {
            throw new RuntimeException("Failed to open output stream.", exception);
        }
    }

    /**
     * Sendet ein Paket an den Server.
     * @param packet das zu sendende Paket.
     * @throws RuntimeException wenn ein Fehler beim Senden des Pakets auftritt.
     */
    public void sendPacket(Packet packet) {
        try {
            out.writeObject(packet);
            out.flush();
        } catch (IOException exception) {
            throw new RuntimeException("An error occurred while sending packet to the server", exception);
        }
    }

    /**
     * Schließt den Object-Output-Stream.
     * @throws RuntimeException wenn ein Fehler beim Schließen des Output-Streams auftritt.
     */
    public void close() {
        try {
            out.close();
        } catch (IOException exception) {
            throw new RuntimeException("An error occurred while closing the output stream.", exception);
        }
    }
}