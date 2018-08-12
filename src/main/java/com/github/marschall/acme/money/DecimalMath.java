package com.github.marschall.acme.money;

final class DecimalMath {

  private DecimalMath() {
    throw new AssertionError("not instantiable");
  }

  static long pow10(long base, int exponent) {
    if (exponent == 0L) {
      return 1L;
    }
    if (exponent > 0L) {
      return pow10positive(base, exponent);
    } else {
      return pow10negative(base, exponent);
    }
  }

  private static long pow10positive(long base, int exponent) {
    long result = base;
    for (int i = 0; i < exponent; i++) {
      result = Math.multiplyExact(result, 10L);
    }
    return result;
  }

  private static long pow10negative(long base, int exponent) {
    long result = base;
    for (int i = 0; i > exponent; i--) {
      if ((result % 10) != 0L) {
        throw new ArithmeticException();
      }
      result = result / 10L;
    }
    return result;
  }

}
