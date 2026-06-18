package com.APIFramework.tests.sample;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestHelloRA {

    @Test
    public void test_hello_world_positive(){

        Assert.assertEquals("James Brown", "James Brown");
    }

    @Test
    public void test_hello_world_negative(){
        String actual = "James Brown";
        String expected = "james brown";

        Assert.assertEquals(actual.toLowerCase(), expected.toLowerCase());
    }
}