/**
 * Copyright (c) 2012, 2014, Credit Suisse (Anatole Tresch), Werner Keil and others by the @author tag.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.marschall.acme.money;

import static java.lang.Math.negateExact;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.MonetaryAmountFactory;
import javax.money.MonetaryContext;
import javax.money.MonetaryContextBuilder;
import javax.money.MonetaryException;
import javax.money.MonetaryOperator;
import javax.money.MonetaryQuery;
import javax.money.NumberValue;
import javax.money.UnknownCurrencyException;
import javax.money.format.MonetaryAmountFormat;

import org.javamoney.moneta.spi.DefaultNumberValue;
import org.javamoney.moneta.spi.MonetaryConfig;
import org.javamoney.moneta.spi.MoneyUtils;

import com.github.marschall.acme.money.ToStringMonetaryAmountFormat.ToStringMonetaryAmountFormatStyle;

/**
 * Like FastMoney but has 6 decimal places and does no silently overflow.
 */
public final class FastMoney6 implements MonetaryAmount, Comparable<MonetaryAmount>, Serializable {

  /**
   * The logger used.
   */
  private static final Logger LOG = Logger.getLogger(FastMoney6.class.getName());

  /**
   * The currency of this amount.
   */
  private final CurrencyUnit currency;

  /**
   * The numeric part of this amount.
   */
  private final long number;

  /**
   * The current scale represented by the number.
   */
  static final int SCALE = 6;

  /**
   * the {@link MonetaryContext} used by this instance, e.g. on division.
   */
  static final MonetaryContext MONETARY_CONTEXT =
      MonetaryContextBuilder.of(FastMoney6.class).setMaxScale(SCALE).setFixedScale(true).setPrecision(19).build();

  /**
   * Maximum possible value supported, using XX (no currency).
   */
  // REVIEW maybe replace with BigDecimal or similar
  public static final FastMoney6 MAX_VALUE = new FastMoney6(Long.MAX_VALUE, Monetary.getCurrency("XXX"));
  /**
   * Maximum possible numeric value supported.
   */
  private static final BigDecimal MAX_BD = MAX_VALUE.getBigDecimal();
  /**
   * Minimum possible value supported, using XX (no currency).
   */
  // REVIEW maybe replace with BigDecimal or similar
  public static final FastMoney6 MIN_VALUE = new FastMoney6(Long.MIN_VALUE, Monetary.getCurrency("XXX"));
  /**
   * Minimum possible numeric value supported.
   */
  private static final BigDecimal MIN_BD = MIN_VALUE.getBigDecimal();


  /**
   * Creates a new instance os {@link FastMoney6}.
   *
   * @param currency the currency, not null.
   * @param number   the amount, not null.
   */
  private FastMoney6(Number number, CurrencyUnit currency, boolean allowInternalRounding) {
    Objects.requireNonNull(currency, "Currency is required.");
    this.currency = currency;
    Objects.requireNonNull(number, "Number is required.");
    this.number = this.getInternalNumber(number, allowInternalRounding);
  }

  /**
   * Creates a new instance os {@link FastMoney6}.
   *
   * @param currency    the currency, not null.
   * @param numberValue the numeric value, not null.
   */
  private FastMoney6(NumberValue numberValue, CurrencyUnit currency, boolean allowInternalRounding) {
    Objects.requireNonNull(currency, "Currency is required.");
    this.currency = currency;
    Objects.requireNonNull(numberValue, "Number is required.");
    this.number = this.getInternalNumber(numberValue.numberValue(BigDecimal.class), allowInternalRounding);
  }

  /**
   * Creates a new instance os {@link FastMoney6}.
   *
   * @param number   The internal number value
   * @param currency the currency, not null.
   */
  private FastMoney6(long number, CurrencyUnit currency) {
    Objects.requireNonNull(currency, "Currency is required.");
    this.currency = currency;
    this.number = number;
  }

