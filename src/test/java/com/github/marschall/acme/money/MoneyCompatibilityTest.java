package com.github.marschall.acme.money;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.javamoney.moneta.FastMoney;
import org.junit.jupiter.api.Test;

class MoneyCompatibilityTest {

  @Test
  void multiply() {
    FastMoney tenEur = FastMoney.of(10, "EUR");
    FastMoney one = tenEur.multiply(new BigDecimal("0.1"));
    assertThat(one.getNumber().numberValueExact(BigDecimal.class), comparesEqualTo(BigDecimal.ONE));
    assertEquals(Long.valueOf(1L), one.getNumber().numberValueExact(Long.class));
    assertEquals(Integer.valueOf(1), one.getNumber().numberValueExact(Integer.class));
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
    assertEquals(expected, tenCent.multiply(multiplicand));
  }

  @Test
  void division() {
    BigDecimal dividend = new BigDecimal("1000000");
    BigDecimal divisor = new BigDecimal("0.00000001");

    FastMoney oneEur = FastMoney.of(dividend, "EUR");
    FastMoney tenEur = oneEur.divide(divisor);
    assertThat(tenEur.getNumber().numberValueExact(BigDecimal.class), comparesEqualTo(dividend.divide(divisor)));
  }

}
