package com.github.marschall.acme.money;

import static java.lang.Math.abs;

final class FractionMath {

  private FractionMath() {
    throw new AssertionError("not instantiable");
  }

  static long toLongExact(double value) {
    if ((long) value != value) {
        throw new ArithmeticException("integer overflow");
    }
    return (long) value;
  }

  static long gcd(long initialK, long initialM) {
    if (initialK == Long.MIN_VALUE) {
      throw new ArithmeticException("overflow");
    }
    if (initialM == Long.MIN_VALUE) {
      throw new ArithmeticException("overflow");
    }
    if (initialK == 0) {
      return initialM;
    }
    long k = abs(initialK);
    long m = abs(initialM);
    while (k != m) {
      if ((k == 1) || (m == 1)) {
        return 1;
      }

      if (k > m) {
        k = k - m;
      } else {
        m = m - k;
      }
    }
    return k;
  }

  static int hashCode(long numerator, long denominator) {
    int result = 17;
    result = (31 * result) + Long.hashCode(numerator);
    result = (31 * result) + Long.hashCode(denominator);
    return result;
  }

  static String toString(long numerator, long denominator) {
    return Long.toString(numerator) + '/' + denominator;
  }

}