  /**
   * Returns the amount’s currency, modelled as {@link CurrencyUnit}.
   * Implementations may co-variantly change the return type to a more
   * specific implementation of {@link CurrencyUnit} if desired.
   *
   * @return the currency, never {@code null}
   * @see javax.money.MonetaryAmount#getCurrency()
   */
  @Override
  public CurrencyUnit getCurrency() {
    return this.currency;
  }

  /**
   * Access the {@link MonetaryContext} used by this instance.
   *
   * @return the {@link MonetaryContext} used, never null.
   * @see javax.money.MonetaryAmount#getContext()
   */
  @Override
  public MonetaryContext getContext() {
    return MONETARY_CONTEXT;
  }

  private long getInternalNumber(Number number, boolean allowInternalRounding) {
    BigDecimal bd = MoneyUtils.getBigDecimal(number);
    if (!allowInternalRounding && (bd.scale() > SCALE)) {
      throw new ArithmeticException(number + " can not be represented by this class, scale > " + SCALE);
    }
    if (bd.compareTo(MIN_BD) < 0) {
      throw new ArithmeticException("Overflow: " + number + " < " + MIN_BD);
    } else if (bd.compareTo(MAX_BD) > 0) {
      throw new ArithmeticException("Overflow: " + number + " > " + MAX_BD);
    }
    return bd.movePointRight(SCALE).longValue();
  }


  /**
   * Static factory method for creating a new instance of {@link FastMoney6}.
   *
   * @param currency      The target currency, not null.
   * @param numberBinding The numeric part, not null.
   * @return A new instance of {@link FastMoney6}.
   */
  public static FastMoney6 of(NumberValue numberBinding, CurrencyUnit currency) {
    return new FastMoney6(numberBinding, currency, false);
  }

  /**
   * Static factory method for creating a new instance of {@link FastMoney6}.
   *
   * @param currency The target currency, not null.
   * @param number   The numeric part, not null.
   * @return A new instance of {@link FastMoney6}.
   */
  public static FastMoney6 of(Number number, CurrencyUnit currency) {
    return new FastMoney6(number, currency, false);
  }

  /**
   * Static factory method for creating a new instance of {@link FastMoney6}.
   *
   * @param currencyCode The target currency as currency code.
   * @param number       The numeric part, not null.
   * @return A new instance of {@link FastMoney6}.
   */
  public static FastMoney6 of(Number number, String currencyCode) {
    CurrencyUnit currency = Monetary.getCurrency(currencyCode);
    return of(number, currency);
  }

  /*
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(MonetaryAmount o) {
    Objects.requireNonNull(o);
    int compare = this.getCurrency().getCurrencyCode().compareTo(o.getCurrency().getCurrencyCode());
    if (compare == 0) {
      compare = this.getNumber().numberValue(BigDecimal.class).compareTo(o.getNumber().numberValue(BigDecimal.class));
    }
    return compare;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.currency, this.number);
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof FastMoney6) {
      FastMoney6 other = (FastMoney6) obj;
      return Objects.equals(this.currency, other.currency) && Objects.equals(this.number, other.number);
    }
    return false;
  }


  /*
   * (non-Javadoc)
   * @see javax.money.MonetaryAmount#abs()
   */
  @Override
  public FastMoney6 abs() {
    if (this.isPositiveOrZero()) {
      return this;
    }
    return this.negate();
  }

  // Arithmetic Operations

  /*
   * (non-Javadoc)
   * @see javax.money.MonetaryAmount#add(javax.money.MonetaryAmount)
   */
  @Override
  public FastMoney6 add(MonetaryAmount amount) {
    this.checkAmountParameter(amount);
    if (amount.isZero()) {
      return this;
    }
    return new FastMoney6(Math.addExact(this.number, this.getInternalNumber(amount.getNumber(), false)), this.getCurrency());
  }

