package com.github.marschall.acme.money;

import javax.money.MonetaryAmountFactory;
import javax.money.MonetaryContext;
import javax.money.spi.MonetaryAmountFactoryProviderSpi;

/**
 * An Implementation of {@link MonetaryAmountFactoryProviderSpi} that creates instances of
 * {@link FastMoney6}.
 */
public final class FastMoney6AmountFactoryProvider implements MonetaryAmountFactoryProviderSpi<FastMoney6> {

  @Override
  public Class<FastMoney6> getAmountType() {
    return FastMoney6.class;
  }

  @Override
  public MonetaryAmountFactory<FastMoney6> createMonetaryAmountFactory() {
    return new FastMoney6AmountFactory();
  }

  @Override
  public MonetaryContext getDefaultMonetaryContext() {
    return FastMoney6.MONETARY_CONTEXT;
  }

}
