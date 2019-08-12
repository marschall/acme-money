package com.github.marschall.acme.money.spis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;

import javax.money.CurrencyUnit;
import javax.money.MonetaryException;
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
  public ExchangeRateProvider getExchangeRateProvider(ConversionQuery conversionQuery) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isExchangeRateProviderAvailable(ConversionQuery conversionQuery) {
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
  public boolean isConversionAvailable(CurrencyUnit termCurrency, String... providers) {
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
  public CurrencyConversion getConversion(CurrencyUnit termCurrency, String... providers) {
    // TODO Auto-generated method stub
    return MonetaryConversionsSingletonSpi.super.getConversion(termCurrency,
            providers);
  }

  /**
   * Implementation for no providers.
   */
  static final class NoProviders implements MonetaryConversionsSingletonSpi {

    @Override
    public Collection<String> getProviderNames() {
      return Collections.emptySet();
    }

    @Override
    public List<String> getDefaultProviderChain() {
      // Javadoc says we should not return empty
      // but the RI DefaultMonetaryConversionsSingletonSpi does it as well
      return Collections.emptyList();
    }

    @Override
    public ExchangeRateProvider getExchangeRateProvider(ConversionQuery conversionQuery) {
      throw new MonetaryException("No such providers: " + conversionQuery);
    }

    @Override
    public boolean isExchangeRateProviderAvailable(ConversionQuery conversionQuery) {
      return false;
    }

    @Override
    public boolean isConversionAvailable(ConversionQuery conversionQuery) {
      return false;
    }

    @Override
    public boolean isConversionAvailable(CurrencyUnit termCurrency, String... providers) {
      return false;
    }

    @Override
    public List<ExchangeRateProvider> getExchangeRateProviders(String... providers) {
      return Collections.emptyList();
    }

    @Override
    public ExchangeRateProvider getExchangeRateProvider(String... providers) {
      throw new MonetaryException("No such providers: " + Arrays.toString(providers));
    }

    @Override
    public CurrencyConversion getConversion(ConversionQuery conversionQuery) {
      throw new MonetaryException("No such conversions: " + conversionQuery);
    }

    @Override
    public CurrencyConversion getConversion(CurrencyUnit termCurrency, String... providers) {
      throw new MonetaryException("No such conversion:" + termCurrency +  " providers: " + Arrays.toString(providers));
    }

  }

  /**
   * Implementation for one provider.
   */
  static final class OneProvider implements MonetaryConversionsSingletonSpi {

    private final ExchangeRateProvider provider;

    private final String providerName;

    OneProvider(ExchangeRateProvider provider) {
      Objects.requireNonNull(provider, "provider");
      this.provider = provider;
      this.providerName = provider.getContext().getProviderName();
    }

    @Override
    public Collection<String> getProviderNames() {
      return Collections.singleton(this.providerName);
    }

    @Override
    public List<String> getDefaultProviderChain() {
      return Collections.singletonList(this.providerName);
    }

    @Override
    public ExchangeRateProvider getExchangeRateProvider(ConversionQuery conversionQuery) {
      throw new MonetaryException("No such providers: " + conversionQuery);
    }

    @Override
    public boolean isExchangeRateProviderAvailable(ConversionQuery conversionQuery) {
      return false;
    }

    @Override
    public boolean isConversionAvailable(ConversionQuery conversionQuery) {
      String wantedProviderName = conversionQuery.getProviderName();
      if (wantedProviderName == null || wantedProviderName.equals(this.providerName)) {
        return this.provider.isAvailable(conversionQuery);
      }
      return false;
    }

    @Override
    public boolean isConversionAvailable(CurrencyUnit termCurrency, String... providers) {
      return false;
    }

    @Override
    public List<ExchangeRateProvider> getExchangeRateProviders(String... providers) {
      return Collections.emptyList();
    }

    @Override
    public ExchangeRateProvider getExchangeRateProvider(String... providers) {
      throw new MonetaryException("No such providers: " + Arrays.toString(providers));
    }

    @Override
    public CurrencyConversion getConversion(ConversionQuery conversionQuery) {
      String wantedProviderName = conversionQuery.getProviderName();
      if (wantedProviderName == null || wantedProviderName.equals(this.providerName)) {
        return this.provider.getCurrencyConversion(conversionQuery);
      }
      return false;
    }

    @Override
    public CurrencyConversion getConversion(CurrencyUnit termCurrency, String... providers) {
      throw new MonetaryException("No such conversion:" + termCurrency +  " providers: " + Arrays.toString(providers));
    }

  }

  // https://cl4es.github.io/2019/02/21/Cljinit-Woes.html
  static <T> List<T> loadServices(Class<T> serviceClass) {
    ServiceLoader<T> serviceLoader = ServiceLoader.load(serviceClass);
    List<T> services = new ArrayList<>();
    Iterator<T> serviceIterator = serviceLoader.iterator();
    while (serviceIterator.hasNext()) {
      T service = serviceIterator.next();
      services.add(service);
    }
    if (services.isEmpty()) {
      return Collections.emptyList();
    }
    if (services.size() == 1) {
      return Collections.singletonList(services.get(0));
    }
    return services;
  }

}
