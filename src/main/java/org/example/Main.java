package org.example;

import java.io.IOException;
import java.net.Socket;


public class Main extends Socket {
    private final static int PORT = 8888;


    public static void main(String[] args) throws IOException {
        ServerStart server = new ServerStart();
        server.startServer();

    }


}