package com.github.marschall.acme.money;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Objects;

import javax.money.NumberValue;

final class FixedPointNumberValue extends NumberValue {
  
  private final BigDecimal number;

  FixedPointNumberValue(BigDecimal number) {
    Objects.requireNonNull(number, "number");
    this.number = number;
  }

  @Override
  public Class<?> getNumberType() {
    return this.number.getClass();
  }

  @Override
  public int getPrecision() {
    // TODO from MathContext
    return this.number.precision();
  }

  @Override
  public int getScale() {
    // TODO from MathContext
    return this.number.scale();
  }

  @Override
  public int intValueExact() {
    return this.number.intValueExact();
  }

  @Override
  public long longValueExact() {
    return this.number.longValueExact();
  }

  @Override
  public double doubleValueExact() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public <T extends Number> T numberValue(Class<T> numberType) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public NumberValue round(MathContext mathContext) {
    return new FixedPointNumberValue(this.number.round(mathContext));
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
    return this.number.intValue();
  }

  @Override
  public long longValue() {
    return this.number.longValue();
  }

  @Override
  public float floatValue() {
    return this.number.floatValue();
  }

  @Override
  public double doubleValue() {
    return this.number.doubleValue();
  }

}
