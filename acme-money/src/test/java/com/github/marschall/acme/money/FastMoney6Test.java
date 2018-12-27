package com.github.marschall.acme.money;

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
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmountFactory;
import javax.money.MonetaryContext;
import javax.money.NumberValue;
import javax.money.format.MonetaryParseException;

import org.junit.jupiter.api.Test;

class FastMoney6Test {

  private static final CurrencyUnit CHF = Monetary.getCurrency("CHF");

  private static final CurrencyUnit EUR = Monetary.getCurrency("EUR");

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
  void ofByte() {
    Byte value = Byte.valueOf((byte) 1);
    FastMoney6 money = FastMoney6.of(value, CHF);
    assertEquals(value, money.getNumber().numberValueExact(Byte.class));
  }

  @Test
  void ofShort() {
    Short value = Short.valueOf((short) 1);
    FastMoney6 money = FastMoney6.of(value, CHF);
    assertEquals(value, money.getNumber().numberValueExact(Short.class));
  }

  @Test
  void ofInteger() {
    Integer value = Integer.valueOf(1);
    FastMoney6 money = FastMoney6.of(value, CHF);
    assertEquals(value, money.getNumber().numberValueExact(Integer.class));
  }

  @Test
  void ofAtomicInteger() {
    AtomicInteger value = new AtomicInteger(1);
    FastMoney6 money = FastMoney6.of(value, CHF);
    assertEquals(value.get(), money.getNumber().numberValueExact(AtomicInteger.class).get());
  }

  @Test
  void ofLong() {
    Long value = Long.valueOf(1L);
    FastMoney6 money = FastMoney6.of(value, CHF);
    assertEquals(value, money.getNumber().numberValueExact(Long.class));

    assertThrows(ArithmeticException.class, () -> FastMoney6.of(Long.MAX_VALUE, CHF));
  }

  @Test
  void ofAtomicLong() {
    AtomicLong value = new AtomicLong(1L);
    FastMoney6 money = FastMoney6.of(value, CHF);
    assertEquals(value.get(), money.getNumber().numberValueExact(AtomicLong.class).get());

    assertThrows(ArithmeticException.class, () -> FastMoney6.of(new AtomicLong(Long.MAX_VALUE), CHF));
  }

  @Test
  void ofFloat() {
    Float value = Float.valueOf(1.5f);
    FastMoney6 money = FastMoney6.of(value, CHF);
    assertEquals(value, money.getNumber().numberValue(Float.class), 0.000001f);

    assertThrows(ArithmeticException.class, () -> FastMoney6.of(Float.MAX_VALUE, CHF));
  }

  @Test
  void ofDouble() {
    Double value = Double.valueOf(1.5d);
    FastMoney6 money = FastMoney6.of(value, CHF);
    assertEquals(value, money.getNumber().numberValue(Double.class), 0.000001d);

    assertThrows(ArithmeticException.class, () -> FastMoney6.of(Double.MAX_VALUE, CHF));
  }

  @Test
  void ofBigDecimal() {
    BigDecimal value = BigDecimal.valueOf(15, 1);
    FastMoney6 money = FastMoney6.of(value, CHF);
    assertThat(money.getNumber().numberValueExact(BigDecimal.class), comparesEqualTo(value));

    assertThrows(ArithmeticException.class, () -> FastMoney6.of(BigDecimal.valueOf(Long.MAX_VALUE), CHF));
    assertThrows(ArithmeticException.class, () -> FastMoney6.of(new BigDecimal("0.0000001"), CHF));
  }

