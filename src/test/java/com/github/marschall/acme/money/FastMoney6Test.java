package com.github.marschall.acme.money;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    assertEquals(19, money.getPrecision());
    assertEquals(6, money.getScale());
  }

  @Test
  void factory() {
    FastMoney6 money = FastMoney6.of(1L, CHF);
    MonetaryAmountFactory<FastMoney6> factory = money.getFactory();

    assertNotNull(factory.getMaxNumber());
    assertNotNull(factory.getMinNumber());
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

  private static void validateContext(MonetaryContext context) {
    assertEquals(19, context.getPrecision());
    assertEquals(6, context.getMaxScale());
    assertEquals("acme", context.getProviderName());
    assertEquals(FastMoney6.class, context.getAmountType());
  }

}
