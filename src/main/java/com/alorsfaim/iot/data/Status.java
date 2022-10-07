package com.alorsfaim.iot.data;

public enum Status {
    STABLE,
    UNSTABLE,
    OUT_OF_RANGE;

    public static Status of(Byte statusByte) {
        // Convert byte to a 8bit binary string
        var byteStr = String.format("%8s", Integer.toBinaryString(statusByte & 0xFF)).replace(' ', '0');
        // 4th bit represent stability
        char stability = byteStr.charAt(3);
        // return stability enum
        if ('0' == stability)
            return UNSTABLE;
        else
            return STABLE;
    }
}
