package com.alorsfaim.iot.data;

import java.math.BigDecimal;
import java.util.Deque;
import java.util.Objects;
import java.util.Optional;


public class MasterDBalanceMessage extends BalanceMessage {
    private static final int MESSAGE_LENGTH = 8;
    private static final int WEIGHT_LENGTH = 7;

    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

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
                msg.setStatus(Status.of(byteStack.pop()));

                return Optional.of(msg);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error when reading Master D balance message", ex);
        } finally {
            byteStack.clear();
        }

        return Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MasterDBalanceMessage)) return false;
        if (!super.equals(o)) return false;
        MasterDBalanceMessage that = (MasterDBalanceMessage) o;
        return status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), status);
    }

    @Override
    public String toString() {
        return "MasterDBalanceMessage{" +
                "status=" + status +
                "weight=" + weight +
                '}';
    }
}
