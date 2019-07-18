package com.github.marschall.acme.money;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

class CompressedCurrencyCodeTest {

  @Test
  void compress() {
    assertEquals(0, CompressedCurrencyCode.compress("AAA"));
    assertEquals(1, CompressedCurrencyCode.compress("BAA"));
    assertEquals(CompressedCurrencyCode.ARRAY_SIZE - 2, CompressedCurrencyCode.compress("YZZ"));
    assertEquals(CompressedCurrencyCode.ARRAY_SIZE - 1, CompressedCurrencyCode.compress("ZZZ"));
  }

  @Test
  void arraySize() {
    short max = CompressedCurrencyCode.compress("ZZZ");
    assertEquals(CompressedCurrencyCode.ARRAY_SIZE - 1, max); // index is 0 based, if index is 3 array size is 4
  }

  @Test
  void compressAllCodes() {
    Set<Short> compressed = new HashSet<>(13 * 13 * 13);
    for (int i = 'A'; i <= 'Z'; i++) {
      for (int j = 'A'; j <= 'Z'; j++) {
        for (int k = 'A'; k <= 'Z'; k++) {
          String alphaNumericCode = new String(new char[] {(char) i, (char) j, (char) k});
          short code = CompressedCurrencyCode.compress(alphaNumericCode);
          boolean added = compressed.add(code);
          assertTrue(added, () -> "Code already present: " + alphaNumericCode);
        }
      }
    }
  }

  @Test
  void compressDecompress() {
    for (int i = 'A'; i <= 'Z'; i++) {
      for (int j = 'A'; j <= 'Z'; j++) {
        for (int k = 'A'; k <= 'Z'; k++) {
          String alphaNumericCode = new String(new char[] {(char) i, (char) j, (char) k});
          short compressed = CompressedCurrencyCode.compress(alphaNumericCode);
          String decompressed = CompressedCurrencyCode.decompress(compressed);
          assertEquals(alphaNumericCode, decompressed);
        }
      }
    }
  }

  @Test
  void recreate() {
    for (int i = 'A'; i <= 'Z'; i++) {
      for (int j = 'A'; j <= 'Z'; j++) {
        for (int k = 'A'; k <= 'Z'; k++) {
          String alphaNumericCode = new String(new char[] {(char) i, (char) j, (char) k});
          short code = CompressedCurrencyCode.compress(alphaNumericCode);
          String recreated = CompressedCurrencyCode.recreate(code);
          assertEquals(alphaNumericCode, recreated);
        }
      }
    }
  }

}
