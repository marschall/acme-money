package com.github.marschall.acme.money.tck;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import javax.money.MonetaryOperator;

import org.javamoney.tck.JSR354TestConfiguration;

import com.github.marschall.acme.money.FastMoney6;
import com.github.marschall.acme.money.FractionMoney;
import com.github.marschall.acme.money.IsoCurrencyUnit;

public class AcmeMoneyTestConfiguration implements JSR354TestConfiguration {

  @Override
  public Collection<Class> getAmountClasses() {
    return Arrays.asList(FastMoney6.class, FractionMoney.class);
  }

  @Override
  public Collection<Class> getCurrencyClasses() {
    return Collections.singletonList(IsoCurrencyUnit.class);
  }

  @Override
  public Collection<MonetaryOperator> getMonetaryOperators4Test() {
    return Collections.emptyList();
  }

}
