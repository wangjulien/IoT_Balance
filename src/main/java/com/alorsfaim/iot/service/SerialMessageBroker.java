package com.alorsfaim.iot.service;

import com.alorsfaim.iot.data.BalanceCommand;
import com.alorsfaim.iot.data.BalanceMessage;
import gnu.io.NRSerialPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SerialMessageBroker implements InitializingBean, DisposableBean {

    @Value("${serial.port:COM1}")
    private String port;
    @Value("${serial.rate:9600}")
    private int rate;
    @Value("${serial.frequency:1000}")
    private int frequency;

    private final ArrayBlockingQueue<BalanceMessage> messageQueue;

    private final ThreadPoolTaskExecutor brokerExecutor;

    private NRSerialPort serialPort;

    public SerialMessageBroker(ThreadPoolTaskExecutor brokerExecutor) {
        messageQueue = new ArrayBlockingQueue<>(100, true);
        this.brokerExecutor = brokerExecutor;
    }

    @Override
    public void afterPropertiesSet() {
        log.info("Port {} connected with rate {}", port, rate);
        this.serialPort = new NRSerialPort(port, rate);
        this.serialPort.connect();
        log.info("Serial Port Message Broker starts ...");
        brokerExecutor.execute(this::readSerialPortMessages);
    }

    @Override
    public void destroy() {
        log.info("Port {} disconnected", port);
        this.serialPort.disconnect();
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
            log.error("Thread interrupted.", e);
        }
    }

    /**
     * Get message from blocking queue
     *
     * @return message object
     * @throws InterruptedException
     */
    public BalanceMessage getMessage() throws InterruptedException {
        return messageQueue.poll(10, TimeUnit.SECONDS);
    }

    /**
     * Send command message to Balance
     *
     * @param command pre-defined command
     */
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



