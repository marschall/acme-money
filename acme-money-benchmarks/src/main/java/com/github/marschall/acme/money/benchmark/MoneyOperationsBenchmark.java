package com.github.marschall.acme.money.benchmark;

import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openjdk.jmh.annotations.Mode.Throughput;
import static org.openjdk.jmh.annotations.Scope.Benchmark;

import java.math.BigDecimal;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import org.javamoney.moneta.FastMoney;
import org.javamoney.moneta.Money;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import com.github.marschall.acme.money.FastMoney6;
import com.github.marschall.acme.money.FastNumber6;

/**
 * Micro benchmarks for various money operations.
 *
 * <p>
 * Adapted from org.javamoney.moneta.PerformanceTest
 */
@Warmup(iterations = 5, time = 1, timeUnit = SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = SECONDS)
@Fork(3)
@BenchmarkMode(Throughput)
@OutputTimeUnit(MICROSECONDS)
@State(Benchmark)
public class MoneyOperationsBenchmark {

  private static final CurrencyUnit EURO = Monetary.getCurrency("EUR");
  private static final BigDecimal ONE_POINT_FIVE = BigDecimal.valueOf(15, 1);
  private static final FastNumber6 ONE_POINT_FIVE_FAST_NUMBER = FastNumber6.parse("1.5");

  private Money money1;
  private FastMoney fastMoney1;
  private FastMoney6 acmeMoney1;

  @Setup
  public void setup() {
    this.money1 = Money.of(BigDecimal.ONE, EURO);
    this.fastMoney1 = FastMoney.of(BigDecimal.ONE, EURO);
    this.acmeMoney1 = FastMoney6.of(BigDecimal.ONE, EURO);
  }

  @Benchmark
  public Money createMoneta() {
    return Money.of(BigDecimal.ONE, EURO);
  }

  @Benchmark
  public FastMoney createMonetaFast() {
    return FastMoney.of(BigDecimal.ONE, EURO);
  }

  @Benchmark
  public FastMoney6 createAcme() {
    return FastMoney6.of(BigDecimal.ONE, EURO);
  }

  @Benchmark
  public Money addMoneta() {
    return this.money1.add(this.fastMoney1);
  }

  @Benchmark
  public FastMoney addMonetaFast() {
    return this.fastMoney1.add(this.fastMoney1);
  }

  @Benchmark
  public FastMoney6 addAcme() {
    return this.acmeMoney1.add(this.acmeMoney1);
  }

  @Benchmark
  public Money subtractMoneta() {
    return this.money1.subtract(this.fastMoney1);
  }

  @Benchmark
  public FastMoney subtractMonetaFase() {
    return this.fastMoney1.subtract(this.fastMoney1);
  }

  @Benchmark
  public FastMoney6 subtractAcme() {
    return this.acmeMoney1.subtract(this.acmeMoney1);
  }

  @Benchmark
  public Money multiplyMoneta() {
    return this.money1.multiply(ONE_POINT_FIVE);
  }

  @Benchmark
  public FastMoney multiplyMonetaFast() {
    return this.fastMoney1.multiply(ONE_POINT_FIVE);
  }

  @Benchmark
  public FastMoney6 multiplyAcme() {
    return this.acmeMoney1.multiply(ONE_POINT_FIVE);
  }

  @Benchmark
  public FastMoney6 multiplyAcmeFastNumber() {
    return this.acmeMoney1.multiply(ONE_POINT_FIVE_FAST_NUMBER);
  }

  @Benchmark
  public Money divideMoneta() {
    return this.money1.divide(ONE_POINT_FIVE);
  }

  @Benchmark
  public FastMoney divideMonetaFast() {
    return this.fastMoney1.divide(ONE_POINT_FIVE);
  }

  @Benchmark
  public FastMoney6 divideAcme() {
    return this.acmeMoney1.divide(ONE_POINT_FIVE);
  }

  @Benchmark
  public FastMoney6 divideAcmeFastNumber() {
    return this.acmeMoney1.divide(ONE_POINT_FIVE_FAST_NUMBER);
  }

  @Benchmark
  public String toStringMoneta() {
    return this.money1.toString();
  }

  @Benchmark
  public String toStringMonetaFast() {
    return this.fastMoney1.toString();
  }

  @Benchmark
  public String toStringAcme() {
    return this.acmeMoney1.toString();
  }

}
