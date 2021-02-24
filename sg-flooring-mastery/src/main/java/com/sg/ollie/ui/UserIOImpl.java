/*
 * Implementation of UserIO interface
 */
package com.sg.ollie.ui;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 *
 * @author olive
 */
@Component
public class UserIOImpl implements com.sg.ollie.ui.UserIO {

    Scanner sc = new Scanner(System.in);

    @Override
    public void print(String message) {
        System.out.println(message);
    }

    @Override
    public String readString(String prompt) {
        System.out.println(prompt);
        return sc.nextLine();
    }

    @Override
    public int readInt(String prompt) {
        System.out.println(prompt);
        String stringInput;
        int input = 0;

        while (true) {
            try {
                stringInput = sc.nextLine();
                input = Integer.parseInt(stringInput);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Enter one of the options");
                break;
            }
        }
        return input;
    }

    @Override
    public int readInt(String prompt, int min, int max) {
        System.out.println(prompt);
        String stringInput;
        int input = 0;

        while (true) {
            try {
                stringInput = sc.nextLine();
                input = Integer.parseInt(stringInput);
                if (input >= min && input <= max) {
                    break;
                } else {
                    System.out.println("Integer must be between " + min + " and " + max);
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter an integer");
                break;
            }
        }
        return input;
    }

    @Override
    public double readDouble(String prompt) {
        System.out.println(prompt);
        String stringInput;
        double input = 0;

        while (true) {
            try {
                stringInput = sc.nextLine();
                input = Double.parseDouble(stringInput);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Enter a double");
                break;
            }
        }
        return input;
    }

    @Override
    public double readDouble(String prompt, double min, double max) {
        System.out.println(prompt);
        String stringInput;
        double input = 0;

        while (true) {
            try {
                stringInput = sc.nextLine();
                input = Double.parseDouble(stringInput);
                if (input >= min && input <= max) {
                    break;
                } else {
                    System.out.println("Double must be between " + min + " and " + max);
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter a double");
                break;
            }
        }
        return input;
    }

    @Override
    public float readFloat(String prompt) {
        System.out.println(prompt);
        String stringInput;
        float input = 0;

        while (true) {
            try {
                stringInput = sc.nextLine();
                input = Float.parseFloat(stringInput);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Enter a float");
                break;
            }
        }
        return input;
    }

    @Override
    public float readFloat(String prompt, float min, float max) {
        System.out.println(prompt);
        String stringInput;
        float input = 0;

        while (true) {
            try {
                stringInput = sc.nextLine();
                input = Float.parseFloat(stringInput);
                if (input >= min && input <= max) {
                    break;
                } else {
                    System.out.println("Float must be between " + min + " and " + max);
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter a float");
                break;
            }
        }
        return input;
    }

    @Override
    public long readLong(String prompt) {
        System.out.println(prompt);
        String stringInput;
        long input = 0;

        while (true) {
            try {
                stringInput = sc.nextLine();
                input = Long.parseLong(stringInput);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Enter a long");
                break;
            }
        }
        return input;
    }

    @Override
    public long readLong(String prompt, long min, long max) {
        System.out.println(prompt);
        String stringInput;
        long input = 0;

        while (true) {
            try {
                stringInput = sc.nextLine();
                input = Long.parseLong(stringInput);
                if (input >= min && input <= max) {
                    break;
                } else {
                    System.out.println("Long must be between " + min + " and " + max);
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter a long");
                break;
            }
        }
        return input;
    }

    @Override
    public BigDecimal readCurrency(String prompt) {
        System.out.println(prompt);
        String stringInput;
        long longInput;
        BigDecimal input = null;

        while (true) {
            try {
                stringInput = sc.nextLine();
                longInput = Long.parseLong(stringInput);
                input = new BigDecimal(Long.toString(longInput));
                break;
            } catch (NumberFormatException e) {
                System.out.println("Enter a decimal");
                break;
            }
        }
        return input;
    }

    @Override
    public BigDecimal readCurrency(String prompt, long min, long max) {
        System.out.println(prompt);
        String stringInput;
        long longInput;
        BigDecimal input;

        while (true) {
            try {
                stringInput = sc.nextLine();
                longInput = Long.parseLong(stringInput);
                if (longInput >= min && longInput <= max) {
                    input = new BigDecimal(Long.toString(longInput));
                    break;
                } else {
                    System.out.println("Value of decimal must be between " + min + " and " + max);
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter a decimal");
            }
        }
        return input;
    }

    @Override
    public String readLocalDate(String prompt) {
        LocalDate ld = null;
        while (true) {
            try {
                String ldString = readString(prompt);
                ld = LocalDate.parse(ldString, DateTimeFormatter.ofPattern("dd/MM/uuuu"));
                break;
            }
            catch (DateTimeParseException e) {
                System.out.println("Enter a valid date");
            }
        }
        return ld.format(DateTimeFormatter.ofPattern("MMdduuuu"));
    }

    @Override
    public String readFutureLocalDate(String prompt) {
        LocalDate ld = null;
        while (true) {
            try {
                String ldString = readString(prompt);
                ld = LocalDate.parse(ldString, DateTimeFormatter.ofPattern("dd/MM/uuuu"));
                if (ld.isBefore(LocalDate.now().plusDays(1))) {
                    System.out.println("Enter a date from tomorrow onwards");
                    continue;
                }
                break;
            }
            catch (DateTimeParseException e) {
                System.out.println("Enter a valid date");
            }
        }
        return ld.format(DateTimeFormatter.ofPattern("MMdduuuu"));
    }
}