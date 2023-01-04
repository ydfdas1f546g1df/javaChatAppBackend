package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class MySql {
    String sqlSelectAllPersons;
    String connectionUrl;
    private final String dbUsername;
    private final String dbPassword;


    public static void main(String[] args) {
        MySql mySql = new MySql();
        System.out.println(mySql.userPasswdCheck("admin", "1234"));
        mySql.newUser("x", "x");
    }

    public MySql() {
        dbUsername = "admin";
        dbPassword = "1234";
        connectionUrl = connectDB("localhost", 3306, "messages");
    }

    public String connectDB(String host, int port, String db){

        return "jdbc:mysql://"+ host +":"+ port +"/"+ db;
    }

    public boolean userPasswdCheck(String username, String password){
        sqlSelectAllPersons = "SELECT * FROM user";
        try (Connection conn = DriverManager.getConnection(connectionUrl, "admin", "1234");
             PreparedStatement ps = conn.prepareStatement(sqlSelectAllPersons);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                if (Objects.equals(rs.getString("username"), username) && Objects.equals(rs.getString("password"), password)){
                    conn.close();
                    return true;
                }else{
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

    public void newUser(String username, String password){
        try{
            Connection conn = DriverManager.getConnection(connectionUrl, "admin", "1234");

            PreparedStatement ps = conn.prepareStatement("SELECT * FROM user;");
            ResultSet rs = ps.executeQuery();

            ArrayList<Integer> arrayList;
            arrayList = new ArrayList<Integer>();
            while (rs.next()){
                arrayList.add(rs.getInt("id"));
            }
            //System.out.println(arrayList);

            int id = arrayList.size();
            System.out.println(id);

            String sql = "insert into user (ID, username, password) values (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            pst.setString(2, username);
            pst.setString(3, password);
            pst.execute();

            conn.close();
        }catch (Exception e){
            System.err.println("ERROR: " + e);
        }

    }
}
