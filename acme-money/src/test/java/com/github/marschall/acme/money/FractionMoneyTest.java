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

import java.io.IOException;
import java.math.BigDecimal;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryException;

import org.junit.jupiter.api.Test;


class FractionMoneyTest {

  protected static final CurrencyUnit CHF = Monetary.getCurrency("CHF");

  protected static final CurrencyUnit EUR = Monetary.getCurrency("EUR");

  @Test
  void serialize() throws ClassNotFoundException, IOException {
    FractionMoney money = FractionMoney.of(140_000, 1, CHF);
    assertEquals(money, SerializationUtil.serializeCopy(money));
  }

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

  @Test
  void remainderLong() {
    FractionMoney money = FractionMoney.of(5, 2, CHF); // 2.5
    assertEquals(FractionMoney.of(1, 2, CHF), money.remainder(2L));

    money = FractionMoney.of(11, 2, CHF); // 5.5
    assertEquals(FractionMoney.of(3, 2, CHF), money.remainder(2L));

    money = FractionMoney.of(7, 3, CHF); // 2.3333
    assertEquals(FractionMoney.of(1, 3, CHF), money.remainder(2L));

    money = FractionMoney.of(8, 3, CHF); // 2.6666
    assertEquals(FractionMoney.of(2, 3, CHF), money.remainder(2L));
  }

  @Test
  void remainderLongSign() {
    // positive, positive
    FractionMoney money = FractionMoney.of(3, 1, CHF);
    BigDecimal expected = BigDecimal.valueOf(3L).remainder(BigDecimal.valueOf(2L));
    assertEquals(expected.signum(), money.remainder(2L).signum());

    // positive, negative
    money = FractionMoney.of(3, 1, CHF);
    expected = BigDecimal.valueOf(3L).remainder(BigDecimal.valueOf(-2L));
    assertEquals(expected.signum(), money.remainder(-2L).signum());

    // negative, positive
    money = FractionMoney.of(-3, 1, CHF);
    expected = BigDecimal.valueOf(-3L).remainder(BigDecimal.valueOf(2L));
    assertEquals(expected.signum(), money.remainder(2L).signum());

    // negative, negative
    money = FractionMoney.of(-3, 1, CHF);
    expected = BigDecimal.valueOf(-3L).remainder(BigDecimal.valueOf(-2L));
    assertEquals(expected.signum(), money.remainder(-2L).signum());
  }

  @Test
  void remainderFraction() {
    FractionMoney money = FractionMoney.of(31, 10, CHF); // 3.1
    assertEquals(FractionMoney.of(1, 10, CHF), money.remainder(Fraction.of(3L, 2L)));

    money = FractionMoney.of(32, 10, CHF); // 3.2
    assertEquals(FractionMoney.of(1, 5, CHF), money.remainder(Fraction.of(3L, 2L)));

    money = FractionMoney.of(33, 10, CHF); // 3.2
    assertEquals(FractionMoney.of(3, 10, CHF), money.remainder(Fraction.of(3L, 2L)));
  }

}
