package com.github.marschall.acme.money;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

final class ConvertToFastLong6 {

  static long convert(Number number) {
    Objects.requireNonNull(number, "number");
    FastLong6Converter converter = getConverter(number.getClass());
    return converter.convert(number);
  }

  private static FastLong6Converter getConverter(Class<?> numberType) {
    FastLong6Converter converter = CONVERTER_MAP.get(numberType);
    if (converter == null) {
      throw new IllegalArgumentException("Unsupported numeric type: " + numberType);
    }
    return converter;
  }

  interface FastLong6Converter {

    long convert(Number number);

  }

  static final class ConvertDouble implements FastLong6Converter {

    @Override
    public long convert(Number number) {
      double doubleValue = number.doubleValue() * FastMoney6.DIVISOR;
      if ((doubleValue > FastMoney6.MAX_DOUBLE) || (doubleValue < FastMoney6.MIN_DOUBLE)) {
        throw new ArithmeticException();
      }
      return Math.round(doubleValue);
    }

  }

  static final class ConvertLong implements FastLong6Converter {

    @Override
    public long convert(Number number) {
      return Math.multiplyExact(number.longValue(), FastMoney6.DIVISOR);
    }

  }

  static final class ConvertFastNumber6 implements FastLong6Converter {

    @Override
    public long convert(Number number) {
      return ((FastNumber6) number).value;
    }

  }

  // TODO Fraction

  static final class ConvertBigDecimal implements FastLong6Converter {

    @Override
    public long convert(Number number) {
      BigDecimal bigDecimal = (BigDecimal) number;
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

    private static ArithmeticException scaleTooBig(BigDecimal bigDecimal) {
      return new ArithmeticException(bigDecimal + " can not be represented by this class, scale > " + FastMoney6.SCALE);
    }

    private static ArithmeticException valueTooLarge(BigDecimal bigDecimal) {
      return new ArithmeticException("Overflow: " + bigDecimal + " > " + FastMoney6.MAX_BD);
    }

    private static ArithmeticException valueTooSmall(BigDecimal bigDecimal) {
      return new ArithmeticException("Overflow: " + bigDecimal + " < " + FastMoney6.MIN_BD);
    }

  }

  static final class ConvertBigInteger implements FastLong6Converter {

    @Override
    public long convert(Number number) {
      return Math.multiplyExact(((BigInteger) number).longValueExact(), FastMoney6.DIVISOR);
    }

  }

  private static final Map<Class<? extends Number>, FastLong6Converter> CONVERTER_MAP;

  static {
    CONVERTER_MAP = new HashMap<>(10);
    CONVERTER_MAP.put(FastNumber6.class, new ConvertFastNumber6());
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
  }

}