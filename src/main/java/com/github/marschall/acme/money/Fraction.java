package com.github.marschall.acme.money;

import static com.github.marschall.acme.money.FractionMath.gcd;
import static java.lang.Math.multiplyExact;
import static java.lang.Math.subtractExact;

public final class Fraction extends Number implements Comparable<Fraction> {
  
  private final long numerator;
  private final long denominator;
  
  private Fraction(long numerator, long denominator) {
    this.numerator = numerator;
    this.denominator = denominator;
  }
  
  public static Fraction of(long numerator, long denominator) {
    long n = numerator;
    long d = denominator;
    if (d < 0) {
      if (d == Long.MIN_VALUE) {
        throw new ArithmeticException("overflow");
      } else {
        d = -d;
        n = -n;
      }
    }
    long gcd = gcd(n, d);
    if (gcd != 1) {
      d = d / gcd;
      n = n / gcd;
    }
    return new Fraction(n, d);
  }

  @Override
  public int intValue() {
    return (int) (numerator / denominator);
  }

  @Override
  public long longValue() {
    return numerator / denominator;
  }

  @Override
  public float floatValue() {
    return (float) numerator / (float) denominator;
  }

  @Override
  public double doubleValue() {
    return (double) numerator / (double) denominator;
  }
  
  long getNumerator() {
    return numerator;
  }
  
  public long getDenominator() {
    return denominator;
  }

  @Override
  public int compareTo(Fraction o) {
    long ad = multiplyExact(this.numerator, o.denominator);
    long bc = multiplyExact(this.denominator, o.numerator);
    return Long.signum(subtractExact(ad, bc));
  }
  
  
  
  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + Long.hashCode(this.numerator);
    result = 31 * result + Long.hashCode(this.denominator);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Fraction)) {
      return false;
    }
    Fraction other = (Fraction) obj;
    return this.numerator == other.numerator
        && this.denominator == other.denominator;
  }

  @Override
  public String toString() {
    return Long.toString(this.numerator) + '/' + this.denominator;
  }

}
