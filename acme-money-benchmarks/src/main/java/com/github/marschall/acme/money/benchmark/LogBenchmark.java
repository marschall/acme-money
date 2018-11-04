package com.github.marschall.acme.money.benchmark;

import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openjdk.jmh.annotations.Mode.Throughput;
import static org.openjdk.jmh.annotations.Scope.Benchmark;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;;

@Warmup(iterations = 5, time = 1, timeUnit = SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = SECONDS)
@Fork(1)
@BenchmarkMode(Throughput)
@OutputTimeUnit(MICROSECONDS)
@State(Benchmark)
public class LogBenchmark {

  @Param({"1", "10", "100", "1000", "10000", "100000", "1000000", "10000000", "100000000", "1000000000"})
  public int i;

  @Benchmark
  public int lenghtIf() {
    return lenghtIf(this.i);
  }

  @Benchmark
  public int lenghtLoop() {
    return lenghtLoop(this.i);
  }

  private static int lenghtIf(int i) {
    if (i >= 100_000) {
      // 6 or more
      if (i >= 10_000_000) {
        // 8 or more
        if (i >= 1_000_000_000) {
          return 10;
        } else if (i >= 100_000_000) {
          return 9;
        } else {
          return 8;
        }
      } else {
        // 6 or 7
        if (i >= 1_000_000) {
          return 7;
        } else {
          return 6;
        }
      }
    } else {
      // 5 or less
      if (i >= 100) {
        // 3 or 4 or 5
        if (i >= 10_000) {
          return 5;
        } else if (i >= 1_000) {
          return 4;
        } else {
          return 3;
        }
      } else {
        // 1 or 2
        if (i >= 10) {
          return 2;
        } else {
          return 1;
        }
      }
    }
  }

  private static int lenghtLoop(int i) {
    int maxVal = 10;
    for (int j = 1; j <= 10; j++) {
      if (i < maxVal) {
        return j;
      }
      maxVal *= 10;
    }
    return 10;
  }

}
