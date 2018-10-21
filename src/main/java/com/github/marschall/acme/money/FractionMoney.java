package com.github.marschall.acme.money;

import static com.github.marschall.acme.money.FractionMath.gcd;
import static com.github.marschall.acme.money.FractionMath.toLongExact;
import static java.lang.Math.addExact;
import static java.lang.Math.multiplyExact;
import static java.lang.Math.negateExact;
import static java.lang.Math.subtractExact;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Objects;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.money.MonetaryAmountFactory;
import javax.money.MonetaryContext;
import javax.money.MonetaryContextBuilder;
import javax.money.MonetaryException;
import javax.money.NumberValue;

public final class FractionMoney implements MonetaryAmount, Comparable<MonetaryAmount>, Serializable {

  private static final long serialVersionUID = 2L;

  /**
   * the {@link MonetaryContext} used by this instance, e.g. on division.
   */
  static final MonetaryContext MONETARY_CONTEXT =
      MonetaryContextBuilder.of(FractionMoney.class)
      .setFixedScale(false)
      .build();

  final CurrencyUnit currency;

  final long numerator;
  final long denominator;

  FractionMoney(long numerator, long denominator, CurrencyUnit currency) {
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
    Objects.requireNonNull(o);
    int compare = this.getCurrency().compareTo(o.getCurrency());
    if (compare != 0) {
      return compare;
    }
    return this.compareValue(o);
  }

  private int compareValue(MonetaryAmount o) {
    CurrencyUnit amountCurrency = o.getCurrency();
    if (!this.currency.equals(amountCurrency)) {
        throw new MonetaryException("Currency mismatch: " + this.currency + '/' + amountCurrency);
    }
    if (o instanceof FractionMoney) {
      FractionMoney other = (FractionMoney) o;
      long ad = multiplyExact(this.numerator, other.denominator);
      long bc = multiplyExact(this.denominator, other.numerator);
      return Long.signum(subtractExact(ad, bc));
    }
    Fraction fraction = getFraction(o);
    long ad = multiplyExact(this.numerator, fraction.getDenominator());
    long bc = multiplyExact(this.denominator, fraction.getNumerator());
    return Long.signum(subtractExact(ad, bc));
  }

  private static Fraction getFraction(MonetaryAmount o) {
    NumberValue numberValue = o.getNumber();
    Class<? extends Number> numberType = numberValue.getNumberType().asSubclass(Number.class);
    return ConvertToFraction.convert(numberValue.numberValueExact(numberType));
  }

  @Override
  public MonetaryContext getContext() {
    return MONETARY_CONTEXT;
  }

  @Override
  public MonetaryAmountFactory<? extends MonetaryAmount> getFactory() {
    return new FractionMoneyFactory().setAmount(this);
  }

  @Override
  public boolean isGreaterThan(MonetaryAmount amount) {
    return this.compareValue(amount) > 0;
  }

  @Override
  public boolean isGreaterThanOrEqualTo(MonetaryAmount amount) {
    return this.compareValue(amount) >= 0;
  }

  @Override
  public boolean isLessThan(MonetaryAmount amount) {
    return this.compareValue(amount) < 0;
  }

  @Override
  public boolean isLessThanOrEqualTo(MonetaryAmount amount) {
    return this.compareValue(amount) <= 0;
  }

  @Override
  public boolean isEqualTo(MonetaryAmount amount) {
    return this.compareValue(amount) == 0;
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
    return ConvertToFraction.convert(numberValue.numberValueExact(numberClass));
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
        multiplyExact(this.denominator, d), this.currency);
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
        multiplyExact(this.denominator, d), this.currency);
  }

  @Override
  public MonetaryAmount multiply(long multiplicand) {
    return FractionMoney.of(multiplyExact(this.numerator, multiplicand), this.denominator, this.currency);
  }

  @Override
  public MonetaryAmount multiply(double multiplicand) {
    double n = this.numerator * multiplicand;
    return FractionMoney.of(toLongExact(n), this.denominator, this.currency);
  }

  @Override
  public MonetaryAmount multiply(Number multiplicand) {
    Fraction fraction = ConvertToFraction.convert(multiplicand);
    long n = multiplyExact(this.numerator, fraction.getNumerator());
    long d = multiplyExact(this.denominator, fraction.getDenominator());
    return FractionMoney.of(n, d, this.currency);
  }

  @Override
  public MonetaryAmount divide(long divisor) {
    return FractionMoney.of(this.numerator, multiplyExact(this.denominator, divisor), this.currency);
  }

  @Override
  public MonetaryAmount divide(double divisor) {
    double d = this.denominator * divisor;
    return FractionMoney.of(this.numerator, toLongExact(d), this.currency);
  }

  @Override
  public MonetaryAmount divide(Number divisor) {
    Fraction fraction = ConvertToFraction.convert(divisor);
    long n = multiplyExact(this.numerator, fraction.getDenominator());
    long d = multiplyExact(this.denominator, fraction.getNumerator());
    return FractionMoney.of(n, d, this.currency);
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
    if (power == 0) {
      return this;
    } else if (power > 0) {
      long n = this.numerator;
      for (int i = 0; i < power; ++i) {
        n = multiplyExact(n, 10L);
      }
      return FractionMoney.of(n, this.denominator, this.currency);
    } else {
      long d = this.denominator;
      for (int i = power; i < 0; ++i) {
        d = multiplyExact(d, 10L);
      }
      return FractionMoney.of(this.numerator, d, this.currency);
    }
  }

  @Override
  public MonetaryAmount abs() {
    if (this.numerator >= 0) {
      return this;
    }
    return new FractionMoney(negateExact(this.numerator), this.denominator, this.getCurrency());
  }

  @Override
  public MonetaryAmount negate() {
    return new FractionMoney(negateExact(this.numerator), this.denominator, this.currency);
  }

  @Override
  public MonetaryAmount plus() {
    return this;
  }

  @Override
  public MonetaryAmount stripTrailingZeros() {
    return this;
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = (31 * result) + this.currency.hashCode();
    result = (31 * result) + Long.hashCode(this.numerator);
    result = (31 * result) + Long.hashCode(this.denominator);
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
        && (this.numerator == other.numerator)
        && (this.denominator == other.denominator);
  }

  @Override
  public String toString() {
    return this.currency.toString() + ' ' + this.numerator + '/' + this.denominator;
  }

  private Object writeReplace() throws ObjectStreamException {
    return new Ser(this);
  }

}
