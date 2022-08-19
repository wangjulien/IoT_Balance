package com.alorsfaim.iot.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Deque;
import java.util.Optional;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MasterDBalanceMessage extends BalanceMessage {
    private static final int MESSAGE_LENGTH = 8;
    private static final int WEIGHT_LENGTH = 7;

    private byte status;

    public static Optional<BalanceMessage> of(Deque<Byte> byteStack) {
        try {
            if (byteStack.size() >= MESSAGE_LENGTH) {
                // Read Weight from Back
                var sb = new StringBuilder();
                for (int i = 0; i < WEIGHT_LENGTH; i++) {
                    var bt = byteStack.pop();
                    if (bt < 32 || bt > 57) {
                        // Contain no number, space and +/-  ASCII
                        throw new RuntimeException("Byte " + bt + "  may not be able to parse to number");
                    }
                    // Filter spaces (32)
                    if (bt != 32) {
                        sb.append((char) bt.byteValue());
                    }
                }

                // Create object
                var msg = new MasterDBalanceMessage();
                // Set Gross Weight
                msg.setWeight(new BigDecimal(sb.reverse().toString()));
                // Set Status
                msg.setStatus(byteStack.pop());

                return Optional.of(msg);
            }
        } catch (Exception ex) {
            log.error("Error when reading Master D balance message", ex);
        } finally {
            byteStack.clear();
        }

        return Optional.empty();
    }
}
