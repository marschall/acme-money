package com.github.marschall.acme.money;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.money.CurrencyQuery;
import javax.money.CurrencyQueryBuilder;
import javax.money.CurrencyUnit;
import javax.money.Monetary;

import org.junit.jupiter.api.Test;

class IsoCurrencyProviderTest {

  @Test
  void test() {
    CurrencyQuery acmeQuery = CurrencyQueryBuilder.of()
        .setProviderName(AcmeMoneyConstants.PROVIDER_NAME)
        .build();
    Collection<CurrencyUnit> acmeCurrencies = Monetary.getCurrencies(acmeQuery);


    CurrencyQuery jdkCurrencyQuery = CurrencyQueryBuilder.of()
        .setProviderName("default")
        .build();

    Collection<CurrencyUnit> jdkCurrencies = Monetary.getCurrencies(jdkCurrencyQuery);


    Set<String> acmeCurrencyCodes = acmeCurrencies.stream()
        .map(CurrencyUnit::getCurrencyCode)
        .collect(Collectors.toSet());

    Set<String> jdkCurrencyCodes = jdkCurrencies.stream()
        .map(CurrencyUnit::getCurrencyCode)
        .collect(Collectors.toSet());

    Set<String> acmeMissing = new HashSet<>(jdkCurrencyCodes);
    acmeMissing.removeAll(acmeCurrencyCodes);

    Set<String> jdkMissing = new HashSet<>(acmeCurrencyCodes);
    jdkMissing.removeAll(jdkCurrencyCodes);

    System.out.println("acme size: " + acmeCurrencies.size());
    System.out.println("JDK size: " + jdkCurrencyCodes.size());

    System.out.println("acmeMissing: " + acmeMissing);
    System.out.println("jdkMissing: " + jdkMissing);
  }

  @Test
  void interoparabilityEqualsHashCode() {
    CurrencyUnit acmeEur = getAcmeCurrency("EUR");
    CurrencyUnit jdkEur = getJdkCurrency("EUR");

    assertNotSame(jdkEur, acmeEur);
    assertTrue(acmeEur.equals(jdkEur));
    assertTrue(jdkEur.equals(acmeEur));
    assertEquals(jdkEur.hashCode(), acmeEur.hashCode());
  }

  @Test
  void interoparabilityCompareTo() {
    CurrencyUnit acmeEur = getAcmeCurrency("EUR");
    CurrencyUnit jdkEur = getJdkCurrency("EUR");
    CurrencyUnit acmeUsd = getAcmeCurrency("USD");
    CurrencyUnit jdkUsd = getJdkCurrency("USD");
    
    assertTrue(0 < jdkUsd.compareTo(jdkEur));
    assertTrue(0 > jdkEur.compareTo(jdkUsd));
    
    assertTrue(0 < jdkUsd.compareTo(acmeEur));
    assertTrue(0 > acmeEur.compareTo(jdkUsd));
    
    assertTrue(0 < acmeUsd.compareTo(jdkEur));
    assertTrue(0 > jdkEur.compareTo(acmeUsd));
    
    assertTrue(0 < acmeUsd.compareTo(acmeEur));
    assertTrue(0 > acmeEur.compareTo(acmeUsd));
  }

  private static CurrencyUnit getAcmeCurrency(String currencyCode) {
    return getCurrency(currencyCode, AcmeMoneyConstants.PROVIDER_NAME);
  }

  private static CurrencyUnit getJdkCurrency(String currencyCode) {
    return getCurrency(currencyCode, "default");
  }

  private static CurrencyUnit getCurrency(String currencyCode, String providerName) {
    CurrencyQuery query = CurrencyQueryBuilder.of()
        .setProviderName(providerName)
        .setCurrencyCodes(currencyCode)
        .build();

    return Monetary.getCurrency(query);
  }

}
