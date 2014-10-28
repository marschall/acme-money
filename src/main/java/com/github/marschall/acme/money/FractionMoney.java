package com.github.marschall.acme.money;

import java.io.Serializable;
import java.util.Objects;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.money.MonetaryAmountFactory;
import javax.money.MonetaryContext;
import javax.money.NumberValue;

public final class FractionMoney implements MonetaryAmount, Comparable<MonetaryAmount>, Serializable {

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
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int compareTo(MonetaryAmount o) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public MonetaryContext getMonetaryContext() {
    // TODO Auto-generated method stub
    return null;
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
