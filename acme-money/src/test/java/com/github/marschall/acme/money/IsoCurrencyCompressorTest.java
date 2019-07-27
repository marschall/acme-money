package com.github.marschall.acme.money;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

class IsoCurrencyCompressorTest {

  @Test
  void compress() {
    assertEquals(0, IsoCurrencyCompressor.compressCurrencyCode("AAA"));
    assertEquals(1, IsoCurrencyCompressor.compressCurrencyCode("BAA"));
  }

  @Test
  void compressAllCodes() {
    Set<Short> compressed = new HashSet<>(13 * 13 * 13);
    for (int i = 'A'; i <= 'Z'; i++) {
      for (int j = 'A'; j <= 'Z'; j++) {
        for (int k = 'A'; k <= 'Z'; k++) {
          String alphaNumericCode = new String(new char[] {(char) i, (char) j, (char) k});
          short code = IsoCurrencyCompressor.compressCurrencyCode(alphaNumericCode);
          boolean added = compressed.add(code);
          assertTrue(added, () -> "Code already present: " + alphaNumericCode);
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
          short code = IsoCurrencyCompressor.compressCurrencyCode(alphaNumericCode);
          String recreated = IsoCurrencyCompressor.decompressCurrencyCode(code);
          assertEquals(alphaNumericCode, recreated);
        }
      }
    }
  }

}
