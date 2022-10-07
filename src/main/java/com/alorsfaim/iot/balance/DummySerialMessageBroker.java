package com.alorsfaim.iot.balance;

import com.alorsfaim.iot.data.BalanceCommand;
import com.alorsfaim.iot.data.BalanceMessage;
import com.alorsfaim.iot.data.MasterDBalanceMessage;
import com.alorsfaim.iot.data.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class DummySerialMessageBroker extends Thread implements MessageBroker {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String port = "COM1";
    private int rate = 9600;
    private int frequency = 1000;

    private final ArrayBlockingQueue<BalanceMessage> messageQueue;

    public DummySerialMessageBroker() {
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
        log.info("Serial Port Message Broker starts ...");
        readSerialPortMessages();
    }

    private void readSerialPortMessages() {
        try {
            var counter = 0;
            while (true) {
                var balanceMessage = new MasterDBalanceMessage();
                balanceMessage.setWeight(BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(0, 100)));
                if (counter < 10) {
                    balanceMessage.setStatus(Status.STABLE);
                    counter++;
                } else if (counter < 15) {
                    balanceMessage.setStatus(Status.UNSTABLE);
                    counter++;
                } else {
                    counter = 0;
                }
                messageQueue.put(balanceMessage);
                log.debug("Balance message received: " + balanceMessage);
                Thread.sleep(frequency);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted.", e);
        } finally {
            log.info("Port {} disconnected", port);
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
        log.info("Sending command {} to serial port {}", command, port);
    }
}



