package com.github.marschall.acme.money;

import java.io.Serializable;
import java.util.Objects;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.money.MonetaryAmountFactory;
import javax.money.MonetaryContext;
import javax.money.MonetaryContextBuilder;
import javax.money.NumberValue;

import static java.lang.Math.negateExact;

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
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public MonetaryAmount add(MonetaryAmount amount) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MonetaryAmount subtract(MonetaryAmount amount) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MonetaryAmount multiply(long multiplicand) {
    // TODO Auto-generated method stub
    return null;
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
    // TODO Auto-generated method stub
    return null;
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
    return null;
  }

  @Override
  public MonetaryAmount abs() {
    // TODO Auto-generated method stub
    return null;
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

}
