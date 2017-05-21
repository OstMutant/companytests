package org.ost.test.text;

import org.ost.test.text.TextCalculator.LoadFileException;

public class WordCount {
  public static final String HELP_STR = "-----------------------------------" + System.lineSeparator() + "WordCount arg0 arg1" + System.lineSeparator()
      + "arg0 - “word – count”  pairs ordered by count in descending order. " + System.lineSeparator()
      + "arg1 - Exactly N pairs printed out if the amount of unique words is greater or equal to N" + System.lineSeparator()
      + "Example: WordCount c:/lyrics.txt 3" + System.lineSeparator() + "-----------------------------------";

  public static void main(String... args) {
    if (args.length == 0) {
      System.out.println(HELP_STR);
      return;
    }
    String path = null;
    Integer amountWords = null;
    Boolean isCheckCase = null;
    String rowPattern = null;
    int i = 0;
    for (String arg : args) {
      switch (i) {
      case 0: {
        path = arg;
        break;
      }
      case 1: {
        try {
          amountWords = Integer.parseInt(arg);
        } catch (Exception e) {
          System.out.println("Parameter: 'amount of words' is incorrect");
          return;
        }
        break;
      }
      case 2: {
        isCheckCase = Boolean.parseBoolean(arg);
        break;
      }
      case 3: {
        rowPattern = arg;
        break;
      }
      }
      i++;
    }

    if (path == null || amountWords == null) {
      System.out.println("Parameters: 'path' and 'amount of words' is obligatory to fill");
      return;
    }

    try {
      TextCalculator.calculate(path, amountWords, isCheckCase, rowPattern);
    } catch (LoadFileException e) {
      e.printStackTrace();
    } catch (Exception e) {
      System.out.println(System.lineSeparator() + "!!! Something terrible happened with the application !!!");
    }
  }
}