  private void checkAmountParameter(MonetaryAmount amount) {
    MoneyUtils.checkAmountParameter(amount, this.currency);
    // numeric check for overflow...
    if (amount.getNumber().getScale() > SCALE) {
      throw new ArithmeticException("Parameter exceeds maximal scale: " + SCALE);
    }
    if (amount.getNumber().getPrecision() > MAX_BD.precision()) {
      throw new ArithmeticException("Parameter exceeds maximal precision: " + SCALE);
    }
  }


  /*
   * (non-Javadoc)
   * @see javax.money.MonetaryAmount#divide(java.lang.Number)
   */
  @Override
  public FastMoney6 divide(Number divisor) {
    this.checkNumber(divisor);
    if (this.isOne(divisor)) {
      return this;
    }
    // FIXME
    return new FastMoney6(Math.round(this.number / divisor.doubleValue()), this.getCurrency());
  }

  /*
   * (non-Javadoc)
   * @see javax.money.MonetaryAmount#divideAndRemainder(java.lang.Number)
   */
  @Override
  public FastMoney6[] divideAndRemainder(Number divisor) {
    this.checkNumber(divisor);
    if (this.isOne(divisor)) {
      return new FastMoney6[]{this, FastMoney6.of(0, this.getCurrency())};
    }
    BigDecimal div = MoneyUtils.getBigDecimal(divisor);
    BigDecimal[] res = this.getBigDecimal().divideAndRemainder(div);
    return new FastMoney6[]{new FastMoney6(res[0], this.getCurrency(), true), new FastMoney6(res[1], this.getCurrency(), true)};
  }

  /*
   * (non-Javadoc)
   * @see javax.money.MonetaryAmount#divideToIntegralValue(java.lang.Number)
   */
  @Override
  public FastMoney6 divideToIntegralValue(Number divisor) {
    this.checkNumber(divisor);
    if (this.isOne(divisor)) {
      return this;
    }
    BigDecimal div = MoneyUtils.getBigDecimal(divisor);
    return new FastMoney6(this.getBigDecimal().divideToIntegralValue(div), this.getCurrency(), false);
  }

  @Override
  public FastMoney6 multiply(Number multiplicand) {
    this.checkNumber(multiplicand);
    if (this.isOne(multiplicand)) {
      return this;
    }
    // FIXME
    return new FastMoney6(Math.round(this.number * multiplicand.doubleValue()), this.getCurrency());
  }

  /*
   * (non-Javadoc)
   * @see javax.money.MonetaryAmount#negate()
   */
  @Override
  public FastMoney6 negate() {
    return new FastMoney6(negateExact(this.number), this.getCurrency());
  }

  /*
   * (non-Javadoc)
   * @see javax.money.MonetaryAmount#plus()
   */
  @Override
  public FastMoney6 plus() {
    if (this.number >= 0) {
      return this;
    }
    return new FastMoney6(negateExact(this.number), this.getCurrency());
  }

  /*
   * (non-Javadoc)
   * @see javax.money.MonetaryAmount#subtract(javax.money.MonetaryAmount)
   */
  @Override
  public FastMoney6 subtract(MonetaryAmount subtrahend) {
    this.checkAmountParameter(subtrahend);
    if (subtrahend.isZero()) {
      return this;
    }
    long subtrahendAsLong = this.getInternalNumber(subtrahend.getNumber(), false);
    return new FastMoney6(Math.addExact(this.number, negateExact(subtrahendAsLong)), this.getCurrency());
  }

  /*
   * (non-Javadoc)
   * @see javax.money.MonetaryAmount#remainder(java.lang.Number)
   */
  @Override
  public FastMoney6 remainder(Number divisor) {
    this.checkNumber(divisor);
    if (this.isOne(divisor)) {
      return new FastMoney6(0, this.getCurrency());
    }
    return new FastMoney6(this.number % this.getInternalNumber(divisor, true), this.getCurrency());
  }

  private boolean isOne(Number number) {
    BigDecimal bd = MoneyUtils.getBigDecimal(number);
    try {
      return (bd.scale() == 0) && (bd.longValueExact() == 1L);
    } catch (Exception e) {
      // The only way to end up here is that longValueExact throws an ArithmeticException,
      // so the amount is definitively not equal to 1.
      return false;
    }
  }

