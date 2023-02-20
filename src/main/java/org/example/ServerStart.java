package org.example;

import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerStart {

    final int PORT = 8888;

    public void startServer(int PORT) {
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {

            serverSocket = new ServerSocket(PORT);
            Main.logger.log(Level.INFO, "Started server on Port " + PORT);

        } catch (IOException e) {
            e.printStackTrace();
            Main.logger.log(Level.FATAL, "Can't create Server-socket: " + e);
        }

        while (true) {
            //System.out.println("Debug");
            try {
                socket = serverSocket.accept();
                Main.logger.log(Level.INFO, "New client connected");

            } catch (IOException e) {
                //System.out.println("I/O error: " + e);
                Main.logger.log(Level.FATAL, e);
            }
            //System.out.println("Debugg");
            // new thread for a client
            new SocketForServer(socket).start();
            Main.logger.log(Level.INFO, "Starting new server instance");
            //System.out.println("Debuggg");
        }
    }

    public void startServer() {
        startServer(PORT);
    }
}
