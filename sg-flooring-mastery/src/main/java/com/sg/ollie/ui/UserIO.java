/*
 * UserIO interface
 */
package com.sg.ollie.ui;

import java.math.BigDecimal;

/**
 *
 * @author olive
 */
public interface UserIO {

    void print(String message);

    String readString(String prompt);

    int readInt(String prompt);

    int readInt(String prompt, int min, int max);

    double readDouble(String prompt);

    double readDouble(String prompt, double min, double max);

    float readFloat(String prompt);

    float readFloat(String prompt, float min, float max);

    long readLong(String prompt);

    long readLong(String prompt, long min, long max);

    BigDecimal readCurrency(String prompt);

    BigDecimal readCurrency(String prompt, long min, long max);

    String readLocalDate(String prompt);

    String readFutureLocalDate(String prompt);
}