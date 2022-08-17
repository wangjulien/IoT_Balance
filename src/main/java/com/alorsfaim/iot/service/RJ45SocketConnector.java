package com.alorsfaim.iot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

@Slf4j
@Service
public class RJ45SocketConnector {

    public void readFromRJ45() {
        try (var socket = new Socket("192.168.0.188", 11001)) {
            var input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            log.info("Input: " + input.readLine());
        } catch (Exception ex) {
            log.error("Error reading socket", ex);
        }
    }
}



