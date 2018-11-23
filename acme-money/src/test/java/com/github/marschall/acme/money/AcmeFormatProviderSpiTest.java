package com.github.marschall.acme.money;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.money.format.AmountFormatQuery;
import javax.money.format.AmountFormatQueryBuilder;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;

import org.junit.jupiter.api.Test;

class AcmeFormatProviderSpiTest {

  @Test
  void fast6Format() {
    AmountFormatQuery query = AmountFormatQueryBuilder
      .of(FastMoney6AmountFormat.NAME)
      .setMonetaryAmountFactory(new FastMoney6AmountFactory())
      .setProviderName(AcmeMoneyConstants.PROVIDER_NAME)
      .build();
    MonetaryAmountFormat format = MonetaryFormats.getAmountFormat(query);
    assertNotNull(format);
  }

}
