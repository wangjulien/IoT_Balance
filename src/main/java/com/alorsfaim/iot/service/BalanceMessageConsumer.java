package com.alorsfaim.iot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BalanceMessageConsumer {

    private final SerialMessageBroker serialMessageBroker;
    private final ThreadPoolTaskExecutor brokerExecutor;

    public BalanceMessageConsumer(SerialMessageBroker serialMessageBroker, ThreadPoolTaskExecutor brokerExecutor) {
        this.serialMessageBroker = serialMessageBroker;
        this.brokerExecutor = brokerExecutor;
    }

    public void start() {
        log.info("Serial Port Message Broker starts ...");
        brokerExecutor.execute(this::processReceivedMessages);
    }

    private void processReceivedMessages() {
        while (true) {
            try {
                var message = serialMessageBroker.getMessage();
                log.info("Balance Message processed: {}", message);
            } catch (InterruptedException e) {
                log.error("Can not get messages from queue", e);
            }
        }
    }
}
