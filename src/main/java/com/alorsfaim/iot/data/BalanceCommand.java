package com.alorsfaim.iot.data;

import java.util.HexFormat;

/**
 * Master D Protocol Commands
 */
public enum BalanceCommand {

    RESET_COMMAND(HexFormat.of().parseHex("0130320d0a")),
    TARING_COMMAND(HexFormat.of().parseHex("0130330d0a"));

    public final byte[] command;

    BalanceCommand(byte[] bytesCommand) {
        this.command = bytesCommand;
    }
}
