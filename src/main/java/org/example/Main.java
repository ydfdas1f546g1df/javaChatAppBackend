package org.example;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Socket;


public class Main extends Socket {
    private final static int PORT = 8888;
    public static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        ServerStart server = new ServerStart();
        logger.log(Level.INFO, "Starting Server");
        server.startServer();

    }


}