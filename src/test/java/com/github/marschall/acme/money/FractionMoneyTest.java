package com.github.marschall.acme.money;

import static com.github.marschall.acme.money.HasValue.hasValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import javax.money.CurrencyUnit;
import javax.money.MonetaryCurrencies;
import javax.money.MonetaryException;

import org.junit.Test;

public class FractionMoneyTest {

  protected static final CurrencyUnit CHF = MonetaryCurrencies.getCurrency("CHF");

  protected static final CurrencyUnit EUR = MonetaryCurrencies.getCurrency("EUR");


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

  @Test
  public void add() {
    FractionMoney money = FractionMoney.of(3, 4, CHF);
    assertThat(money.add(FractionMoney.of(-5, 8, CHF)), hasValue(1L, 8L));
    assertThat(money.add(FractionMoney.of(-7, 8, CHF)), hasValue(-1L, 8L));
  }

  @Test
  public void multiply() {
    FractionMoney money = FractionMoney.of(1, 4, CHF);
    assertThat(money.multiply(2L), hasValue(1L, 2L));
    assertThat(money.multiply(2.0d), hasValue(1L, 2L));

    long l = 4037000499L;

    money = FractionMoney.of(l, 1, CHF);
    try {
      money.multiply((double) l);
      fail("missed overflow");
    } catch (ArithmeticException e) {
      // should reach here
    }
  }

  @Test
  public void gcdLimit() {
    FractionMoney money = FractionMoney.of(Long.MAX_VALUE, 1, CHF);
    assertNotNull(money);
    money = FractionMoney.of(Long.MIN_VALUE + 1, 1, CHF);
    assertNotNull(money);
  }

  @Test
  public void divide() {
    FractionMoney money = FractionMoney.of(1, 2, CHF);
    assertThat(money.divide(2L), hasValue(1L, 4L));
  }

  @Test
  public void testToString() {
    FractionMoney money = FractionMoney.of(1, 2, CHF);
    assertEquals("CHF 1/2", money.toString());
  }

  @Test
  public void compareDifferentCurrencies() {
    FractionMoney chf = FractionMoney.of(1, 2, CHF);
    FractionMoney eur = FractionMoney.of(1, 2, EUR);

    try {
      chf.isEqualTo(eur);
      fail("can't compare different currencies");
    } catch (MonetaryException e) {
      // should reach here
    }
  }

  @Test
  public void compareToFractionMoney() {
    FractionMoney smaller = FractionMoney.of(1, 2, CHF);
    FractionMoney bigger = FractionMoney.of(4, 3, CHF);

    assertTrue(smaller.isEqualTo(smaller));
    assertFalse(smaller.isEqualTo(bigger));
    assertTrue(smaller.isLessThan(bigger));
    assertTrue(smaller.isLessThanOrEqualTo(bigger));
    assertTrue(smaller.isLessThanOrEqualTo(smaller));
    assertFalse(smaller.isGreaterThan(bigger));
    assertFalse(smaller.isGreaterThanOrEqualTo(bigger));
    assertTrue(smaller.isGreaterThanOrEqualTo(smaller));

    assertTrue(bigger.isEqualTo(bigger));
    assertFalse(bigger.isEqualTo(smaller));
    assertFalse(bigger.isLessThan(smaller));
    assertFalse(bigger.isLessThanOrEqualTo(smaller));
    assertTrue(bigger.isLessThanOrEqualTo(bigger));
    assertTrue(bigger.isGreaterThan(smaller));
    assertTrue(bigger.isGreaterThanOrEqualTo(smaller));
    assertTrue(bigger.isGreaterThanOrEqualTo(bigger));

    assertEquals(0, smaller.compareTo(smaller));
    assertEquals(-1, smaller.compareTo(bigger));
    assertEquals(1, bigger.compareTo(smaller));
  }

  @Test
  public void compareToOtherMoneyEqual() {
    FractionMoney fraction = FractionMoney.of(2, 1, CHF);
    FastMoney6 fast6 = FastMoney6.of(2, CHF);

    assertTrue(fraction.isEqualTo(fast6));
    assertTrue(fast6.isEqualTo(fraction));
  }

  @Test
  public void scaleByPowerOfTen() {
    FractionMoney money = FractionMoney.of(10L, 1L, CHF);
    assertEquals(0, new BigDecimal("1000").compareTo(new BigDecimal("10").scaleByPowerOfTen(2)));
    assertThat(money.scaleByPowerOfTen(2), hasValue(1000L, 1L));
    assertEquals(new BigDecimal("0.01"), new BigDecimal("10").scaleByPowerOfTen(-3).stripTrailingZeros());
    assertThat(money.scaleByPowerOfTen(-3), hasValue(1L, 100L));
  }

}