  /*
   * (non-Javadoc)
   * @see javax.money.MonetaryAmount#scaleByPowerOfTen(int)
   */
  @Override
  public FastMoney6 scaleByPowerOfTen(int n) {
    return new FastMoney6(this.getNumber().numberValue(BigDecimal.class).scaleByPowerOfTen(n), this.getCurrency(), true);
  }

  /*
   * (non-Javadoc)
   * @see javax.money.MonetaryAmount#isZero()
   */
  @Override
  public boolean isZero() {
    return this.number == 0L;
  }

  /*
   * (non-Javadoc)
   * @see javax.money.MonetaryAmount#isPositive()
   */
  @Override
  public boolean isPositive() {
    return this.number > 0L;
  }

  /*
   * (non-Javadoc)
   * @see javax.money.MonetaryAmount#isPositiveOrZero()
   */
  @Override
  public boolean isPositiveOrZero() {
    return this.number >= 0L;
  }

  /*
   * (non-Javadoc)
   * @see javax.money.MonetaryAmount#isNegative()
   */
  @Override
  public boolean isNegative() {
    return this.number < 0L;
  }

  /*
   * (non-Javadoc)
   * @see javax.money.MonetaryAmount#isNegativeOrZero()
   */
  @Override
  public boolean isNegativeOrZero() {
    return this.number <= 0L;
  }

  /*
   * (non-Javadoc)
   * @see javax.money.MonetaryAmount#getScale()
   */
  public int getScale() {
    return FastMoney6.SCALE;
  }

  /*
   * (non-Javadoc)
   * @see javax.money.MonetaryAmount#getPrecision()
   */
  public int getPrecision() {
    return this.getNumber().numberValue(BigDecimal.class).precision();
  }

  /*
   * (non-Javadoc)
   * @see javax.money.MonetaryAmount#signum()
   */

  @Override
  public int signum() {
    if (this.number < 0) {
      return -1;
    }
    if (this.number == 0) {
      return 0;
    }
    return 1;
  }

  /*
   * (non-Javadoc)
   * @see javax.money.MonetaryAmount#lessThan(javax.money.MonetaryAmount)
   */
  @Override
  public boolean isLessThan(MonetaryAmount amount) {
    this.checkAmountParameter(amount);
    return this.getBigDecimal().compareTo(amount.getNumber().numberValue(BigDecimal.class)) < 0;
  }

  /*
   * (non-Javadoc)
   * @see javax.money.MonetaryAmount#lessThan(java.lang.Number)
   */
  public boolean isLessThan(Number number) {
    this.checkNumber(number);
    return this.getBigDecimal().compareTo(MoneyUtils.getBigDecimal(number)) < 0;
  }

  /*
   * (non-Javadoc)
   * @see javax.money.MonetaryAmount#lessThanOrEqualTo(javax.money.MonetaryAmount)
   */
  @Override
  public boolean isLessThanOrEqualTo(MonetaryAmount amount) {
    this.checkAmountParameter(amount);
    return this.getBigDecimal().compareTo(amount.getNumber().numberValue(BigDecimal.class)) <= 0;
  }

  /*
   * (non-Javadoc)
   * @see javax.money.MonetaryAmount#lessThanOrEqualTo(java.lang.Number)
   */
  public boolean isLessThanOrEqualTo(Number number) {
    this.checkNumber(number);
    return this.getBigDecimal().compareTo(MoneyUtils.getBigDecimal(number)) <= 0;
  }

  /*
   * (non-Javadoc)
   * @see javax.money.MonetaryAmount#greaterThan(javax.money.MonetaryAmount)
   */
  @Override
  public boolean isGreaterThan(MonetaryAmount amount) {
    this.checkAmountParameter(amount);
    return this.getBigDecimal().compareTo(amount.getNumber().numberValue(BigDecimal.class)) > 0;
  }

