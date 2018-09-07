package com.github.marschall.acme.money;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


final class ConvertToFraction {

  private ConvertToFraction() {
    throw new AssertionError("not instantiable");
  }

  static Fraction convert(Number number) {
    Objects.requireNonNull(number, "number");
    if (number instanceof Fraction) {
      return (Fraction) number;
    }
    return getConverter(number.getClass()).convert(number);
  }

  private static FranctionConverter getConverter(Class<?> numberType) {
    FranctionConverter converter = CONVERTER_MAP.get(numberType);
    if (converter == null) {
      throw new IllegalArgumentException("Unsupported numeric type: " + numberType);
    }
    return converter;
  }

  private static Fraction fromBigDecimal(BigDecimal b) {
    if (b.signum() == 0) {
      return Fraction.of(0L, 1L);
    }
    BigDecimal decimal = b;
    if (decimal.scale() > 0) {
      decimal = decimal.stripTrailingZeros();
    }
    long denominator = DecimalMath.pow10(1, decimal.scale());
    long numerator = decimal.movePointRight(decimal.scale()).longValueExact();
    return Fraction.of(numerator, denominator);
  }



  interface FranctionConverter {

    Fraction convert(Number number);

  }

  static final class ConvertByte implements FranctionConverter {

    @Override
    public Fraction convert(Number number) {
      return Fraction.of((byte) number, 1L);
    }

  }

  static final class ConvertShort implements FranctionConverter {

    @Override
    public Fraction convert(Number number) {
      return Fraction.of((short) number, 1L);
    }

  }

  static final class ConvertInteger implements FranctionConverter {

    @Override
    public Fraction convert(Number number) {
      return Fraction.of((int) number, 1L);
    }

  }

  static final class AtomicIntegerFranctionConverter implements FranctionConverter {

    @Override
    public Fraction convert(Number number) {
      return Fraction.of(((AtomicInteger) number).intValue(), 1L);
    }

  }

  static final class ConvertLong implements FranctionConverter {

    @Override
    public Fraction convert(Number number) {
      return Fraction.of((long) number, 1L);
    }

  }

  static final class AtomicLongFranctionConverter implements FranctionConverter {

    @Override
    public Fraction convert(Number number) {
      return Fraction.of(((AtomicLong) number).longValue(), 1L);
    }

  }

  static final class ConvertBigInteger implements FranctionConverter {

    @Override
    public Fraction convert(Number number) {
      return Fraction.of(((BigInteger) number).longValueExact(), 1L);
    }

  }

  static final class ConvertBigDecimal implements FranctionConverter {

    @Override
    public Fraction convert(Number number) {
      return fromBigDecimal((BigDecimal) number);
    }

  }

  static final class ConvertFloat implements FranctionConverter {

    @Override
    public Fraction convert(Number number) {
      return fromBigDecimal(BigDecimal.valueOf((float) number));
    }

  }

  static final class ConvertDouble implements FranctionConverter {

    @Override
    public Fraction convert(Number number) {
      return fromBigDecimal(BigDecimal.valueOf((double) number));
    }

  }

  static final class ConvertFastNumber6 implements FranctionConverter {

    @Override
    public Fraction convert(Number number) {
      FastNumber6 number6 = (FastNumber6) number;
      return new Fraction(number6.value, FastMoney6.DIVISOR);
    }

  }

  private static final Map<Class<? extends Number>, FranctionConverter> CONVERTER_MAP;

  static {
    CONVERTER_MAP = new HashMap<>(16);
    CONVERTER_MAP.put(BigInteger.class, new ConvertBigInteger());
    CONVERTER_MAP.put(BigDecimal.class, new ConvertBigDecimal());
    CONVERTER_MAP.put(Float.class, new ConvertFloat());
    CONVERTER_MAP.put(Double.class, new ConvertDouble());
    CONVERTER_MAP.put(Long.class, new ConvertLong());
    CONVERTER_MAP.put(Integer.class, new ConvertInteger());
    CONVERTER_MAP.put(Short.class, new ConvertShort());
    CONVERTER_MAP.put(Byte.class, new ConvertByte());
    CONVERTER_MAP.put(AtomicInteger.class, new AtomicIntegerFranctionConverter());
    CONVERTER_MAP.put(AtomicLong.class, new AtomicLongFranctionConverter());
    CONVERTER_MAP.put(FastNumber6.class, new ConvertFastNumber6());
  }

}
