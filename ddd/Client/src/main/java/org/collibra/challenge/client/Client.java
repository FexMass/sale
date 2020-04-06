package org.collibra.challenge.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Client class which includes main() for running the Client that connects
 * to the specified hostname and port of Server and handling inputs and outputs
 */
public class Client {

    private static final Logger log = Logger.getLogger(Client.class.getName());
    private static final int PORT = 50000;

    Client(Socket clientSocket) {
        try {
            clientSocket.connect(new InetSocketAddress("127.0.0.1", PORT));
            log.info("Client connecting to Server");

            run(clientSocket);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Could not connect to Server", e);
        }
    }

    private void run(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            AtomicBoolean connected = new AtomicBoolean(true);
            //anonymous thread or keeping track of Server response
            new Thread(() -> {
                String write;
                try {
                    while ((write = in.readLine()) != null) {
                        log.info("Server: " + write);
                        System.out.println(write);
                    }
                } catch (IOException e) {
                    log.info("Client disconnected");
                } finally {
                    connected.set(false);
                }
            }).start();

            Scanner scanner = new Scanner(System.in);
            //wait 0.5 seconds until connection is up and Server sends first message
            Thread.sleep(500);
            System.out.print("Your name: ");

            //sending first message
            out.println("HI, I'M " + scanner.nextLine().toUpperCase());

            while (connected.get()) {
                String input = scanner.nextLine().toUpperCase().trim();
                if (input.equals("BYE MATE!")) {
                    log.info("Closing connection");
                    Thread.sleep(2000);
                    break;
                }
                //loop input messages
                out.println(input);
            }
        } catch (IOException | InterruptedException | NoSuchElementException e) {
            log.log(Level.SEVERE, "Exception occurred", e);
        }
    }

    public static void main(String[] args) {
        new Client(new Socket());
    }
}