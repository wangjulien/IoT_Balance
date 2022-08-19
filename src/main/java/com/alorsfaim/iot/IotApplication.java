package com.alorsfaim.iot;

import com.alorsfaim.iot.service.BalanceMessageConsumer;
import com.alorsfaim.iot.service.SerialMessageBroker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class IotApplication implements CommandLineRunner {

    private final SerialMessageBroker serialMessageBroker;

    private final BalanceMessageConsumer balanceMessageConsumer;

    public IotApplication(SerialMessageBroker serialMessageBroker, BalanceMessageConsumer balanceMessageConsumer) {
        this.serialMessageBroker = serialMessageBroker;
        this.balanceMessageConsumer = balanceMessageConsumer;
    }

    public static void main(String[] args) {
        log.info("STARTING THE APPLICATION");
        SpringApplication.run(IotApplication.class, args);
        log.info("APPLICATION FINISHED");
    }

    @Override
    public void run(String... args) {
        serialMessageBroker.start();
        balanceMessageConsumer.start();
    }
}