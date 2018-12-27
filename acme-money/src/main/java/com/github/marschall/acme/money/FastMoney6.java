package com.github.marschall.acme.money;

import static java.lang.Math.negateExact;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

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
import javax.money.format.MonetaryParseException;

import com.github.marschall.acme.money.FastNumber6Math.NumberAccessor;

/**
 * Like FastMoney but has 6 decimal places and does not silently overflow.
 */
public final class FastMoney6 implements MonetaryAmount, Comparable<MonetaryAmount>, Serializable {
  
  // TODO infinity and NaN
  // TODO constants for zero or one

  private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;

  private static final long serialVersionUID = 2L;

  /**
   * The currency of this amount.
   */
  final CurrencyUnit currency;

  /**
   * The numeric part of this amount.
   */
  final long value;
  
  static final int PRECISION = 19;

  /**
   * The current scale represented by the number.
   */
  static final int SCALE = 6;

  static final long DIVISOR = DecimalMath.pow10(1L, SCALE);

  /**
   * the {@link MonetaryContext} used by this instance, e.g. on division.
   */
  static final MonetaryContext MONETARY_CONTEXT =
          MonetaryContextBuilder.of(FastMoney6.class)
          .setMaxScale(SCALE)
          .setFixedScale(true)
          .setPrecision(PRECISION)
          .setProviderName(AcmeMoneyConstants.PROVIDER_NAME)
          .build();

  /**
   * Maximum possible value supported.
   */
  public static final NumberValue MAX_VALUE = new FastNumberValue6(Long.MAX_VALUE);

  /**
   * Minimum possible value supported.
   */
  public static final NumberValue MIN_VALUE = new FastNumberValue6(Long.MIN_VALUE);

  /**
   * Maximum possible numeric value supported.
   */
  static final BigDecimal MAX_BD = MAX_VALUE.numberValueExact(BigDecimal.class);

  /**
   * Minimum possible numeric value supported.
   */
  static final BigDecimal MIN_BD = MIN_VALUE.numberValueExact(BigDecimal.class);

  static final double MAX_DOUBLE = (double) Long.MAX_VALUE / DIVISOR;

  static final double MIN_DOUBLE = (double) Long.MIN_VALUE / DIVISOR;


  /**
   * Creates a new instance of {@link FastMoney6}.
   *
   * @param number   the amount, not null
   * @param currency the currency, not null
   */
  private FastMoney6(Number number, CurrencyUnit currency) {
    Objects.requireNonNull(currency, "currency");
    Objects.requireNonNull(number, "number");
    this.currency = currency;
    this.value = getInternalNumber(number);
  }

  /**
   * Creates a new instance of {@link FastMoney6}.
   *
   * @param numberValue the numeric value, not null
   * @param currency    the currency, not null
   */
  private FastMoney6(NumberValue numberValue, CurrencyUnit currency) {
    Objects.requireNonNull(currency, "currency");
    Objects.requireNonNull(numberValue, "number");
    this.currency = currency;
    this.value = getInternalNumber(numberValue);
  }

