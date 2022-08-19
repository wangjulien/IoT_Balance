package com.alorsfaim.iot.data;

import lombok.Data;

import java.math.BigDecimal;

@Data
public abstract class BalanceMessage {
    private BigDecimal weight;
}
