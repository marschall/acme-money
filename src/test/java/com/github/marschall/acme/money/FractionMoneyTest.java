package com.github.marschall.acme.money;

import static com.github.marschall.acme.money.HasValue.hasValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import javax.money.CurrencyUnit;
import javax.money.MonetaryCurrencies;

import org.junit.Test;

public class FractionMoneyTest {
  
  protected static final CurrencyUnit CHF = MonetaryCurrencies.getCurrency("CHF");
  

  @Test
  public void zero() {
    FractionMoney money = FractionMoney.of(0, 5, CHF);
    assertThat(money, hasValue(0L, 1L));
  }

  @Test
  public void signum() {
    FractionMoney money = FractionMoney.of(0, 1, CHF);
    assertEquals(0, money.signum());
    
    money = FractionMoney.of(1, 1, CHF);
    assertEquals(1, money.signum());
    
    money = FractionMoney.of(-1, 1, CHF);
    assertEquals(-1, money.signum());
  }
  
  @Test
  public void subtract() {
    FractionMoney money = FractionMoney.of(3, 4, CHF);
    assertThat(money.subtract(FractionMoney.of(5, 8, CHF)), hasValue(1L, 8L));
    assertThat(money.subtract(FractionMoney.of(7, 8, CHF)), hasValue(-1L, 8L));
  }

}