  @Test
  void ofBigInteger() {
    BigInteger value = BigInteger.valueOf(1L);
    FastMoney6 money = FastMoney6.of(value, CHF);
    assertEquals(value, money.getNumber().numberValueExact(BigInteger.class));

    assertThrows(ArithmeticException.class, () -> FastMoney6.of(BigInteger.valueOf(Long.MAX_VALUE), CHF));
    assertThrows(ArithmeticException.class, () -> FastMoney6.of(BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE), CHF));
  }

  @Test
  void ofFastNumber6() {
    Long value = Long.valueOf(1L);
    FastMoney6 money = FastMoney6.of(value, CHF);
    FastNumber6 number = money.getNumber().numberValueExact(FastNumber6.class);

    money = FastMoney6.of(number, CHF);

    assertEquals(number, money.getNumber().numberValueExact(FastNumber6.class));
  }

  @Test
  void ofFastNumberValue6() {
    Long value = Long.valueOf(1L);
    FastMoney6 money = FastMoney6.of(value, CHF);
    NumberValue numberValue = money.getNumber();

    money = FastMoney6.of(numberValue, CHF);

    assertThat(money.getNumber(), comparesEqualTo(numberValue));
  }

  @Test
  void ofFraction() {
    FractionMoney fractionMoney = FractionMoney.of(3, 4, CHF);
    FastMoney6 fastMoney = FastMoney6.of(fractionMoney.getNumber().numberValueExact(Fraction.class), CHF);
    assertThat(fastMoney.getNumber().numberValueExact(BigDecimal.class), comparesEqualTo(BigDecimal.valueOf(75, 2)));

    fractionMoney = FractionMoney.of(1, 3, CHF);
    fastMoney = FastMoney6.of(fractionMoney.getNumber().numberValueExact(Fraction.class), CHF);
    assertThat(fastMoney.getNumber().numberValueExact(BigDecimal.class), comparesEqualTo(new BigDecimal("0.333333")));
  }

  @Test
  void ofFractionValue() {
    FractionMoney fractionMoney = FractionMoney.of(3, 4, CHF);
    FastMoney6 fastMoney = FastMoney6.of(fractionMoney.getNumber(), CHF);
    assertThat(fastMoney.getNumber().numberValueExact(BigDecimal.class), comparesEqualTo(BigDecimal.valueOf(75, 2)));

    fractionMoney = FractionMoney.of(1, 3, CHF);
    fastMoney = FastMoney6.of(fractionMoney.getNumber(), CHF);
    assertThat(fastMoney.getNumber().numberValueExact(BigDecimal.class), comparesEqualTo(new BigDecimal("0.333333")));
  }

  @Test
  void parse() {
    FastMoney6 fastMoney = FastMoney6.parse("CHF 1");
    assertEquals(FastMoney6.of(Integer.valueOf(1), Monetary.getCurrency("CHF")), fastMoney);

    fastMoney = FastMoney6.parse("CHF 1.0");
    assertEquals(FastMoney6.of(Integer.valueOf(1), Monetary.getCurrency("CHF")), fastMoney);

    fastMoney = FastMoney6.parse("CHF -1");
    assertEquals(FastMoney6.of(Integer.valueOf(-1), Monetary.getCurrency("CHF")), fastMoney);

    fastMoney = FastMoney6.parse("CHF -1.123456");
    assertEquals(FastMoney6.of(new BigDecimal("-1.123456"), Monetary.getCurrency("CHF")), fastMoney);

    fastMoney = FastMoney6.parse("CHF 1.123456");
    assertEquals(FastMoney6.of(new BigDecimal("1.123456"), Monetary.getCurrency("CHF")), fastMoney);

    fastMoney = FastMoney6.parse("CHF 1.123");
    assertEquals(FastMoney6.of(new BigDecimal("1.123"), Monetary.getCurrency("CHF")), fastMoney);

    fastMoney = FastMoney6.parse("CHF -9223372036854.775808");
    assertEquals(FastMoney6.of(new BigDecimal("-9223372036854.775808"), Monetary.getCurrency("CHF")), fastMoney);
  }

  @Test
  void parseError() {
    assertThrows(MonetaryParseException.class, () -> FastMoney6.parse("CHF"));
    assertThrows(MonetaryParseException.class, () -> FastMoney6.parse("CHF "));
    assertThrows(MonetaryParseException.class, () -> FastMoney6.parse("CHF a"));
    assertThrows(MonetaryParseException.class, () -> FastMoney6.parse("CHF 1."));
    assertThrows(MonetaryParseException.class, () -> FastMoney6.parse("CHF 1.a"));
    assertThrows(MonetaryParseException.class, () -> FastMoney6.parse("CHF 1.1234567"));
    assertThrows(MonetaryParseException.class, () -> FastMoney6.parse("CHF 1.1234567"));
    assertThrows(MonetaryParseException.class, () -> FastMoney6.parse("CHF 12345678901234"));
  }

  @Test
  void fromFractionMoney() {
    FractionMoney fractionMoney = FractionMoney.of(3, 4, CHF);

    FastMoney6 fastMoney = FastMoney6.from(fractionMoney);

    assertThat(fastMoney.getNumber().numberValueExact(BigDecimal.class), comparesEqualTo(BigDecimal.valueOf(75, 2)));
  }

  @Test
  void serialize() throws ClassNotFoundException, IOException {
    FastMoney6 money = FastMoney6.of(1L, CHF);
    assertSame(money, money.plus());

    money = FastMoney6.of(-1L, CHF);
    assertEquals(money, SerializationUtil.serializeCopy(money));
  }

  @Test
  void plus() {
    FastMoney6 money = FastMoney6.of(1L, CHF);
    assertSame(money, money.plus());

    money = FastMoney6.of(-1L, CHF);
    assertSame(money, money.plus());
  }

  @Test
  void stripTrailingZeros() {
    FastMoney6 money = FastMoney6.of(new BigDecimal("1.000000"), CHF);
    assertSame(money, money.stripTrailingZeros());
  }

  @Test
  void abs() {
    FastMoney6 money = FastMoney6.of(1L, CHF);
    assertSame(money, money.abs());

    money = FastMoney6.of(0L, CHF);
    assertSame(money, money.abs());

    money = FastMoney6.of(-1L, CHF);
    assertEquals(FastMoney6.of(1L, CHF), money.abs());

    money = FastMoney6.of(FastMoney6.MIN_VALUE, CHF);
    assertThrows(ArithmeticException.class, money::abs);
  }

  @Test
  void negate() {
    FastMoney6 money = FastMoney6.of(1L, CHF);
    assertEquals(FastMoney6.of(-1L, CHF), money.negate());

    money = FastMoney6.of(0L, CHF);
    assertSame(money, money.negate());

    money = FastMoney6.of(-1L, CHF);
    assertEquals(FastMoney6.of(1L, CHF), money.negate());

    money = FastMoney6.of(FastMoney6.MIN_VALUE, CHF);
    assertThrows(ArithmeticException.class, money::negate);
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
  void add() {
    FastMoney6 original = FastMoney6.of(new BigDecimal("3.3"), CHF);
    FastMoney6 sum = original.add(FastMoney6.of(new BigDecimal("5.5"), CHF));
    assertThat(sum.getNumber().numberValueExact(BigDecimal.class), comparesEqualTo(new BigDecimal("8.8")));
  }

  @Test
  void subtract() {
    FastMoney6 minuend = FastMoney6.of(new BigDecimal("8.8"), CHF);
    FastMoney6 subtrahend = FastMoney6.of(new BigDecimal("5.5"), CHF);
    FastMoney6 difference = minuend.subtract(subtrahend);
    assertThat(difference.getNumber().numberValueExact(BigDecimal.class), comparesEqualTo(new BigDecimal("3.3")));
  }

  @Test
  void testDivideNumberAvoidsDouble() {
    BigDecimal baseValue = new BigDecimal("1000000");
    BigDecimal divisor = new BigDecimal("0.000001");

    FastMoney6 money = FastMoney6.of(baseValue, CHF);
    BigDecimal expectedValue = baseValue.divide(divisor);
    assertEquals(FastMoney6.of(expectedValue, CHF), money.divide(divisor));
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

  @Test
  void multiplyDouble() {
    FastMoney6 oneMicro = FastMoney6.of(BigDecimal.ONE, CHF);
    assertEquals(0.000001d, oneMicro.multiply(0.000001d).getNumber().doubleValue(), 0.000000000001d);

    FastMoney6 one = FastMoney6.of(BigDecimal.ONE, CHF);
    assertEquals(-1.5d, oneMicro.multiply(-1.5d).getNumber().doubleValue(), 0.000001d);
    assertThrows(ArithmeticException.class, () -> one.multiply(Double.POSITIVE_INFINITY));
    assertThrows(ArithmeticException.class, () -> one.multiply(Double.NEGATIVE_INFINITY));
    assertThrows(ArithmeticException.class, () -> one.multiply(Double.NaN));
  }

  @Test
  void multiplyGreaterScale() {
    BigDecimal multiplier = new BigDecimal("10000000");
    BigDecimal multiplicand = new BigDecimal("0.0000001");
    assertThat(multiplier.multiply(multiplicand), comparesEqualTo(BigDecimal.ONE));
    FastMoney6 money = FastMoney6.of(multiplier, CHF);
    assertEquals(1, money.multiply(multiplicand).getNumber().intValueExact());
  }

  @Test
  void divideGreaterScale() {
    BigDecimal dividend = BigDecimal.ONE;
    BigDecimal divisor = new BigDecimal("0.0000001");
    assertThat(dividend.divide(divisor), comparesEqualTo(new BigDecimal("10000000")));
    FastMoney6 money = FastMoney6.of(dividend, CHF);
    assertThat(money.divide(divisor).getNumber().numberValueExact(BigDecimal.class), comparesEqualTo(new BigDecimal("10000000")));
  }

  @Test
  void divideDouble() {
    FastMoney6 oneMicro = FastMoney6.of(BigDecimal.valueOf(1, 6), CHF);
    assertEquals(1d, oneMicro.divide(0.000001d).getNumber().doubleValue(), 0.000001d);

    FastMoney6 one = FastMoney6.of(BigDecimal.valueOf(-3.75), CHF);
    assertEquals(-2.5d, one.divide(1.5d).getNumber().doubleValue(), 0.000001d);
    assertTrue(one.divide(Double.POSITIVE_INFINITY).isZero());
    assertTrue(one.divide(Double.NEGATIVE_INFINITY).isZero());
    assertThrows(ArithmeticException.class, () -> one.divide(0.0d));
    assertThrows(ArithmeticException.class, () -> one.divide(-0.0d));
    assertThrows(ArithmeticException.class, () -> one.divide(Double.NaN));
  }

  @Test
  void remainderDouble() {
    FastMoney6 money = FastMoney6.of(BigDecimal.valueOf(5, 1), CHF);
    assertEquals(0.1d, money.remainder(0.2d).getNumber().doubleValue(), 0.000001d);

    money = FastMoney6.of(BigDecimal.valueOf(15, 1), CHF);
    assertEquals(0.1d, money.remainder(0.2d).getNumber().doubleValue(), 0.000001d);

    money = FastMoney6.of(BigDecimal.valueOf(-15, 1), CHF);
    assertEquals(-0.1d, money.remainder(0.2d).getNumber().doubleValue(), 0.000001d);

    money = FastMoney6.of(BigDecimal.valueOf(15, 1), CHF);
    assertEquals(0.1d, money.remainder(-0.2d).getNumber().doubleValue(), 0.000001d);
  }

  @Test
  void remainderNumber() {
    FastMoney6 money = FastMoney6.of(10L, CHF);
    FastMoney6 remainder = money.remainder(BigDecimal.valueOf(3L));
    assertEquals(remainder, FastMoney6.of(1L, CHF));
  }

  @Test
  void isMethods() {
    FastMoney6 money = FastMoney6.of(0L, CHF);
    assertTrue(money.isZero());
    assertFalse(money.isPositive());
    assertTrue(money.isPositiveOrZero());
    assertFalse(money.isNegative());
    assertTrue(money.isNegativeOrZero());
    assertEquals(0, money.signum());

    money = FastMoney6.of(1L, CHF);
    assertFalse(money.isZero());
    assertTrue(money.isPositive());
    assertTrue(money.isPositiveOrZero());
    assertFalse(money.isNegative());
    assertFalse(money.isNegativeOrZero());
    assertEquals(1, money.signum());

    money = FastMoney6.of(-1L, CHF);
    assertFalse(money.isZero());
    assertFalse(money.isPositive());
    assertFalse(money.isPositiveOrZero());
    assertTrue(money.isNegative());
    assertTrue(money.isNegativeOrZero());
    assertEquals(-1, money.signum());
  }

  @Test
  void compareMethods() {
    FastMoney6 smaller = FastMoney6.of(2L, CHF);
    FastMoney6 greater = FastMoney6.of(3L, CHF);

    assertTrue(smaller.isLessThan(greater));
    assertTrue(smaller.isLessThanOrEqualTo(greater));
    assertFalse(greater.isLessThan(smaller));
    assertFalse(greater.isLessThanOrEqualTo(smaller));

    assertFalse(smaller.isEqualTo(greater));
    assertTrue(smaller.isEqualTo(smaller));
    assertTrue(smaller.isLessThanOrEqualTo(smaller));
    assertTrue(smaller.isGreaterThanOrEqualTo(smaller));

    assertFalse(smaller.isGreaterThan(greater));
    assertFalse(smaller.isGreaterThanOrEqualTo(greater));
    assertTrue(greater.isGreaterThan(smaller));
    assertTrue(greater.isGreaterThanOrEqualTo(smaller));
  }

  @Test
  public void multiplyNonTerminating() {
    FastMoney6 money = FastMoney6.of(BigDecimal.ONE, CHF);
    assertEquals(money.multiply(Fraction.of(1L, 3L)).getNumber().numberValueExact(BigDecimal.class), new BigDecimal("0.333333"));
  }

  @Test
  public void multiplyRounding() {
    FastMoney6 money = FastMoney6.of(new BigDecimal("0.000005"), CHF);
    assertEquals(money.multiply(new BigDecimal("0.5")).getNumber().numberValueExact(BigDecimal.class), new BigDecimal("0.000002"));
  }

  @Test
  void multiplyLarge() {
    FastMoney6 money = FastMoney6.of(new BigDecimal("1111111111111.111111"), CHF);

    FastMoney6 product = money.multiply(new BigDecimal("2"));
    assertThat(product.getNumber().numberValueExact(BigDecimal.class), comparesEqualTo(new BigDecimal("2222222222222.222222")));
  }

  @Test
  void multiplyMultiplier() {
    FastMoney6 money = FastMoney6.of(new BigDecimal("0.000001"), CHF);

    FastMoney6 product = money.multiply(new BigInteger("1000000000000000000"));
    assertThat(product.getNumber().numberValueExact(BigDecimal.class), comparesEqualTo(new BigDecimal("1000000000000")));
  }

  @Test
  void multiplyLongMaxValue() {
    FastMoney6 money = FastMoney6.of(new BigDecimal("0.000001"), CHF);

    FastMoney6 product = money.multiply(BigDecimal.valueOf(Long.MAX_VALUE));
    assertThat(product.getNumber().numberValueExact(BigDecimal.class), comparesEqualTo(new BigDecimal("9223372036854.775807")));
  }

  @Test
  public void divideNonTerminating() {
    FastMoney6 money = FastMoney6.of(1L, CHF);
    assertEquals(money.divide(BigDecimal.valueOf(3L)).getNumber().numberValueExact(BigDecimal.class), new BigDecimal("0.333333"));
    assertEquals(money.divide(3L).getNumber().numberValueExact(BigDecimal.class), new BigDecimal("0.333333"));
  }

  @Test
  void divideAndRemainder() {
    FastMoney6 money = FastMoney6.of(10L, CHF);
    FastMoney6[] divideAndRemainder = money.divideAndRemainder(BigDecimal.valueOf(3L));
    FastMoney6 quotient = divideAndRemainder[0];
    FastMoney6 remainder = divideAndRemainder[1];
    assertEquals(quotient, FastMoney6.of(3L, CHF));
    assertEquals(remainder, FastMoney6.of(1L, CHF));
  }

  @Test
  void divideAndRemainderByZero() {
    FastMoney6 money = FastMoney6.of(10L, CHF);
    assertThrows(ArithmeticException.class, () -> money.divideAndRemainder(0.0d));
    assertThrows(ArithmeticException.class, () -> money.divideAndRemainder(-0.0d));
    assertThrows(ArithmeticException.class, () -> money.divideAndRemainder(Double.NaN));
  }

  @Test
  void divideToIntegralValueDouble() {
    FastMoney6 money = FastMoney6.of(10L, CHF);
    FastMoney6 quotient = money.divideToIntegralValue(BigDecimal.valueOf(3L));
    assertEquals(quotient, FastMoney6.of(3L, CHF));
  }

  private static void validateContext(MonetaryContext context) {
    assertEquals(19, context.getPrecision());
    assertEquals(6, context.getMaxScale());
    assertEquals("acme", context.getProviderName());
    assertEquals(FastMoney6.class, context.getAmountType());
  }

}
