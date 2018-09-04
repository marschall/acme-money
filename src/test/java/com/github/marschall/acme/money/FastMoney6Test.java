package com.github.marschall.acme.money;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.math.BigDecimal;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmountFactory;
import javax.money.MonetaryContext;

import org.junit.jupiter.api.Test;

class FastMoney6Test {

  protected static final CurrencyUnit CHF = Monetary.getCurrency("CHF");

  protected static final CurrencyUnit EUR = Monetary.getCurrency("EUR");

  @Test
  void money() {
    FastMoney6 money = FastMoney6.of(1L, CHF);
  }

  @Test
  void monetary() {
    FastMoney6 money = Monetary.getAmountFactory(FastMoney6.class)
      .setCurrency(CHF)
      .setNumber(new BigDecimal("-1.23"))
      .create();

    assertEquals(CHF.getCurrencyCode(), money.getCurrency().getCurrencyCode());
    assertThat(money.getNumber().numberValueExact(BigDecimal.class), comparesEqualTo(new BigDecimal("-1.23")));
  }

  @Test
  void plus() {
    FastMoney6 money = FastMoney6.of(1L, CHF);
    assertSame(money, money.plus());

    money = FastMoney6.of(-1L, CHF);
    assertSame(money, money.plus());
  }

  @Test
  void factory() {
    FastMoney6 money = FastMoney6.of(1L, CHF);
    MonetaryAmountFactory<FastMoney6> factory = money.getFactory();

    assertNotNull(factory.getMaxNumber());
    assertNotNull(factory.getMinNumber());
    assertSame(FastMoney6.class, factory.getAmountType());
  }

  @Test
  void factoryDefaultContext() {
    FastMoney6 money = FastMoney6.of(1L, CHF);
    MonetaryContext context = money.getFactory().getDefaultMonetaryContext();

    validateContext(context);
  }

  @Test
  void factoryMaximalContext() {
    FastMoney6 money = FastMoney6.of(1L, CHF);
    MonetaryContext context = money.getFactory().getMaximalMonetaryContext();

    validateContext(context);
  }

  @Test
  void context() {
    FastMoney6 money = FastMoney6.of(1L, CHF);
    MonetaryContext context = money.getContext();

    validateContext(context);
  }

  @Test
  void scaleByPowerOfTen() {
    FastMoney6 money = FastMoney6.of(2L, CHF);

    assertEquals(FastMoney6.of(BigDecimal.valueOf(2L).scaleByPowerOfTen(2), CHF), money.scaleByPowerOfTen(2));
    assertEquals(FastMoney6.of(BigDecimal.valueOf(2L).scaleByPowerOfTen(1), CHF), money.scaleByPowerOfTen(1));
    assertEquals(FastMoney6.of(BigDecimal.valueOf(2L).scaleByPowerOfTen(0), CHF), money.scaleByPowerOfTen(0));
    assertEquals(FastMoney6.of(BigDecimal.valueOf(2L).scaleByPowerOfTen(-2), CHF), money.scaleByPowerOfTen(-2));

    // TODO overflow
    // TODO underflow
  }

  @Test
  void divideToIntegralValue() {
    FastMoney6 original = FastMoney6.of(new BigDecimal("8.4"), "EUR");
    FastMoney6 divided = original.divideToIntegralValue(2L);
    assertThat(divided.getNumber().numberValueExact(BigDecimal.class), comparesEqualTo(BigDecimal.valueOf(4L)));
  }

  @Test
  void divideToIntegralValueNegative() {
    FastMoney6 original = FastMoney6.of(new BigDecimal("8.4"), "EUR");
    FastMoney6 divided = original.divideToIntegralValue(-2L);
    assertThat(divided.getNumber().numberValueExact(BigDecimal.class), comparesEqualTo(BigDecimal.valueOf(-4L)));
  }

  private static void validateContext(MonetaryContext context) {
    assertEquals(19, context.getPrecision());
    assertEquals(6, context.getMaxScale());
    assertEquals("acme", context.getProviderName());
    assertEquals(FastMoney6.class, context.getAmountType());
  }

}
