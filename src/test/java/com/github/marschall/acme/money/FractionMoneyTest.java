package com.github.marschall.acme.money;

import static com.github.marschall.acme.money.HasValue.hasValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryException;

import org.junit.jupiter.api.Test;


class FractionMoneyTest {

  protected static final CurrencyUnit CHF = Monetary.getCurrency("CHF");

  protected static final CurrencyUnit EUR = Monetary.getCurrency("EUR");


  @Test
  void zero() {
    FractionMoney money = FractionMoney.of(0, 5, CHF);
    assertThat(money, hasValue(0L, 1L));
  }

  @Test
  void signum() {
    FractionMoney money = FractionMoney.of(0, 1, CHF);
    assertEquals(0, money.signum());

    money = FractionMoney.of(1, 1, CHF);
    assertEquals(1, money.signum());

    money = FractionMoney.of(-1, 1, CHF);
    assertEquals(-1, money.signum());
  }

  @Test
  void plus() {
    FractionMoney money = FractionMoney.of(3, 4, CHF);
    assertSame(money, money.plus());

    money = FractionMoney.of(-3, 4, CHF);
    assertSame(money, money.plus());
  }

  @Test
  void subtract() {
    FractionMoney money = FractionMoney.of(3, 4, CHF);
    assertThat(money.subtract(FractionMoney.of(5, 8, CHF)), hasValue(1L, 8L));
    assertThat(money.subtract(FractionMoney.of(7, 8, CHF)), hasValue(-1L, 8L));
  }

  @Test
  void add() {
    FractionMoney money = FractionMoney.of(3, 4, CHF);
    assertThat(money.add(FractionMoney.of(-5, 8, CHF)), hasValue(1L, 8L));
    assertThat(money.add(FractionMoney.of(-7, 8, CHF)), hasValue(-1L, 8L));
  }

  @Test
  void multiply() {
    FractionMoney money = FractionMoney.of(1, 4, CHF);
    assertThat(money.multiply(2L), hasValue(1L, 2L));
    assertThat(money.multiply(2.0d), hasValue(1L, 2L));
  }

  @Test
  void multiplyNotExact() {
    long l = 4037000499L;
    FractionMoney money = FractionMoney.of(l, 1, CHF);
    assertThrows(ArithmeticException.class, () -> money.multiply((double) l));
  }

  @Test
  void gcdLimit() {
    FractionMoney money = FractionMoney.of(Long.MAX_VALUE, 1, CHF);
    assertNotNull(money);
    money = FractionMoney.of(Long.MIN_VALUE + 1, 1, CHF);
    assertNotNull(money);
  }

  @Test
  void divide() {
    FractionMoney money = FractionMoney.of(1, 2, CHF);
    assertThat(money.divide(2L), hasValue(1L, 4L));
  }

  @Test
  void testToString() {
    FractionMoney money = FractionMoney.of(1, 2, CHF);
    assertEquals("CHF 1/2", money.toString());
  }

  @Test
  void compareDifferentCurrencies() {
    FractionMoney chf = FractionMoney.of(1, 2, CHF);
    FractionMoney eur = FractionMoney.of(1, 2, EUR);


    assertThrows(MonetaryException.class, () -> chf.isEqualTo(eur));
  }

  @Test
  void compareToFractionMoney() {
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
  void compareToOtherMoneyEqual() {
    FractionMoney fraction = FractionMoney.of(2, 1, CHF);
    FastMoney6 fast6 = FastMoney6.of(2, CHF);

    assertTrue(fraction.isEqualTo(fast6));
    assertTrue(fast6.isEqualTo(fraction));
  }

  @Test
  void scaleByPowerOfTen() {
    FractionMoney money = FractionMoney.of(10L, 1L, CHF);
    assertEquals(0, new BigDecimal("1000").compareTo(new BigDecimal("10").scaleByPowerOfTen(2)));
    assertThat(money.scaleByPowerOfTen(2), hasValue(1000L, 1L));
    assertEquals(new BigDecimal("0.01"), new BigDecimal("10").scaleByPowerOfTen(-3).stripTrailingZeros());
    assertThat(money.scaleByPowerOfTen(-3), hasValue(1L, 100L));
  }

  @Test
  void monetary() {
    FractionMoney money = Monetary.getAmountFactory(FractionMoney.class)
      .setCurrency(CHF)
      .setNumber(new BigDecimal("-1.23"))
      .create();

    assertEquals(CHF.getCurrencyCode(), money.getCurrency().getCurrencyCode());
    assertThat(money.getNumber().numberValueExact(BigDecimal.class), comparesEqualTo(new BigDecimal("-1.23")));
  }

}
