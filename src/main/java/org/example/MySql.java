package org.example;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class MySql {
    String sqlSelectAllPersons;
    String connectionUrlMessages;
    private final String dbUsername;
    private final String dbPassword;
    private final int PORT;


//    public static void main(String[] args) {
//        MySql mySql = new MySql();
//    }

    public MySql() {
        PORT = 3306;
        dbUsername = "admin";
        dbPassword = "1234";
        connectionUrlMessages = connectDB("localhost", PORT, "messages");
    }

    public String connectDB(String host, int port, String db) {

        return "jdbc:mysql://" + host + ":" + port + "/" + db;
    }


    //overloading
    public String userSwitch(int id) {
        String str;
        String sql = "SELECT * FROM user";
        try (Connection conn = DriverManager.getConnection(connectionUrlMessages, "admin", "1234"); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                if (Objects.equals(rs.getInt("id"), id)) {
                    str = rs.getString("username");
                    conn.close();
                    return str;
                }
            }
            conn.close();
        } catch (SQLException e) {
            // handle the exception
            System.err.println("error" + e);
            return "No such user";
        }
        return "No such user";
    }

    public int userSwitch(String username) {
        int id;
        String sql = "SELECT * FROM user";
        try (Connection conn = DriverManager.getConnection(connectionUrlMessages, "admin", "1234"); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                if (Objects.equals(rs.getString("username"), username)) {
                    id = rs.getInt("id");
                    conn.close();
                    return id;
                }
            }
            conn.close();
        } catch (SQLException e) {
            // handle the exception
            System.err.println("error" + e);
            return -1;
        }
        return -1;
    }

    public boolean userPasswdCheck(String username, String password) {
        password = encrypting.encryptSHA512(password);
        sqlSelectAllPersons = "SELECT * FROM user";
        try (Connection conn = DriverManager.getConnection(connectionUrlMessages, "admin", "1234"); PreparedStatement ps = conn.prepareStatement(sqlSelectAllPersons); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                if (Objects.equals(rs.getString("username"), username) && Objects.equals(rs.getString("password"), password)) {
                    conn.close();
                    return true;
                }
            }
            conn.close();
            return false;
        } catch (SQLException e) {
            // handle the exception
            System.err.println("error" + e);
            return false;
        }

    }

    public boolean checkUser(String username) {
        sqlSelectAllPersons = "SELECT * FROM user";
        try (Connection conn = DriverManager.getConnection(connectionUrlMessages, "admin", "1234"); PreparedStatement ps = conn.prepareStatement(sqlSelectAllPersons); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                if (Objects.equals(rs.getString("username"), username)) {
                    conn.close();
                    return true;
                } else {
                    continue;
                }
            }
            conn.close();
            return false;
        } catch (SQLException e) {
            // handle the exception
            System.err.println("error" + e);
            return false;
        }
    }

    public void newUser(String username, String password) {
        if (!checkUser(username)) {
            password = encrypting.encryptSHA512(password);
            try {
                Connection conn = DriverManager.getConnection(connectionUrlMessages, "admin", "1234");

                PreparedStatement ps = conn.prepareStatement("SELECT * FROM user;");
                ResultSet rs = ps.executeQuery();

                ArrayList<Integer> arrayList;
                arrayList = new ArrayList<Integer>();
                while (rs.next()) {
                    arrayList.add(rs.getInt("id"));
                }
                //System.out.println(arrayList);

                int id = arrayList.size();
                //System.out.println(id);

                String sql = "insert into user (ID, username, password) values (?, ?, ?)";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, id);
                pst.setString(2, username);
                pst.setString(3, password);
                pst.execute();

                conn.close();
                return;
            } catch (Exception e) {
                System.err.println("ERROR: " + e);
            }
        }
        System.out.println("User " + username + " exists already");
    }

    public void deleteUser(String username, String password) {
        if (userPasswdCheck(username, password)) {
            try {
                Connection conn = DriverManager.getConnection(connectionUrlMessages, "admin", "1234");

                String sql = "DELETE FROM user WHERE username = \"" + username + "\";";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.execute();

                conn.close();
            } catch (Exception e) {
                System.err.println("ERROR: " + e);
            }
        }
    }

    // For Messages

    public void newMsg(int roomId, int userId, String msg) {
        try {
            Connection conn = DriverManager.getConnection(connectionUrlMessages, "admin", "1234");

            String sql = "insert into chatapp (timestamp, roomid, userid, message) values (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);

            long time = System.currentTimeMillis();
            java.sql.Timestamp timestamp = new java.sql.Timestamp(time);

            pst.setTimestamp(1, timestamp);
            pst.setInt(2, roomId);
            pst.setInt(3, userId);
            pst.setString(4, msg);
            pst.execute();

            conn.close();
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
        }
    }

    public String getAllFormChatroom(int chatroomId) {
        String str = "";
        sqlSelectAllPersons = "SELECT * FROM chatapp WHERE roomid = " + chatroomId + ";";
        try (Connection conn = DriverManager.getConnection(connectionUrlMessages, "admin", "1234"); PreparedStatement ps = conn.prepareStatement(sqlSelectAllPersons); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                str += rs.getTimestamp(1).getTime();
                str += ":";
                str += rs.getString(2);
                str += ":";
                str += rs.getString(3);
                str += ":";
                str += rs.getString(4);
                str += ";";
            }
            //System.out.println(str);
            conn.close();
        } catch (SQLException e) {
            // handle the exception
            System.err.println("error" + e);
        }
        return str;
    }

    //SQL

    public void sqlCommand(String sql) {
        try {
            Connection conn = DriverManager.getConnection(connectionUrlMessages, "admin", "1234");
            PreparedStatement pst = conn.prepareStatement(sql);
            System.out.println(pst.execute());

            conn.close();
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
        }
    }

    // Room management

    public ArrayList<Integer> roomListOfUser(String username) {
        sqlSelectAllPersons = "SELECT * FROM user";
        String str;
        str = "";
        ArrayList<Integer> x;
        x = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(connectionUrlMessages, "admin", "1234"); PreparedStatement ps = conn.prepareStatement(sqlSelectAllPersons); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                if (Objects.equals(rs.getString("username"), username)) {
                    str = rs.getString("Rooms");
                    conn.close();
                    break;
                }
            }
        } catch (SQLException e) {
            // handle the exception

            System.err.println("error" + e);

        }
        if (str == null) {
            x = new ArrayList<>();
            return x;
        } else {
            String[] s = str.split(":");
            int size = s.length;
            int[] arr = new int[size];
            if (s.length <= 1) {
                return x;
            } else {
                //System.out.println(s.length + " " + str);
                for (int i = 0; i < size; i++) {
                    arr[i] = Integer.parseInt(s[i]);
                }
                for (int j : arr) {
                    x.add(j);
                }
                //System.out.println(x);
                return x;
            }
        }
    }

    public ArrayList<Integer> roomListOfUser(Integer id) {
        sqlSelectAllPersons = "SELECT * FROM user";
        String str;
        str = "";
        ArrayList<Integer> x;
        x = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(connectionUrlMessages, "admin", "1234"); PreparedStatement ps = conn.prepareStatement(sqlSelectAllPersons); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                if (Objects.equals(rs.getInt("ID"), id)) {
                    str = rs.getString("Rooms");
                    conn.close();
                    break;
                }
            }
        } catch (SQLException e) {
            // handle the exception
            System.err.println("error" + e);

        }
        String[] s = str.split(":");
        int size = s.length;
        int[] arr = new int[size];
        if (s.length <= 1) {
            return x;
        } else {
            //System.out.println(s.length + " " + str);
            for (int i = 0; i < size; i++) {
                arr[i] = Integer.parseInt(s[i]);
            }
            for (int j : arr) {
                x.add(j);
            }
            //System.out.println(x);
            return x;
        }
    }

    public void addUserToGroup(String username, int RoomId) {
        ArrayList<Integer> groups;
        String s = "";
        groups = new ArrayList<>();
        groups = roomListOfUser(username);


        s += RoomId;
        if (!(groups.size() == 0)) {
            for (Integer group : groups) {
                s += ":";
                s += group;
            }
        }
        String sql = "UPDATE user SET Rooms=\"" + s + "\" WHERE username=\"" + username + "\";";
        sqlCommand(sql);
        //System.out.println(s);
    }

    public void addUserToGroup(int id, int RoomId) {
        ArrayList<Integer> groups;
        String s = "";
        groups = new ArrayList<>();
        groups = roomListOfUser(id);
        if (groups.contains(RoomId)) {
            System.err.println("User is already in this Room");
            return;
        }
        s += RoomId;

        for (Integer group : groups) {
            s += ":";
            s += group;
        }

        String sql = "UPDATE user SET Rooms=\"" + s + "\" WHERE ID=" + id + ";";
        sqlCommand(sql);
        //System.out.println(s);
    }

    public void removeUserFromGroup(int id, int groupId) {
        ArrayList<Integer> groups;
        String s = "";
        groups = roomListOfUser(id);

        //System.out.println(groups + " " + groups.size());
        if (groups.size() == 0) {
            System.out.println("Update to Zero");
            String sql = "UPDATE user SET Rooms=NULL WHERE ID=" + id;
            System.out.println(sql);
            sqlCommand(sql);
        } else {
            System.out.println(groups.get(0));
            if (groups.contains(groupId)) {
                groups.remove(Integer.valueOf(groupId));
                if (groups.size() == 0) {
                    String sql = "UPDATE user SET Rooms=NULL WHERE ID=" + id;
                    sqlCommand(sql);
                } else {
                    s += groups.get(0);
                    for (int i = 1; i <= groups.size() - 1; i++) {
                        s += ":";
                        s += groups.get(i);
                    }
                    System.out.println(s);
                    String sql = "UPDATE user SET Rooms=\"" + s + "\" WHERE ID=" + id + ";";
                    sqlCommand(sql);
                }
            } else {
                System.err.println("User is not in Group: " + groupId);
            }
        }
    }

    public void removeUserFromGroup(String username, int groupId) {
        ArrayList<Integer> groups;
        String s = "";
        groups = roomListOfUser(username);
        //System.out.println(groups + " " + groups.size());
        if (groups.size() == 0) {
            String sql = "UPDATE user SET Rooms= NULL WHERE username=\"" + username + "\";";
            sqlCommand(sql);
        } else {
            if (groups.contains(groupId)) {
                groups.remove(Integer.valueOf(groupId));
                if (groups.size() == 0) {
                    String sql = "UPDATE user SET Rooms=NULL WHERE username=\"" + username + "\";";
                    sqlCommand(sql);
                } else {
                    s += groups.get(0);
                    for (int i = 1; i <= groups.size() - 1; i++) {
                        s += ":";
                        s += groups.get(i);
                    }
                    System.out.println(s);
                    String sql = "UPDATE user SET Rooms=\"" + s + "\" WHERE username=\"" + username + "\";";
                    sqlCommand(sql);
                }
            } else {
                System.err.println("User is not in Group: " + groupId);
            }
        }
    }


    public static void main(String[] args) {
        MySql mySql = new MySql();
        //mySql.addUserToGroup("x", 1);

        mySql.removeUserFromGroup(0,1);

    }
}
