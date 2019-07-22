package com.github.marschall.acme.money;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.money.CurrencyUnit;
import javax.xml.stream.XMLStreamException;

import com.github.marschall.acme.money.IsoCurrencyParser.ParsedCurrrency;

final class IsoCurrencyCompressor {

  private static final int LETTERS_IN_ALPHABET = 26;

  private IsoCurrencyCompressor() {
    throw new AssertionError("not instantiable");
  }
  
  static Map<Short, CurrencyUnit> parse() {
    Map<String, ParsedCurrrency> parsedMap;
    try {
      parsedMap = IsoCurrencyParser.parseToMap();
    } catch (IOException | XMLStreamException e) {
      throw new RuntimeException("could not load currencies", e);
    }
    
    Map<Short, CurrencyUnit> currencyMap = new HashMap<>(parsedMap.size());
    
    for (Entry<String, ParsedCurrrency> entry : parsedMap.entrySet()) {
      String currencyCode = entry.getKey();
      short key = compressCurrencyCode(currencyCode);
      IsoCurrency currency = new IsoCurrency(currencyCode, key, entry.getValue().currencyNumber, entry.getValue().minorUnits);
      currencyMap.put(key, currency);
    }
    return currencyMap;
  }

  static short compressCurrencyCode(String currencyCode) {
    if (currencyCode.length() != 3) {
      throw invalidFormat();
    }
    int index = toFactor(currencyCode.charAt(0))
        + toFactor(currencyCode.charAt(1)) * LETTERS_IN_ALPHABET
        + toFactor(currencyCode.charAt(2)) * LETTERS_IN_ALPHABET * LETTERS_IN_ALPHABET;
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

  static String decompressCurrencyCode(short compressed) {
    int char1 = compressed % LETTERS_IN_ALPHABET;
    int char2 = (compressed / LETTERS_IN_ALPHABET)  % LETTERS_IN_ALPHABET;
    int char3 = (compressed / LETTERS_IN_ALPHABET / LETTERS_IN_ALPHABET);

    return new String(new char[] {(char) (char1 + 'A'), (char) (char2 + 'A'), (char) (char3 + 'A')});
  }


}
