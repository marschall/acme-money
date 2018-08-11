package com.github.marschall.acme.money;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

enum ConvertFastNumber6ToNumber {

  INSTANCE;

  static <T extends Number> T of(Class<T> numberType, FastNumberValue6 numberValue, long value6) {
    return INSTANCE.convert(numberType, numberValue, value6);
  }

  static <T extends Number> T ofExact(Class<T> numberType, FastNumberValue6 numberValue, long value6) {
    return INSTANCE.convertExact(numberType, numberValue, value6);
  }

  <T extends Number> T convert(Class<T> numberType, FastNumberValue6 numberValue, long value6) {
    FastNumber6Converter<T> converter = this.getConverter(numberType);
    return converter.convert(numberValue, value6);
  }

  <T extends Number> T convertExact(Class<T> numberType, FastNumberValue6 numberValue, long value6) {
    FastNumber6Converter<T> converter = this.getConverter(numberType);
    return converter.convertExact(numberValue, value6);
  }

  private <T extends Number> FastNumber6Converter<T> getConverter(Class<T> numberType) {
    @SuppressWarnings("unchecked")
    FastNumber6Converter<T> converter = this.converterMap.get(numberType);
    if (converter == null) {
      new IllegalArgumentException("Unsupported numeric type: " + numberType);
    }
    return converter;
  }

  interface FastNumber6Converter<N extends Number> {

    N convert(FastNumberValue6 numberValue, long value6);

    N convertExact(FastNumberValue6 numberValue, long value6);

  }

  static final class ConvertToDouble implements FastNumber6Converter<Double> {

    @Override
    public Double convert(FastNumberValue6 numberValue, long value6) {
      return numberValue.doubleValue();
    }

    @Override
    public Double convertExact(FastNumberValue6 numberValue, long value6) {
      return numberValue.doubleValueExact();
    }

  }

  static final class ConvertToFloat implements FastNumber6Converter<Float> {

    @Override
    public Float convert(FastNumberValue6 numberValue, long value6) {
      return numberValue.floatValue();
    }

    @Override
    public Float convertExact(FastNumberValue6 numberValue, long value6) {
      float converted = numberValue.floatValue();
      if ((converted * FastMoney6.DIVISOR) == value6) {
        return converted;
      } else {
        throw new ArithmeticException("no exact representation");
      }
    }

  }

  static final class ConvertToLong implements FastNumber6Converter<Long> {

    @Override
    public Long convert(FastNumberValue6 numberValue, long value6) {
      return numberValue.longValue();
    }

    @Override
    public Long convertExact(FastNumberValue6 numberValue, long value6) {
      return numberValue.longValueExact();
    }

  }

  static final class ConvertToAtomictLong implements FastNumber6Converter<AtomicLong> {

    @Override
    public AtomicLong convert(FastNumberValue6 numberValue, long value6) {
      return new AtomicLong(numberValue.longValue());
    }

    @Override
    public AtomicLong convertExact(FastNumberValue6 numberValue, long value6) {
      return new AtomicLong(numberValue.longValueExact());
    }

  }

  static final class ConvertToInteger implements FastNumber6Converter<Integer> {

    @Override
    public Integer convert(FastNumberValue6 numberValue, long value6) {
      return numberValue.intValue();
    }

    @Override
    public Integer convertExact(FastNumberValue6 numberValue, long value6) {
      return numberValue.intValueExact();
    }

  }

  static final class ConvertToAtomictInteger implements FastNumber6Converter<AtomicInteger> {

    @Override
    public AtomicInteger convert(FastNumberValue6 numberValue, long value6) {
      return new AtomicInteger(numberValue.intValue());
    }

    @Override
    public AtomicInteger convertExact(FastNumberValue6 numberValue, long value6) {
      return new AtomicInteger(numberValue.intValueExact());
    }

  }

  static final class ConvertToShort implements FastNumber6Converter<Short> {

    @Override
    public Short convert(FastNumberValue6 numberValue, long value6) {
      long value = numberValue.longValue();
      return (short) value;
    }

    @Override
    public Short convertExact(FastNumberValue6 numberValue, long value6) {
      long value = numberValue.longValueExact();
      if ((value > Short.MAX_VALUE) || (value < Short.MIN_VALUE)) {
        throw new ArithmeticException("overflow");
      } else {
        return (short) value;
      }
    }

  }

  static final class ConvertToByte implements FastNumber6Converter<Byte> {

    @Override
    public Byte convert(FastNumberValue6 numberValue, long value6) {
      long value = numberValue.longValue();
      return (byte) value;
    }

    @Override
    public Byte convertExact(FastNumberValue6 numberValue, long value6) {
      long value = numberValue.longValueExact();
      if ((value > Short.MAX_VALUE) || (value < Short.MIN_VALUE)) {
        throw new ArithmeticException("overflow");
      } else {
        return (byte) value;
      }
    }

  }

  static final class ConvertToBigInteger implements FastNumber6Converter<BigInteger> {

    @Override
    public BigInteger convert(FastNumberValue6 numberValue, long value6) {
      return BigInteger.valueOf(numberValue.longValue());
    }

    @Override
    public BigInteger convertExact(FastNumberValue6 numberValue, long value6) {
      return BigInteger.valueOf(numberValue.longValueExact());
    }

  }

  static final class ConvertToBigDecimal implements FastNumber6Converter<BigDecimal> {

    @Override
    public BigDecimal convert(FastNumberValue6 numberValue, long value6) {
      return BigDecimal.valueOf(value6, FastMoney6.SCALE);
    }

    @Override
    public BigDecimal convertExact(FastNumberValue6 numberValue, long value6) {
      return this.convert(numberValue, value6);
    }

  }

  private Map<Class<? extends Number>, FastNumber6Converter> converterMap;

  {
    this.converterMap = new HashMap<>(10);
    this.converterMap.put(BigDecimal.class, new ConvertToBigDecimal());
    this.converterMap.put(BigInteger.class, new ConvertToBigInteger());
    this.converterMap.put(Float.class, new ConvertToFloat());
    this.converterMap.put(Double.class, new ConvertToDouble());
    this.converterMap.put(Long.class, new ConvertToLong());
    this.converterMap.put(Integer.class, new ConvertToInteger());
    this.converterMap.put(Short.class, new ConvertToShort());
    this.converterMap.put(Byte.class, new ConvertToByte());
    this.converterMap.put(AtomicInteger.class, new ConvertToAtomictInteger());
    this.converterMap.put(AtomicLong.class, new ConvertToAtomictLong());
  }

}
