package com.github.marschall.acme.money;

import java.math.BigDecimal;

import javax.money.NumberValue;

final class FastNumber6Value extends NumberValue {

  private final long value;

  FastNumber6Value(long value) {
    this.value = value;
  }

  @Override
  public Class<?> getNumberType() {
    return BigDecimal.class;
  }
  
  private BigDecimal getBigDecimal() {
    return BigDecimal.valueOf(this.value).movePointLeft(FastMoney6.SCALE);
  }

  @Override
  public int getPrecision() {
    return 19;
  }

  @Override
  public int getScale() {
    return FastMoney6.SCALE;
  }

  @Override
  public int intValueExact() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public long longValueExact() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public double doubleValueExact() {
    return this.doubleValue();
  }

  @Override
  public <T extends Number> T numberValue(Class<T> numberType) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <T extends Number> T numberValueExact(Class<T> numberType) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public long getAmountFractionNumerator() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public long getAmountFractionDenominator() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int intValue() {
    return (int) this.longValue();
  }

  @Override
  public long longValue() {
    return this.value / 1000000L;
  }

  @Override
  public float floatValue() {
    return this.value / 1000000.0f;
  }

  @Override
  public double doubleValue() {
    return this.value / 1000000.0d;
  }

}
