package com.alorsfaim.iot.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RJ45SocketConnectorTest {

    @Autowired
    private RJ45SocketConnector rj45SocketConnector;

    @Test
    public void testRJ45SocketConnector() {
        while(true) {
            rj45SocketConnector.readFromRJ45();
        }
    }

}
