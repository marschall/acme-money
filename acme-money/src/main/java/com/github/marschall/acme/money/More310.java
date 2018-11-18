package com.github.marschall.acme.money;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class More310 {

  public static void main(String[] args) {
String localDateTime = "2018-11-16 18:02:00";

TimeZone.setDefault(TimeZone.getTimeZone("America/Los_Angeles"));
Instant instant1 = Timestamp.valueOf(localDateTime).toInstant();
long epochMilli1 = instant1.toEpochMilli();

TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
Instant instant2 = Timestamp.valueOf(localDateTime).toInstant();
long epochMilli2 = instant2.toEpochMilli();

System.out.printf("epochMilli1: %d%n", epochMilli1);
System.out.printf("epochMilli2: %d%n", epochMilli2);
System.out.println("same instant: " + instant1.equals(instant2));
  }

}
