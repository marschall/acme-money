package com.github.marschall.acme.money;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.money.NumberValue;

import org.javamoney.moneta.spi.ConvertBigDecimal;
import org.omg.CosNaming.IstringHelper;


enum ConvertFraction {
  /** Conversion from integral numeric types, short, int, long. */
  INTEGER {
    @Override
    Fraction getFraction(Number num) {
      return Fraction.of(num.longValue(), 1L);
    }
  },
  /** Conversion for floating point numbers. */
  FLUCTUAGE {
    @Override
    Fraction getFraction(Number num) {
      return new BigDecimal(num.toString());
    }
  },
  /** Conversion from BigInteger. */
  BIGINTEGER {
    @Override
    Fraction getFraction(Number num) {
      return new BigDecimal((BigInteger) num);
    }
  },
  /** Conversion from NumberValue. */
  NUMBERVALUE {
    @Override
    Fraction getFraction(Number num) {
      BigDecimal result = ((NumberValue)num).numberValue(BigDecimal.class);
      return isScaleZero(result);
    }
  },
  /** Conversion from BigDecimal. */
  BIGDECIMAL {
    @Override
    Fraction getFraction(Number num) {
      BigDecimal result = ((BigDecimal)num);
      return isScaleZero(result);
    }
  },
  /** COnversion from BigDecimal, extended. */
  BIGDECIMAL_EXTENDS {
    @Override
    Fraction getFraction(Number num) {
      BigDecimal result = ((BigDecimal)num).stripTrailingZeros();
      return isScaleZero(result);
    }
  },
  /** Default conversion based on String, if everything else failed. */
  DEFAULT {
    @Override
    Fraction getFraction(Number num) {
      BigDecimal result = null;
      try {
        result = new BigDecimal(num.toString());
      } catch (NumberFormatException ignored) {
      }
      result = Optional.ofNullable(result).orElse(
          BigDecimal.valueOf(num.doubleValue()));
      return isScaleZero(result);
    }
  };


  abstract Fraction getFraction(Number num);

  static Fraction of(Number num) {
    Objects.requireNonNull(num, "Number is required.");
    if (num instanceof Fraction) {
      return (Fraction) num;
    }
    return factory(num).getFraction(num);
  }

  private static ConvertFraction factory(Number num) {
    if (INSTEGERS.contains(num.getClass())) {
      return INTEGER;
    }
    if (FLOATINGS.contains(num.getClass())) {
      return FLUCTUAGE;
    }
    if (num instanceof NumberValue) {
      return NUMBERVALUE;
    }
    if (BigDecimal.class.equals(num.getClass())) {
      return BIGDECIMAL;
    }
    if (num instanceof BigInteger) {
      return BIGINTEGER;
    }
    if (num instanceof BigDecimal) {
      return BIGDECIMAL_EXTENDS;
    }
    return DEFAULT;
  }

  private static List<Class<? extends Number>> INSTEGERS = Arrays.asList(
      Long.class, Integer.class, Short.class, Byte.class,
      AtomicLong.class, AtomicInteger.class);

  private static List<Class<? extends Number>> FLOATINGS = Arrays.asList(
      Float.class, Double.class);

  private static BigDecimal isScaleZero(BigDecimal result) {
    if (result.signum() == 0) {
      return BigDecimal.ZERO;
    }
    if (result.scale() > 0) {
      return result.stripTrailingZeros();
    }
    return result;
  }

}
