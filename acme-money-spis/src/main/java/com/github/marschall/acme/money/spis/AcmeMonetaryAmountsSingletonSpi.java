package com.github.marschall.acme.money.spis;

import java.util.Collection;

import javax.money.MonetaryAmount;
import javax.money.MonetaryAmountFactory;
import javax.money.spi.MonetaryAmountsSingletonSpi;

public final class AcmeMonetaryAmountsSingletonSpi implements MonetaryAmountsSingletonSpi {
  
  /**
   * Not supposed to be called by user code, supposed to be called by javamoney-api.
   */
  public AcmeMonetaryAmountsSingletonSpi() {
    super();
  }

  @Override
  public <T extends MonetaryAmount> MonetaryAmountFactory<T> getAmountFactory(
          Class<T> amountType) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Class<? extends MonetaryAmount> getDefaultAmountType() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<Class<? extends MonetaryAmount>> getAmountTypes() {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public MonetaryAmountFactory<?> getDefaultAmountFactory() {
    // TODO Auto-generated method stub
    return MonetaryAmountsSingletonSpi.super.getDefaultAmountFactory();
  }
  
  @Override
  public Collection<MonetaryAmountFactory<?>> getAmountFactories() {
    // TODO Auto-generated method stub
    return MonetaryAmountsSingletonSpi.super.getAmountFactories();
  }

}
