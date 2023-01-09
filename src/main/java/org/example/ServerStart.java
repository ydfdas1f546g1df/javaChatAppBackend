package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerStart {
    final int PORT = 8888;

    public void startServer() {
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            //System.out.println("Debug");
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            //System.out.println("Debugg");
            // new thread for a client
            new SocketForServer(socket).start();
            //System.out.println("Debuggg");
        }
    }
}
