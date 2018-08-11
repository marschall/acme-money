package com.github.marschall.acme.money;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.NumberValue;

import org.junit.jupiter.api.Test;

class FastNumber6ValueTest {

  protected static final CurrencyUnit CHF = Monetary.getCurrency("CHF");

  private static NumberValue numberValue(Number number) {
    return FastMoney6.of(number, CHF).getNumber();
  }

  @Test
  void round() {
    BigDecimal bigDecimal = new BigDecimal("123.456");
    assertThat(bigDecimal.round(new MathContext(6, RoundingMode.UNNECESSARY)), comparesEqualTo(new BigDecimal("123.456")));
    assertThat(bigDecimal.round(new MathContext(5, RoundingMode.DOWN)), comparesEqualTo(new BigDecimal("123.45")));
  }

  @Test
  void intValueExact() {
    assertEquals(1, numberValue(1).intValueExact());

    NumberValue numberValue2 = numberValue(new BigDecimal("1.000001"));
    assertThrows(ArithmeticException.class, () -> numberValue2.intValueExact());

    NumberValue numberValue3 = numberValue(new BigDecimal("1.1"));
    assertThrows(ArithmeticException.class, () -> numberValue3.intValueExact());

    NumberValue numberValue4 = numberValue(Long.valueOf((Integer.MAX_VALUE) + 1L));
    assertThrows(ArithmeticException.class, () -> numberValue4.intValueExact());
  }

  @Test
  void longValueExact() {
    assertEquals(1L, numberValue(1L).longValueExact());

    NumberValue numberValue2 = numberValue(new BigDecimal("1.000001"));
    assertThrows(ArithmeticException.class, () -> numberValue2.longValueExact());

    NumberValue numberValue3 = numberValue(new BigDecimal("1.1"));
    assertThrows(ArithmeticException.class, () -> numberValue3.longValueExact());
  }

}
