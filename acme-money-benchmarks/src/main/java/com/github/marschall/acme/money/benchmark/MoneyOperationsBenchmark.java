package com.github.marschall.acme.money.benchmark;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import org.javamoney.moneta.FastMoney;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import com.github.marschall.acme.money.FastMoney6;

/**
 * Micro benchmarks for various money operations.
 *
 * <p>
 * Adapted from org.javamoney.moneta.PerformanceTest
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class MoneyOperationsBenchmark {

  private static final CurrencyUnit EURO = Monetary.getCurrency("EUR");
  private static final BigDecimal ONE_POINT_FIVE = BigDecimal.valueOf(15, 1);


  private FastMoney fastMoney1;
  private FastMoney6 acmeMoney1;

  @Setup
  public void setup() {
    this.fastMoney1 = FastMoney.of(BigDecimal.ONE, EURO);
    this.acmeMoney1 = FastMoney6.of(BigDecimal.ONE, EURO);
  }

  @Benchmark
  public FastMoney createMoneta() {
    return FastMoney.of(BigDecimal.ONE, EURO);
  }

  @Benchmark
  public FastMoney6 createAcme() {
    return FastMoney6.of(BigDecimal.ONE, EURO);
  }

  @Benchmark
  public FastMoney addMoneta() {
    return this.fastMoney1.add(this.fastMoney1);
  }

  @Benchmark
  public FastMoney6 addAcme() {
    return this.acmeMoney1.add(this.acmeMoney1);
  }

  @Benchmark
  public FastMoney subtractMoneta() {
    return this.fastMoney1.subtract(this.fastMoney1);
  }

  @Benchmark
  public FastMoney6 subtractAcme() {
    return this.acmeMoney1.subtract(this.acmeMoney1);
  }

  @Benchmark
  public FastMoney multiplyMoneta() {
    return this.fastMoney1.multiply(ONE_POINT_FIVE);
  }

  @Benchmark
  public FastMoney6 multiplyAcme() {
    return this.acmeMoney1.multiply(ONE_POINT_FIVE);
  }

  @Benchmark
  public FastMoney divideMoneta() {
    return this.fastMoney1.divide(ONE_POINT_FIVE);
  }

  @Benchmark
  public FastMoney6 divideAcme() {
    return this.acmeMoney1.divide(ONE_POINT_FIVE);
  }

}
