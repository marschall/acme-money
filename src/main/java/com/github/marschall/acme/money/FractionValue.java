package com.github.marschall.acme.money;

import javax.money.NumberValue;

public final class FractionValue extends NumberValue {
  
  private final long numerator;
  private final long denominator;
  
  FractionValue(long numerator, long denominator) {
    this.numerator = numerator;
    this.denominator = denominator;
  }

  @Override
  public Class<?> getNumberType() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getPrecision() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getScale() {
    // TODO Auto-generated method stub
    return 0;
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
    // TODO Auto-generated method stub
    return 0;
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
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public long longValue() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public float floatValue() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public double doubleValue() {
    // TODO Auto-generated method stub
    return 0;
  }

}
