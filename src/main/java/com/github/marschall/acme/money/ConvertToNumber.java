package com.github.marschall.acme.money;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

enum ConvertToNumber {

  INSTANCE;

  public static <T extends Number> T of(Class<T> numberType, long numerator, long denominator) {
    return INSTANCE.convert(numberType, numerator, denominator);
  }
  
  public static <T extends Number> T ofExact(Class<T> numberType, long numerator, long denominator) {
    return INSTANCE.convertExact(numberType, numerator, denominator);
  }
  
  public <T extends Number> T convert(Class<T> numberType, long numerator, long denominator) {
    FractionConverter<T> converter = getConverter(numberType);
    return converter.convert(numerator, denominator);
  }
  
  public <T extends Number> T convertExact(Class<T> numberType, long numerator, long denominator) {
    FractionConverter<T> converter = getConverter(numberType);
    return converter.convertExact(numerator, denominator);
  }

  private <T extends Number> FractionConverter<T> getConverter(Class<T> numberType) {
    @SuppressWarnings("unchecked")
    FractionConverter<T> converter = this.converterMap.get(numberType);
    if (converter == null) {
      new IllegalArgumentException("Unsupported numeric type: " + numberType);
    }
    return converter;
  }

  interface FractionConverter<N extends Number> {
    N convert(long numerator, long denominator);
    N convertExact(long numerator, long denominator);
  }

  static final class ConvertToDouble implements FractionConverter<Double> {

    @Override
    public Double convert(long numerator, long denominator) {
      return (double) numerator / (double) denominator;
    }

    @Override
    public Double convertExact(long numerator, long denominator) {
      Double converted = convert(numerator, denominator);
      if ((converted * denominator / numerator) == 1.0d) {
        return converted;
      } else {
        throw new ArithmeticException("overflow");
      }
    }

  }

  static final class ConvertToFloat implements FractionConverter<Float> {

    @Override
    public Float convert(long numerator, long denominator) {
      return (float) numerator / (float) denominator;
    }

    @Override
    public Float convertExact(long numerator, long denominator) {
      Float converted = convert(numerator, denominator);
      if ((converted * denominator / numerator) == 1.0f) {
        return converted;
      } else {
        throw new ArithmeticException("overflow");
      }
    }

  }

  static final class ConvertToLong implements FractionConverter<Long> {

    @Override
    public Long convert(long numerator, long denominator) {
      return numerator / denominator;
    }

    @Override
    public Long convertExact(long numerator, long denominator) {
      if (denominator != 1) {
        throw new ArithmeticException("overflow");
      }
      return convert(numerator, denominator);
    }

  }

  static final class ConvertToAtomictLong implements FractionConverter<AtomicLong> {

    @Override
    public AtomicLong convert(long numerator, long denominator) {
      return new AtomicLong(numerator / denominator);
    }

    @Override
    public AtomicLong convertExact(long numerator, long denominator) {
      if (denominator != 1) {
        throw new ArithmeticException("overflow");
      }
      return convert(numerator, denominator);

    }

  }

  static final class ConvertToInteger implements FractionConverter<Integer> {

    @Override
    public Integer convert(long numerator, long denominator) {
      long value = numerator / denominator;
      if (value > Integer.MAX_VALUE || value < Integer.MIN_VALUE) {
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
      return convert(numerator, denominator);
    }

  }

  static final class ConvertToAtomictInteger implements FractionConverter<AtomicInteger> {

    @Override
    public AtomicInteger convert(long numerator, long denominator) {
      long value = numerator / denominator;
      if (value > Integer.MAX_VALUE || value < Integer.MIN_VALUE) {
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
      return convert(numerator, denominator);
    }

  }

  static final class ConvertToShort implements FractionConverter<Short> {

    @Override
    public Short convert(long numerator, long denominator) {
      long value = numerator / denominator;
      if (value > Short.MAX_VALUE || value < Short.MIN_VALUE) {
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
      return convert(numerator, denominator);
    }

  }
  
  static final class ConvertToByte implements FractionConverter<Byte> {
    
    @Override
    public Byte convert(long numerator, long denominator) {
      long value = numerator / denominator;
      if (value > Byte.MAX_VALUE || value < Byte.MIN_VALUE) {
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
      return convert(numerator, denominator);
    }
    
  }
  
  static final class ConvertToBigInteger implements FractionConverter<BigInteger> {
    
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
      return convert(numerator, denominator);
    }
    
  }
  
  static final class ConvertToBigDecimal implements FractionConverter<BigDecimal> {
    
    @Override
    public BigDecimal convert(long numerator, long denominator) {
      BigDecimal divisor = BigDecimal.valueOf(denominator);
      BigDecimal dividend = BigDecimal.valueOf(numerator);
      int scale = 6;
      RoundingMode roundingMode = RoundingMode.HALF_EVEN;
      return dividend.divide(divisor, scale, roundingMode);
    }
    
    @Override
    public BigDecimal convertExact(long numerator, long denominator) {
      BigDecimal divisor = BigDecimal.valueOf(denominator);
      BigDecimal dividend = BigDecimal.valueOf(numerator);
      return dividend.divide(divisor);
    }
    
  }
  
  private Map<Class<? extends Number>, FractionConverter> converterMap;

  {
    converterMap = new HashMap<>(10);
    converterMap.put(BigDecimal.class, new ConvertToBigDecimal());
    converterMap.put(BigInteger.class, new ConvertToBigInteger());
    converterMap.put(Float.class, new ConvertToFloat());
    converterMap.put(Double.class, new ConvertToDouble());
    converterMap.put(Long.class, new ConvertToLong());
    converterMap.put(Integer.class, new ConvertToInteger());
    converterMap.put(Short.class, new ConvertToShort());
    converterMap.put(Byte.class, new ConvertToByte());
    converterMap.put(AtomicInteger.class, new ConvertToAtomictInteger());
    converterMap.put(AtomicLong.class, new ConvertToAtomictLong());
  }

}
