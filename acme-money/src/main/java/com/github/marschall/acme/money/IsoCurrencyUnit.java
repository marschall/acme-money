package com.github.marschall.acme.money;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Objects;

import javax.money.CurrencyContext;
import javax.money.CurrencyContextBuilder;
import javax.money.CurrencyUnit;

/**
 * Ah simple ISO currency unit.
 */
public final class IsoCurrencyUnit implements CurrencyUnit, Serializable {

  private static final CurrencyContext CONTEXT = CurrencyContextBuilder.of(AcmeMoneyConstants.PROVIDER_NAME).build();

  private final String currencyCode;

  final short compressedCurrencyCode;

  private final short numericCode;

  private final byte defaultFractionDigits;

  IsoCurrencyUnit(String currencyCode, short compressedCurrencyCode, int numericCode, int defaultFractionDigits) {
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
    if (o instanceof IsoCurrencyUnit) {
      return Integer.compare(this.compressedCurrencyCode, ((IsoCurrencyUnit) o).compressedCurrencyCode);
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
  public String toString() {
    return getCurrencyCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof IsoCurrencyUnit) {
      return obj == this;
    }
    if (!(obj instanceof CurrencyUnit)) {
      return false;
    }
    CurrencyUnit other = (CurrencyUnit) obj;
    return this.currencyCode.equals(other.getCurrencyCode());
  }

  @Override
  public int hashCode() {
    // TODO
    return this.currencyCode.hashCode();
  }

  @Override
  public CurrencyContext getContext() {
    return CONTEXT;
  }

  private Object writeReplace() throws ObjectStreamException {
    return new Ser(this);
  }

}
