package com.alorsfaim.iot.balance;

import com.alorsfaim.iot.data.BalanceMessage;
import com.alorsfaim.iot.data.MasterDBalanceMessage;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;

import static com.alorsfaim.iot.data.SpecialAsciiCode.CR;

public class MasterDMessageReader implements Closeable {

    private final Deque<Byte> byteStack = new LinkedList<>();

    private final DataInputStream ins;
    private final int frequency;

    public MasterDMessageReader(DataInputStream inputStream, int frequency) {
        this.ins = inputStream;
        this.frequency = frequency;
    }

    @Override
    public void close() throws IOException {
        ins.close();
    }

    public Optional<BalanceMessage> readMessage() throws IOException, InterruptedException {
        byteStack.clear();
        while (true) {
            // Continuously read stream util CR - end of message
            while (ins.available() > 0) {
                var bt = ins.readByte();
                // End of message
                if (bt == CR) {
                    // Create message form read bytes. and clear stack
                    return MasterDBalanceMessage.of(byteStack);
                }
                // Add to stack
                byteStack.push(bt);
            }
            // No byte read, sleep awhile
            Thread.sleep(frequency);
        }
    }
}
