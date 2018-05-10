package com.github.marschall.acme.money;

import static com.github.marschall.acme.money.ConvertsToFraction.convertsToFraction;
import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.Test;


class ConvertFractionTest {

  @Test
  void floats() {
    assertThat(1.5f, convertsToFraction(Fraction.of(3, 2)));
    assertThat(-1.5f, convertsToFraction(Fraction.of(-3, 2)));
  }

  @Test
  void integers() {
    assertThat(10, convertsToFraction(Fraction.of(10, 1)));
    assertThat(-10, convertsToFraction(Fraction.of(-10, 1)));
  }

  @Test
  void bytes() {
    assertThat((byte) 10, convertsToFraction(Fraction.of(10, 1)));
    assertThat((byte) -10, convertsToFraction(Fraction.of(-10, 1)));
  }

  @Test
  void shorts() {
    assertThat((short) 10, convertsToFraction(Fraction.of(10, 1)));
    assertThat((short) -10, convertsToFraction(Fraction.of(-10, 1)));
  }

  @Test
  void atomicIntegers() {
    assertThat(new AtomicInteger(10), convertsToFraction(Fraction.of(10, 1)));
    assertThat(new AtomicInteger(-10), convertsToFraction(Fraction.of(-10, 1)));
  }

  @Test
  void atomicLongs() {
    assertThat(new AtomicLong(10L), convertsToFraction(Fraction.of(10, 1)));
    assertThat(new AtomicLong(-10L), convertsToFraction(Fraction.of(-10, 1)));
  }

  @Test
  void longs() {
    assertThat(10L, convertsToFraction(Fraction.of(10, 1)));
    assertThat(-10L, convertsToFraction(Fraction.of(-10, 1)));
  }

  @Test
  void bigDecimals() {
    assertThat(new BigDecimal("1.5"), convertsToFraction(Fraction.of(3, 2)));
    assertThat(new BigDecimal("-1.5"), convertsToFraction(Fraction.of(-3, 2)));
  }

  @Test
  void bigIntegers() {
    assertThat(BigInteger.valueOf(10L), convertsToFraction(Fraction.of(10, 1)));
    assertThat(BigInteger.valueOf(-10L), convertsToFraction(Fraction.of(-10, 1)));
  }

  @Test
  void doubles() {
    assertThat(1.5d, convertsToFraction(Fraction.of(3, 2)));
    assertThat(-1.5d, convertsToFraction(Fraction.of(-3, 2)));
  }

}
