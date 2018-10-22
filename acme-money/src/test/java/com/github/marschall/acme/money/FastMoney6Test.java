package com.github.marschall.acme.money;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

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
  }

  @Test
  void ofFractionValue() {
    FractionMoney fractionMoney = FractionMoney.of(3, 4, CHF);

    FastMoney6 fastMoney = FastMoney6.of(fractionMoney.getNumber(), CHF);

    assertThat(fastMoney.getNumber().numberValueExact(BigDecimal.class), comparesEqualTo(BigDecimal.valueOf(75, 2)));
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
    FastMoney6 money = FastMoney6.of(BigDecimal.ONE, CHF);
    assertEquals(0.000001d, money.multiply(0.000001d).getNumber().doubleValue(), 0.000000000001d);

    money = FastMoney6.of(BigDecimal.ONE, CHF);
    assertEquals(-1.5d, money.multiply(-1.5d).getNumber().doubleValue(), 0.000001d);
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
    FastMoney6 money = FastMoney6.of(BigDecimal.valueOf(1, 6), CHF);
    assertEquals(1d, money.divide(0.000001d).getNumber().doubleValue(), 0.000001d);

    money = FastMoney6.of(BigDecimal.valueOf(-3.75), CHF);
    assertEquals(-2.5d, money.divide(1.5d).getNumber().doubleValue(), 0.000001d);
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
  void divideAndRemainderDouble() {
    fail("implement");
  }

  @Test
  void divideToIntegralValueDouble() {
    fail("implement");
  }

  private static void validateContext(MonetaryContext context) {
    assertEquals(19, context.getPrecision());
    assertEquals(6, context.getMaxScale());
    assertEquals("acme", context.getProviderName());
    assertEquals(FastMoney6.class, context.getAmountType());
  }

}
