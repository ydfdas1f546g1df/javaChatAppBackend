package org.example;

import org.apache.logging.log4j.Level;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class MySql {
    String sqlSelectAllPersons;
    String connectionUrlMessages;
    String dbUser;
    String dbpasswd;


//    public static void main(String[] args) {
//        MySql mySql = new MySql();
//    }

    public MySql() {
        Main.logger.log(Level.DEBUG, "Initialize Mysql module");
        int PORT = 3306;
        dbUser = "admin";
        dbpasswd = "1234";
        connectionUrlMessages = connectDB("localhost", PORT, "messages");
    }

    public String connectDB(String host, int port, String db) {

        return "jdbc:mysql://" + host + ":" + port + "/" + db;
    }


    //overloading
    public String userSwitch(int id) {
        Main.logger.log(Level.DEBUG, "mySql: ID to username");
        String str;
        String sql = "SELECT * FROM user";
        try (Connection conn = DriverManager.getConnection(connectionUrlMessages, dbUser, dbpasswd); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                if (Objects.equals(rs.getInt("id"), id)) {
                    str = rs.getString("username");
                    conn.close();
                    return str;
                }
            }
        } catch (SQLException e) {
            Main.logger.log(Level.WARN, e);
            // handle the exception
            System.err.println("error" + e);
            return "No such user";
        }
        return "No such user";
    }

    public int userSwitch(String username) {
        Main.logger.log(Level.DEBUG, "mySql: username to ID");
        int id;
        String sql = "SELECT * FROM user";
        try (Connection conn = DriverManager.getConnection(connectionUrlMessages, dbUser, dbpasswd); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                if (Objects.equals(rs.getString("username"), username)) {
                    id = rs.getInt("id");
                    conn.close();
                    return id;
                }
            }
        } catch (SQLException e) {
            // handle the exception
            System.err.println("error" + e);
            Main.logger.log(Level.WARN, e);
            return -1;
        }
        return -1;
    }

    public boolean userPasswdCheck(String username, String password) {
        Main.logger.log(Level.INFO, "Check password from " + username);
        password = encrypting.encryptSHA512(password);
        sqlSelectAllPersons = "SELECT * FROM user";
        try (Connection conn = DriverManager.getConnection(connectionUrlMessages, dbUser, dbpasswd); PreparedStatement ps = conn.prepareStatement(sqlSelectAllPersons); ResultSet rs = ps.executeQuery()) {

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
            Main.logger.log(Level.WARN, e);
            return false;
        }

    }

    public boolean userPasswdCheck(int id, String password) {
        Main.logger.log(Level.INFO, "Check password from " + id);
        password = encrypting.encryptSHA512(password);
        sqlSelectAllPersons = "SELECT * FROM user";
        try (Connection conn = DriverManager.getConnection(connectionUrlMessages, dbUser, dbpasswd); PreparedStatement ps = conn.prepareStatement(sqlSelectAllPersons); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                if (Objects.equals(rs.getInt("ID"), id) && Objects.equals(rs.getString("password"), password)) {
                    conn.close();
                    return true;
                }
            }
            conn.close();
            return false;
        } catch (SQLException e) {
            // handle the exception
            System.err.println("error" + e);
            Main.logger.log(Level.WARN, e);
            return false;
        }

    }

    public boolean checkUser(String username) {
        Main.logger.log(Level.INFO, "Check if user in db " + username);
        sqlSelectAllPersons = "SELECT * FROM user";
        try (Connection conn = DriverManager.getConnection(connectionUrlMessages, dbUser, dbpasswd); PreparedStatement ps = conn.prepareStatement(sqlSelectAllPersons); ResultSet rs = ps.executeQuery()) {

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
            Main.logger.log(Level.WARN, e);
            return false;
        }
    }

    public boolean checkUser(int id) {
        Main.logger.log(Level.INFO, "Check if user in db " + id);
        sqlSelectAllPersons = "SELECT * FROM user";
        try (Connection conn = DriverManager.getConnection(connectionUrlMessages, dbUser, dbpasswd); PreparedStatement ps = conn.prepareStatement(sqlSelectAllPersons); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                if (Objects.equals(rs.getInt("ID"), id)) {
                    conn.close();
                    return true;
                } else {
                    continue;
                }
            }
            conn.close();
            return false;
        } catch (SQLException e) {
            Main.logger.log(Level.WARN, e);
            // handle the exception
            System.err.println("error" + e);
            return false;
        }
    }

    public boolean checkAdmin(String username) {
        Main.logger.log(Level.INFO, "Check if User " + username + " is Admin");
        sqlSelectAllPersons = "SELECT * FROM admins";
        try (Connection conn = DriverManager.getConnection(connectionUrlMessages, dbUser, dbpasswd); PreparedStatement ps = conn.prepareStatement(sqlSelectAllPersons); ResultSet rs = ps.executeQuery()) {

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
            Main.logger.log(Level.WARN, e);
            // handle the exception
            System.err.println("error" + e);
            return false;
        }
    }

    public boolean checkAdmin(int id) {
        String s = userSwitch(id);
        return checkAdmin(s);
    }

    public String listUsers() {

        Main.logger.log(Level.DEBUG, "Admin requests userList");
        StringBuilder rommlst;
        String sqlx = "Select * from user";
        rommlst = new StringBuilder();

        try (Connection conn = DriverManager.getConnection(connectionUrlMessages, dbUser, dbpasswd); PreparedStatement ps = conn.prepareStatement(sqlx); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rommlst.append(rs.getString("ID")).append(":").append(rs.getString("username")).append(";");

            }
        } catch (SQLException e) {
            // handle the exception
            Main.logger.log(Level.WARN, e);
            System.err.println("error" + e);

        }


        return rommlst.toString();
    }

    public void newUser(String username, String password) {
        if (!checkUser(username) && !checkPUser(username)) {
            Main.logger.log(Level.INFO, "Create new User: " + username);
            password = encrypting.encryptSHA512(password);
            try {
                Connection conn = DriverManager.getConnection(connectionUrlMessages, dbUser, dbpasswd);

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
                Main.logger.log(Level.WARN, e);
            }
        }
        System.out.println("User " + username + " exists already");
    }

    public void deleteUser(String username) {
        if (true) {
            Main.logger.log(Level.INFO, "Delete User: " + username);
            try {
                Connection conn = DriverManager.getConnection(connectionUrlMessages, dbUser, dbpasswd);

                String sql = "DELETE FROM user WHERE username = \"" + username + "\";";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.execute();

                conn.close();
            } catch (Exception e) {
                System.err.println("ERROR: " + e);
                Main.logger.log(Level.WARN, e);
            }
        }
    }

    // For Messages

    public void newMsg(int roomId, int userId, String msg) {
        Main.logger.log(Level.DEBUG, "New Message From: " + userId);
        try {
            Connection conn = DriverManager.getConnection(connectionUrlMessages, dbUser, dbpasswd);

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
            Main.logger.log(Level.DEBUG, "ERROR: " + e);
        }
    }

    public String getAllFormChatroom(int chatroomId) {
        Main.logger.log(Level.DEBUG, "Someone wants all Message from Room: " + chatroomId);
        String str = "";
        sqlSelectAllPersons = "SELECT * FROM chatapp WHERE roomid = " + chatroomId + ";";
        try (Connection conn = DriverManager.getConnection(connectionUrlMessages, dbUser, dbpasswd); PreparedStatement ps = conn.prepareStatement(sqlSelectAllPersons); ResultSet rs = ps.executeQuery()) {

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
            Main.logger.log(Level.DEBUG, "error" + e);
        }
        return str;
    }

    //SQL

    public void sqlCommand(String sql) {
        Main.logger.log(Level.INFO, "SQL Command: " + sql);
        try {
            Connection conn = DriverManager.getConnection(connectionUrlMessages, dbUser, dbpasswd);
            PreparedStatement pst = conn.prepareStatement(sql);
            //System.out.println(pst.execute());

            conn.close();
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
            Main.logger.log(Level.WARN, e);
        }
    }

    // Room management

    public String listRooms() {
        Main.logger.log(Level.DEBUG, "Admin requests roomList");
        String rommlst;
        String sqlx = "Select * from groups";
        rommlst = "";

        try (Connection conn = DriverManager.getConnection(connectionUrlMessages, dbUser, dbpasswd); PreparedStatement ps = conn.prepareStatement(sqlx); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rommlst += rs.getString(1) + ":" + rs.getString(2) + ";";

            }
        } catch (SQLException e) {
            // handle the exception
            Main.logger.log(Level.WARN, e);
            System.err.println("error" + e);

        }


        return rommlst;
    }

    public ArrayList<Integer> roomListOfUser(String username) {
        Main.logger.log(Level.DEBUG, "Roomlist from " + username + " is wanted.");
        sqlSelectAllPersons = "SELECT * FROM user";
        String str;
        str = "";
        ArrayList<Integer> x;
        x = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(connectionUrlMessages, dbUser, dbpasswd); PreparedStatement ps = conn.prepareStatement(sqlSelectAllPersons); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                if (Objects.equals(rs.getString("username"), username)) {
                    str = rs.getString("Rooms");
                    conn.close();
                    break;
                }
            }
        } catch (SQLException e) {
            // handle the exception
            Main.logger.log(Level.WARN, e);
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
        Main.logger.log(Level.DEBUG, "Roomlist from " + id + " is wanted.");
        sqlSelectAllPersons = "SELECT * FROM user";
        String str;
        str = "";
        ArrayList<Integer> x;
        x = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(connectionUrlMessages, dbUser, dbpasswd); PreparedStatement ps = conn.prepareStatement(sqlSelectAllPersons); ResultSet rs = ps.executeQuery()) {

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
            Main.logger.log(Level.WARN, e);

        }
        if (str != null) {
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
        } else {
            ArrayList<Integer> e = new ArrayList<>();
            return e;
        }
    }

    public void addUserToGroup(String username, int RoomId) {
        Main.logger.log(Level.DEBUG, "Add user " + username + " to Group " + RoomId + ".");
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
        Main.logger.log(Level.DEBUG, "Add user " + id + " to Group " + RoomId + ".");
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
        Main.logger.log(Level.DEBUG, "Remove user " + id + " to Group " + groupId + ".");
        String username = userSwitch(id);
        if (!checkUser(username)) {
            System.err.println("No such User: " + username);
        } else {
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
                        //System.out.println(s);
                        String sql = "UPDATE user SET Rooms=\"" + s + "\" WHERE username=\"" + username + "\";";
                        sqlCommand(sql);
                    }
                } else {
                    System.err.println("User is not in Group: " + groupId);
                }
            }
        }
    }

    public void removeUserFromGroup(String username, int groupId) {
        Main.logger.log(Level.DEBUG, "Remove user " + username + " to Group " + groupId + ".");
        if (!checkUser(username)) {
            System.err.println("No such User: " + username);
        } else {
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
                        //System.out.println(s);
                        String sql = "UPDATE user SET Rooms=\"" + s + "\" WHERE username=\"" + username + "\";";
                        sqlCommand(sql);
                    }
                } else {
                    System.err.println("User is not in Group: " + groupId);
                }
            }
        }
    }

    public String groupSwitch(int id) {
        Main.logger.log(Level.DEBUG, "mySql: ID to groupname");
        String str;
        String sql = "SELECT * FROM groups";
        try (Connection conn = DriverManager.getConnection(connectionUrlMessages, dbUser, dbpasswd); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                if (Objects.equals(rs.getInt("id"), id)) {
                    str = rs.getString("name");
                    conn.close();
                    return str;
                }
            }
            conn.close();
        } catch (SQLException e) {
            Main.logger.log(Level.WARN, e);
            // handle the exception
            System.err.println("error" + e);
            return "No such group";
        }
        return "No such group";
    }

    public int groupSwitch(String groupname) {
        Main.logger.log(Level.DEBUG, "mySql: groupname to ID");
        int id;
        String sql = "SELECT * FROM groups";
        try (Connection conn = DriverManager.getConnection(connectionUrlMessages, dbUser, dbpasswd); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                if (Objects.equals(rs.getString("username"), groupname)) {
                    id = rs.getInt("id");
                    conn.close();
                    return id;
                }
            }
            conn.close();
        } catch (SQLException e) {
            // handle the exception
            System.err.println("error" + e);
            Main.logger.log(Level.WARN, e);
            return -1;
        }
        return -1;
    }

    // Pending Users

    public boolean checkPUser(String username) {
        Main.logger.log(Level.INFO, "Check if pUser in db " + username);
        String sqlc = "SELECT * FROM `pendingusers`;";
        try (Connection conn = DriverManager.getConnection(connectionUrlMessages, dbUser, dbpasswd); PreparedStatement ps = conn.prepareStatement(sqlc); ResultSet rs = ps.executeQuery()) {

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
            Main.logger.log(Level.WARN, e);
            // handle the exception
            System.err.println("error" + e);
            return false;
        }
    }

    public void addPendingUser(String username) {
        if (!checkUser(username)) {
            if (!checkPUser(username)) {
                Main.logger.log(Level.INFO, "Create new PendingUser: " + username);
                try {
                    Connection conn = DriverManager.getConnection(connectionUrlMessages, dbUser, dbpasswd);
                    String sql = "INSERT INTO `pendingusers` (`username`) VALUES (?)";
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, username);
                    pst.execute();
                    conn.close();
                    return;
                } catch (Exception e) {
                    System.err.println("ERROR: " + e);
                    Main.logger.log(Level.WARN, e);
                }
            }
        }
        System.out.println("User " + username + " exists already");
    }

    public String listPendingUser() {
        Main.logger.log(Level.DEBUG, "Admin requests pUserList");
        String rommlst;
        String sqlx = "SELECT * FROM `pendingusers`;";
        rommlst = "";

        try (Connection conn = DriverManager.getConnection(connectionUrlMessages, dbUser, dbpasswd); PreparedStatement ps = conn.prepareStatement(sqlx); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rommlst += rs.getString("username") + ";";

            }
        } catch (SQLException e) {
            // handle the exception
            Main.logger.log(Level.WARN, e);
            System.err.println("error" + e);

        }
        return rommlst;
    }

    public void deletePendingUser(String username) {
        if (checkPUser(username)) {
            Main.logger.log(Level.INFO, "Delete pendingUser: " + username);
            try {
                Connection conn = DriverManager.getConnection(connectionUrlMessages, dbUser, dbpasswd);

                String sql = "DELETE FROM `pendingusers` WHERE username = \"" + username + "\";";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.execute();

                conn.close();
            } catch (Exception e) {
                System.err.println("ERROR: " + e);
                Main.logger.log(Level.WARN, e);
            }
        }
    }

    public void convertPendingUser(String username, String pw) {
        deletePendingUser(username);
        newUser(username, pw);
    }

    //Admin

    public String listAdmin() {
        Main.logger.log(Level.DEBUG, "User requests AdminList");
        String rommlst;
        String sqlx = "Select * from admin";
        rommlst = "";

        try (Connection conn = DriverManager.getConnection(connectionUrlMessages, dbUser, dbpasswd); PreparedStatement ps = conn.prepareStatement(sqlx); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rommlst += rs.getString("username") + ";";

            }
        } catch (SQLException e) {
            // handle the exception
            Main.logger.log(Level.WARN, e);
            System.err.println("error" + e);

        }


        return rommlst;
    }

    public void removeAdmin(String username) {
        if (checkAdmin(username)) {
            Main.logger.log(Level.INFO, "Remove Admin: " + username);
            try {
                Connection conn = DriverManager.getConnection(connectionUrlMessages, dbUser, dbpasswd);

                String sql = "DELETE FROM `admins` WHERE username = \"" + username + "\";";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.execute();

                conn.close();
            } catch (Exception e) {
                System.err.println("ERROR: " + e);
                Main.logger.log(Level.WARN, e);
            }
        }
    }

    public void addAdmin(String username) {
        if (!checkAdmin(username) && checkUser(username) && !checkPUser(username)) {
            Main.logger.log(Level.INFO, "Add Admin: " + username);
            try {
                Connection conn = DriverManager.getConnection(connectionUrlMessages, dbUser, dbpasswd);
                String sql = "INSERT INTO `admins` (`username`) VALUES (?)";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, username);
                pst.execute();
                conn.close();
                return;
            } catch (Exception e) {
                System.err.println("ERROR: " + e);
                Main.logger.log(Level.WARN, e);
            }
        }
        System.out.println("Admin " + username + " exists already");
    }

    //statistics

    public int userCount() {
        String[] x = listUsers().split(";");
        if(Objects.equals(x[0], "")){return 0;}
        return x.length;
    }
    public int groupCount(){
        String[] x = listRooms().split(";");
        if(Objects.equals(x[0], "")){return 0;}
        return x.length;
    }

    public int pendingCount(){
        String[] x = listPendingUser().split(";");
        if(Objects.equals(x[0], "")){return 0;}
        return x.length;
    }

    public int messageCount(){
        int x = 0;
        Main.logger.log(Level.DEBUG, "Ounting Messages");
        sqlSelectAllPersons = "SELECT * FROM chatapp;";
        try (Connection conn = DriverManager.getConnection(connectionUrlMessages, dbUser, dbpasswd); PreparedStatement ps = conn.prepareStatement(sqlSelectAllPersons); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                x += 1;
            }
            conn.close();
        } catch (SQLException e) {
            // handle the exception
            Main.logger.log(Level.DEBUG, "error" + e);
        }
        return x;
    }

    public String countUserLogins(){
        Main.logger.log(Level.DEBUG, "Admin requests userLoginOCunt");
        String rommlst;
        String sqlx = "Select * from user";
        rommlst = "";

        try (Connection conn = DriverManager.getConnection(connectionUrlMessages, dbUser, dbpasswd); PreparedStatement ps = conn.prepareStatement(sqlx); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rommlst += rs.getString("username") + ":" + rs.getString("loginCount") + ";";

            }
        } catch (SQLException e) {
            // handle the exception
            Main.logger.log(Level.WARN, e);
            System.err.println("error" + e);

        }


        return rommlst;
    }

    public void addUserLogin(String username){
        Main.logger.log(Level.INFO, "Add login to: " + username);
            try {
                Connection conn = DriverManager.getConnection(connectionUrlMessages, dbUser, dbpasswd);

                String sql = "UPDATE user SET loginCount = loginCount+1 WHERE username = '" +username+ "';";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.execute();

                conn.close();
            } catch (Exception e) {
                System.err.println("ERROR: " + e);
                Main.logger.log(Level.WARN, e);
            }
    }
    public void changePW(String username, String password) {
        password = encrypting.encryptSHA512(password);
        Main.logger.log(Level.INFO, "Change login from: " + username);
            try {
                Connection conn = DriverManager.getConnection(connectionUrlMessages, dbUser, dbpasswd);

                String sql = "UPDATE user SET password = " + password + " WHERE username = '" + username + "';";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.execute();

                conn.close();
            } catch (Exception e) {
                System.err.println("ERROR: " + e);
                Main.logger.log(Level.WARN, e);
            }

    }

    public static void main(String[] args) {
        MySql mySql = new MySql();

        System.out.println(mySql.countUserLogins());
    }


}
