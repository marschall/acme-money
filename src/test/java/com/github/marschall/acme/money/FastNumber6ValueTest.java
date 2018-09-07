package com.github.marschall.acme.money;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
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
    NumberValue numberValue = numberValue(bigDecimal);
    assertThat(bigDecimal.round(new MathContext(6, RoundingMode.UNNECESSARY)), comparesEqualTo(new BigDecimal("123.456")));
    assertThat(numberValue.round(new MathContext(6, RoundingMode.UNNECESSARY)), comparesEqualTo(numberValue(new BigDecimal("123.456"))));

    assertThat(bigDecimal.round(new MathContext(5, RoundingMode.DOWN)), comparesEqualTo(new BigDecimal("123.45")));
    assertThat(numberValue.round(new MathContext(5, RoundingMode.DOWN)), comparesEqualTo(numberValue(new BigDecimal("123.45"))));

    assertThat(bigDecimal.round(new MathContext(1, RoundingMode.DOWN)), comparesEqualTo(new BigDecimal("100")));
    assertThat(numberValue.round(new MathContext(1, RoundingMode.DOWN)), comparesEqualTo(numberValue(new BigDecimal("100"))));
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

  @Test
  void getAmountFraction() {
    NumberValue numberValue = numberValue(new BigDecimal("-123.456789"));
    assertEquals(-456789L, numberValue.getAmountFractionNumerator());
    assertEquals(1_000_000L, numberValue.getAmountFractionDenominator());

    numberValue = numberValue(new BigDecimal("123.456789"));
    assertEquals(456789L, numberValue.getAmountFractionNumerator());
    assertEquals(1_000_000L, numberValue.getAmountFractionDenominator());

    numberValue = numberValue(new BigDecimal("-123.000009"));
    assertEquals(-9L, numberValue.getAmountFractionNumerator());
    assertEquals(1_000_000L, numberValue.getAmountFractionDenominator());

    numberValue = numberValue(new BigDecimal("123.000009"));
    assertEquals(9L, numberValue.getAmountFractionNumerator());
    assertEquals(1_000_000L, numberValue.getAmountFractionDenominator());

    numberValue = numberValue(new BigDecimal("-123.900000"));
    assertEquals(-900_000L, numberValue.getAmountFractionNumerator());
    assertEquals(1_000_000L, numberValue.getAmountFractionDenominator());

    numberValue = numberValue(new BigDecimal("123.900000"));
    assertEquals(900_000L, numberValue.getAmountFractionNumerator());
    assertEquals(1_000_000L, numberValue.getAmountFractionDenominator());
  }

  @Test
  void byteValue() {
    BigDecimal bigDecimal = BigDecimal.valueOf(Long.MAX_VALUE, FastMoney6.SCALE);
    NumberValue numberValue = numberValue(bigDecimal);
    assertEquals(bigDecimal.byteValue(), numberValue.byteValue());

    bigDecimal = BigDecimal.valueOf(Long.MIN_VALUE, FastMoney6.SCALE);
    numberValue = numberValue(bigDecimal);
    assertEquals(bigDecimal.byteValue(), numberValue.byteValue());
  }

  @Test
  void shortValue() {
    BigDecimal bigDecimal = BigDecimal.valueOf(Long.MAX_VALUE, FastMoney6.SCALE);
    NumberValue fastNumber = numberValue(bigDecimal);
    assertEquals(bigDecimal.shortValue(), fastNumber.shortValue());

    bigDecimal = BigDecimal.valueOf(Long.MIN_VALUE, FastMoney6.SCALE);
    fastNumber = numberValue(bigDecimal);
    assertEquals(bigDecimal.shortValue(), fastNumber.shortValue());
  }

  @Test
  void intValueRounding() {

    // positive

    BigDecimal bigDecimal = BigDecimal.valueOf(19, 1);
    NumberValue numberValue = numberValue(bigDecimal);
    assertEquals(1, bigDecimal.intValue());
    assertEquals(1, numberValue.intValue());

    bigDecimal = BigDecimal.valueOf(11, 1);
    numberValue = numberValue(bigDecimal);
    assertEquals(1, bigDecimal.intValue());
    assertEquals(1, numberValue.intValue());

    bigDecimal = BigDecimal.valueOf(29, 1);
    numberValue = numberValue(bigDecimal);
    assertEquals(2, bigDecimal.intValue());
    assertEquals(2, numberValue.intValue());

    bigDecimal = BigDecimal.valueOf(21, 1);
    numberValue = numberValue(bigDecimal);
    assertEquals(2, bigDecimal.intValue());
    assertEquals(2, numberValue.intValue());

    // negative

    bigDecimal = BigDecimal.valueOf(-19, 1);
    numberValue = numberValue(bigDecimal);
    assertEquals(-1, bigDecimal.intValue());
    assertEquals(-1, numberValue.intValue());

    bigDecimal = BigDecimal.valueOf(-11, 1);
    numberValue = numberValue(bigDecimal);
    assertEquals(-1, bigDecimal.intValue());
    assertEquals(-1, numberValue.intValue());

    bigDecimal = BigDecimal.valueOf(-29, 1);
    numberValue = numberValue(bigDecimal);
    assertEquals(-2, bigDecimal.intValue());
    assertEquals(-2, numberValue.intValue());

    bigDecimal = BigDecimal.valueOf(-21, 1);
    numberValue = numberValue(bigDecimal);
    assertEquals(-2, bigDecimal.intValue());
    assertEquals(-2, numberValue.intValue());
  }

  @Test
  void intValueOverflow() {
    BigDecimal bigDecimal = BigDecimal.valueOf(1L + Integer.MAX_VALUE);
    NumberValue numberValue = numberValue(bigDecimal);
    assertEquals(bigDecimal.intValue(), numberValue.intValue());

    bigDecimal = BigDecimal.valueOf(-1L - Integer.MIN_VALUE);
    numberValue = numberValue(bigDecimal);
    assertEquals(bigDecimal.intValue(), numberValue.intValue());

    bigDecimal = BigDecimal.valueOf(Long.MAX_VALUE, FastMoney6.SCALE);
    numberValue = numberValue(bigDecimal);
    assertEquals(bigDecimal.intValue(), numberValue.intValue());

    bigDecimal = BigDecimal.valueOf(Long.MIN_VALUE, FastMoney6.SCALE);
    numberValue = numberValue(bigDecimal);
    assertEquals(bigDecimal.intValue(), numberValue.intValue());
  }

  @Test
  void longValue() {
    BigDecimal bigDecimal = BigDecimal.valueOf(Long.MAX_VALUE, FastMoney6.SCALE);
    NumberValue numberValue = numberValue(bigDecimal);
    assertEquals(bigDecimal.longValue(), numberValue.longValue());

    bigDecimal = BigDecimal.valueOf(Long.MIN_VALUE, FastMoney6.SCALE);
    numberValue = numberValue(bigDecimal);
    assertEquals(bigDecimal.longValue(), numberValue.longValue());
  }

  @Test
  void floatValue() {
    BigDecimal bigDecimal = BigDecimal.valueOf(Long.MAX_VALUE, FastMoney6.SCALE);
    NumberValue numberValue = numberValue(bigDecimal);
    assertEquals(bigDecimal.floatValue(), numberValue.floatValue(), 0.000001f);

    bigDecimal = BigDecimal.valueOf(Long.MIN_VALUE, FastMoney6.SCALE);
    numberValue = numberValue(bigDecimal);
    assertEquals(bigDecimal.floatValue(), numberValue.floatValue(), 0.000001f);
  }

  @Test
  void doubleValue() {
    BigDecimal bigDecimal = BigDecimal.valueOf(Long.MAX_VALUE, FastMoney6.SCALE);
    NumberValue numberValue = numberValue(bigDecimal);
    assertEquals(bigDecimal.doubleValue(), numberValue.doubleValue(), 0.000001d);

    bigDecimal = BigDecimal.valueOf(Long.MIN_VALUE, FastMoney6.SCALE);
    numberValue = numberValue(bigDecimal);
    assertEquals(bigDecimal.doubleValue(), numberValue.doubleValue(), 0.000001d);
  }

  @Test
  void testEquals() {
    NumberValue zero = numberValue(0);
    NumberValue one = numberValue(1);

    assertEquals(one, one);
    assertEquals(one, numberValue(1));
    assertNotEquals(one, zero);
  }

  @Test
  void testToString() {
    assertEquals("0.000000", numberValue(0).toString());
    assertEquals("0.000001", numberValue(new BigDecimal("0.000001")).toString());
    assertEquals("-0.000001", numberValue(new BigDecimal("-0.000001")).toString());
    assertEquals("1.000000", numberValue(1).toString());
    assertEquals("-1.000000", numberValue(-1).toString());
  }

  @Test
  void serialize() throws ClassNotFoundException, IOException {
    NumberValue numberValue = numberValue(new BigDecimal("123456789"));
    assertEquals(numberValue, SerializationUtil.serializeCopy(numberValue));
  }

}