  /*
   * (non-Javadoc)
   * @see javax.money.MonetaryAmount#greaterThan(java.lang.Number)
   */
  public boolean isGreaterThan(Number number) {
    this.checkNumber(number);
    return this.getBigDecimal().compareTo(MoneyUtils.getBigDecimal(number)) > 0;
  }

  /*
   * (non-Javadoc)
   * @see javax.money.MonetaryAmount#greaterThanOrEqualTo(javax.money.MonetaryAmount ) #see
   */
  @Override
  public boolean isGreaterThanOrEqualTo(MonetaryAmount amount) {
    this.checkAmountParameter(amount);
    return this.getBigDecimal().compareTo(amount.getNumber().numberValue(BigDecimal.class)) >= 0;
  }

  /*
   * (non-Javadoc)
   * @see javax.money.MonetaryAmount#greaterThanOrEqualTo(java.lang.Number)
   */
  public boolean isGreaterThanOrEqualTo(Number number) {
    this.checkNumber(number);
    return this.getBigDecimal().compareTo(MoneyUtils.getBigDecimal(number)) >= 0;
  }

  /*
   * (non-Javadoc)
   * @see javax.money.MonetaryAmount#isEqualTo(javax.money.MonetaryAmount)
   */
  @Override
  public boolean isEqualTo(MonetaryAmount amount) {
    this.checkAmountParameter(amount);
    return this.getBigDecimal().compareTo(amount.getNumber().numberValue(BigDecimal.class)) == 0;
  }

  /*
   * (non-Javadoc)
   * @see javax.money.MonetaryAmount#hasSameNumberAs(java.lang.Number)
   */
  public boolean hasSameNumberAs(Number number) {
    this.checkNumber(number);
    try {
      return this.number == this.getInternalNumber(number, false);
    } catch (ArithmeticException e) {
      return false;
    }
  }


  /**
   * Gets the number representation of the numeric value of this item.
   *
   * @return The {@link Number} represention matching best.
   */
  @Override
  public NumberValue getNumber() {
    return new DefaultNumberValue(this.getBigDecimal());
  }

  @Override
  public String toString() {
    return this.currency.toString() + ' ' + this.getBigDecimal();
  }

  // Internal helper methods

  /**
   * Internal method to check for correct number parameter.
   *
   * @param number the number to be checked, including null..
   * @throws NullPointerException          If the number is null
   * @throws java.lang.ArithmeticException If the number exceeds the capabilities of this class.
   */
  protected void checkNumber(Number number) {
    Objects.requireNonNull(number, "Number is required.");
    // numeric check for overflow...
    if (number.longValue() > MAX_BD.longValue()) {
      throw new ArithmeticException("Value exceeds maximal value: " + MAX_BD);
    }
    BigDecimal bd = MoneyUtils.getBigDecimal(number);
    if (bd.precision() > MAX_BD.precision()) {
      throw new ArithmeticException("Precision exceeds maximal precision: " + MAX_BD.precision());
    }
    if (bd.scale() > SCALE) {
      if (Boolean.parseBoolean(MonetaryConfig.getConfig()
          .getOrDefault("org.javamoney.moneta.FastMoney.enforceScaleCompatibility",
              "false"))) {
        throw new ArithmeticException("Scale of " + bd + " exceeds maximal scale: " + SCALE);
      } else {
        if (LOG.isLoggable(Level.FINEST)) {
          LOG.finest("Scale exceeds maximal scale of FastMoney (" + SCALE +
              "), implicit rounding will be applied to " + number);
        }
      }
    }
  }

  /*
   * }(non-Javadoc)
   * @see javax.money.MonetaryAmount#adjust(javax.money.AmountAdjuster)
   */
  @Override
  public FastMoney6 with(MonetaryOperator operator) {
    Objects.requireNonNull(operator);
    try {
      return FastMoney6.class.cast(operator.apply(this));
    } catch (ArithmeticException e) {
      throw e;
    } catch (Exception e) {
      throw new MonetaryException("Operator failed: " + operator, e);
    }
  }

