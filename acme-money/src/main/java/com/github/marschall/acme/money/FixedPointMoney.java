package com.github.marschall.acme.money;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;

abstract class FixedPointMoney implements MonetaryAmount, Comparable<MonetaryAmount>, Serializable {

  /**
   * The currency of this amount.
   */
  /* final */ CurrencyUnit currency;

  /**
   * The numeric part of this amount.
   */
  /* final */ BigDecimal number;
  
  private /* final */ MathContext mathContext;
  
  private /* final */ int scale;

}
