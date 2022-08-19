package com.alorsfaim.iot.service;

import com.alorsfaim.iot.data.BalanceCommand;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class CommandRunner {

    private final SerialMessageBroker serialMessageBroker;

    public CommandRunner(SerialMessageBroker serialMessageBroker) {
        this.serialMessageBroker = serialMessageBroker;
    }

    public void runCommand(BalanceCommand balanceCommand) {
        serialMessageBroker.sendCommandMessage(balanceCommand);
    }
}
