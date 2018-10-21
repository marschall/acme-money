package com.github.marschall.acme.money;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

final class ConvertToBigDecimal {

  static BigDecimal convert(Number number) {
    Objects.requireNonNull(number, "number");
    if (number instanceof BigDecimal) {
      return (BigDecimal) number;
    }
    BigDecimalConverter converter = getConverter(number.getClass());
    return converter.convert(number);
  }

  private static BigDecimalConverter getConverter(Class<?> numberType) {
    BigDecimalConverter converter = CONVERTER_MAP.get(numberType);
    if (converter == null) {
      throw new IllegalArgumentException("Unsupported numeric type: " + numberType);
    }
    return converter;
  }

  interface BigDecimalConverter {

    BigDecimal convert(Number number);

  }

  static final class ConvertDouble implements BigDecimalConverter {

    @Override
    public BigDecimal convert(Number number) {
      return BigDecimal.valueOf((double) number);
    }

  }

  static final class ConvertFloat implements BigDecimalConverter {

    @Override
    public BigDecimal convert(Number number) {
      return BigDecimal.valueOf((float) number);
    }

  }

  static final class ConvertLong implements BigDecimalConverter {

    @Override
    public BigDecimal convert(Number number) {
      return BigDecimal.valueOf((long) number);
    }

  }

  static final class ConvertAtomictLong implements BigDecimalConverter {

    @Override
    public BigDecimal convert(Number number) {
      return BigDecimal.valueOf(((AtomicLong) number).get());
    }

  }

  static final class ConvertInteger implements BigDecimalConverter {

    @Override
    public BigDecimal convert(Number number) {
      return BigDecimal.valueOf((int) number);
    }

  }

  static final class ConvertAtomictInteger implements BigDecimalConverter {

    @Override
    public BigDecimal convert(Number number) {
      return BigDecimal.valueOf(((AtomicInteger) number).get());
    }

  }

  static final class ConvertShort implements BigDecimalConverter {

    @Override
    public BigDecimal convert(Number number) {
      return BigDecimal.valueOf((short) number);
    }

  }

  static final class ConvertByte implements BigDecimalConverter {

    @Override
    public BigDecimal convert(Number number) {
      return BigDecimal.valueOf((byte) number);
    }

  }

  static final class ConvertBigInteger implements BigDecimalConverter {

    @Override
    public BigDecimal convert(Number number) {
      return new BigDecimal((BigInteger) number);
    }

  }

  // TODO Fraction

  private static final Map<Class<? extends Number>, BigDecimalConverter> CONVERTER_MAP;

  static {
    CONVERTER_MAP = new HashMap<>(16);
    CONVERTER_MAP.put(BigInteger.class, new ConvertBigInteger());
    CONVERTER_MAP.put(Float.class, new ConvertFloat());
    CONVERTER_MAP.put(Double.class, new ConvertDouble());
    CONVERTER_MAP.put(Long.class, new ConvertLong());
    CONVERTER_MAP.put(Integer.class, new ConvertInteger());
    CONVERTER_MAP.put(Short.class, new ConvertShort());
    CONVERTER_MAP.put(Byte.class, new ConvertByte());
    CONVERTER_MAP.put(AtomicInteger.class, new ConvertAtomictInteger());
    CONVERTER_MAP.put(AtomicLong.class, new ConvertAtomictLong());
  }

}
