package com.github.marschall.acme.money;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import javax.money.NumberValue;

import org.javamoney.moneta.spi.DefaultNumberValue;
import org.junit.jupiter.api.Test;

class DefaultNumberValueCompatibilityTest {

  @Test
  void getAmountFraction() {
    NumberValue numberValue = new DefaultNumberValue(new BigDecimal("-123.456789"));
    assertEquals(-456789L, numberValue.getAmountFractionNumerator());
    assertEquals(1_000_000L, numberValue.getAmountFractionDenominator());

    numberValue = new DefaultNumberValue(new BigDecimal("123.456789"));
    assertEquals(456789L, numberValue.getAmountFractionNumerator());
    assertEquals(1_000_000L, numberValue.getAmountFractionDenominator());

    numberValue = new DefaultNumberValue(new BigDecimal("-123.000009"));
    assertEquals(-9L, numberValue.getAmountFractionNumerator());
    assertEquals(1_000_000L, numberValue.getAmountFractionDenominator());

    numberValue = new DefaultNumberValue(new BigDecimal("123.000009"));
    assertEquals(9L, numberValue.getAmountFractionNumerator());
    assertEquals(1_000_000L, numberValue.getAmountFractionDenominator());

    numberValue = new DefaultNumberValue(new BigDecimal("-123.900000"));
    assertEquals(-9L, numberValue.getAmountFractionNumerator());
    assertEquals(10L, numberValue.getAmountFractionDenominator());

    numberValue = new DefaultNumberValue(new BigDecimal("123.900000"));
    assertEquals(9L, numberValue.getAmountFractionNumerator());
    assertEquals(10L, numberValue.getAmountFractionDenominator());
  }

}
