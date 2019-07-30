package com.github.marschall.acme.money.spis;

import java.util.Collection;
import java.util.List;

import javax.money.CurrencyUnit;
import javax.money.convert.ConversionQuery;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.ExchangeRateProvider;
import javax.money.spi.MonetaryConversionsSingletonSpi;

public final class AcmeMonetaryConversionsSingletonSpi implements MonetaryConversionsSingletonSpi {
  
  /**
   * Not supposed to be called by user code, supposed to be called by javamoney-api.
   */
  public AcmeMonetaryConversionsSingletonSpi() {
    super();
  }

  @Override
  public Collection<String> getProviderNames() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<String> getDefaultProviderChain() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ExchangeRateProvider getExchangeRateProvider(
          ConversionQuery conversionQuery) {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public boolean isExchangeRateProviderAvailable(
          ConversionQuery conversionQuery) {
    // TODO Auto-generated method stub
    return MonetaryConversionsSingletonSpi.super.isExchangeRateProviderAvailable(
            conversionQuery);
  }
  
  @Override
  public boolean isConversionAvailable(ConversionQuery conversionQuery) {
    // TODO Auto-generated method stub
    return MonetaryConversionsSingletonSpi.super.isConversionAvailable(
            conversionQuery);
  }
  
  @Override
  public boolean isConversionAvailable(CurrencyUnit termCurrency,
          String... providers) {
    // TODO Auto-generated method stub
    return MonetaryConversionsSingletonSpi.super.isConversionAvailable(termCurrency,
            providers);
  }
  
  @Override
  public List<ExchangeRateProvider> getExchangeRateProviders(
          String... providers) {
    // TODO Auto-generated method stub
    return MonetaryConversionsSingletonSpi.super.getExchangeRateProviders(
            providers);
  }
  
  @Override
  public ExchangeRateProvider getExchangeRateProvider(String... providers) {
    // TODO Auto-generated method stub
    return MonetaryConversionsSingletonSpi.super.getExchangeRateProvider(providers);
  }
  
  @Override
  public CurrencyConversion getConversion(ConversionQuery conversionQuery) {
    // TODO Auto-generated method stub
    return MonetaryConversionsSingletonSpi.super.getConversion(conversionQuery);
  }
  
  @Override
  public CurrencyConversion getConversion(CurrencyUnit termCurrency,
          String... providers) {
    // TODO Auto-generated method stub
    return MonetaryConversionsSingletonSpi.super.getConversion(termCurrency,
            providers);
  }

}
