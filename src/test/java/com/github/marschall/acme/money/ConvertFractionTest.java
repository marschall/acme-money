package com.github.marschall.acme.money;

import static com.github.marschall.acme.money.ConvertsToFraction.convertsToFraction;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

public class ConvertFractionTest {

  @Test
  public void floats() {
    assertThat(1.5f, convertsToFraction(Fraction.of(3, 2)));
    assertThat(-1.5f, convertsToFraction(Fraction.of(-3, 2)));
  }

  @Test
  public void integers() {
    assertThat(10, convertsToFraction(Fraction.of(10, 1)));
    assertThat(-10, convertsToFraction(Fraction.of(-10, 1)));
  }

  @Test
  public void bytes() {
    assertThat((byte) 10, convertsToFraction(Fraction.of(10, 1)));
    assertThat((byte) -10, convertsToFraction(Fraction.of(-10, 1)));
  }

  @Test
  public void shorts() {
    assertThat((short) 10, convertsToFraction(Fraction.of(10, 1)));
    assertThat((short) -10, convertsToFraction(Fraction.of(-10, 1)));
  }

  @Test
  public void atomicIntegers() {
    assertThat(new AtomicInteger(10), convertsToFraction(Fraction.of(10, 1)));
    assertThat(new AtomicInteger(-10), convertsToFraction(Fraction.of(-10, 1)));
  }

  @Test
  public void atomicLongs() {
    assertThat(new AtomicLong(10L), convertsToFraction(Fraction.of(10, 1)));
    assertThat(new AtomicLong(-10L), convertsToFraction(Fraction.of(-10, 1)));
  }

  @Test
  public void longs() {
    assertThat(10L, convertsToFraction(Fraction.of(10, 1)));
    assertThat(-10L, convertsToFraction(Fraction.of(-10, 1)));
  }

  @Test
  public void bigDecimals() {
    assertThat(new BigDecimal("1.5"), convertsToFraction(Fraction.of(3, 2)));
    assertThat(new BigDecimal("-1.5"), convertsToFraction(Fraction.of(-3, 2)));
  }

  @Test
  public void bigIntegers() {
    assertThat(BigInteger.valueOf(10L), convertsToFraction(Fraction.of(10, 1)));
    assertThat(BigInteger.valueOf(-10L), convertsToFraction(Fraction.of(-10, 1)));
  }

  @Test
  public void doubles() {
    assertThat(1.5d, convertsToFraction(Fraction.of(3, 2)));
    assertThat(-1.5d, convertsToFraction(Fraction.of(-3, 2)));
  }

}