  /**
   * Creates a new instance of {@link FastMoney6}.
   *
   * @param number   The internal number value
   * @param currency the currency, not null.
   */
  FastMoney6(long number, CurrencyUnit currency) {
    Objects.requireNonNull(currency, "currency");
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

  private static long getInternalNumber(MonetaryAmount amount) {
    if (amount instanceof FastMoney6) {
      return ((FastMoney6) amount).value;
    }
    return getInternalNumber(amount.getNumber());
  }

  private static long getInternalNumber(NumberValue numberValue) {
    if (numberValue instanceof FastNumberValue6) {
      return ((FastNumberValue6) numberValue).value;
    }
    Number number = numberValue.numberValueExact(numberValue.getNumberType().asSubclass(Number.class));
    return getInternalNumber(number);
  }

  private static long getInternalNumber(Number number) {
    return FastNumber6Math.getAccessor(number.getClass()).convertToNumber6(number);
  }


  /**
   * Static factory method for creating a new instance of {@link FastMoney6}.
   *
   * @param currency      The target currency, not null.
   * @param numberBinding The numeric part, not null.
   * @return A new instance of {@link FastMoney6}.
   */
  public static FastMoney6 of(NumberValue numberBinding, CurrencyUnit currency) {
    return new FastMoney6(numberBinding, currency);
  }

  /**
   * Static factory method for creating a new instance of {@link FastMoney6}.
   *
   * @param currency The target currency, not null.
   * @param number   The numeric part, not null.
   * @return A new instance of {@link FastMoney6}.
   */
  public static FastMoney6 of(Number number, CurrencyUnit currency) {
    return new FastMoney6(number, currency);
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
    int compare = this.currency.compareTo(o.getCurrency());
    if (compare != 0) {
      return compare;
    }
    if (o instanceof FastMoney6) {
      return Long.compare(this.value, ((FastMoney6) o).value);
    } else {
      return this.getBigDecimal().compareTo(o.getNumber().numberValue(BigDecimal.class));
    }
  }

  private int compareAmountTo(MonetaryAmount amount) {
    this.requireSameCurrency(amount);
    if (amount instanceof FastMoney6) {
      return Long.compare(this.value, ((FastMoney6) amount).value);
    } else {
      return this.getBigDecimal().compareTo(amount.getNumber().numberValue(BigDecimal.class));
    }
  }

  private void requireSameCurrency(MonetaryAmount amount) {
    Objects.requireNonNull(amount, "amount");
    CurrencyUnit amountCurrency = amount.getCurrency();
    if (!this.currency.equals(amountCurrency)) {
        throw new MonetaryException("Currency mismatch: " + this.currency + '/' + amountCurrency);
    }
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
    if (!(obj instanceof FastMoney6)) {
      return false;
    }
    FastMoney6 other = (FastMoney6) obj;
    return (this.value == other.value) && this.currency.equals(other.currency);
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
    this.requireSameCurrency(amount);
    if (amount.isZero()) {
      return this;
    }
    return new FastMoney6(Math.addExact(this.value, getInternalNumber(amount)), this.currency);
  }
  
  @Override
  public FastMoney6 divide(Number divisor) {
    Objects.requireNonNull(divisor, "divisor");
    if (isOne(divisor)) {
      return this;
    }
    BigDecimal bigDecimalDivisor = convertToBigDecimal(divisor);
    return new FastMoney6(this.getBigDecimal().divide(bigDecimalDivisor, SCALE, ROUNDING_MODE), this.currency);
  }

  @Override
  public FastMoney6[] divideAndRemainder(Number divisor) {
    Objects.requireNonNull(divisor, "divisor");
    if (isOne(divisor)) {
      return new FastMoney6[]{this, FastMoney6.of(0, this.currency)};
    }
    BigDecimal div = convertToBigDecimal(divisor);
    BigDecimal[] result = this.getBigDecimal().divideAndRemainder(div);
    return new FastMoney6[]{new FastMoney6(result[0], this.currency), new FastMoney6(result[1], this.currency)};
  }

  @Override
  public FastMoney6 divideToIntegralValue(Number divisor) {
    Objects.requireNonNull(divisor, "divisor");
    if (isOne(divisor)) {
      return this;
    }
    BigDecimal div = convertToBigDecimal(divisor);
    return new FastMoney6(this.getBigDecimal().divideToIntegralValue(div), this.currency);
  }

  @Override
  public FastMoney6 multiply(Number multiplicand) {
    Objects.requireNonNull(multiplicand, "multiplicand");
    long product = getAccessor(multiplicand).multiply(this.value, multiplicand);
    if (product == this.value) {
      return this;
    } else {
      return new FastMoney6(product, this.currency);
    }
  }

  @Override
  public FastMoney6 negate() {
    if (this.value == 0L) {
      return this;
    }
    return new FastMoney6(negateExact(this.value), this.currency);
  }

  @Override
  public FastMoney6 plus() {
    return this;
  }

  @Override
  public FastMoney6 subtract(MonetaryAmount amount) {
    this.requireSameCurrency(amount);
    if (amount.isZero()) {
      return this;
    }
    return new FastMoney6(Math.subtractExact(this.value, getInternalNumber(amount)), this.currency);
  }

  @Override
  public FastMoney6 remainder(Number divisor) {
    Objects.requireNonNull(divisor, "divisor");
    if (isOne(divisor)) {
      return new FastMoney6(0L, this.currency);
    }
    return new FastMoney6(this.value % getInternalNumber(divisor), this.currency);
  }
  
  private static NumberAccessor getAccessor(Number number) {
    return FastNumber6Math.getAccessor(number.getClass());
  }

  private static boolean isOne(Number number) {
    return getAccessor(number).isOne(number);
  }

  private static BigDecimal convertToBigDecimal(Number number) {
    return getAccessor(number).convertToBigDecimal(number);
  }

  @Override
  public FastMoney6 scaleByPowerOfTen(int power) {
    // not really correct, different scale
    if (power == 0) {
      return this;
    }
    return new FastMoney6(DecimalMath.pow10(this.value, power), this.currency);
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
    return this.compareAmountTo(amount) < 0;
  }

  @Override
  public boolean isLessThanOrEqualTo(MonetaryAmount amount) {
    return this.compareAmountTo(amount) <= 0;
  }

  @Override
  public boolean isGreaterThan(MonetaryAmount amount) {
    return this.compareAmountTo(amount) > 0;
  }

  @Override
  public boolean isGreaterThanOrEqualTo(MonetaryAmount amount) {
    return this.compareAmountTo(amount) >= 0;
  }

  @Override
  public boolean isEqualTo(MonetaryAmount amount) {
    return this.compareAmountTo(amount) == 0;
  }

  @Override
  public NumberValue getNumber() {
    return new FastNumberValue6(this.value);
  }

  @Override
  public String toString() {
    StringBuilder buffer = new StringBuilder(25); // currency code (3) + space + 19 (numbers) + sign + decimal point
    buffer.append(this.currency.toString());
    buffer.append(' ');
    try {
      DecimalMath.fastNumber6ToStringOn(this.value, buffer);
    } catch (IOException e) {
      // should not happen
      throw new RuntimeException("could not write to StringBuilder", e);
    }
    return buffer.toString(); 
  }

  void toStringOn(Appendable appendable) throws IOException {
    appendable.append(this.currency.toString());
    appendable.append(' ');
    DecimalMath.fastNumber6ToStringOn(this.value, appendable);
  }

  @Override
  public FastMoney6 with(MonetaryOperator operator) {
    Objects.requireNonNull(operator, "operator");
    MonetaryAmount result = operator.apply(this);
    Objects.requireNonNull(result, "result");
    return FastMoney6.class.cast(result);
  }

  @Override
  public <R> R query(MonetaryQuery<R> query) {
    Objects.requireNonNull(query);
    return query.queryFrom(this);
  }

  public static FastMoney6 from(MonetaryAmount amount) {
    if (amount instanceof FastMoney6) {
      return (FastMoney6) amount;
    }
    return of(amount.getNumber(), amount.getCurrency());
  }

  /**
   * Obtains an instance of {@link FastMoney6} from a text string such as 'EUR 25.25'.
   *
   * @param text the text to parse not null
   * @return FastMoney instance
   * @throws NullPointerException
   * @throws MonetaryParseException
   * @throws UnknownCurrencyException
   */
  public static FastMoney6 parse(CharSequence text) {
    return parse(text, FastMoney6AmountFormat.INSTANCE);
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

  private BigDecimal getBigDecimal() {
    return DecimalMath.bigDecimal(this.value);
  }

  @Override
  public FastMoney6 multiply(double multiplicand) {
    checkFinite(multiplicand);
    if (multiplicand == 1.0) {
      return this;
    }
    if (multiplicand == 0.0 || multiplicand == -0.0) {
      return new FastMoney6(0, this.currency);
    }
    return new FastMoney6(Math.round(this.value * multiplicand), this.currency);
  }

  private static void checkFinite(double multiplicand) {
    if (!Double.isFinite(multiplicand)) {
      throw new ArithmeticException("invalid multiplicand: " + multiplicand);
    }
  }

  @Override
  public FastMoney6 divide(long divisor) {
    if (divisor == 1L) {
      return this;
    }
    return new FastMoney6(this.value / divisor, this.currency);
  }

  @Override
  public FastMoney6 divide(double divisor) {
    if (Double.isInfinite(divisor)) {
      return new FastMoney6(0L, this.currency);
    }
    if (divisor == 0.0d) {
      throw new ArithmeticException("Division by zero");
    }
    if (divisor == 1.0d) {
      return this;
    }
    return new FastMoney6(Math.round(this.value / divisor), this.currency);
  }

  @Override
  public FastMoney6 remainder(long divisor) {
    return this.remainder(BigDecimal.valueOf(divisor));
  }

  @Override
  public FastMoney6 remainder(double divisor) {
    return this.remainder(new BigDecimal(String.valueOf(divisor)));
  }

  @Override
  public FastMoney6[] divideAndRemainder(long divisor) {
    return this.divideAndRemainder(BigDecimal.valueOf(divisor));
  }

  @Override
  public FastMoney6[] divideAndRemainder(double divisor) {
    if (divisor == 0.0d) {
      throw new ArithmeticException("Division by zero");
    }
    return this.divideAndRemainder(new BigDecimal(divisor));
  }

  @Override
  public FastMoney6 stripTrailingZeros() {
    // not really correct, different scale
    return this;
  }

  @Override
  public FastMoney6 multiply(long multiplicand) {
    if (multiplicand == 1L) {
      return this;
    }
    return new FastMoney6(Math.multiplyExact(this.value, multiplicand), this.currency);
  }

  @Override
  public FastMoney6 divideToIntegralValue(long divisor) {
    if (divisor == 1L) {
      return this;
    }
    if (divisor == 0L) {
      throw new ArithmeticException("Division by zero");
    }
    long result = ((this.value / divisor) / DIVISOR) * DIVISOR;
    return new FastMoney6(result, this.currency);
  }

  @Override
  public FastMoney6 divideToIntegralValue(double divisor) {
    if (divisor == 1.0d) {
      return this;
    }
    if (divisor == 0.0d) {
      throw new ArithmeticException("Division by zero");
    }
    if (Double.isNaN(divisor)|| Double.isInfinite(divisor)) {
        throw new IllegalArgumentException();
    }
    long result = (long) (Math.floor(this.value / divisor) * DIVISOR);
    return new FastMoney6(result, this.currency);
  }

  @Override
  public MonetaryAmountFactory<FastMoney6> getFactory() {
    // TODO look at
    return new FastMoney6AmountFactory().setAmount(this);
  }

  private Object writeReplace() throws ObjectStreamException {
    return new Ser(this);
  }

}
