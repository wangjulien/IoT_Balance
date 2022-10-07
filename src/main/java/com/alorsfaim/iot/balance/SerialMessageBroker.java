package com.alorsfaim.iot.balance;

import com.alorsfaim.iot.data.BalanceCommand;
import com.alorsfaim.iot.data.BalanceMessage;
import gnu.io.NRSerialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class SerialMessageBroker extends Thread implements MessageBroker {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String port = "COM1";
    private int rate = 9600;
    private int frequency = 1000;

    private final ArrayBlockingQueue<BalanceMessage> messageQueue;

    private NRSerialPort serialPort;

    public SerialMessageBroker() {
        messageQueue = new ArrayBlockingQueue<>(100, true);
    }

    @Override
    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public void setRate(int rate) {
        this.rate = rate;
    }

    @Override
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    /**
     * Thread begins
     */
    @Override
    public void run() {
        log.info("Port {} connected with rate {}", port, rate);
        this.serialPort = new NRSerialPort(port, rate);
        this.serialPort.connect();
        log.info("Serial Port Message Broker starts ...");
        readSerialPortMessages();
    }

    private void readSerialPortMessages() {
        try {
            while (true) {
                // We have here a plain socket or a wrapped TLS socket
                try (InputStream ins = serialPort.getInputStream();
                     DataInputStream dins = new DataInputStream(ins);
                     MasterDMessageReader reader = new MasterDMessageReader(dins, frequency)) {
                    while (true) {
                        Optional<BalanceMessage> message = reader.readMessage();

                        if (message.isPresent()) {
                            messageQueue.put(message.get());
                            log.debug("Balance message received: " + message.get());
                        }
                    }
                } catch (Exception e) {
                    log.error("Error occurred while reading from serial port", e);
                    Thread.sleep(frequency);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted.", e);
        } finally {
            log.info("Port {} disconnected", port);
            this.serialPort.disconnect();
        }
    }

    /**
     * Get message from blocking queue
     *
     * @return message object
     */
    @Override
    public BalanceMessage getMessage() throws InterruptedException {
        return messageQueue.poll(10, TimeUnit.SECONDS);
    }

    /**
     * Send command message to Balance
     *
     * @param command pre-defined command
     */
    @Override
    public void sendCommandMessage(BalanceCommand command) {
        try (OutputStream outs = serialPort.getOutputStream();
             DataOutputStream douts = new DataOutputStream(outs)) {
            douts.write(command.bytesCommand);
            douts.flush();
        } catch (IOException e) {
            log.error("Error occurred while sending command {} to serial port {}", command, port, e);
            throw new RuntimeException(e);
        }
    }
}



