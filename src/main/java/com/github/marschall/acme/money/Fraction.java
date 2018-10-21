package com.github.marschall.acme.money;

import static com.github.marschall.acme.money.FractionMath.gcd;
import static java.lang.Math.multiplyExact;
import static java.lang.Math.subtractExact;

import java.io.ObjectStreamException;

public final class Fraction extends Number implements Comparable<Fraction> {

  private static final long serialVersionUID = 2L;

  final long numerator;
  final long denominator;

  Fraction(long numerator, long denominator) {
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
    return (int) (this.numerator / this.denominator);
  }

  @Override
  public long longValue() {
    return this.numerator / this.denominator;
  }

  @Override
  public float floatValue() {
    return (float) this.numerator / (float) this.denominator;
  }

  @Override
  public double doubleValue() {
    return (double) this.numerator / (double) this.denominator;
  }

  long getNumerator() {
    return this.numerator;
  }

  long getDenominator() {
    return this.denominator;
  }

  @Override
  public int compareTo(Fraction o) {
    long ad = multiplyExact(this.numerator, o.denominator);
    long bc = multiplyExact(this.denominator, o.numerator);
    return Long.signum(subtractExact(ad, bc));
  }

  @Override
  public int hashCode() {
    return FractionMath.hashCode(this.numerator, this.denominator);
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
    return (this.numerator == other.numerator)
        && (this.denominator == other.denominator);
  }

  long fastNumberValue6() {
    return numeratorWithDenominator(FastMoney6.DIVISOR);
  }

  private long numeratorWithDenominator(long base) {
    if (this.denominator > base) {
      throw new ArithmeticException("precision exceeded");
    }
    if (this.denominator == base) {
      return denominator;
    }
    boolean divisible = base % denominator == 0;
    if (!divisible) {
      throw new ArithmeticException("not terminating expression");
    }
    // 1/2 -> 50_000/100_000
    return Math.multiplyExact(this.numerator, base / denominator);
    
  }

  @Override
  public String toString() {
    return FractionMath.toString(this.numerator, this.denominator);
  }

  private Object writeReplace() throws ObjectStreamException {
    return new Ser(this);
  }

}
