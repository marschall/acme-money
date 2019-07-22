package com.github.marschall.acme.money;

import java.io.Serializable;
import java.util.Objects;

import javax.money.CurrencyContext;
import javax.money.CurrencyContextBuilder;
import javax.money.CurrencyUnit;

final class IsoCurrency implements CurrencyUnit, Serializable {

  private static final CurrencyContext CONTEXT = CurrencyContextBuilder.of(AcmeMoneyConstants.PROVIDER_NAME).build();
  
  private final String currencyCode;
  
  private final short compressedCurrencyCode;
  
  private final short numericCode;
  
  private final byte defaultFractionDigits;

  IsoCurrency(String currencyCode, short compressedCurrencyCode, int numericCode, int defaultFractionDigits) {
    Objects.requireNonNull(currencyCode, "currencyCode");
    if (numericCode  > 999 || numericCode < -1) {
      throw new IllegalArgumentException("invalid numeric code");
    }
    if (defaultFractionDigits  > Byte.MAX_VALUE || defaultFractionDigits < -1) {
      throw new IllegalArgumentException("invalid numeric code");
    }
    this.currencyCode = currencyCode;
    this.compressedCurrencyCode = compressedCurrencyCode;
    this.numericCode = (short) numericCode;
    this.defaultFractionDigits = (byte) defaultFractionDigits;
  }

  @Override
  public int compareTo(CurrencyUnit o) {
    Objects.requireNonNull(o);
    if (o instanceof IsoCurrency) {
      return Integer.compare(this.compressedCurrencyCode, ((IsoCurrency) o).compressedCurrencyCode);
    }
    return getCurrencyCode().compareTo(o.getCurrencyCode());
  }

  @Override
  public String getCurrencyCode() {
    return this.currencyCode;
  }

  @Override
  public int getNumericCode() {
    return this.numericCode;
  }

  @Override
  public int getDefaultFractionDigits() {
    return this.defaultFractionDigits;
  }
  
  @Override
  public boolean equals(Object obj) {
    return obj == this;
  }
  
  @Override
  public int hashCode() {
    return this.compressedCurrencyCode;
  }

  @Override
  public CurrencyContext getContext() {
    return CONTEXT;
  }

}
