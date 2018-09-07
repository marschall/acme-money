package com.github.marschall.acme.money;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class FastNumber6Test {

  @Test
  void byteValue() {
    BigDecimal bigDecimal = BigDecimal.valueOf(Long.MAX_VALUE, FastMoney6.SCALE);
    FastNumber6 fastNumber = new FastNumber6(Long.MAX_VALUE);
    assertEquals(bigDecimal.byteValue(), fastNumber.byteValue());

    bigDecimal = BigDecimal.valueOf(Long.MIN_VALUE, FastMoney6.SCALE);
    fastNumber = new FastNumber6(Long.MIN_VALUE);
    assertEquals(bigDecimal.byteValue(), fastNumber.byteValue());
  }

  @Test
  void shortValue() {
    BigDecimal bigDecimal = BigDecimal.valueOf(Long.MAX_VALUE, FastMoney6.SCALE);
    FastNumber6 fastNumber = new FastNumber6(Long.MAX_VALUE);
    assertEquals(bigDecimal.shortValue(), fastNumber.shortValue());

    bigDecimal = BigDecimal.valueOf(Long.MIN_VALUE, FastMoney6.SCALE);
    fastNumber = new FastNumber6(Long.MIN_VALUE);
    assertEquals(bigDecimal.shortValue(), fastNumber.shortValue());
  }

  @Test
  void intValueRounding() {

    // positive

    BigDecimal bigDecimal = BigDecimal.valueOf(19, 1);
    FastNumber6 fastNumber = new FastNumber6(1_900000L);
    assertEquals(1, bigDecimal.intValue());
    assertEquals(1, fastNumber.intValue());

    bigDecimal = BigDecimal.valueOf(11, 1);
    fastNumber = new FastNumber6(1_100000L);
    assertEquals(1, bigDecimal.intValue());
    assertEquals(1, fastNumber.intValue());

    bigDecimal = BigDecimal.valueOf(29, 1);
    fastNumber = new FastNumber6(2_900000L);
    assertEquals(2, bigDecimal.intValue());
    assertEquals(2, fastNumber.intValue());

    bigDecimal = BigDecimal.valueOf(21, 1);
    fastNumber = new FastNumber6(2_100000L);
    assertEquals(2, bigDecimal.intValue());
    assertEquals(2, fastNumber.intValue());

    // negative

    bigDecimal = BigDecimal.valueOf(-19, 1);
    fastNumber = new FastNumber6(-1_900000L);
    assertEquals(-1, bigDecimal.intValue());
    assertEquals(-1, fastNumber.intValue());

    bigDecimal = BigDecimal.valueOf(-11, 1);
    fastNumber = new FastNumber6(-1_100000L);
    assertEquals(-1, bigDecimal.intValue());
    assertEquals(-1, fastNumber.intValue());

    bigDecimal = BigDecimal.valueOf(-29, 1);
    fastNumber = new FastNumber6(-2_900000L);
    assertEquals(-2, bigDecimal.intValue());
    assertEquals(-2, fastNumber.intValue());

    bigDecimal = BigDecimal.valueOf(-21, 1);
    fastNumber = new FastNumber6(-2_100000L);
    assertEquals(-2, bigDecimal.intValue());
    assertEquals(-2, fastNumber.intValue());
  }

  @Test
  void intValueOverflow() {
    BigDecimal bigDecimal = BigDecimal.valueOf(1L + Integer.MAX_VALUE);
    FastNumber6 fastNumber = new FastNumber6((1L + Integer.MAX_VALUE) * FastMoney6.DIVISOR);
    assertEquals(bigDecimal.intValue(), fastNumber.intValue());

    bigDecimal = BigDecimal.valueOf(-1L - Integer.MIN_VALUE);
    fastNumber = new FastNumber6((-1L - Integer.MIN_VALUE) * FastMoney6.DIVISOR);
    assertEquals(bigDecimal.intValue(), fastNumber.intValue());

    bigDecimal = BigDecimal.valueOf(Long.MAX_VALUE, FastMoney6.SCALE);
    fastNumber = new FastNumber6(Long.MAX_VALUE);
    assertEquals(bigDecimal.intValue(), fastNumber.intValue());

    bigDecimal = BigDecimal.valueOf(Long.MIN_VALUE, FastMoney6.SCALE);
    fastNumber = new FastNumber6(Long.MIN_VALUE);
    assertEquals(bigDecimal.intValue(), fastNumber.intValue());
  }

  @Test
  void longValue() {
    BigDecimal bigDecimal = BigDecimal.valueOf(Long.MAX_VALUE, FastMoney6.SCALE);
    FastNumber6 fastNumber = new FastNumber6(Long.MAX_VALUE);
    assertEquals(bigDecimal.longValue(), fastNumber.longValue());

    bigDecimal = BigDecimal.valueOf(Long.MIN_VALUE, FastMoney6.SCALE);
    fastNumber = new FastNumber6(Long.MIN_VALUE);
    assertEquals(bigDecimal.longValue(), fastNumber.longValue());
  }

  @Test
  void floatValue() {
    BigDecimal bigDecimal = BigDecimal.valueOf(Long.MAX_VALUE, FastMoney6.SCALE);
    FastNumber6 fastNumber = new FastNumber6(Long.MAX_VALUE);
    assertEquals(bigDecimal.floatValue(), fastNumber.floatValue(), 0.000001f);

    bigDecimal = BigDecimal.valueOf(Long.MIN_VALUE, FastMoney6.SCALE);
    fastNumber = new FastNumber6(Long.MIN_VALUE);
    assertEquals(bigDecimal.floatValue(), fastNumber.floatValue(), 0.000001f);
  }

  @Test
  void doubleValue() {
    BigDecimal bigDecimal = BigDecimal.valueOf(Long.MAX_VALUE, FastMoney6.SCALE);
    FastNumber6 fastNumber = new FastNumber6(Long.MAX_VALUE);
    assertEquals(bigDecimal.doubleValue(), fastNumber.doubleValue(), 0.000001d);

    bigDecimal = BigDecimal.valueOf(Long.MIN_VALUE, FastMoney6.SCALE);
    fastNumber = new FastNumber6(Long.MIN_VALUE);
    assertEquals(bigDecimal.doubleValue(), fastNumber.doubleValue(), 0.000001d);
  }

  @Test
  void testEquals() {
    FastNumber6 zero = new FastNumber6(0L);
    FastNumber6 one = new FastNumber6(1_000000L);

    assertEquals(one, one);
    assertEquals(one, new FastNumber6(1_000000L));
    assertNotEquals(one, zero);
  }

  @Test
  void testToString() {
    assertEquals("0.000000", new FastNumber6(0L).toString());
    assertEquals("0.000001", new FastNumber6(1L).toString());
    assertEquals("-0.000001", new FastNumber6(-1L).toString());
    assertEquals("1.000000", new FastNumber6(1_000000L).toString());
    assertEquals("-1.000000", new FastNumber6(-1_000000L).toString());
  }

  @Test
  void compareTo() {
    FastNumber6 zero = new FastNumber6(0L);
    FastNumber6 one = new FastNumber6(1_000000L);

    assertThat(zero, comparesEqualTo(zero));
    assertThat(zero, lessThan(one));
    assertThat(zero, lessThanOrEqualTo(one));
    assertThat(one, greaterThan(zero));
    assertThat(one, greaterThanOrEqualTo(zero));
  }

  @Test
  void serialize() throws ClassNotFoundException, IOException {
    FastNumber6 number = new FastNumber6(123456789L);
    assertEquals(number, SerializationUtil.serializeCopy(number));
  }

}
