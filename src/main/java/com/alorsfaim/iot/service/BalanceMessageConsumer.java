package com.alorsfaim.iot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BalanceMessageConsumer implements InitializingBean {

    private final SerialMessageBroker serialMessageBroker;
    private final ThreadPoolTaskExecutor brokerExecutor;

    public BalanceMessageConsumer(SerialMessageBroker serialMessageBroker, ThreadPoolTaskExecutor brokerExecutor) {
        this.serialMessageBroker = serialMessageBroker;
        this.brokerExecutor = brokerExecutor;
    }


    @Override
    public void afterPropertiesSet() {
        log.info("Message Consumer starts ...");
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
