package com.alorsfaim.iot.service;

import com.alorsfaim.iot.balance.MessageBroker;
import com.alorsfaim.iot.balance.UsbMessageBroker;
import com.alorsfaim.iot.data.BalanceCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BalanceMessageConsumer implements InitializingBean {

    private final MessageBroker messageBroker;
    private final ThreadPoolTaskExecutor brokerExecutor;

    public BalanceMessageConsumer(ThreadPoolTaskExecutor brokerExecutor) {
//        for (String s : NRSerialPort.getAvailableSerialPorts()) {
//            System.out.println("Availible port: " + s);
//        }

        this.messageBroker = new UsbMessageBroker();
        this.messageBroker.setPort("COM1");
//        this.messageBroker = new DummySerialMessageBroker();
        this.brokerExecutor = brokerExecutor;
    }


    @Override
    public void afterPropertiesSet() {
        log.info("Message Consumer starts ...");
        messageBroker.start();
        brokerExecutor.execute(this::processReceivedMessages);
    }

    private void processReceivedMessages() {
        while (true) {
            try {
                var message = messageBroker.getMessage();
                log.info("Balance Message processed: {}", message);
            } catch (InterruptedException e) {
                log.error("Can not get messages from queue", e);
            }
        }
    }

    public void sendCommandMessage(BalanceCommand balanceCommand) {
        messageBroker.sendCommandMessage(balanceCommand);
    }

    public void terminate() {
        messageBroker.terminate();
    }
}
