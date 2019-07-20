package com.github.marschall.acme.money.proprietary;

import java.io.Serializable;
import java.util.Objects;

import javax.money.MonetaryAmount;
import javax.money.MonetaryOperator;
import javax.money.MonetaryQuery;

final class AcmeMoney implements MonetaryAmount, Comparable<MonetaryAmount>, Serializable {

  // TODO should comparison methods round?

  private final /* final */ byte scale;

  private final /* final */ MonetaryAmount delegate;

  private AcmeMoney(MonetaryAmount delegate, byte scale) {
    this.delegate = delegate;
    this.scale = scale;
  }

  private MonetaryAmount roundToScale() {
    // TODO implement
    return null;
  }

  private MonetaryAmount asRational() {
    // TODO implement
    return null;
  }

  private AcmeMoney adapt(MonetaryAmount newDelegate) {
    if (newDelegate == this.delegate) {
      return this;
    }
    return new AcmeMoney(newDelegate, this.scale);
  }

  @Override
  public AcmeMoney add(MonetaryAmount amount) {
    return adapt(this.roundToScale().add(amount));
  }

  @Override
  public AcmeMoney subtract(MonetaryAmount amount) {
    return adapt(this.roundToScale().subtract(amount));
  }

  @Override
  public AcmeMoney multiply(long multiplicand) {
    return adapt(this.asRational().multiply(multiplicand));
  }

  @Override
  public AcmeMoney multiply(double multiplicand) {
    return adapt(this.asRational().multiply(multiplicand));
  }

  @Override
  public AcmeMoney multiply(Number multiplicand) {
    return adapt(this.asRational().multiply(multiplicand));
  }

  @Override
  public AcmeMoney divide(long divisor) {
    return adapt(this.asRational().divide(divisor));
  }

  @Override
  public AcmeMoney divide(double divisor) {
    return adapt(this.asRational().divide(divisor));
  }

  @Override
  public AcmeMoney divide(Number divisor) {
    return adapt(this.asRational().divide(divisor));
  }

  @Override
  public MonetaryAmount remainder(long divisor) {
    return adapt(this.asRational().remainder(divisor));
  }

  @Override
  public MonetaryAmount remainder(double divisor) {
    return adapt(this.asRational().remainder(divisor));
  }

  @Override
  public MonetaryAmount remainder(Number divisor) {
    return adapt(this.asRational().remainder(divisor));
  }

  @Override
  public AcmeMoney[] divideAndRemainder(long divisor) {
    MonetaryAmount[] quotientAndRemainder = this.asRational().divideAndRemainder(divisor);
    MonetaryAmount quotient = quotientAndRemainder[0];
    MonetaryAmount remainder = quotientAndRemainder[1];
    return new AcmeMoney[]{adapt(quotient), adapt(remainder)};
  }

  @Override
  public AcmeMoney[] divideAndRemainder(double divisor) {
    MonetaryAmount[] quotientAndRemainder = this.asRational().divideAndRemainder(divisor);
    MonetaryAmount quotient = quotientAndRemainder[0];
    MonetaryAmount remainder = quotientAndRemainder[1];
    return new AcmeMoney[]{adapt(quotient), adapt(remainder)};
  }

  @Override
  public AcmeMoney[] divideAndRemainder(Number divisor) {
    MonetaryAmount[] quotientAndRemainder = this.asRational().divideAndRemainder(divisor);
    MonetaryAmount quotient = quotientAndRemainder[0];
    MonetaryAmount remainder = quotientAndRemainder[1];
    return new AcmeMoney[]{adapt(quotient), adapt(remainder)};
  }

  @Override
  public AcmeMoney divideToIntegralValue(long divisor) {
    return adapt(this.asRational().divideToIntegralValue(divisor));
  }

  @Override
  public AcmeMoney divideToIntegralValue(double divisor) {
    return adapt(this.asRational().divideToIntegralValue(divisor));
  }

  @Override
  public AcmeMoney divideToIntegralValue(Number divisor) {
    return adapt(this.asRational().divideToIntegralValue(divisor));
  }

  @Override
  public int signum() {
    return this.delegate.signum();
  }

  @Override
  public MonetaryAmount abs() {
    return adapt(this.delegate.abs());
  }

  @Override
  public MonetaryAmount negate() {
    return adapt(this.delegate.negate());
  }

  @Override
  public AcmeMoney plus() {
    return this;
  }

  @Override
  public boolean isGreaterThan(MonetaryAmount amount) {
    return this.delegate.isGreaterThan(amount);
  }

  @Override
  public boolean isGreaterThanOrEqualTo(MonetaryAmount amount) {
    return this.delegate.isGreaterThanOrEqualTo(amount);
  }

  @Override
  public boolean isLessThan(MonetaryAmount amount) {
    return this.delegate.isLessThan(amount);
  }

  @Override
  public boolean isLessThanOrEqualTo(MonetaryAmount amt) {
    return this.delegate.isLessThanOrEqualTo(amt);
  }

  @Override
  public boolean isEqualTo(MonetaryAmount amount) {
    return this.delegate.isEqualTo(amount);
  }

  @Override
  public boolean isNegative() {
    return this.delegate.isNegative();
  }

  @Override
  public boolean isNegativeOrZero() {
    return this.delegate.isNegativeOrZero();
  }

  @Override
  public boolean isPositive() {
    return this.delegate.isPositive();
  }

  @Override
  public boolean isPositiveOrZero() {
    return this.delegate.isPositiveOrZero();
  }

  @Override
  public boolean isZero() {
    return this.delegate.isZero();
  }

  @Override
  public <R> R query(MonetaryQuery<R> query) {
    Objects.requireNonNull(query);
    return query.queryFrom(this);
  }

  @Override
  public AcmeMoney with(MonetaryOperator operator) {
    Objects.requireNonNull(operator, "operator");
    MonetaryAmount result = operator.apply(this);
    Objects.requireNonNull(result, "result");
    return AcmeMoney.class.cast(result);
  }

}
