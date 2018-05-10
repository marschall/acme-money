package com.github.marschall.acme.money;

import static java.lang.Math.toIntExact;

import javax.money.NumberValue;

final class FractionValue extends NumberValue {

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
    return toIntExact(this.longValueExact());
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
    return (int) (this.numerator / this.denominator);
  }

  @Override
  public long longValue() {
    return this.numerator / this.denominator;
  }

  @Override
  public float floatValue() {
    return (float) this.numerator / (float) this.denominator;
  }

  @Override
  public double doubleValue() {
    return (double) this.numerator / (double) this.denominator;
  }

  @Override
  public String toString() {
    return Long.toString(this.numerator) + '/' + this.denominator;
  }

}
