package com.github.marschall.acme.money;

import java.util.Objects;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.money.MonetaryAmountFactory;
import javax.money.MonetaryContext;

abstract class AbstractAmountFactory<T extends MonetaryAmount> implements MonetaryAmountFactory<T> {

  CurrencyUnit currency;
  Number number;

  @Override
  public MonetaryAmountFactory<T> setCurrency(CurrencyUnit currency) {
    Objects.requireNonNull(currency, "currency");
    this.currency = currency;
    return this;
  }

  @Override
  public MonetaryAmountFactory<T> setNumber(double number) {
    this.number = number;
    return this;
  }

  @Override
  public MonetaryAmountFactory<T> setNumber(long number) {
    this.number = number;
    return this;
  }

  @Override
  public MonetaryAmountFactory<T> setNumber(Number number) {
    Objects.requireNonNull(number, "number");
    this.number = number;
    return this;
  }

  @Override
  public MonetaryAmountFactory<T> setContext(MonetaryContext monetaryContext) {
    // TODO
    return this;
  }

  @Override
  public T create() {
    // TODO Auto-generated method stub
    return null;
  }


}
