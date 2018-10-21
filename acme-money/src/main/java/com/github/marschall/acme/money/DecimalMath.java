package com.github.marschall.acme.money;

import java.math.BigDecimal;

final class DecimalMath {

  private DecimalMath() {
    throw new AssertionError("not instantiable");
  }

  static double doubleValue(long number6) {
    return (double) number6 / FastMoney6.DIVISOR;
  }

  static BigDecimal bigDecimal(long number6) {
    return BigDecimal.valueOf(number6, FastMoney6.SCALE);
  }

  static String fastNumber6ToString(long number6) {
    // TODO
    return BigDecimal.valueOf(number6, FastMoney6.SCALE).toString();
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

  static int log10if(int i) {
    if (i >= 100_000) {
      // 5 or more
      if (i >= 10_000_000) {
        // 7 or more
        if (i >= 1_000_000_000) {
          return 9;
        } else if (i >= 100_000_000) {
          return 8;
        } else {
          return 7;
        }
      } else {
        // 5 or 6
        if (i >= 1_000_000) {
          return 6;
        } else {
          return 5;
        }
      }
    } else {
      // 4 or less
      if (i >= 100) {
        // 3 or 4
        if (i >= 1_000) {
          return 4;
        } else {
          return 3;
        }
      } else {
        // 0 or 1
        if (i >= 10) {
          return 1;
        } else {
          return 0;
        }
      }
    }
  }

  static int log10double(int i) {
    return (int) Math.log10(i);
  }

  static int log10loop(int i) {
    return 0;
  }

}
