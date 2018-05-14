package com.cheerbuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Конвертирует валюты
 */
public class Converter {

    private String valute1;
    private String valute2;
    private Float value1;
    private Float value2;
    private Map<String,Float> moneyMap;
    private Float coeff;
    private List<Float> convertedMoney;

    public Converter(String valute1, String valute2, Float value1,
                     Float value2, Map<String, Float> moneyMap) {
        this.valute1 = valute1;
        this.valute2 = valute2;
        this.value1 = value1;
        this.value2 = value2;
        this.moneyMap = moneyMap;
    }

    /**/
    public List<Float> getConvertedMoney() {
        return convertedMoney;
    }

    /**/
    public void convert() {
        coeff = coeffCalc(moneyMap,valute1,valute2);
        valuteCalc();
    }



    /*Возвращает коэффициент (отношение) для двух валют*/
    private  Float coeffCalc(Map<String,Float> moneyMap,
                                   String valute1,
                                   String valute2) {
        float k = 0; //Коэффициент (котировка)

        k = moneyMap.get(valute1)
            / moneyMap.get(valute2);

        return k;
    }

    /*Получает массив из значений двух пересчитанных валют*/
    private  void valuteCalc() {

        convertedMoney = new ArrayList<>();

        //if ((valute1 != "RUB") && (valute2 != "RUB")) {
            if ((value1 == null) && (value2 != null)) {
                value1 =  value2 / coeff;
            }

            if ((value1 != null) && (value2 == null)) {
                value2 = value1 * coeff;
            }
        //} else {
        //    if ((value1 == null) && (value2 != null)) {
        //        value1 =  value2 / coeff ;
        //    }

        //    if ((value1 != null) && (value2 == null)) {
        //        value2 = value1 * coeff;
        //    }
        //}


        convertedMoney.add(value1);
        convertedMoney.add(value2);

    }








}
