package org.collibra.challenge.server;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Single Threaded class for one connection to the Server
 */
public class Session implements Runnable {

    private static final int TIMEOUT = 30000;

    private Socket clientSocket;
    private String sessionKey;
    private long startedConnectionTime = System.currentTimeMillis();
    private static final Logger LOGGER = Logger.getLogger(Session.class.getName());

    Session(Socket clientSocket, String sessionKey) throws SocketException {
        LOGGER.info("Adding connection to Thread pool");
        this.clientSocket = clientSocket;
        this.clientSocket.setSoTimeout(TIMEOUT);
        this.sessionKey = sessionKey;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            //sending first message to Client when he connects
            out.println("HI, I'M " + sessionKey);
            String wordFromClient = in.readLine();
            LOGGER.info("Client: " + wordFromClient);
            //printing Client name
            System.out.println(wordFromClient);
            String userName = wordFromClient.substring(8);
            //sending second message to Client
            out.println("HI, " + userName);

            while (true) {
                try {
                    //loop response to Client for each input Client provides
                    out.println(MessagesFromClient.processResponse(in.readLine()));
                    if (in.readLine() == null || in.readLine().equals("BYE MATE!")) {
                        out.println("BYE " + userName + ", WE SPOKE FOR " + (System.currentTimeMillis() - startedConnectionTime) + " MS");
                        break;
                    }
                } catch (Exception e) {
                    LOGGER.info("Client disconnected: " + userName);
                    //send last message to Client
                    out.println("BYE " + userName + ", WE SPOKE FOR " + (System.currentTimeMillis() - startedConnectionTime) + " MS");
                    break;
                }
            }
        } catch (IOException e) {
            LOGGER.info("Client disconnected!");
        }
    }
}