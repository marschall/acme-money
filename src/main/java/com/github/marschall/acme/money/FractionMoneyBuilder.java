package com.github.marschall.acme.money;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.money.MonetaryContext;
import javax.money.MonetaryContextBuilder;
import javax.money.NumberValue;

import org.javamoney.moneta.spi.AbstractAmountBuilder;

final class FractionMoneyBuilder extends AbstractAmountBuilder<FractionMoney> {

  static final MonetaryContext DEFAULT_CONTEXT =
      MonetaryContextBuilder.of(FractionMoney.class)
      .setFixedScale(false)
      .build();

  static final MonetaryContext MAX_CONTEXT = DEFAULT_CONTEXT;

  @Override
  public Class<? extends MonetaryAmount> getAmountType() {
    return FractionMoney.class;
  }

  @Override
  public NumberValue getMaxNumber() {
    return new FractionValue(Long.MAX_VALUE, 1L);
  }

  @Override
  public NumberValue getMinNumber() {
    return new FractionValue(Long.MIN_VALUE, 1L);
  }

  @Override
  protected FractionMoney create(Number number, CurrencyUnit currency, MonetaryContext monetaryContext) {
    Fraction fraction = ConvertToFraction.of(number);
    // TODO optimize gcd away
    return FractionMoney.of(fraction.getNumerator(), fraction.getDenominator(), currency);
  }

  @Override
  protected MonetaryContext loadDefaultMonetaryContext() {
    return DEFAULT_CONTEXT;
  }

  @Override
  protected MonetaryContext loadMaxMonetaryContext() {
    return MAX_CONTEXT;
  }

}
