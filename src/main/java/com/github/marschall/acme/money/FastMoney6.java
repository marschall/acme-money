package com.github.marschall.acme.money;

import static java.lang.Math.negateExact;

import java.io.ObjectStreamException;
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

import org.javamoney.moneta.spi.MonetaryConfig;
import org.javamoney.moneta.spi.MoneyUtils;

import com.github.marschall.acme.money.ToStringMonetaryAmountFormat.ToStringMonetaryAmountFormatStyle;

/**
 * Like FastMoney but has 6 decimal places and does not silently overflow.
 */
public final class FastMoney6 implements MonetaryAmount, Comparable<MonetaryAmount>, Serializable {

  private static final long serialVersionUID = 2L;

  private static final String PROVIDER_NAME = "acme";

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
  final long value;

  static final long DIVISOR = 1000000L;

  /**
   * The current scale represented by the number.
   */
  static final int SCALE = 6;

  /**
   * the {@link MonetaryContext} used by this instance, e.g. on division.
   */
  static final MonetaryContext MONETARY_CONTEXT =
          MonetaryContextBuilder.of(FastMoney6.class)
          .setMaxScale(SCALE)
          .setFixedScale(true)
          .setPrecision(19)
          .setProviderName(PROVIDER_NAME)
          .build();

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
   * Creates a new instance of {@link FastMoney6}.
   *
   * @param currency the currency, not null.
   * @param number   the amount, not null.
   */
  private FastMoney6(Number number, CurrencyUnit currency, boolean allowInternalRounding) {
    Objects.requireNonNull(currency, "Currency is required.");
    this.currency = currency;
    Objects.requireNonNull(number, "Number is required.");
    this.value = this.getInternalNumber(number, allowInternalRounding);
  }

  /**
   * Creates a new instance of {@link FastMoney6}.
   *
   * @param currency    the currency, not null.
   * @param numberValue the numeric value, not null.
   */
  private FastMoney6(NumberValue numberValue, CurrencyUnit currency, boolean allowInternalRounding) {
    Objects.requireNonNull(currency, "Currency is required.");
    this.currency = currency;
    Objects.requireNonNull(numberValue, "Number is required.");
    this.value = this.getInternalNumber(numberValue.numberValue(BigDecimal.class), allowInternalRounding);
  }

  /**
   * Creates a new instance of {@link FastMoney6}.
   *
   * @param number   The internal number value
   * @param currency the currency, not null.
   */
  FastMoney6(long number, CurrencyUnit currency) {
    Objects.requireNonNull(currency, "Currency is required.");
    this.currency = currency;
    this.value = number;
  }

  @Override
  public CurrencyUnit getCurrency() {
    return this.currency;
  }

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

