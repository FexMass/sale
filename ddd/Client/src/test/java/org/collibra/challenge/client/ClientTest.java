package org.collibra.challenge.client;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class ClientTest {

    private PrintWriter out;
    private BufferedReader in;


    @Test
    public void testClientConnected() {

        // Pripremi testni env.
//        Server server = new Server();


            // Executaj clienta
        Client client = new Client(new Socket());



            // Assertaj da je server dobio poruku od klijenta
//        assertEquals("", server.getLastMessage);
            // Assertaj da je client dobio poruku od servera
//        assertEquals("", client.getLaseServerMessage());

    }

}
