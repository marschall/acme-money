package com.github.marschall.acme.money;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

final class ConvertFastNumber6ToNumber {

  static <T extends Number> T of(Class<T> numberType, FastNumberValue6 numberValue, long value6) {
    return convert(numberType, numberValue, value6);
  }

  static <T extends Number> T ofExact(Class<T> numberType, FastNumberValue6 numberValue, long value6) {
    return convertExact(numberType, numberValue, value6);
  }

  static <T extends Number> T convert(Class<T> numberType, FastNumberValue6 numberValue, long value6) {
    FastNumber6Converter converter = getConverter(numberType);
    return numberType.cast(converter.convert(numberValue, value6));
  }

  static <T extends Number> T convertExact(Class<T> numberType, FastNumberValue6 numberValue, long value6) {
    FastNumber6Converter converter = getConverter(numberType);
    return numberType.cast(converter.convertExact(numberValue, value6));
  }

  private static FastNumber6Converter getConverter(Class<?> numberType) {
    FastNumber6Converter converter = CONVERTER_MAP.get(numberType);
    if (converter == null) {
      throw new IllegalArgumentException("Unsupported numeric type: " + numberType);
    }
    return converter;
  }

  interface FastNumber6Converter {

    Number convert(FastNumberValue6 numberValue, long value6);

    Number convertExact(FastNumberValue6 numberValue, long value6);

  }

  static final class ConvertToDouble implements FastNumber6Converter {

    @Override
    public Double convert(FastNumberValue6 numberValue, long value6) {
      return numberValue.doubleValue();
    }

    @Override
    public Double convertExact(FastNumberValue6 numberValue, long value6) {
      return numberValue.doubleValueExact();
    }

  }

  static final class ConvertToFloat implements FastNumber6Converter {

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

  static final class ConvertToLong implements FastNumber6Converter {

    @Override
    public Long convert(FastNumberValue6 numberValue, long value6) {
      return numberValue.longValue();
    }

    @Override
    public Long convertExact(FastNumberValue6 numberValue, long value6) {
      return numberValue.longValueExact();
    }

  }

  static final class ConvertToAtomictLong implements FastNumber6Converter {

    @Override
    public AtomicLong convert(FastNumberValue6 numberValue, long value6) {
      return new AtomicLong(numberValue.longValue());
    }

    @Override
    public AtomicLong convertExact(FastNumberValue6 numberValue, long value6) {
      return new AtomicLong(numberValue.longValueExact());
    }

  }

  static final class ConvertToInteger implements FastNumber6Converter {

    @Override
    public Integer convert(FastNumberValue6 numberValue, long value6) {
      return numberValue.intValue();
    }

    @Override
    public Integer convertExact(FastNumberValue6 numberValue, long value6) {
      return numberValue.intValueExact();
    }

  }

  static final class ConvertToAtomicInteger implements FastNumber6Converter {

    @Override
    public AtomicInteger convert(FastNumberValue6 numberValue, long value6) {
      return new AtomicInteger(numberValue.intValue());
    }

    @Override
    public AtomicInteger convertExact(FastNumberValue6 numberValue, long value6) {
      return new AtomicInteger(numberValue.intValueExact());
    }

  }

  static final class ConvertToShort implements FastNumber6Converter {

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

  static final class ConvertToByte implements FastNumber6Converter {

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

  static final class ConvertToBigInteger implements FastNumber6Converter {

    @Override
    public BigInteger convert(FastNumberValue6 numberValue, long value6) {
      return BigInteger.valueOf(numberValue.longValue());
    }

    @Override
    public BigInteger convertExact(FastNumberValue6 numberValue, long value6) {
      return BigInteger.valueOf(numberValue.longValueExact());
    }

  }

  static final class ConvertToBigDecimal implements FastNumber6Converter {

    @Override
    public BigDecimal convert(FastNumberValue6 numberValue, long value6) {
      return DecimalMath.bigDecimal(value6);
    }

    @Override
    public BigDecimal convertExact(FastNumberValue6 numberValue, long value6) {
      return this.convert(numberValue, value6);
    }

  }

  private static final Map<Class<? extends Number>, FastNumber6Converter> CONVERTER_MAP;

  static {
    CONVERTER_MAP = new HashMap<>(16);
    CONVERTER_MAP.put(BigDecimal.class, new ConvertToBigDecimal());
    CONVERTER_MAP.put(BigInteger.class, new ConvertToBigInteger());
    CONVERTER_MAP.put(Float.class, new ConvertToFloat());
    CONVERTER_MAP.put(Double.class, new ConvertToDouble());
    CONVERTER_MAP.put(Long.class, new ConvertToLong());
    CONVERTER_MAP.put(Integer.class, new ConvertToInteger());
    CONVERTER_MAP.put(Short.class, new ConvertToShort());
    CONVERTER_MAP.put(Byte.class, new ConvertToByte());
    CONVERTER_MAP.put(AtomicInteger.class, new ConvertToAtomicInteger());
    CONVERTER_MAP.put(AtomicLong.class, new ConvertToAtomictLong());
  }

}
