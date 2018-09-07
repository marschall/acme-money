package com.github.marschall.acme.money;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

final class ConvertFractionToNumber {

  static <T extends Number> T of(Class<T> numberType, long numerator, long denominator) {
    return convert(numberType, numerator, denominator);
  }

  static <T extends Number> T ofExact(Class<T> numberType, long numerator, long denominator) {
    return convertExact(numberType, numerator, denominator);
  }

  static <T extends Number> T convert(Class<T> numberType, long numerator, long denominator) {
    FastNumber6Converter converter = getConverter(numberType);
    return numberType.cast(converter.convert(numerator, denominator));
  }

  static <T extends Number> T convertExact(Class<T> numberType, long numerator, long denominator) {
    FastNumber6Converter converter = getConverter(numberType);
    return numberType.cast(converter.convertExact(numerator, denominator));
  }

  private static FastNumber6Converter getConverter(Class<?> numberType) {
    FastNumber6Converter converter = CONVERTER_MAP.get(numberType);
    if (converter == null) {
      throw new IllegalArgumentException("Unsupported numeric type: " + numberType);
    }
    return converter;
  }

  interface FastNumber6Converter {

    Number convert(long numerator, long denominator);

    Number convertExact(long numerator, long denominator);

  }

  static final class ConvertToDouble implements FastNumber6Converter {

    @Override
    public Double convert(long numerator, long denominator) {
      return (double) numerator / (double) denominator;
    }

    @Override
    public Double convertExact(long numerator, long denominator) {
      Double converted = this.convert(numerator, denominator);
      if (((converted * denominator) / numerator) == 1.0d) {
        return converted;
      } else {
        throw new ArithmeticException("overflow");
      }
    }

  }

  static final class ConvertToFloat implements FastNumber6Converter {

    @Override
    public Float convert(long numerator, long denominator) {
      return (float) numerator / (float) denominator;
    }

    @Override
    public Float convertExact(long numerator, long denominator) {
      Float converted = this.convert(numerator, denominator);
      if (((converted * denominator) / numerator) == 1.0f) {
        return converted;
      } else {
        throw new ArithmeticException("overflow");
      }
    }

  }

  static final class ConvertToLong implements FastNumber6Converter {

    @Override
    public Long convert(long numerator, long denominator) {
      return numerator / denominator;
    }

    @Override
    public Long convertExact(long numerator, long denominator) {
      if (denominator != 1) {
        throw new ArithmeticException("overflow");
      }
      return this.convert(numerator, denominator);
    }

  }

  static final class ConvertToAtomictLong implements FastNumber6Converter {

    @Override
    public AtomicLong convert(long numerator, long denominator) {
      return new AtomicLong(numerator / denominator);
    }

    @Override
    public AtomicLong convertExact(long numerator, long denominator) {
      if (denominator != 1) {
        throw new ArithmeticException("overflow");
      }
      return this.convert(numerator, denominator);

    }

  }

  static final class ConvertToInteger implements FastNumber6Converter {

    @Override
    public Integer convert(long numerator, long denominator) {
      long value = numerator / denominator;
      if ((value > Integer.MAX_VALUE) || (value < Integer.MIN_VALUE)) {
        throw new ArithmeticException("overflow");
      } else {
        return (int) value;
      }
    }

    @Override
    public Integer convertExact(long numerator, long denominator) {
      if (denominator != 1) {
        throw new ArithmeticException("overflow");
      }
      return this.convert(numerator, denominator);
    }

  }

  static final class ConvertToAtomictInteger implements FastNumber6Converter {

    @Override
    public AtomicInteger convert(long numerator, long denominator) {
      long value = numerator / denominator;
      if ((value > Integer.MAX_VALUE) || (value < Integer.MIN_VALUE)) {
        throw new ArithmeticException("overflow");
      } else {
        return new AtomicInteger((int) value);
      }
    }

    @Override
    public AtomicInteger convertExact(long numerator, long denominator) {
      if (denominator != 1) {
        throw new ArithmeticException("overflow");
      }
      return this.convert(numerator, denominator);
    }

  }

  static final class ConvertToShort implements FastNumber6Converter {

    @Override
    public Short convert(long numerator, long denominator) {
      long value = numerator / denominator;
      if ((value > Short.MAX_VALUE) || (value < Short.MIN_VALUE)) {
        throw new ArithmeticException("overflow");
      } else {
        return (short) value;
      }
    }

    @Override
    public Short convertExact(long numerator, long denominator) {
      if (denominator != 1) {
        throw new ArithmeticException("overflow");
      }
      return this.convert(numerator, denominator);
    }

  }

  static final class ConvertToByte implements FastNumber6Converter {

    @Override
    public Byte convert(long numerator, long denominator) {
      long value = numerator / denominator;
      if ((value > Byte.MAX_VALUE) || (value < Byte.MIN_VALUE)) {
        throw new ArithmeticException("overflow");
      } else {
        return (byte) value;
      }
    }

    @Override
    public Byte convertExact(long numerator, long denominator) {
      if (denominator != 1) {
        throw new ArithmeticException("overflow");
      }
      return this.convert(numerator, denominator);
    }

  }

  static final class ConvertToBigInteger implements FastNumber6Converter {

    @Override
    public BigInteger convert(long numerator, long denominator) {
      long value = numerator / denominator;
      return BigInteger.valueOf(value);
    }

    @Override
    public BigInteger convertExact(long numerator, long denominator) {
      if (denominator != 1) {
        throw new ArithmeticException("overflow");
      }
      return this.convert(numerator, denominator);
    }

  }

  static final class ConvertToBigDecimal implements FastNumber6Converter {

    @Override
    public BigDecimal convert(long numerator, long denominator) {
      BigDecimal divisor = BigDecimal.valueOf(denominator);
      BigDecimal dividend = BigDecimal.valueOf(numerator);
      RoundingMode roundingMode = RoundingMode.HALF_EVEN;  //FIXME
      return dividend.divide(divisor, FastMoney6.SCALE, roundingMode);
    }

    @Override
    public BigDecimal convertExact(long numerator, long denominator) {
      BigDecimal divisor = BigDecimal.valueOf(denominator);
      BigDecimal dividend = BigDecimal.valueOf(numerator);
      return dividend.divide(divisor);
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
    CONVERTER_MAP.put(AtomicInteger.class, new ConvertToAtomictInteger());
    CONVERTER_MAP.put(AtomicLong.class, new ConvertToAtomictLong());
  }

}
