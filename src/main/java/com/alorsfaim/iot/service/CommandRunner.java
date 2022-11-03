package com.alorsfaim.iot.service;

import com.alorsfaim.iot.data.BalanceCommand;
import org.springframework.stereotype.Service;


@Service
public class CommandRunner {

    private final BalanceMessageConsumer balanceMessageConsumer;

    public CommandRunner(BalanceMessageConsumer balanceMessageConsumer) {
        this.balanceMessageConsumer = balanceMessageConsumer;
    }

    public void runCommand(BalanceCommand balanceCommand) {
        balanceMessageConsumer.sendCommandMessage(balanceCommand);
    }
}
