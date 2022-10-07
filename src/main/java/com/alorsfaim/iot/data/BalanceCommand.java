package com.alorsfaim.iot.data;

/**
 * Master D Protocol Commands
 */
public enum BalanceCommand {

    RESET_COMMAND(hexStringToByteArray("0130320d0a")),
    TARING_COMMAND(hexStringToByteArray("0130330d0a"));

    public final byte[] bytesCommand;

    BalanceCommand(byte[] bytesCommand) {
        this.bytesCommand = bytesCommand;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
