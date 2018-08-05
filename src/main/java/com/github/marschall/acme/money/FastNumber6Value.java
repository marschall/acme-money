package com.github.marschall.acme.money;

import java.math.BigDecimal;
import java.math.MathContext;

import javax.money.NumberValue;

final class FastNumber6Value extends NumberValue {

  private static final long serialVersionUID = 1L;

  private final long value;

  FastNumber6Value(long value) {
    this.value = value;
  }

  @Override
  public Class<?> getNumberType() {
    return FastNumber6.class;
  }

  private BigDecimal getBigDecimal() {
    return BigDecimal.valueOf(this.value, FastMoney6.SCALE);
  }

  @Override
  public int getPrecision() {
    return 19;
  }

  @Override
  public int getScale() {
    return FastMoney6.SCALE;
  }

  private boolean hasDecimalPlaces() {
    long remainder = this.value % FastMoney6.DIVISOR;
    return remainder != 0;
  }

  private void checkNoDecimalPlaces() {
    if (this.hasDecimalPlaces()) {
      throw new ArithmeticException("decimal places present");
    }
  }

  @Override
  public NumberValue round(MathContext mathContext) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int intValueExact() {
    this.checkNoDecimalPlaces();
    return Math.toIntExact(this.value / FastMoney6.DIVISOR);
  }

  @Override
  public long longValueExact() {
    this.checkNoDecimalPlaces();
    return this.value / FastMoney6.DIVISOR;
  }

  @Override
  public double doubleValueExact() {
    return this.doubleValue();
  }

  @Override
  public <T extends Number> T numberValue(Class<T> numberType) {
    if (numberType == FastNumber6.class) {
      return numberType.cast(new FastNumber6(this.value));
    }
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <T extends Number> T numberValueExact(Class<T> numberType) {
    if (numberType == FastNumber6.class) {
      return numberType.cast(new FastNumber6(this.value));
    }
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
    return this.value / FastMoney6.DIVISOR;
  }

  @Override
  public float floatValue() {
    return this.value / (float) FastMoney6.DIVISOR;
  }

  @Override
  public double doubleValue() {
    return this.value / (double) FastMoney6.DIVISOR;
  }

}
