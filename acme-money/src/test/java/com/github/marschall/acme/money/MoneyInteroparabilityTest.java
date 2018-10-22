package com.github.marschall.acme.money;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;

import java.math.BigDecimal;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

import org.javamoney.moneta.FastMoney;
import org.junit.jupiter.api.Test;

class MoneyInteroparabilityTest {

  protected static final CurrencyUnit CHF = Monetary.getCurrency("CHF");

  @Test
  void addFast6() {
    MonetaryAmount acmeMoney = FastMoney6.of(new BigDecimal("3.3"), CHF);
    FastMoney monetaMoney = FastMoney.of(new BigDecimal("5.5"), CHF);

    MonetaryAmount sum = acmeMoney.add(monetaMoney);
    assertThat(sum.getNumber().numberValueExact(BigDecimal.class), comparesEqualTo(new BigDecimal("8.8")));

    sum = monetaMoney.add(acmeMoney);
    assertThat(sum.getNumber().numberValueExact(BigDecimal.class), comparesEqualTo(new BigDecimal("8.8")));
  }

  @Test
  void subtractFast6() {
    MonetaryAmount acmeMoney = FastMoney6.of(new BigDecimal("3.3"), CHF);
    FastMoney monetaMoney = FastMoney.of(new BigDecimal("5.5"), CHF);

    MonetaryAmount difference = acmeMoney.subtract(monetaMoney);
    assertThat(difference.getNumber().numberValueExact(BigDecimal.class), comparesEqualTo(new BigDecimal("-2.2")));

    difference = monetaMoney.subtract(acmeMoney);
    assertThat(difference.getNumber().numberValueExact(BigDecimal.class), comparesEqualTo(new BigDecimal("2.2")));
  }

  @Test
  void addFraction() {
    MonetaryAmount acmeMoney = FractionMoney.of(33L, 10L, CHF);
    FastMoney monetaMoney = FastMoney.of(new BigDecimal("5.5"), CHF);

    MonetaryAmount sum = acmeMoney.add(monetaMoney);
    assertThat(sum.getNumber().numberValueExact(BigDecimal.class), comparesEqualTo(new BigDecimal("8.8")));

    sum = monetaMoney.add(acmeMoney);
    assertThat(sum.getNumber().numberValueExact(BigDecimal.class), comparesEqualTo(new BigDecimal("8.8")));
  }

  @Test
  void subtractFraction() {
    MonetaryAmount acmeMoney = FractionMoney.of(33L, 10L, CHF);
    FastMoney monetaMoney = FastMoney.of(new BigDecimal("5.5"), CHF);

    MonetaryAmount difference = acmeMoney.subtract(monetaMoney);
    assertThat(difference.getNumber().numberValueExact(BigDecimal.class), comparesEqualTo(new BigDecimal("-2.2")));

    difference = monetaMoney.subtract(acmeMoney);
    assertThat(difference.getNumber().numberValueExact(BigDecimal.class), comparesEqualTo(new BigDecimal("2.2")));
  }

}
