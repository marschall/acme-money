package com.github.marschall.acme.money;

import javax.money.format.AmountFormatQuery;
import javax.money.format.AmountFormatQueryBuilder;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;

import org.junit.jupiter.api.Test;

class ToStringMonetaryAmountFormatTest {

  @Test
  void getAmountFormat() {
    AmountFormatQuery query = AmountFormatQueryBuilder
      .of("test")
      .setMonetaryAmountFactory(new FastMoney6AmountFactory())
//      .setMonetaryQuery(null)
      .setProviderName(AcmeMoneyConstants.PROVIDER_NAME)
      .build();
    MonetaryAmountFormat format = MonetaryFormats.getAmountFormat(query);
  }

}
