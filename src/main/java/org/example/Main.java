package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Main extends Socket {
    private ServerSocket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Socket cSocket;
    private static Main INSTANCE;
    ArrayList<String> input;
    private Boolean auth;
    ArrayList<ArrayList<String>> message;
    // HttpRequest httpRequest;

    public static void startAndStop() throws IOException {
        INSTANCE.stop();
        INSTANCE.start(8888);

    }

    private void start(int port) throws IOException {
        //httpRequest = new HttpRequest();
        socket = new ServerSocket(port);
        cSocket = socket.accept();
        out = new PrintWriter(cSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
        auth = false;
        message = new ArrayList<ArrayList<String>>();
        loop();
    }

    private void stop() throws IOException {
        in.close();
        out.close();
        socket.close();
        cSocket.close();
    }

    public void loop() throws IOException {
        INSTANCE = this;
        while (true) {
            input = new ArrayList<String>(readFromClient(in));
            if ("EOF".equals(input.get(0))) {
                System.out.println(message);
                break;
            //}
            //else if ("GET".equals(input.get(0))) {
            //    input.forEach(System.out::print);
            //    System.out.print("\n");
            //    httpRequest.HttpRequest(out, input);
            //
            //
            } else if ("AUTH".equals(input.get(0))) {
                if (input.size() >= 3) {
                    if ("username".equals(input.get(1)) && "1234".equals(input.get(2))) {
                        out.println("INFO Authenticated");
                        auth = true;
                    } else {
                        out.println("INFO Authentication failed");
                    }
                }else {
                    out.println("INFO Authentication failed");
                }
                // TODO: authentication
            } else if ("MSG".equals(input.get(0))) {
                // TODO: msg

                if (auth) {
                    try {
                        if ("SEND".equals(input.get(1))) {
                            try {
                                message.get(Integer.parseInt(input.get(3))).add(input.get(2));
                            } catch (Exception e) {
                                out.println("ERROR: " + e);
                            }
                        }
                    } catch (Exception e) {
                        out.println("ERROR " + e);
                    }

                } else {
                    out.println("ERROR Not Authenticated");
                }

            } else if ("DEAUTH".equals(input.get(0)) && auth) {
                // TODO: deauth
            } else if ("CREATE".equals(input.get(0))) {
                if (auth) {
                    message.add(new ArrayList<String>());
                }
            } else {
                System.err.println("ERROR FALSE INPUT:" + input);
            }
        }
        stop();
    }

    public ArrayList<String> readFromClient(BufferedReader inS) throws IOException {
        String rawInput = inS.readLine();
        ArrayList<String> processedInput = null;
        try {
            String[] str = rawInput.split(" ");

            List<String> halfProcessed = Arrays.asList(str);

            processedInput = new ArrayList<String>(halfProcessed);
        } catch (Exception e) {
            out.println("ERROR " + e);
        }

        return processedInput;
    }

    public static void main(String[] args) throws IOException {
        Main server = new Main();
        server.start(8888);
    }


}