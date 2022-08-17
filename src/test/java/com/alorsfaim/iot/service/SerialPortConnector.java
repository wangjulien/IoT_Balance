package com.alorsfaim.iot.service;


import gnu.io.NRSerialPort;

import java.io.DataInputStream;

public class SerialPortConnector {

    void connectSerialPort() {
        String port = "";
        for (String s : NRSerialPort.getAvailableSerialPorts()) {
            System.out.println("Availible port: " + s);
            port = s;
        }

        int baudRate = 9600;
        NRSerialPort serial = new NRSerialPort(port, baudRate);
        serial.connect();

        try (DataInputStream ins = new DataInputStream(serial.getInputStream());
             //     DataOutputStream outs = new DataOutputStream(serial.getOutputStream())
        ) {
            //while(ins.available()==0 && !Thread.interrupted());// wait for a byte
            while (!Thread.interrupted()) {// read all bytes
                if (ins.available() > 0) {
                    int b = ins.read();
                    //outs.write((byte)b);
                    System.out.print(b);
                }
                Thread.sleep(5);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        serial.disconnect();
    }

    public static void main(String[] args) {
        var connector = new SerialPortConnector();
        connector.connectSerialPort();
    }
}
