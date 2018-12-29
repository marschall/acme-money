package com.github.marschall.acme.money;

import static com.github.marschall.acme.money.HasNoNumberValue.hasNoNumberValue;
import static com.github.marschall.acme.money.HasNoNumberValueExact.hasNoNumberValueExact;
import static com.github.marschall.acme.money.HasNumberValue.hasNumberValue;
import static com.github.marschall.acme.money.HasNumberValueExact.hasNumberValueExcat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.javamoney.moneta.spi.DefaultNumberValue;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class FractionValueTest {

  @Test
  void testEquals() {
    assertEquals(new FractionValue(1, 2), new FractionValue(1, 2));
    assertNotEquals(new FractionValue(-1, 2), new FractionValue(1, 2));
    assertNotEquals(new FractionValue(1, 2), null);
    assertNotEquals(new FractionValue(1, 2), Fraction.of(1, 2));
    assertNotEquals(new FractionValue(1, 2), BigDecimal.valueOf(5, 1));
  }

  @Test
  void serialize() throws ClassNotFoundException, IOException {
    FractionValue value = new FractionValue(10, 1);
    assertEquals(value, SerializationUtil.serializeCopy(value));
  }

  @Test
  void testToString() {
    assertEquals("1/2", new FractionValue(1, 2).toString());
  }

  @Test
  void convertToInteger() {
    FractionValue value = new FractionValue(10, 1);
    assertThat(value, hasNumberValue(10));
    assertThat(value, hasNumberValueExcat(10));

    value = new FractionValue(5, 2);
    assertThat(value, hasNumberValue(Integer.valueOf(2)));
    assertThat(value, hasNoNumberValueExact(Integer.class));

    value = new FractionValue(-10, 1);
    assertThat(value, hasNumberValue(-10));
    assertThat(value, hasNumberValueExcat(-10));

    value = new FractionValue(Integer.MAX_VALUE + 1L, 1);
    assertThat(value, hasNoNumberValue(Integer.class));
    assertThat(value, hasNoNumberValueExact(Integer.class));

    value = new FractionValue(Integer.MIN_VALUE - 1L, 1);
    assertThat(value, hasNoNumberValue(Integer.class));
    assertThat(value, hasNoNumberValueExact(Integer.class));
  }

  @Test
  void convertToAtomicInteger() {
    FractionValue value = new FractionValue(10, 1);
    assertThat(value, hasNumberValue(new AtomicInteger(10)));
    assertThat(value, hasNumberValueExcat(new AtomicInteger(10)));

    value = new FractionValue(5, 2);
    assertThat(value, hasNumberValue(new AtomicInteger(2)));
    assertThat(value, hasNoNumberValueExact(AtomicInteger.class));

    value = new FractionValue(Integer.MAX_VALUE + 1L, 1);
    assertThat(value, hasNoNumberValue(AtomicInteger.class));
    assertThat(value, hasNoNumberValueExact(AtomicInteger.class));

    value = new FractionValue(Integer.MIN_VALUE - 1L, 1);
    assertThat(value, hasNoNumberValue(AtomicInteger.class));
    assertThat(value, hasNoNumberValueExact(AtomicInteger.class));
  }

  @Test
  void convertToLong() {
    FractionValue value = new FractionValue(10, 1);
    assertThat(value, hasNumberValue(10L));
    assertThat(value, hasNumberValueExcat(10L));

    value = new FractionValue(5, 2);
    assertThat(value, hasNumberValue(2L));
    assertThat(value, hasNoNumberValueExact(Long.class));
  }

  @Test
  void convertToAtomicLong() {
    FractionValue value = new FractionValue(10, 1);
    assertThat(value, hasNumberValue(new AtomicLong(10)));
    assertThat(value, hasNumberValueExcat(new AtomicLong(10)));

    value = new FractionValue(5, 2);
    assertThat(value, hasNumberValue(new AtomicLong(2)));
    assertThat(value, hasNoNumberValueExact(AtomicLong.class));
  }

  @Test
  void convertToShort() {
    FractionValue value = new FractionValue(10, 1);
    assertThat(value, hasNumberValue(Short.valueOf((short) 10)));
    assertThat(value, hasNumberValueExcat(Short.valueOf((short) 10)));

    value = new FractionValue(5, 2);
    assertThat(value, hasNumberValue(Short.valueOf((short) 2)));
    assertThat(value, hasNoNumberValueExact(Short.class));

    value = new FractionValue(Short.MAX_VALUE + 1L, 1);
    assertThat(value, hasNoNumberValue(Short.class));
    assertThat(value, hasNoNumberValueExact(Short.class));

    value = new FractionValue(Short.MIN_VALUE - 1L, 1);
    assertThat(value, hasNoNumberValue(Short.class));
    assertThat(value, hasNoNumberValueExact(Short.class));
  }

  @Test
  void convertToByte() {
    FractionValue value = new FractionValue(10, 1);
    assertThat(value, hasNumberValue(Byte.valueOf((byte) 10)));
    assertThat(value, hasNumberValueExcat(Byte.valueOf((byte) 10)));

    value = new FractionValue(5, 2);
    assertThat(value, hasNumberValue(Byte.valueOf((byte) 2)));
    assertThat(value, hasNoNumberValueExact(Byte.class));

    value = new FractionValue(Byte.MAX_VALUE + 1L, 1);
    assertThat(value, hasNoNumberValue(Byte.class));
    assertThat(value, hasNoNumberValueExact(Byte.class));

    value = new FractionValue(Byte.MIN_VALUE - 1L, 1);
    assertThat(value, hasNoNumberValue(Byte.class));
    assertThat(value, hasNoNumberValueExact(Byte.class));
  }

  @Test
  void convertToDouble() {
    FractionValue value = new FractionValue(10, 1);
    assertThat(value, hasNumberValue(10.0d));
    assertThat(value, hasNumberValueExcat(10.0d));

    value = new FractionValue(Long.MAX_VALUE, 1);
    assertThat(value, hasNumberValue((double) Long.MAX_VALUE));
    assertThat(value, hasNumberValueExcat((double) Long.MAX_VALUE));
  }

  @Test
  void convertToFloat() {
    FractionValue value = new FractionValue(10, 1);
    assertThat(value, hasNumberValue(10.0f));
    assertThat(value, hasNumberValueExcat(10.0f));

    value = new FractionValue(Long.MAX_VALUE, 1);
    assertThat(value, hasNumberValue((float) Long.MAX_VALUE));
    assertThat(value, hasNumberValueExcat((float) Long.MAX_VALUE));
  }

  @Test
  void convertToBigDecimal() {
    FractionValue value = new FractionValue(3, 2);
    assertThat(value, hasNumberValue(new BigDecimal("1.5")));
    assertThat(value, hasNumberValueExcat(new BigDecimal("1.5")));

    value = new FractionValue(-3, 2);
    assertThat(value, hasNumberValue(new BigDecimal("-1.5")));
    assertThat(value, hasNumberValueExcat(new BigDecimal("-1.5")));

    value = new FractionValue(1, 3);
    assertThat(value, hasNumberValue(new BigDecimal("0.333333")));
    assertThat(value, hasNoNumberValueExact(BigDecimal.class));
  }

  @Test
  @Disabled
  void maxBigDecimal() {
    int MAX_MAG_LENGTH = 67_108_864 * 4; // 16_777_216
    BigDecimal onethird = BigDecimal.valueOf(1L)
        .divide(BigDecimal.valueOf(3L), 1000, RoundingMode.HALF_EVEN);
    assertThat(onethird, comparesEqualTo(new BigDecimal("0.333333")));
  }


  @Test
  void convertToBigInteger() {
    FractionValue value = new FractionValue(10, 1);
    assertThat(value, hasNumberValue(BigInteger.valueOf(10L)));
    assertThat(value, hasNumberValueExcat(BigInteger.valueOf(10L)));

    value = new FractionValue(5, 2);
    assertThat(value, hasNumberValue(BigInteger.valueOf(2L)));
    assertThat(value, hasNoNumberValueExact(BigInteger.class));
  }

  @Test
  void getAmountFraction() {
    BigDecimal d = new BigDecimal("0.5");
    DefaultNumberValue v = new DefaultNumberValue(d);
    assertEquals(5L, v.getAmountFractionNumerator());
    assertEquals(10L, v.getAmountFractionDenominator());

    d = new BigDecimal("1.5");
    v = new DefaultNumberValue(d);
    assertEquals(5L, v.getAmountFractionNumerator());
    assertEquals(10L, v.getAmountFractionDenominator());

    d = new BigDecimal("-0.5");
    v = new DefaultNumberValue(d);
    assertEquals(-5L, v.getAmountFractionNumerator());
    assertEquals(10L, v.getAmountFractionDenominator());

    d = new BigDecimal("-1.5");
    v = new DefaultNumberValue(d);
    assertEquals(-5L, v.getAmountFractionNumerator());
    assertEquals(10L, v.getAmountFractionDenominator());

    FractionValue f = new FractionValue(1, 2);
    assertEquals(1L, f.getAmountFractionNumerator());
    assertEquals(2L, f.getAmountFractionDenominator());

    f = new FractionValue(3, 2);
    assertEquals(1L, f.getAmountFractionNumerator());
    assertEquals(2L, f.getAmountFractionDenominator());

    f = new FractionValue(-1, 2);
    assertEquals(-1L, f.getAmountFractionNumerator());
    assertEquals(2L, f.getAmountFractionDenominator());

    f = new FractionValue(-3, 2);
    assertEquals(-1L, f.getAmountFractionNumerator());
    assertEquals(2L, f.getAmountFractionDenominator());
  }

  @Test
  void intValue() {
    FractionValue value = new FractionValue(5, 2);
    assertEquals(2, value.intValue());
  }

  @Test
  void intValueExact() {
    FractionValue value = new FractionValue(5, 2);
    assertThrows(ArithmeticException.class, value::intValueExact);
  }

  @Test
  void longValue() {
    FractionValue value = new FractionValue(5, 2);
    assertEquals(2, value.longValue());
  }

  @Test
  void longValueExact() {
    FractionValue value = new FractionValue(5, 2);
    assertThrows(ArithmeticException.class, value::longValueExact);
  }

  @Test
  void doubleValue() {
    FractionValue value = new FractionValue(5, 2);
    assertEquals(2.5d, value.doubleValue(), 0.0000001d);
  }

  @Test
  void floatValue() {
    FractionValue value = new FractionValue(5, 2);
    assertEquals(2.5f, value.floatValue(), 0.0000001f);
  }

}
