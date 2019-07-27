package com.github.marschall.acme.money;

import javax.money.MonetaryAmountFactory;
import javax.money.MonetaryContext;
import javax.money.spi.MonetaryAmountFactoryProviderSpi;

/**
 * An Implementation of {@link MonetaryAmountFactoryProviderSpi} that creates instances of
 * {@link FractionMoney}.
 */
public final class FractionAmountFactoryProvider implements MonetaryAmountFactoryProviderSpi<FractionMoney> {
  
  /**
   * Not supposed to be called by user code.
   */
  public FractionAmountFactoryProvider() {
    super();
  }

  @Override
  public Class<FractionMoney> getAmountType() {
    return FractionMoney.class;
  }

  @Override
  public MonetaryAmountFactory<FractionMoney> createMonetaryAmountFactory() {
    return new FractionMoneyFactory();
  }

  @Override
  public MonetaryContext getMaximalMonetaryContext() {
    return FractionMoneyFactory.MAX_CONTEXT;
  }

  @Override
  public MonetaryContext getDefaultMonetaryContext() {
    return FractionMoneyFactory.DEFAULT_CONTEXT;
  }

}
