/**
 * Copyright (c) 2012, 2014, Credit Suisse (Anatole Tresch), Werner Keil and others by the @author tag.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.marschall.acme.money;

import java.math.RoundingMode;

import javax.money.CurrencyUnit;
import javax.money.MonetaryContext;
import javax.money.MonetaryContextBuilder;
import javax.money.NumberValue;

import org.javamoney.moneta.spi.AbstractAmountBuilder;

class FastMoney6AmountBuilder extends AbstractAmountBuilder<FastMoney6>{

  static final MonetaryContext DEFAULT_CONTEXT =
      MonetaryContextBuilder.of(FastMoney6.class).setPrecision(19).setMaxScale(FastMoney6.SCALE).setFixedScale(true)
      .set(RoundingMode.HALF_EVEN).build();
  static final MonetaryContext MAX_CONTEXT = DEFAULT_CONTEXT;

  @Override
  protected FastMoney6 create(Number number, CurrencyUnit currency, MonetaryContext monetaryContext){
    return FastMoney6.of(number, currency);
  }

  @Override
  public Class<FastMoney6> getAmountType(){
    return FastMoney6.class;
  }

  @Override
  public NumberValue getMaxNumber(){
    return FastMoney6.MAX_VALUE.getNumber();
  }

  @Override
  public NumberValue getMinNumber(){
    return FastMoney6.MIN_VALUE.getNumber();
  }

  @Override
  protected MonetaryContext loadDefaultMonetaryContext(){
    return DEFAULT_CONTEXT;
  }

  @Override
  protected MonetaryContext loadMaxMonetaryContext(){
    return MAX_CONTEXT;
  }

}
