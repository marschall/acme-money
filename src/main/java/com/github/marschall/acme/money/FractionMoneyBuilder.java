package com.github.marschall.acme.money;

import javax.money.MonetaryAmount;
import javax.money.MonetaryContext;
import javax.money.MonetaryContextBuilder;
import javax.money.NumberValue;

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
  public FractionMoney create() {
    Fraction fraction = ConvertToFraction.convert(this.number);
    // TODO optimize gcd away
    return FractionMoney.of(fraction.getNumerator(), fraction.getDenominator(), this.currency);
  }

  @Override
  public MonetaryContext getDefaultMonetaryContext() {
    return DEFAULT_CONTEXT;
  }

}
