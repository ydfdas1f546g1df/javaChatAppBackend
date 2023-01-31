package org.example;

import org.apache.logging.log4j.Level;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SocketForServer extends Thread {
    /**
     * Declare the Variables
     */
    private java.net.Socket cSocket;
    private BufferedReader in;
    private PrintWriter out;
    ArrayList<String> input;
    private Boolean auth;
    private ArrayList<Integer> chatroomsAcces;
    private int id;
    private String username;
    private String dbHost;
    private int dbPort;
    private MySql mySql;
    private static SocketForServer INSTANCE2;


    public SocketForServer(Socket clientSocket) {
        this.cSocket = clientSocket;
    }

    public void run() {
        startServer();
        Main.logger.log(Level.INFO, "New server instance started");
    }

    public void startServer() {

        /*
        Declare Variables
         */
        // System.out.println("Debug 1");

        /*
        STATUS CODES:
            0:  No ERRORS
            103: db is not Reachable
         */
        Main.logger.log(Level.DEBUG, "Setting Variable");
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
        if (ping(dbHost, dbPort)) {
            //System.out.println("db is reachable");
            Main.logger.log(Level.INFO, "db is reachable with address and port: " + dbHost + ":" + dbPort);
        } else {
            Main.logger.log(Level.ERROR, "db is Not reachable");
            out.println("ERROR");
            stopServer();
            return;
        }
        //httpRequest = new HttpRequest();
        auth = false;
        try {
            loopServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Main.logger.log(Level.DEBUG, "Variables are set");
    }

    public void stopServer() {
        Main.logger.log(Level.INFO, "Stop server instance");
        try {
            in.close();
        } catch (IOException e) {
            Main.logger.log(Level.ERROR, e);
            throw new RuntimeException(e);

        }
        out.close();
        try {
            cSocket.close();
        } catch (IOException e) {
            Main.logger.log(Level.ERROR, e);
            throw new RuntimeException(e);
        }
        Main.logger.log(Level.DEBUG, "Instance stopped");
        return;
    }

    public void loopServer() throws IOException {
        Main.logger.log(Level.INFO, "Start loop");
        INSTANCE2 = this;
        while (true) {


            input = new ArrayList<String>(readFromClient(in));
            Main.logger.log(Level.TRACE, "New Command" + input);
            if ("EOF".equals(input.get(0))) {
                this.cSocket.close();
                Main.logger.log(Level.INFO, "Client Disconnected");
                return;
                //}
                //else if ("GET".equals(input.get(0))) {
                //    input.forEach(System.out::print);
                //    System.out.print("\n");
                //    httpRequest.HttpRequest(out, input);
                //
                //
            } else if ("AUTH".equals(input.get(0)) && !auth) {
                if (input.size() == 3) {
                    if (mySql.userPasswdCheck(input.get(1), input.get(2))) {
                        out.println("OK" + ":" + mySql.checkAdmin(input.get(1)));

                        username = input.get(1);
                        id = mySql.userSwitch(username);
                        auth = true;
                        mySql.addUserLogin(username);
                        Main.logger.log(Level.INFO, "Auth: " + username + " " + id);

                    } else {
                        out.println("ERROR");
                        Main.logger.log(Level.WARN, "Authentication error 1: User or Password is wrong");
                    }
                } else {
                    out.println("ERROR");
                    Main.logger.log(Level.DEBUG, "Authentication error 2: False amount of Arguments");
                }
            } else if ("MSG".equals(input.get(0))) {

                if (auth) {
                    if (input.size() >= 2) {
                        if (input.size() == 5) {
                            Main.logger.log(Level.DEBUG, "SEND MESSAGE");
                            if ("SEND".equals(input.get(1))) { // save to database
                                //System.out.println("SEND-");
                                mySql.newMsg(Integer.parseInt(input.get(2)), id, mergeMsg(input));


                            }
                        } else if (input.size() == 3) {
                            if ("ALL".equals(input.get(1))) {
                                out.println(mySql.getAllFormChatroom(Integer.parseInt(input.get(2))));
                            }
                        }
                    } else {
                        out.println("ERROR");
                        Main.logger.log(Level.DEBUG, "False amount of arguments");
                    }
                } else {
                    out.println("ERROR");
                    Main.logger.log(Level.WARN, "Authentication error");
                }

            } else if ("DEAUTH".equals(input.get(0)) && auth) {
                Main.logger.log(Level.INFO, "DeAuth: " + username + " " + id);
                auth = false;
                id = 0;
                username = null;


            } else if ("USER".equals(input.get(0))) {
                if (auth) {
                    if (mySql.checkAdmin(username)) {
                        if (input.size() == 4) {
                            if ("CREATE".equals(input.get(1))) {
                                Main.logger.log(Level.WARN, "User " + username + " Created new User: " + input.get(2));
                                mySql.newUser(input.get(2), input.get(3));
                                out.println(mySql.userSwitch(input.get(2)));
                            }
                        } else if ("LIST".equals(input.get(1))) {
                            out.println(mySql.listUsers());
                        }else if ("DELETE".equals(input.get(1))&& mySql.checkAdmin(username)) {
                            mySql.deleteUser(input.get(2));
                        }
                        else {
                            out.println("ERROR");
                        }
                    } else {
                        out.println("ERROR");
                        Main.logger.log(Level.ERROR, username + " tried to do something with higher rights.");
                    }
                } else {
                    out.println("ERROR");
                }


            } else if ("GROUP".equals(input.get(0))) {
                if (input.size() == 4) {
                    if (auth) {
                        if (mySql.checkAdmin(username)) {
                            if ("ADDUSER".equals(input.get(1))) {
                                try {
                                    mySql.addUserToGroup(input.get(2), Integer.parseInt(input.get(3)));
                                } catch (Exception e) {
                                    System.err.println("ERROR: " + e);
                                }
                            } else if ("DELUSER".equals(input.get(1))) {
                                mySql.removeUserFromGroup(input.get(2), Integer.parseInt(input.get(3)));
                            }
                        } else {
                            out.println("ERROR");
                            Main.logger.log(Level.ERROR, username + " tried to do something with higher rights.");
                        }
                    } else {
                        out.println("ERROR");
                    }

                } else if (input.size() > 4) {
                    out.println("ERROR");
                } else {
                    if (input.size() == 2) {
                        if (auth) {
                            if ("ALL".equals(input.get(1))) {
                                out.println(formatter(mySql.roomListOfUser(username)));

                            }

                            if ("NAME".equals(input.get(1))) {
                                mySql.groupSwitch(input.get(2));
                            }

                            if (mySql.checkAdmin(username) && "LIST".equals(input.get(1))) {
                                out.println(mySql.listRooms());
                            }
                        } else {
                            out.println("ERROR");
                        }
                    } else {
                        out.println("ERROR");
                    }
                }

            } else if ("PENDING".equals(input.get(0))) {
                if (input.get(1).equals("ADD")) {
                    mySql.addPendingUser(input.get(2));
                } else if (input.get(1).equals("DELETE")) {
                    mySql.deletePendingUser(input.get(2));
                } else if (input.get(1).equals("LIST")) {
                    out.println(mySql.listPendingUser());
                } else if (input.get(1).equals("CONVERT") && mySql.checkAdmin(username)) {
                    mySql.convertPendingUser(input.get(2), input.get(3));
                }

            } else if ("STAT".equals(input.get(0))) {
                if ("U".equals(input.get(1))){
                    out.println(mySql.userCount());
                }else if ("G".equals(input.get(1))){
                    out.println(mySql.groupCount());
                }else if ("M".equals(input.get(1))){
                    out.println(mySql.messageCount());
                }else if ("L".equals(input.get(1))){
                    out.println(mySql.countUserLogins());
                }

            } else if ("GET".equals(input.get(0))) {
                out.println("MSG;RECIEVE;12:0:0:dsfsf;");
            } else {
                Main.logger.log(Level.INFO, "ERROR FALSE INPUT:" + input);
                out.println("ERROR");
            }
        }

    }

    public ArrayList<String> readFromClient(BufferedReader inS) throws IOException {
        Main.logger.log(Level.TRACE, "Reading from Client");
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
        Main.logger.log(Level.DEBUG, "Start and Stop instance");
        INSTANCE2.stopServer();
        INSTANCE2.startServer();

    }

    public String mergeMsg(ArrayList<String> zz) {
        String zzz;
        zzz = "";

        for (int i = 4; i < zz.size(); i++) {
            zzz += zz.get(i);
            if(i+1<zz.size()){
                zzz += " ";
            }
        }
        return zzz;
    }

    public boolean ping(String host, int port) {
        Main.logger.log(Level.DEBUG, "Ping: " + host + " on Port: " + port);

        String timeStamp = "";
        Socket socket1 = null;
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

    public String formatter(ArrayList<Integer> x) {
        ArrayList<String> names;
        names = new ArrayList<>();
        String output = "";

        for (int i = 0; i < x.size(); i++) {
            names.add(mySql.groupSwitch(x.get(i)));
        }

        for (int i = 0; i < x.size(); i++) {
            output += x.get(Integer.parseInt(i + ":"));
            output += names.get(Integer.parseInt(i + ";"));
        }
        return output;
    }
}
