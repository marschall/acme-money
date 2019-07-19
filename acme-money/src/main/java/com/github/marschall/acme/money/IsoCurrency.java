package com.github.marschall.acme.money;

import java.io.Serializable;

import javax.money.CurrencyContext;
import javax.money.CurrencyUnit;

final class IsoCurrency implements CurrencyUnit, Serializable {
  
  private final short compressedCurrencyCode;
  
  private IsoCurrency(short compressedCurrencyCode) {
    this.compressedCurrencyCode = compressedCurrencyCode;
  }

  @Override
  public int compareTo(CurrencyUnit o) {
    if (o instanceof IsoCurrency) {
      
    }
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public String getCurrencyCode() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getNumericCode() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getDefaultFractionDigits() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public CurrencyContext getContext() {
    // TODO Auto-generated method stub
    return null;
  }

}
