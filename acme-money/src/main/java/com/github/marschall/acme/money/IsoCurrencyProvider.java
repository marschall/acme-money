package com.github.marschall.acme.money;

import java.util.Set;

import javax.money.CurrencyQuery;
import javax.money.CurrencyUnit;
import javax.money.spi.CurrencyProviderSpi;

final class IsoCurrencyProvider implements CurrencyProviderSpi {
  
  @Override
  public String getProviderName() {
    // TODO Auto-generated method stub
    return CurrencyProviderSpi.super.getProviderName();
  }

  // https://www.currency-iso.org/dam/downloads/lists/list_one.xml

  @Override
  public Set<CurrencyUnit> getCurrencies(CurrencyQuery query) {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public boolean isCurrencyAvailable(CurrencyQuery query) {
    // TODO Auto-generated method stub
    return CurrencyProviderSpi.super.isCurrencyAvailable(query);
  }

}
