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

  static int lenghtIf(int i) {
    if (i >= 100_000) {
      // 6 or more
      if (i >= 10_000_000) {
        // 8 or more
        if (i >= 1_000_000_000) {
          return 10;
        } else if (i >= 100_000_000) {
          return 9;
        } else {
          return 8;
        }
      } else {
        // 6 or 7
        if (i >= 1_000_000) {
          return 7;
        } else {
          return 6;
        }
      }
    } else {
      // 5 or less
      if (i >= 100) {
        // 3 or 4 or 5
        if (i >= 10_000) {
          return 5;
        } else if (i >= 1_000) {
          return 4;
        } else {
          return 3;
        }
      } else {
        // 1 or 2
        if (i >= 10) {
          return 2;
        } else {
          return 1;
        }
      }
    }
  }

  private static int log10If(int i) {
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

  static int lenghtLoop(int i) {
    int maxVal = 10;
    for (int j = 1; j <= 10; j++) {
      if (i < maxVal) {
        return j;
      }
      maxVal *= 10;
    }
    return 10;
  }

}
