package com.github.marschall.acme.money;

import static com.github.marschall.acme.money.FractionMath.gcd;

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
    throw new UnsupportedOperationException();
  }

}
