package org.collibra.challenge.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;

/**
 * The Server class which includes main() for running the server that listens
 * for incoming connections and adding to Thread pool for multiple instances
 */
public class Server {

    private static final int PORT = 50000;
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public Server() {
        LOGGER.info("Starting server on port: " + PORT);
        //Opening server on port 50000
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            acceptClient(serverSocket);
        } catch (IOException e) {
            LOGGER.log(SEVERE, "Error starting server: ", e);
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        new Server();
    }

    /**
     * Accepting all incoming connections and adding them to Thread pool
     */
    private void acceptClient(ServerSocket serverSocket) {
        while (true) {
            try {
                //adding connection to thread pool
                this.executorService.submit(new Session(serverSocket.accept(), UUID.randomUUID().toString()));
            } catch (IOException e) {
                LOGGER.log(SEVERE, "Error listening to client: ", e);
                break;
            }
        }
    }
}