  @Override
  public <R> R query(MonetaryQuery<R> query) {
    Objects.requireNonNull(query);
    try {
      return query.queryFrom(this);
    } catch (MonetaryException | ArithmeticException e) {
      throw e;
    } catch (Exception e) {
      throw new MonetaryException("Query failed: " + query, e);
    }
  }

  public static FastMoney6 from(MonetaryAmount amount) {
    if (FastMoney6.class.isInstance(amount)) {
      return FastMoney6.class.cast(amount);
    }
    return new FastMoney6(amount.getNumber(), amount.getCurrency(), false);
  }

  /**
   * Obtains an instance of FastMoney from a text string such as 'EUR 25.25'.
   *
   * @param text the text to parse not null
   * @return FastMoney instance
   * @throws NullPointerException
   * @throws NumberFormatException
   * @throws UnknownCurrencyException
   */
  public static FastMoney6 parse(CharSequence text) {
    return parse(text, DEFAULT_FORMATTER);
  }

  /**
   * Obtains an instance of FastMoney from a text using specific formatter.
   *
   * @param text      the text to parse not null
   * @param formatter the formatter to use not null
   * @return FastMoney instance
   */
  public static FastMoney6 parse(CharSequence text, MonetaryAmountFormat formatter) {
    return from(formatter.parse(text));
  }

  private static ToStringMonetaryAmountFormat DEFAULT_FORMATTER = ToStringMonetaryAmountFormat
      .of(ToStringMonetaryAmountFormatStyle.FAST_MONEY_6);

  private BigDecimal getBigDecimal() {
    return BigDecimal.valueOf(this.number).movePointLeft(SCALE);
  }

  @Override
  public FastMoney6 multiply(double amount) {
    if (amount == 1.0) {
      return this;
    }
    if (amount == 0.0) {
      return new FastMoney6(0, this.currency);
    }
    return new FastMoney6(Math.round(this.number * amount), this.currency);
  }

  @Override
  public FastMoney6 divide(long amount) {
    if (amount == 1L) {
      return this;
    }
    return new FastMoney6(this.number / amount, this.currency);
  }

  @Override
  public FastMoney6 divide(double number) {
    if (number == 1.0d) {
      return this;
    }
    return new FastMoney6(Math.round(this.number / number), this.getCurrency());
  }

  @Override
  public FastMoney6 remainder(long number) {
    return this.remainder(BigDecimal.valueOf(number));
  }

  @Override
  public FastMoney6 remainder(double amount) {
    return this.remainder(new BigDecimal(String.valueOf(amount)));
  }

  @Override
  public FastMoney6[] divideAndRemainder(long amount) {
    return this.divideAndRemainder(BigDecimal.valueOf(amount));
  }

  @Override
  public FastMoney6[] divideAndRemainder(double amount) {
    return this.divideAndRemainder(new BigDecimal(String.valueOf(amount)));
  }

  @Override
  public FastMoney6 stripTrailingZeros() {
    return this;
  }

  @Override
  public FastMoney6 multiply(long multiplicand) {
    if (multiplicand == 1) {
      return this;
    }
    if (multiplicand == 0) {
      return new FastMoney6(0L, this.currency);
    }
    return new FastMoney6(multiplicand * this.number, this.currency);
  }

  @Override
  public FastMoney6 divideToIntegralValue(long divisor) {
    if (divisor == 1L) {
      return this;
    }
    return this.divideToIntegralValue(MoneyUtils.getBigDecimal(divisor));
  }

  @Override
  public FastMoney6 divideToIntegralValue(double divisor) {
    if (divisor == 1.0d) {
      return this;
    }
    return this.divideToIntegralValue(MoneyUtils.getBigDecimal(divisor));
  }

  @Override
  public MonetaryAmountFactory<FastMoney6> getFactory() {
    return new FastMoney6AmountBuilder().setAmount(this);
  }

}
