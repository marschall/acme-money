package com.github.marschall.acme.money;

import static com.github.marschall.acme.money.HasNoNumberValue.hasNoNumberValue;
import static com.github.marschall.acme.money.HasNoNumberValueExact.hasNoNumberValueExact;
import static com.github.marschall.acme.money.HasNumberValue.hasNumberValue;
import static com.github.marschall.acme.money.HasNumberValueExact.hasNumberValueExcat;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class FractionValueTest {

  @Test
  public void convertToInteger() {
    FractionValue value = new FractionValue(10, 1);
    assertThat(value, hasNumberValue(Integer.valueOf(10)));
    assertThat(value, hasNumberValueExcat(Integer.valueOf(10)));
    
    value = new FractionValue(5, 2);
    assertThat(value, hasNumberValue(Integer.valueOf(2)));
    assertThat(value, hasNoNumberValueExact(Integer.class));
    
    value = new FractionValue((long) Integer.MAX_VALUE + 1L, 1);
    assertThat(value, hasNoNumberValue(Integer.class));
    assertThat(value, hasNoNumberValueExact(Integer.class));
    
    value = new FractionValue((long) Integer.MIN_VALUE - 1L, 1);
    assertThat(value, hasNoNumberValue(Integer.class));
    assertThat(value, hasNoNumberValueExact(Integer.class));
  }

}
