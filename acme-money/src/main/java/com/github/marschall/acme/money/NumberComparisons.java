package com.github.marschall.acme.money;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

final class NumberComparisons {

  static boolean isOne(Number number) {
    Objects.requireNonNull(number, "number");
    NumberChecker converter = getConverter(number.getClass());
    return converter.isOne(number);
  }

  private static NumberChecker getConverter(Class<?> numberType) {
    NumberChecker converter = CONVERTER_MAP.get(numberType);
    if (converter == null) {
      throw new IllegalArgumentException("Unsupported numeric type: " + numberType);
    }
    return converter;
  }

  static long fromBigDecimal(BigDecimal bigDecimal) {
    if (bigDecimal.scale() > FastMoney6.SCALE) {
      // strip trailing zeros only if we have to
      bigDecimal = bigDecimal.stripTrailingZeros();
      if (bigDecimal.scale() > FastMoney6.SCALE) {
        throw scaleTooBig(bigDecimal);
      }
    }
    if (bigDecimal.compareTo(FastMoney6.MIN_BD) < 0) {
      throw valueTooSmall(bigDecimal);
    } else if (bigDecimal.compareTo(FastMoney6.MAX_BD) > 0) {
      throw valueTooLarge(bigDecimal);
    }
    return bigDecimal.movePointRight(FastMoney6.SCALE).longValue();
  }

  static ArithmeticException scaleTooBig(Number number) {
    return new ArithmeticException(number + " can not be represented by this class, scale > " + FastMoney6.SCALE);
  }

  static ArithmeticException valueTooLarge(Number number) {
    return new ArithmeticException("Overflow: " + number + " > " + FastMoney6.MAX_BD);
  }

  static ArithmeticException valueTooSmall(Number number) {
    return new ArithmeticException("Overflow: " + number + " < " + FastMoney6.MIN_BD);
  }

  interface NumberChecker {

    boolean isOne(Number number);

  }

  static final class ConvertDouble implements NumberChecker {

    @Override
    public boolean isOne(Number number) {
      return number.doubleValue() == 1.0d;
    }

  }

  static final class ConvertLong implements NumberChecker {

    @Override
    public boolean isOne(Number number) {
      return number.longValue() == 1L;
    }

  }

  static final class ConvertFastNumber6 implements NumberChecker {

    private static final long ONE = DecimalMath.pow10(1, FastMoney6.SCALE);

    @Override
    public boolean isOne(Number number) {
      return ((FastNumber6) number).value == ONE;
    }

  }

  static final class ConvertFastNumberValue6 implements NumberChecker {

    private static final long ONE = DecimalMath.pow10(1, FastMoney6.SCALE);

    @Override
    public boolean isOne(Number number) {
      return ((FastNumberValue6) number).value == ONE;
    }

  }

  static final class ConvertFraction implements NumberChecker {

    private static final Fraction ONE = Fraction.of(1L, 1L);

    @Override
    public boolean isOne(Number number) {
      return number.equals(ONE);
    }

  }

  static final class ConvertBigDecimal implements NumberChecker {

    @Override
    public boolean isOne(Number number) {
      return ((BigDecimal) number).compareTo(BigDecimal.ONE) == 0;
    }

  }

  static final class ConvertBigInteger implements NumberChecker {

    @Override
    public boolean isOne(Number number) {
      return ((BigInteger) number).longValueExact() == 1L;
    }

  }

  private static final Map<Class<? extends Number>, NumberChecker> CONVERTER_MAP;

  static {
    CONVERTER_MAP = new HashMap<>(16);
    CONVERTER_MAP.put(FastNumber6.class, new ConvertFastNumber6());
    CONVERTER_MAP.put(FastNumberValue6.class, new ConvertFastNumberValue6());
    CONVERTER_MAP.put(BigInteger.class, new ConvertBigInteger());
    CONVERTER_MAP.put(BigDecimal.class, new ConvertBigDecimal());
    CONVERTER_MAP.put(Float.class, new ConvertDouble());
    CONVERTER_MAP.put(Double.class, new ConvertDouble());
    CONVERTER_MAP.put(Long.class, new ConvertLong());
    CONVERTER_MAP.put(Integer.class, new ConvertLong());
    CONVERTER_MAP.put(Short.class, new ConvertLong());
    CONVERTER_MAP.put(Byte.class, new ConvertLong());
    CONVERTER_MAP.put(AtomicInteger.class, new ConvertLong());
    CONVERTER_MAP.put(AtomicLong.class, new ConvertLong());
    CONVERTER_MAP.put(Fraction.class, new ConvertFraction());
  }

}
