package com.github.marschall.acme.money;

import javax.money.NumberValue;

import static java.lang.Math.*;

public final class FractionValue extends NumberValue {
  
  private final long numerator;
  private final long denominator;
  
  FractionValue(long numerator, long denominator) {
    this.numerator = numerator;
    this.denominator = denominator;
  }

  @Override
  public Class<?> getNumberType() {
    return Fraction.class;
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
    return toIntExact(longValueExact());
  }

  @Override
  public long longValueExact() {
    if (this.denominator == 1) {
      return this.numerator;
    } else {
      throw new ArithmeticException("integer overflow");
    }
  }

  @Override
  public double doubleValueExact() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public <T extends Number> T numberValue(Class<T> numberType) {
    if (numberType == Fraction.class) {
      return (T) Fraction.of(this.numerator, this.denominator);
    }
    return ConvertToNumber.of(numberType, this.numerator, this.denominator);
  }

  @Override
  public <T extends Number> T numberValueExact(Class<T> numberType) {
    if (numberType == Fraction.class) {
      return (T) Fraction.of(this.numerator, this.denominator);
    }
    return ConvertToNumber.ofExact(numberType, this.numerator, this.denominator);
  }

  @Override
  public long getAmountFractionNumerator() {
    return this.numerator % this.denominator;
  }

  @Override
  public long getAmountFractionDenominator() {
    return this.denominator;
  }

  @Override
  public int intValue() {
    return (int) (numerator / denominator);
  }

  @Override
  public long longValue() {
    return numerator / denominator;
  }

  @Override
  public float floatValue() {
    return (float) numerator / (float) denominator;
  }

  @Override
  public double doubleValue() {
    return (double) numerator / (double) denominator;
  }

  @Override
  public String toString() {
    return Long.toString(this.numerator) + '/' + this.denominator;
  }

}
