package com.github.marschall.acme.money.proprietary;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.money.MonetaryAmount;

import com.github.marschall.acme.money.FixedPointMoney;

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
  public int signum() {
    return this.delegate.signum();
  }



}
