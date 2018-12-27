package com.github.marschall.acme.money;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.money.MonetaryAmountFactory;
import javax.money.MonetaryContext;
import javax.money.MonetaryOperator;
import javax.money.MonetaryQuery;
import javax.money.NumberValue;

import com.github.marschall.acme.money.FastNumber6Math.NumberAccessor;

final class FixedPointMoney implements MonetaryAmount, Comparable<MonetaryAmount>, Serializable {

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

  private FixedPointMoney(BigDecimal number, CurrencyUnit currency, MathContext mathContext, int scale) {
    this.number = number;
    this.currency = currency;
    this.mathContext = mathContext;
    this.scale = scale;
  }
  
  private RoundingMode getRoundingMode() {
    return this.mathContext.getRoundingMode();
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
  public CurrencyUnit getCurrency() {
    return this.currency;
  }

  @Override
  public NumberValue getNumber() {
    return new FixedPointNumberValue(this.number);
  }

  @Override
  public int compareTo(MonetaryAmount o) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public MonetaryContext getContext() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <R> R query(MonetaryQuery<R> query) {
    Objects.requireNonNull(query);
    return query.queryFrom(this);
  }

  @Override
  public FixedPointMoney with(MonetaryOperator operator) {
    Objects.requireNonNull(operator, "operator");
    MonetaryAmount result = operator.apply(this);
    Objects.requireNonNull(result, "result");
    return FixedPointMoney.class.cast(result);
  }

  @Override
  public MonetaryAmountFactory<? extends MonetaryAmount> getFactory() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isGreaterThan(MonetaryAmount amount) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isGreaterThanOrEqualTo(MonetaryAmount amount) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isLessThan(MonetaryAmount amount) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isLessThanOrEqualTo(MonetaryAmount amt) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isEqualTo(MonetaryAmount amount) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isNegative() {
    return this.number.signum() < 0;
  }

  @Override
  public boolean isNegativeOrZero() {
    return this.number.signum() <= 0;
  }

  @Override
  public boolean isPositive() {
    return this.number.signum() > 0;
  }

  @Override
  public boolean isPositiveOrZero() {
    return this.number.signum() >= 0;
  }

  @Override
  public boolean isZero() {
    return this.number.signum() == 0;
  }

  @Override
  public int signum() {
    return this.number.signum();
  }

  @Override
  public FixedPointMoney add(MonetaryAmount amount) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public FixedPointMoney subtract(MonetaryAmount amount) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public FixedPointMoney multiply(long multiplicand) {
    return this.multiply(Long.valueOf(multiplicand));
  }

  @Override
  public FixedPointMoney multiply(double multiplicand) {
    return this.multiply(Double.valueOf(multiplicand));
  }

  @Override
  public FixedPointMoney multiply(Number multiplicand) {
    // TODO max precision?
    BigDecimal product = this.number.multiply(convertToBigDecimal(multiplicand), mathContext);
    product = product.setScale(this.scale);
    return new FixedPointMoney(product, this.currency, this.mathContext, this.scale);
  }

  @Override
  public FixedPointMoney divide(long divisor) {
    return divide(Long.valueOf(divisor));
  }

  @Override
  public FixedPointMoney divide(double divisor) {
    return divide(Double.valueOf(divisor));
  }

  @Override
  public FixedPointMoney divide(Number divisor) {
    BigDecimal divided = this.number.divide(convertToBigDecimal(divisor), this.scale, getRoundingMode());
    return new FixedPointMoney(divided, this.currency, this.mathContext, this.scale);
  }

  @Override
  public FixedPointMoney remainder(long divisor) {
    return this.remainder(Long.valueOf(divisor));
  }

  @Override
  public FixedPointMoney remainder(double divisor) {
    return this.remainder(Double.valueOf(divisor));
  }

  @Override
  public FixedPointMoney remainder(Number divisor) {
    // TODO max precision?
    BigDecimal remainder = this.number.remainder(convertToBigDecimal(divisor), this.mathContext);
    return new FixedPointMoney(remainder, this.currency, this.mathContext, this.scale);
  }

  @Override
  public FixedPointMoney[] divideAndRemainder(long divisor) {
    return this.divideAndRemainder(Long.valueOf(divisor));
  }

  @Override
  public FixedPointMoney[] divideAndRemainder(double divisor) {
    return this.divideAndRemainder(Double.valueOf(divisor));
  }

  @Override
  public FixedPointMoney[] divideAndRemainder(Number divisor) {
    // TODO max precision?
    BigDecimal[] quotientAndRemainder = this.number.divideAndRemainder(convertToBigDecimal(divisor), this.mathContext);
    BigDecimal quotient = quotientAndRemainder[0];
    BigDecimal remainder = quotientAndRemainder[1];
    // TODO make sure scale is correct
    return new FixedPointMoney[]{new FixedPointMoney(quotient, this.currency, this.mathContext, this.scale),
        new FixedPointMoney(remainder, this.currency, this.mathContext, this.scale)};
  }

  @Override
  public FixedPointMoney divideToIntegralValue(long divisor) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public FixedPointMoney divideToIntegralValue(double divisor) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public FixedPointMoney divideToIntegralValue(Number divisor) {
    // TODO max precision?
    BigDecimal integerPart = this.number.divideToIntegralValue(number, this.mathContext);
    return new FixedPointMoney(integerPart, this.currency, this.mathContext, this.scale);
  }

  @Override
  public FixedPointMoney scaleByPowerOfTen(int power) {
    if (power == 0) {
      return this;
    }
    // TODO max scale
    BigDecimal scaled = this.number.scaleByPowerOfTen(power);
    if (scaled.scale() > this.scale) {
      scaled = scaled.setScale(power, this.getRoundingMode());
    }
    return new FixedPointMoney(scaled, this.currency, this.mathContext, this.scale);
  }

  @Override
  public FixedPointMoney abs() {
    BigDecimal abs = this.number.abs();
    if (abs == this.number) {
      return this;
    } else {
      return new FixedPointMoney(abs, this.currency, this.mathContext, this.scale);
    }
  }

  @Override
  public FixedPointMoney negate() {
    BigDecimal negated = this.number.negate();
    return new FixedPointMoney(negated, this.currency, this.mathContext, this.scale);
  }

  @Override
  public FixedPointMoney plus() {
    return this;
  }

  @Override
  public FixedPointMoney stripTrailingZeros() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int hashCode() {
    // TODO Auto-generated method stub
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    // TODO Auto-generated method stub
    return super.equals(obj);
  }

  @Override
  public String toString() {
    // TODO Auto-generated method stub
    return super.toString();
  }

}
