package org.example;

import org.junit.jupiter.api.Test;

import java.util.EmptyStackException;

import  static org.junit.jupiter.api.Assertions.*;

class encryptingTest {
    @Test
    public void SHA512(){
        assertEquals("4dff4ea340f0a823f15d3f4f01ab62eae0e5da579ccb851f8db9dfe84c58b2b37b89903a740e1ee172da793a6e79d560e5f7f9bd058a12a280433ed6fa46510a", Encrypting.encryptSHA512("1"));
    }

    @Test
    public void SHA512_2(){
        assertEquals("06df05371981a237d0ed11472fae7c94c9ac0eff1d05413516710d17b10a4fb6f4517bda4a695f02d0a73dd4db543b4653df28f5d09dab86f92ffb9b86d01e25" , Encrypting.encryptSHA512("5"));
    }

    @Test
    public void SHA512_3(){
        assertEquals("f05210c5b4263f0ec4c3995bdab458d81d3953f354a9109520f159db1e8800bcd45b97c56dce90a1fc27ab03e0b8a9af8673747023c406299374116d6f966981" , Encrypting.encryptSHA512("7"));
    }

    @Test
    public void SHA256(){
        assertEquals("6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b", Encrypting.encryptSHA256("1"));
    }

    @Test
    public void SHA256_2(){
        assertEquals("ef2d127de37b942baad06145e54b0c619a1f22327b2ebbcfbec78f5564afe39d" , Encrypting.encryptSHA256("5"));
    }

    @Test
    public void SHA256_3(){
        assertEquals("7902699be42c8a8e46fbbb4501726517e86b22c56a189f7625a6da49081b2451" , Encrypting.encryptSHA256("7"));
    }

    @Test
    public void SHA256_4(){
        assertEquals("0bdc0a88309a4b67bf29ba1fa09c0c5a1468b46a91d35e1bb54a69d5a4b93b0d", Encrypting.encryptSHA256("7ret"));
    }
}