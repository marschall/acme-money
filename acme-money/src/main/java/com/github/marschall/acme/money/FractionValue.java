package com.github.marschall.acme.money;

import static java.lang.Math.toIntExact;

import java.io.ObjectStreamException;
import java.math.MathContext;

import javax.money.NumberValue;

final class FractionValue extends NumberValue {

  private static final long serialVersionUID = 2L;

  final long numerator;
  final long denominator;

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
  public NumberValue round(MathContext mathContext) {
    // TODO Auto-generated method stub
    // TODO round to 10^-x
    return null;
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
      return numberType.cast(Fraction.of(this.numerator, this.denominator));
    }
    return ConvertFractionToNumber.of(numberType, this.numerator, this.denominator);
  }

  @Override
  public <T extends Number> T numberValueExact(Class<T> numberType) {
    if (numberType == Fraction.class) {
      return numberType.cast(Fraction.of(this.numerator, this.denominator));
    }
    return ConvertFractionToNumber.ofExact(numberType, this.numerator, this.denominator);
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
    return FractionMath.intValue(this.numerator, this.denominator);
  }

  @Override
  public long longValue() {
    return FractionMath.longValue(this.numerator, this.denominator);
  }

  @Override
  public float floatValue() {
    return FractionMath.floatValue(this.numerator, this.denominator);
  }

  @Override
  public double doubleValue() {
    return FractionMath.doubleValue(this.numerator, this.denominator);
  }

  @Override
  public int hashCode() {
    return FractionMath.hashCode(this.numerator, this.denominator);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof FractionValue)) {
      return false;
    }
    FractionValue other = (FractionValue) obj;
    return (this.numerator == other.numerator)
        && (this.denominator == other.denominator);
  }

  @Override
  public String toString() {
    return FractionMath.toString(this.numerator, this.denominator);
  }

  private Object writeReplace() throws ObjectStreamException {
    return new Ser(this);
  }

}
