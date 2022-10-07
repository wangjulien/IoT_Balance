package com.alorsfaim.iot.balance;

import com.alorsfaim.iot.data.BalanceCommand;
import com.alorsfaim.iot.data.BalanceMessage;

public interface MessageBroker {
    void setPort(String port);

    void setRate(int rate);

    void setFrequency(int frequency);

    /**
     * Thread begins
     */
    void run();

    /**
     * Get message from blocking queue
     *
     * @return message object
     * @throws InterruptedException
     */
    BalanceMessage getMessage() throws InterruptedException;

    /**
     * Send command message to Balance
     *
     * @param command pre-defined command
     */
    void sendCommandMessage(BalanceCommand command);
}
