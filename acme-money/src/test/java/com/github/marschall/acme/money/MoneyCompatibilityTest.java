package com.github.marschall.acme.money;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

import org.javamoney.moneta.FastMoney;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

class MoneyCompatibilityTest {

  @Test
  void moneyThrows() {
    byte[] twosComplement = new byte[67_108_864 * 4];
    twosComplement[0] = 0b01111111;
    Arrays.fill(twosComplement, 1, twosComplement.length, (byte) 0b11111111);

    Money money = Money.of(new BigInteger(twosComplement), "EUR");
    money.negate();
  }

  @Test
  void testToString() {
    FastMoney monetaMoney = FastMoney.of(new BigDecimal("-1.12345"), "EUR");
    FastMoney6 acmeMoney = FastMoney6.of(new BigDecimal("-1.12345"), "EUR");
    assertEquals(monetaMoney.toString() + '0', acmeMoney.toString());
  }

  @Test
  void multiply() {
    FastMoney tenEur = FastMoney.of(10, "EUR");
    FastMoney one = tenEur.multiply(new BigDecimal("0.1"));
    assertThat(one.getNumber().numberValueExact(BigDecimal.class), comparesEqualTo(BigDecimal.ONE));
    assertEquals(Long.valueOf(1L), one.getNumber().numberValueExact(Long.class));
    assertEquals(Integer.valueOf(1), one.getNumber().numberValueExact(Integer.class));
  }

  @Test
  void divideToIntegralValue() {
    FastMoney original = FastMoney.of(new BigDecimal("8.4"), "EUR");
    FastMoney divided = original.divideToIntegralValue(2L);
    assertThat(divided.getNumber().numberValueExact(BigDecimal.class), comparesEqualTo(BigDecimal.valueOf(4L)));
    assertEquals(5, divided.getScale());
  }

  @Test
  void divideToIntegralValueNegative() {
    FastMoney original = FastMoney.of(new BigDecimal("8.4"), "EUR");
    FastMoney divided = original.divideToIntegralValue(-2L);
    assertThat(divided.getNumber().numberValueExact(BigDecimal.class), comparesEqualTo(BigDecimal.valueOf(-4L)));
    assertEquals(5, divided.getScale());
  }

  @Test
  void multiplyNumberCheck1() {
    BigDecimal multiplicand = new BigDecimal("0.1");
    BigDecimal multiplier = new BigDecimal("92233720368548");

    FastMoney expected = FastMoney.of(multiplier.multiply(multiplicand), "EUR");

    FastMoney tenCent = FastMoney.of(multiplicand, "EUR");
    assertEquals(expected, tenCent.multiply(multiplier));
  }

  @Test
  void isLessThan() {
    FastMoney tenEur = FastMoney.of(10L, "EUR");
    assertTrue(tenEur.isLessThan(Long.MAX_VALUE));
  }

  @Test
  void multiplyNumberCheck2() {
    BigDecimal multiplicand = new BigDecimal("0.1");
    BigDecimal multiplier = new BigDecimal("92233720368548");

    FastMoney expected = FastMoney.of(multiplier.multiply(multiplicand), "EUR");

    FastMoney tenCent = FastMoney.of(multiplicand, "EUR");
    assertEquals(expected, tenCent.multiply(multiplier));
  }

  @Test
  void division() {
    BigDecimal dividend = new BigDecimal("1000000");
    BigDecimal divisor = new BigDecimal("0.00000001");

    FastMoney oneEur = FastMoney.of(dividend, "EUR");
    FastMoney tenEur = oneEur.divide(divisor);
    assertThat(tenEur.getNumber().numberValueExact(BigDecimal.class), comparesEqualTo(dividend.divide(divisor)));
  }

  @Test
  void divisionByZero() {
    FastMoney one = FastMoney.of(1L, "EUR");
    one.divide(Double.valueOf(0.0d));
  }

  @Test
  void negate() {
    FastMoney oneEur = FastMoney.of(-1L, "EUR");
    oneEur.negate();
    oneEur.abs();
  }

  @Test
  void scaleByPowerOfTen() {
    BigDecimal original = BigDecimal.valueOf(16, 5);
    FastMoney money = FastMoney.of(original, "EUR");
    FastMoney scaled = money.scaleByPowerOfTen(-1);
    assertThat(scaled.scaleByPowerOfTen(1).getNumber().numberValueExact(BigDecimal.class), comparesEqualTo(original));
  }

  @Test
  void divideAndRemainder() {
    BigDecimal original = BigDecimal.ONE;
    FastMoney money = FastMoney.of(original, "EUR");
    BigDecimal divisor = new BigDecimal("0.333333");
    FastMoney[] result = money.divideAndRemainder(divisor);
    FastMoney quotient = result[0];
    FastMoney remainder = result[1];
    assertThat(quotient.getNumber().numberValueExact(BigDecimal.class)
        .multiply(divisor)
        .add(remainder.getNumber().numberValueExact(BigDecimal.class)), comparesEqualTo(original));
  }

}
