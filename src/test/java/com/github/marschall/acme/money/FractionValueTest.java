package com.github.marschall.acme.money;

import static com.github.marschall.acme.money.HasNoNumberValue.hasNoNumberValue;
import static com.github.marschall.acme.money.HasNoNumberValueExact.hasNoNumberValueExact;
import static com.github.marschall.acme.money.HasNumberValue.hasNumberValue;
import static com.github.marschall.acme.money.HasNumberValueExact.hasNumberValueExcat;
import static org.junit.Assert.assertThat;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

public class FractionValueTest {

  @Test
  public void convertToInteger() {
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
  public void convertToAtomicInteger() {
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
  public void convertToLong() {
    FractionValue value = new FractionValue(10, 1);
    assertThat(value, hasNumberValue(10L));
    assertThat(value, hasNumberValueExcat(10L));

    value = new FractionValue(5, 2);
    assertThat(value, hasNumberValue(2L));
    assertThat(value, hasNoNumberValueExact(Long.class));
  }

  @Test
  public void convertToAtomicLong() {
    FractionValue value = new FractionValue(10, 1);
    assertThat(value, hasNumberValue(new AtomicLong(10)));
    assertThat(value, hasNumberValueExcat(new AtomicLong(10)));

    value = new FractionValue(5, 2);
    assertThat(value, hasNumberValue(new AtomicLong(2)));
    assertThat(value, hasNoNumberValueExact(AtomicLong.class));
  }

  @Test
  public void convertToShort() {
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
  public void convertToByte() {
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
  public void convertToDouble() {
    FractionValue value = new FractionValue(10, 1);
    assertThat(value, hasNumberValue(10.0d));
    assertThat(value, hasNumberValueExcat(10.0d));

    value = new FractionValue(Long.MAX_VALUE, 1);
    assertThat(value, hasNumberValue((double) Long.MAX_VALUE));
    assertThat(value, hasNumberValueExcat((double) Long.MAX_VALUE));
  }

  @Test
  public void convertToFloat() {
    FractionValue value = new FractionValue(10, 1);
    assertThat(value, hasNumberValue(10.0f));
    assertThat(value, hasNumberValueExcat(10.0f));

    value = new FractionValue(Long.MAX_VALUE, 1);
    assertThat(value, hasNumberValue((float) Long.MAX_VALUE));
    assertThat(value, hasNumberValueExcat((float) Long.MAX_VALUE));
  }

}
