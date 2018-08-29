package com.github.marschall.acme.money;

import javax.money.MonetaryContext;
import javax.money.NumberValue;

final class FastMoney6AmountFactory extends AbstractAmountFactory<FastMoney6>{

  @Override
  public FastMoney6 create(){
    return FastMoney6.of(this.number, this.currency);
  }

  @Override
  public Class<FastMoney6> getAmountType(){
    return FastMoney6.class;
  }

  @Override
  public NumberValue getMaxNumber(){
    return FastMoney6.MAX_VALUE.getNumber();
  }

  @Override
  public NumberValue getMinNumber(){
    return FastMoney6.MIN_VALUE.getNumber();
  }

  @Override
  public MonetaryContext getDefaultMonetaryContext() {
    return FastMoney6.MONETARY_CONTEXT;
  }

}
