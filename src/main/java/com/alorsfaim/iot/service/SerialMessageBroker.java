package com.alorsfaim.iot.service;

import com.alorsfaim.iot.data.BalanceMessage;
import gnu.io.NRSerialPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.DataInputStream;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SerialMessageBroker {

    @Value("${serial.port:COM1}")
    private String port;
    @Value("${serial.rate:9600}")
    private int rate;
    @Value("${serial.frequency:1000}")
    private int frequency;

    private final ArrayBlockingQueue<BalanceMessage> messageQueue;

    private final ThreadPoolTaskExecutor brokerExecutor;

    public SerialMessageBroker(ThreadPoolTaskExecutor brokerExecutor) {
        messageQueue = new ArrayBlockingQueue<>(100, true);
        this.brokerExecutor = brokerExecutor;
    }

    public void start() {
        log.info("Serial Port Message Broker starts ...");
        brokerExecutor.execute(this::readSerialPortMessages);
    }

    private void readSerialPortMessages() {
        NRSerialPort serial = new NRSerialPort(port, rate);
        try {
            serial.connect();
            log.info("Port {} connected with rate {}", port, rate);

            while (true) {
                // We have here a plain socket or a wrapped TLS socket
                try (DataInputStream ins = new DataInputStream(serial.getInputStream());
                     MasterDMessageReader reader = new MasterDMessageReader(ins, frequency)) {
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
        } finally {
            serial.disconnect();
        }
    }

    public BalanceMessage getMessage() throws InterruptedException {
        return messageQueue.poll(10, TimeUnit.SECONDS);
    }
}



