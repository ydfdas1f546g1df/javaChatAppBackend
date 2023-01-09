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

public class SocketForServer extends Thread{
    private ServerSocket socket;
    private java.net.Socket cSocket;
    private BufferedReader in;
    private PrintWriter out;
    ArrayList<String> input;
    private Boolean auth;
    private String dbHost;
    private int dbPort;
    private MySql mySql;
    private static SocketForServer INSTANCE2;
    private int PORT;


    ArrayList<ArrayList<String>> message;

    public SocketForServer(Socket clientSocket, int Port){
        this.PORT = Port;
        this.cSocket = clientSocket;
    }

    public void start(){
        startServer();
    }

    public void startServer() {

        /*
        Declare Variables
         */
        // System.out.println("Debug 1");

        /*
        STATUS CODES:
            0:  No ERRORS
            1:  MySQL Server not Reachable
            2:
         */

        try {
            out = new PrintWriter(cSocket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            in = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dbHost = "localhost";
        dbPort = 3306;
        mySql = new MySql();
        if(ping(dbHost, dbPort)){
            System.out.println("db is reachable");
        }else {
            System.exit(1);
        }
        //httpRequest = new HttpRequest();
        auth = false;
        message = new ArrayList<ArrayList<String>>();
        try {
            loopServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stopServer() {
        try {
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        out.close();
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            cSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return;
    }

    public void loopServer() throws IOException {
        INSTANCE2 = this;
        while (true) {



            input = new ArrayList<String>(readFromClient(in));
            if ("EOF".equals(input.get(0))) {
                this.cSocket.close();
                System.out.println("Client Disconnected");
                return;
                //}
                //else if ("GET".equals(input.get(0))) {
                //    input.forEach(System.out::print);
                //    System.out.print("\n");
                //    httpRequest.HttpRequest(out, input);
                //
                //
            } else if ("AUTH".equals(input.get(0))) {
                if (input.size() >= 3) {
                    if (mySql.userPasswdCheck(input.get(1), input.get(2))) {
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
                out.println("NO VALID COMMAND");
            }
        }
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

    public static void startAndStop() throws Exception {
        INSTANCE2.stopServer();
        INSTANCE2.startServer();

    }
    public boolean ping(String host, int port){

        String timeStamp = "";
        Socket socket1 = null;//from w ww.  j  a  v a 2s  . c  om
        BufferedReader br1 = null;
        try {
            socket1 = new Socket(host, port);
            br1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
            timeStamp = br1.readLine();
            socket1.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
