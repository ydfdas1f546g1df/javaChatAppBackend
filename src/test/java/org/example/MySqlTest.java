package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MySqlTest {


    @Test
    public void switchUser(){
        MySql mySql = new MySql();

        assertEquals("admin", mySql.userSwitch(0));
    }

    @Test
    public void switchUser_2(){
        MySql mySql = new MySql();

        assertEquals(0, mySql.userSwitch("admin"));
    }

    @Test
    public void switchUser_3(){
        MySql mySql = new MySql();

        assertEquals("No such user", mySql.userSwitch(900));
    }

    @Test
    public void switchUser_4(){
        MySql mySql = new MySql();

        assertEquals(-1, mySql.userSwitch("adasas"));
    }




    @Test
    public void userPasswdCheck(){
        MySql mySql = new MySql();

        assertTrue(mySql.userPasswdCheck("admin", "1234"));
    }

    @Test
    public void userPasswdCheck_2(){
        MySql mySql = new MySql();

        assertFalse(mySql.userPasswdCheck("admin", "5555"));
    }

    @Test
    public void userPasswdCheck_3(){
        MySql mySql = new MySql();

        assertFalse(mySql.userPasswdCheck("adminy", "1234"));
    }

    @Test
    public void userPasswdCheck_4(){
        MySql mySql = new MySql();

        assertTrue(mySql.userPasswdCheck(0, "1234"));
    }

    @Test
    public void userPasswdCheck_5(){
        MySql mySql = new MySql();

        assertFalse(mySql.userPasswdCheck(0, "5555"));
    }

    @Test
    public void userPasswdCheck_6(){
        MySql mySql = new MySql();

        assertFalse(mySql.userPasswdCheck(1, "1234"));
    }




    @Test
    public void checkUser(){
        MySql mySql = new MySql();

        assertTrue(mySql.checkUser("admin"));
    }

    @Test
    public void checkUser_2(){
        MySql mySql = new MySql();

        assertTrue(mySql.checkUser("x"));
    }

    @Test
    public void checkUser_3(){
        MySql mySql = new MySql();

        assertFalse(mySql.checkUser("fgxgnnhnghn"));
    }

    @Test
    public void checkUser_4(){
        MySql mySql = new MySql();

        assertTrue(mySql.checkUser(0));
    }
    @Test
    public void checkUser_5(){
        MySql mySql = new MySql();

        assertTrue(mySql.checkUser(1));
    }
    @Test
    public void checkUser_6(){
        MySql mySql = new MySql();

        assertFalse(mySql.checkUser(900));
    }




    @Test
    public void checkAdmin(){
        MySql mySql = new MySql();

        assertTrue(mySql.checkAdmin("admin"));
    }

    @Test
    public void checkAdmin_2(){
        MySql mySql = new MySql();

        assertTrue(mySql.checkAdmin(0));
    }
    @Test
    public void checkAdmin_3(){
        MySql mySql = new MySql();

        assertFalse(mySql.checkAdmin("dsddss"));
    }
    @Test
    public void checkAdmin_4(){
        MySql mySql = new MySql();

        assertFalse(mySql.checkAdmin(900));
    }
}