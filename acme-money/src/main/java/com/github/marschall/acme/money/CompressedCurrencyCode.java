package com.github.marschall.acme.money;

import java.util.concurrent.atomic.AtomicReferenceArray;

final class CompressedCurrencyCode {

  private static final int LETTERS_IN_ALPHABET = 26;

  static final int ARRAY_SIZE = LETTERS_IN_ALPHABET * LETTERS_IN_ALPHABET * LETTERS_IN_ALPHABET;

  private static final AtomicReferenceArray<String> CURRENCY_CODES = new AtomicReferenceArray<>(ARRAY_SIZE);

  static short compress(String currencyCode) {
    if (currencyCode.length() != 3) {
      throw invalidFormat();
    }
    int index = toFactor(currencyCode.charAt(0))
        + toFactor(currencyCode.charAt(1)) * LETTERS_IN_ALPHABET
        + toFactor(currencyCode.charAt(2)) * LETTERS_IN_ALPHABET * LETTERS_IN_ALPHABET;
    CURRENCY_CODES.compareAndSet(index, null, currencyCode);
    return (short) index;

  }

  private static int toFactor(char c) {
    if (c < 'A' || c > 'Z') {
      throw invalidFormat();
    }
    return (short) (c - 'A');
  }

  private static IllegalArgumentException invalidFormat() {
    return new IllegalArgumentException("currency code must be 3 letters A-Z");
  }

  static String decompress(short compressed) {
    if (compressed < 0 || compressed > ARRAY_SIZE) {
      throw new IllegalArgumentException("invalid compressed reference");
    }
    String currencyCode = CURRENCY_CODES.get(compressed);
    if (currencyCode == null) {
      return recreateAndStore(compressed);
    } else {
      return currencyCode;
    }
  }
  
  private static String recreateAndStore(short compressed) {
    String recreated = recreate(compressed);
    CURRENCY_CODES.compareAndSet(compressed, null, recreated);
    return recreated;
  }

  static String recreate(short compressed) {
    int char1 = compressed % LETTERS_IN_ALPHABET;
    int char2 = (compressed / LETTERS_IN_ALPHABET)  % LETTERS_IN_ALPHABET;
    int char3 = (compressed / LETTERS_IN_ALPHABET / LETTERS_IN_ALPHABET);

    return new String(new char[] {(char) (char1 + 'A'), (char) (char2 + 'A'), (char) (char3 + 'A')});
  }

}
