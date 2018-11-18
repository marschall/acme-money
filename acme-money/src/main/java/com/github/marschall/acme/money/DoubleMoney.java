package com.github.marschall.acme.money;

import java.io.Serializable;
import java.util.Objects;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.money.MonetaryAmountFactory;
import javax.money.MonetaryContext;
import javax.money.MonetaryException;
import javax.money.MonetaryOperator;
import javax.money.MonetaryQuery;
import javax.money.NumberValue;

final class DoubleMoney implements MonetaryAmount, Comparable<MonetaryAmount>, Serializable {

  private static final long serialVersionUID = 2L;

  /**
   * The currency of this amount.
   */
  final CurrencyUnit currency;

  /**
   * The numeric part of this amount.
   */
  final double value;

  private DoubleMoney(CurrencyUnit currency, double value) {
    Objects.requireNonNull(currency, "currency");
    this.currency = currency;
    this.value = value;
  }

  private void requireSameCurrency(MonetaryAmount amount) {
    Objects.requireNonNull(amount, "amount");
    CurrencyUnit amountCurrency = amount.getCurrency();
    if (!this.currency.equals(amountCurrency)) {
        throw new MonetaryException("Currency mismatch: " + this.currency + '/' + amountCurrency);
    }
  }

  @Override
  public CurrencyUnit getCurrency() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public NumberValue getNumber() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int compareTo(MonetaryAmount o) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public MonetaryContext getContext() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <R> R query(MonetaryQuery<R> query) {
    Objects.requireNonNull(query);
    return query.queryFrom(this);
  }

  @Override
  public MonetaryAmount with(MonetaryOperator operator) {
    // TODO Auto-generated method stub
    return MonetaryAmount.super.with(operator);
  }

  @Override
  public MonetaryAmountFactory<? extends MonetaryAmount> getFactory() {
    // TODO Auto-generated method stub
    return null;
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
  public boolean isNegative() {
    // TODO Auto-generated method stub
    return MonetaryAmount.super.isNegative();
  }

  @Override
  public boolean isNegativeOrZero() {
    // TODO Auto-generated method stub
    return MonetaryAmount.super.isNegativeOrZero();
  }

  @Override
  public boolean isPositive() {
    // TODO Auto-generated method stub
    return MonetaryAmount.super.isPositive();
  }

  @Override
  public boolean isPositiveOrZero() {
    // TODO Auto-generated method stub
    return MonetaryAmount.super.isPositiveOrZero();
  }

  @Override
  public boolean isZero() {
    // TODO Auto-generated method stub
    return MonetaryAmount.super.isZero();
  }

  @Override
  public int signum() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public MonetaryAmount add(MonetaryAmount amount) {
    this.requireSameCurrency(amount);
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MonetaryAmount subtract(MonetaryAmount amount) {
    this.requireSameCurrency(amount);
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
    // TODO infinite nan
    double product = this.value * multiplicand;
    if (product == this.value) {
      return this;
    }
    return new DoubleMoney(this.currency, product);
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
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MonetaryAmount plus() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MonetaryAmount stripTrailingZeros() {
    // TODO Auto-generated method stub
    return null;
  }



}
