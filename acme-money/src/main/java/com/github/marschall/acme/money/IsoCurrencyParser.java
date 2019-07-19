package com.github.marschall.acme.money;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

final class IsoCurrencyParser {

  // https://www.currency-iso.org/dam/downloads/lists/list_one.xml


  private static void parse() throws IOException, XMLStreamException {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    try (InputStream inputStream = IsoCurrencyParser.class.getClassLoader().getResourceAsStream("list_one.xml");
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
      XMLStreamReader streamReader = factory.createXMLStreamReader(bufferedInputStream);
      parseDocument(streamReader);
    }

  }


  private static void parseDocument(XMLStreamReader streamReader) throws XMLStreamException {
    while (streamReader.hasNext()) {
      int event = streamReader.next();
      switch (event) {
        case XMLStreamConstants.START_ELEMENT:
          String localName = streamReader.getLocalName();
          if (localName.equals("ISO_4217")) {
            parseIso4217(streamReader);
          } else {
            throw new IllegalArgumentException("<ISO_4217> expected");
          }
          break;
        case XMLStreamConstants.COMMENT:
        case XMLStreamConstants.SPACE:
        case XMLStreamConstants.PROCESSING_INSTRUCTION:
          // ignore
          break;
        case XMLStreamConstants.END_DOCUMENT:
          return;
  
        default:
          throw new IllegalStateException("unknown event: " + event);
      }
    }
  }

  private static void parseIso4217(XMLStreamReader streamReader) throws XMLStreamException {
    while (streamReader.hasNext()) {

      int event = streamReader.nextTag();
      if (event == XMLStreamConstants.START_ELEMENT) {
        String localName = streamReader.getLocalName();
        if (localName.equals("CcyTbl")) {
          parseCurrencyTable(streamReader);
        } else {
          throw new IllegalArgumentException("<CcyTbl> expected");
        }
      } else if (event == XMLStreamConstants.END_ELEMENT) {
        return;
      } else {
        throw new IllegalStateException("unknown event: " + event);
      }
    }
  }

  private static void parseCurrencyTable(XMLStreamReader streamReader) throws XMLStreamException {
    while (streamReader.hasNext()) {

      int event = streamReader.nextTag();
      if (event == XMLStreamConstants.START_ELEMENT) {
        String localName = streamReader.getLocalName();
        if (localName.equals("CcyNtry")) {
          parseCurrencyEntry(streamReader);
        } else {
          throw new IllegalArgumentException("<CcyNtry> expected");
        }
      } else if (event == XMLStreamConstants.END_ELEMENT) {
        return;
      } else {
        throw new IllegalStateException("unknown event: " + event);
      }
    }
  }

  private static void parseCurrencyEntry(XMLStreamReader streamReader) throws XMLStreamException {
    String currencyCode = null;
    int currencyNumber = -1;
    int minorUnits = -1;
    while (streamReader.hasNext()) {
      int event = streamReader.nextTag();
      if (event == XMLStreamConstants.START_ELEMENT) {
        String localName = streamReader.getLocalName();
        switch (localName) {
        case "CtryNm":
          parseCountryName(streamReader);
          break;
        case "CcyNm":
          parseCurrencyName(streamReader);
          break;
        case "Ccy":
          currencyCode = parseCurrencyCode(streamReader);
          break;
        case "CcyNbr":
          currencyNumber = parseCurrencyNumber(streamReader);
          break;
        case "CcyMnrUnts":
          minorUnits = parseMinorUnits(streamReader);
          break;
        default:
          throw new IllegalArgumentException("unknown tag");
        }
      } else if (event == XMLStreamConstants.END_ELEMENT) {
        // ANTARCTICA
        // PALESTINE, STATE OF
        // SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS
        if (currencyCode != null) {
          System.out.println(currencyCode + '(' + currencyNumber + ')' + minorUnits);
        }
        return;
      } else {
        throw new IllegalStateException("unknown event: " + event);
      }
    }
  }

  private static void parseCountryName(XMLStreamReader streamReader) throws XMLStreamException {
    parseVoid(streamReader);
  }

  private static void parseCurrencyName(XMLStreamReader streamReader) throws XMLStreamException {
    parseVoid(streamReader);
  }

  private static String parseCurrencyCode(XMLStreamReader streamReader) throws XMLStreamException {
    return parseTextValue(streamReader);
  }

  private static int parseCurrencyNumber(XMLStreamReader streamReader) throws XMLStreamException {
    return parseIntValue(streamReader);
  }

  private static int parseMinorUnits(XMLStreamReader streamReader) throws XMLStreamException {
    int event = streamReader.next();
    if (event != XMLStreamConstants.CHARACTERS) {
      throw new IllegalStateException("unknown event: " + event);
    }
    String text = streamReader.getText();
    int i;
    if (text.equals("N.A.")) {
      i = -1;
    } else {
      i = Integer.parseInt(text);
    }
    event = streamReader.nextTag();
    if (event != XMLStreamConstants.END_ELEMENT) {
      throw new IllegalStateException("end element expected");
    }
    return i;
  }


  private static void parseVoid(XMLStreamReader streamReader) throws XMLStreamException {
    while (streamReader.hasNext()) {
      int event = streamReader.next();
      switch (event) {
        case XMLStreamConstants.CHARACTERS:
        case XMLStreamConstants.COMMENT:
        case XMLStreamConstants.CDATA:
        case XMLStreamConstants.SPACE:
          // ignore
          break;
        case XMLStreamConstants.END_ELEMENT:
          return;
        default:
          throw new IllegalStateException("unknown event: " + event);
      }
    }
  }

  private static String parseTextValue(XMLStreamReader streamReader) throws XMLStreamException {
    int event = streamReader.next();
    if (event != XMLStreamConstants.CHARACTERS) {
      throw new IllegalStateException("unknown event: " + event);
    }
    String text = streamReader.getText();
    event = streamReader.nextTag();
    if (event != XMLStreamConstants.END_ELEMENT) {
      throw new IllegalStateException("end element expected");
    }
    return text;
  }

  private static int parseIntValue(XMLStreamReader streamReader) throws XMLStreamException {
    int event = streamReader.next();
    if (event != XMLStreamConstants.CHARACTERS) {
      throw new IllegalStateException("unknown event: " + event);
    }
    String text = streamReader.getText();
    int i = Integer.parseInt(text);
    event = streamReader.nextTag();
    if (event != XMLStreamConstants.END_ELEMENT) {
      throw new IllegalStateException("end element expected");
    }
    return i;
  }


  public static void main(String[] args) throws IOException, XMLStreamException {
    parse();
  }

}
