package com.github.marschall.acme.money;

import javax.money.CurrencyUnit;
import javax.money.MonetaryContext;
import javax.money.NumberValue;

import org.javamoney.moneta.spi.AbstractAmountBuilder;

class FastMoney6AmountBuilder extends AbstractAmountBuilder<FastMoney6>{

  @Override
  protected FastMoney6 create(Number number, CurrencyUnit currency, MonetaryContext monetaryContext){
    return FastMoney6.of(number, currency);
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
  protected MonetaryContext loadDefaultMonetaryContext(){
    return FastMoney6.MONETARY_CONTEXT;
  }

  @Override
  protected MonetaryContext loadMaxMonetaryContext(){
    return FastMoney6.MONETARY_CONTEXT;
  }

}
