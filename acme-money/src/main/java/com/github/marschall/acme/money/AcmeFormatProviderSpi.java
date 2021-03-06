package com.github.marschall.acme.money;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;

import javax.money.format.AmountFormatQuery;
import javax.money.format.MonetaryAmountFormat;
import javax.money.spi.MonetaryAmountFormatProviderSpi;

/**
 * Registers all our {@link MonetaryAmountFormat} implementations.
 */
public final class AcmeFormatProviderSpi implements MonetaryAmountFormatProviderSpi {
  
  /**
   * Not supposed to be called by user code.
   */
  public AcmeFormatProviderSpi() {
    super();
  }
  
  @Override
  public String getProviderName() {
    return AcmeMoneyConstants.PROVIDER_NAME;
  }

  @Override
  public Collection<MonetaryAmountFormat> getAmountFormats(AmountFormatQuery formatQuery) {
    return Collections.singleton(FastMoney6AmountFormat.INSTANCE);
  }

  @Override
  public Set<Locale> getAvailableLocales() {
    return Collections.emptySet();
  }

  @Override
  public Set<String> getAvailableFormatNames() {
    return Collections.singleton(FastMoney6AmountFormat.NAME);
  }

}
