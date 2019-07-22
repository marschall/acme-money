package com.github.marschall.acme.money;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.money.CurrencyQuery;
import javax.money.CurrencyUnit;
import javax.money.spi.CurrencyProviderSpi;

final class IsoCurrencyProvider implements CurrencyProviderSpi {

  private static final int LETTERS_IN_ALPHABET = 26;

  static final int ARRAY_SIZE = LETTERS_IN_ALPHABET * LETTERS_IN_ALPHABET * LETTERS_IN_ALPHABET;

  private static final Map<Short, CurrencyUnit> CURRENCIES = IsoCurrencyCompressor.parse();

  @Override
  public String getProviderName() {
    return AcmeMoneyConstants.PROVIDER_NAME;
  }

  @Override
  public Set<CurrencyUnit> getCurrencies(CurrencyQuery query) {
    Collection<String> currencyCodes = query.getCurrencyCodes();
    if (!currencyCodes.isEmpty()) {
      return searchByCurrencyCode(currencyCodes);
    }
    if (!query.getCountries().isEmpty()) {
      // countries is treated like all currencies
      return allCurrencies();
    }
    Collection<Integer> numericCodes = query.getNumericCodes();
    if (!numericCodes.isEmpty()) {
      return searchByNumericCode(numericCodes);
    }
    // No known constraints defined, return all.
    return allCurrencies();
  }

  private Set<CurrencyUnit> allCurrencies() {
    // TODO optimize
    return new HashSet<>(CURRENCIES.values());
  }

  private Set<CurrencyUnit> searchByCurrencyCode(Collection<String> currencyCodes) {
    // TODO optimize size 1
    Set<CurrencyUnit> result = new HashSet<>();
    for (String currencCode : currencyCodes) {
      Short compressed = IsoCurrencyCompressor.compressCurrencyCode(currencCode);
      CurrencyUnit currency = CURRENCIES.get(compressed);
      if (currency != null) {
        result.add(currency);
      }
    }
    return result;
  }
  
  private Set<CurrencyUnit> searchByNumericCode(Collection<Integer> numericCodes) {
    // TODO optimize size 1
    Set<CurrencyUnit> result = new HashSet<>();
    for (Integer numericCode : numericCodes) {
      if (numericCode.intValue() == -1) {
        continue;
      }
      for (CurrencyUnit currency : CURRENCIES.values()) {
        if (currency.getNumericCode() == numericCode) {
          result.add(currency);
        }
      }
    }
    return result;
  }

  @Override
  public boolean isCurrencyAvailable(CurrencyQuery query) {
    // TODO Auto-generated method stub
    return CurrencyProviderSpi.super.isCurrencyAvailable(query);
  }

  static CurrencyUnit getCurrency(short compressed) {
    CurrencyUnit currency = CURRENCIES.get(compressed);
    if (currency == null) {
      throw currencyNotFound(compressed);
    }
    return currency;
  }

  private static IllegalArgumentException currencyNotFound(short compressed) {
    return new IllegalArgumentException("currency " + IsoCurrencyCompressor.decompressCurrencyCode(compressed) + " not found");
  }

}
