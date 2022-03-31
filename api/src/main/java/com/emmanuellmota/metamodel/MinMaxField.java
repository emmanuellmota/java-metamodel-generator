package com.emmanuellmota.metamodel;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MinMaxField {
    BigDecimal min;
    BigDecimal max;
}
