package com.github.marschall.acme.money;

import static org.junit.Assert.*;

import javax.money.CurrencyUnit;
import javax.money.MonetaryCurrencies;

import org.junit.Test;

public class FractionMoneyTest {
  
  protected static final CurrencyUnit CHF = MonetaryCurrencies.getCurrency("CHF");

  @Test
  public void signum() {
    FractionMoney money = FractionMoney.of(0, 1, CHF);
    assertEquals(0, money.signum());
    
    money = FractionMoney.of(1, 1, CHF);
    assertEquals(1, money.signum());
    
    money = FractionMoney.of(-1, 1, CHF);
    assertEquals(-1, money.signum());
  }

}
