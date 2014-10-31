package com.github.marschall.acme.money;

import static java.lang.Math.abs;

final class FractionMath {

  private FractionMath() {
    throw new AssertionError("not instantiable");
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
      if (k > m) {
        k = k - m;
      } else {
        m = m - k;
      }
    }
    return k;
  }

}