  @Override
  public int compareTo(MonetaryAmount o) {
    Objects.requireNonNull(o);
    int compare = this.getCurrency().getCurrencyCode().compareTo(o.getCurrency().getCurrencyCode());
    if (compare == 0) {
      // TODO fast path
      compare = this.getNumber().numberValue(BigDecimal.class).compareTo(o.getNumber().numberValue(BigDecimal.class));
    }
    return compare;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.currency, this.value);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof FastMoney6) {
      FastMoney6 other = (FastMoney6) obj;
      return Objects.equals(this.currency, other.currency) && Objects.equals(this.value, other.value);
    }
    return false;
  }

  @Override
  public FastMoney6 abs() {
    if (this.isPositiveOrZero()) {
      return this;
    }
    return this.negate();
  }

  // Arithmetic Operations

  @Override
  public FastMoney6 add(MonetaryAmount amount) {
    this.checkAmountParameter(amount);
    if (amount.isZero()) {
      return this;
    }
    // TODO fast path
    return new FastMoney6(Math.addExact(this.value, this.getInternalNumber(amount.getNumber(), false)), this.getCurrency());
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


  @Override
  public FastMoney6 divide(Number divisor) {
    this.checkNumber(divisor);
    if (this.isOne(divisor)) {
      return this;
    }
    // FIXME
    return new FastMoney6(Math.round(this.value / divisor.doubleValue()), this.getCurrency());
  }

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
    return new FastMoney6(Math.round(this.value * multiplicand.doubleValue()), this.getCurrency());
  }

  @Override
  public FastMoney6 negate() {
    return new FastMoney6(negateExact(this.value), this.getCurrency());
  }

  @Override
  public FastMoney6 plus() {
    if (this.value >= 0) {
      return this;
    }
    return new FastMoney6(negateExact(this.value), this.getCurrency());
  }

  @Override
  public FastMoney6 subtract(MonetaryAmount subtrahend) {
    this.checkAmountParameter(subtrahend);
    if (subtrahend.isZero()) {
      return this;
    }
    long subtrahendAsLong = this.getInternalNumber(subtrahend.getNumber(), false);
    return new FastMoney6(Math.addExact(this.value, negateExact(subtrahendAsLong)), this.getCurrency());
  }

  @Override
  public FastMoney6 remainder(Number divisor) {
    this.checkNumber(divisor);
    if (this.isOne(divisor)) {
      return new FastMoney6(0, this.getCurrency());
    }
    return new FastMoney6(this.value % this.getInternalNumber(divisor, true), this.getCurrency());
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

  @Override
  public FastMoney6 scaleByPowerOfTen(int n) {
    return new FastMoney6(this.getNumber().numberValue(BigDecimal.class).scaleByPowerOfTen(n), this.getCurrency(), true);
  }

  @Override
  public boolean isZero() {
    return this.value == 0L;
  }

  @Override
  public boolean isPositive() {
    return this.value > 0L;
  }

  @Override
  public boolean isPositiveOrZero() {
    return this.value >= 0L;
  }

  @Override
  public boolean isNegative() {
    return this.value < 0L;
  }

  @Override
  public boolean isNegativeOrZero() {
    return this.value <= 0L;
  }

  @Override
  public int signum() {
    return Long.signum(this.value);
  }

  @Override
  public boolean isLessThan(MonetaryAmount amount) {
    this.checkAmountParameter(amount);
    // TODO fast path
    return this.getBigDecimal().compareTo(amount.getNumber().numberValue(BigDecimal.class)) < 0;
  }

  @Override
  public boolean isLessThanOrEqualTo(MonetaryAmount amount) {
    this.checkAmountParameter(amount);
    // TODO fast path
    return this.getBigDecimal().compareTo(amount.getNumber().numberValue(BigDecimal.class)) <= 0;
  }

  @Override
  public boolean isGreaterThan(MonetaryAmount amount) {
    this.checkAmountParameter(amount);
    // TODO fast path
    return this.getBigDecimal().compareTo(amount.getNumber().numberValue(BigDecimal.class)) > 0;
  }

  @Override
  public boolean isGreaterThanOrEqualTo(MonetaryAmount amount) {
    this.checkAmountParameter(amount);
    return this.getBigDecimal().compareTo(amount.getNumber().numberValue(BigDecimal.class)) >= 0;
  }

  @Override
  public boolean isEqualTo(MonetaryAmount amount) {
    this.checkAmountParameter(amount);
    // TODO fast path
    return this.getBigDecimal().compareTo(amount.getNumber().numberValue(BigDecimal.class)) == 0;
  }

  @Override
  public NumberValue getNumber() {
    return new FastNumberValue6(this.value);
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
    return BigDecimal.valueOf(this.value).movePointLeft(SCALE);
  }

  @Override
  public FastMoney6 multiply(double amount) {
    if (amount == 1.0) {
      return this;
    }
    if (amount == 0.0) {
      return new FastMoney6(0, this.currency);
    }
    return new FastMoney6(Math.round(this.value * amount), this.currency);
  }

  @Override
  public FastMoney6 divide(long amount) {
    if (amount == 1L) {
      return this;
    }
    return new FastMoney6(this.value / amount, this.currency);
  }

  @Override
  public FastMoney6 divide(double number) {
    if (number == 1.0d) {
      return this;
    }
    return new FastMoney6(Math.round(this.value / number), this.getCurrency());
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
    // TODO not really correct
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
    return new FastMoney6(Math.multiplyExact(this.value, multiplicand), this.currency);
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

  private Object writeReplace() throws ObjectStreamException {
    return new Ser(this);
  }

}
