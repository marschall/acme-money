package com.github.marschall.acme.money;

import static com.github.marschall.acme.money.FractionMath.gcd;
import static java.lang.Math.negateExact;
import static java.lang.Math.addExact;
import static java.lang.Math.multiplyExact;
import static java.lang.Math.subtractExact;

import java.io.Serializable;
import java.util.Objects;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.money.MonetaryAmountFactory;
import javax.money.MonetaryContext;
import javax.money.MonetaryContextBuilder;
import javax.money.NumberValue;

public final class FractionMoney implements MonetaryAmount, Comparable<MonetaryAmount>, Serializable {

  /**
   * the {@link MonetaryContext} used by this instance, e.g. on division.
   */
  static final MonetaryContext MONETARY_CONTEXT =
      MonetaryContextBuilder.of(FractionMoney.class).setFixedScale(true).build();

  private final CurrencyUnit currency;
  
  private final long numerator;
  private final long denominator;
  
  private FractionMoney(long numerator, long denominator, CurrencyUnit currency) {
    Objects.requireNonNull(currency, "currency");
    this.currency = currency;
    this.numerator = numerator;
    this.denominator = denominator;
  }
  
  public static FractionMoney of(long numerator, long denominator, CurrencyUnit currency) {
    long n = numerator;
    long d = denominator;
    if (d < 0) {
      if (d == Long.MIN_VALUE) {
        throw new ArithmeticException("overflow");
      } else {
        d = -d;
        n = -n;
      }
    }
    long gcd = gcd(n, d);
    if (gcd != 1) {
      d = d / gcd;
      n = n / gcd;
    }
    return new FractionMoney(n, d, currency);
  }

  @Override
  public CurrencyUnit getCurrency() {
    return this.currency;
  }

  @Override
  public NumberValue getNumber() {
    return new FractionValue(this.numerator, this.denominator);
  }

  @Override
  public int compareTo(MonetaryAmount o) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public MonetaryContext getMonetaryContext() {
    return MONETARY_CONTEXT;
  }

  @Override
  public MonetaryAmountFactory<? extends MonetaryAmount> getFactory() {
    return new FractionMoneyBuilder().setAmount(this);
  }

  @Override
  public boolean isGreaterThan(MonetaryAmount amount) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isGreaterThanOrEqualTo(MonetaryAmount amount) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isLessThan(MonetaryAmount amount) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isLessThanOrEqualTo(MonetaryAmount amt) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isEqualTo(MonetaryAmount amount) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public int signum() {
    if (this.numerator > 0L) {
      return 1;
    } else if (this.numerator < 0L) {
      return -1;
    } else {
      return 0;
    }
  }
  
  private static Fraction convertTOFraction(MonetaryAmount amount) {
    NumberValue numberValue = amount.getNumber();
    Class<? extends Number> numberClass = numberValue.getNumberType().asSubclass(Number.class);
    return ConvertFraction.of(numberValue.numberValueExact(numberClass));
  }

  @Override
  public MonetaryAmount add(MonetaryAmount amount) {
    long n;
    long d;
    if (amount instanceof FractionMoney) {
      FractionMoney fractionMoney = (FractionMoney) amount;
      n = fractionMoney.numerator;
      d = fractionMoney.denominator;
    } else {
      Fraction fraction = convertTOFraction(amount);
      n = fraction.getNumerator();
      d = fraction.getDenominator();
    }
    return FractionMoney.of(
        addExact(multiplyExact(this.numerator, d), multiplyExact(this.denominator, n)),
        multiplyExact(this.denominator, d), currency);
  }

  @Override
  public MonetaryAmount subtract(MonetaryAmount amount) {
    long n;
    long d;
    if (amount instanceof FractionMoney) {
      FractionMoney fractionMoney = (FractionMoney) amount;
      n = fractionMoney.numerator;
      d = fractionMoney.denominator;
    } else {
      Fraction fraction = convertTOFraction(amount);
      n = fraction.getNumerator();
      d = fraction.getDenominator();
    }
    return FractionMoney.of(
        subtractExact(multiplyExact(this.numerator, d), multiplyExact(this.denominator, n)),
        multiplyExact(this.denominator, d), currency);
  }

  @Override
  public MonetaryAmount multiply(long multiplicand) {
    return FractionMoney.of(multiplyExact(this.numerator, multiplicand), this.denominator, currency);
  }

  @Override
  public MonetaryAmount multiply(double multiplicand) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MonetaryAmount multiply(Number multiplicand) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MonetaryAmount divide(long divisor) {
    return FractionMoney.of(this.numerator, multiplyExact(this.denominator, divisor), currency);
  }

  @Override
  public MonetaryAmount divide(double divisor) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MonetaryAmount divide(Number divisor) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MonetaryAmount remainder(long divisor) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MonetaryAmount remainder(double divisor) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MonetaryAmount remainder(Number divisor) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MonetaryAmount[] divideAndRemainder(long divisor) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MonetaryAmount[] divideAndRemainder(double divisor) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MonetaryAmount[] divideAndRemainder(Number divisor) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MonetaryAmount divideToIntegralValue(long divisor) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MonetaryAmount divideToIntegralValue(double divisor) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MonetaryAmount divideToIntegralValue(Number divisor) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MonetaryAmount scaleByPowerOfTen(int power) {
    // TODO Auto-generated method stub
    // TODO if denominator is power of ten divide
    return null;
  }

  @Override
  public MonetaryAmount abs() {
    if (this.numerator >= 0) {
      return this;
    }
    return new FractionMoney(negateExact(this.numerator), denominator, getCurrency());
  }

  @Override
  public MonetaryAmount negate() {
    return new FractionMoney(negateExact(numerator), denominator, currency);
  }

  @Override
  public MonetaryAmount plus() {
    if (this.numerator >= 0) {
      return this;
    }
    return new FractionMoney(negateExact(this.numerator), denominator, getCurrency());
  }

  @Override
  public MonetaryAmount stripTrailingZeros() {
    return this;
  }
  
  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + this.currency.hashCode();
    result = 31 * result + Long.hashCode(this.numerator);
    result = 31 * result + Long.hashCode(this.denominator);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof FractionMoney)) {
      return false;
    }
    FractionMoney other = (FractionMoney) obj;
    return this.currency.equals(other.currency)
        && this.numerator == other.numerator
        && this.denominator == other.denominator;
  }

}
