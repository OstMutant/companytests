package org.ost.test.text;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

public interface TextCalculator {
  Integer AMOUNTWORDS_DEFAULT = 0;
  Boolean ISCHECKCASE_DEFAULT = Boolean.FALSE;
  String ROWPATTERN_DEFAULT = "%1$s=%2$d";

  @SuppressWarnings("serial")
  public static class LoadFileException extends Exception {
    public LoadFileException(String message, Throwable cause) {
      super(message, cause);
    }

    public LoadFileException(String message) {
      super(message);
    }
  }

  public static void calculate(String path, Integer amountWords, Boolean isCheckCase, String rowPattern) throws LoadFileException {
    Integer amountWordsLoc = amountWords == null ? AMOUNTWORDS_DEFAULT : amountWords;
    Boolean isCheckCaseLoc = isCheckCase == null ? ISCHECKCASE_DEFAULT : isCheckCase;
    String rowPatternLoc = rowPattern == null ? ROWPATTERN_DEFAULT : rowPattern;
    
    Map<String, Integer> sortedWordsMap = new TreeMap<String, Integer>();
    try (Stream<String> fileStream = Files.lines(Paths.get(path), StandardCharsets.UTF_8)) {
      fileStream.map((line) -> {
        line = line.replaceAll("[\\.,]", " ");
        if (!isCheckCaseLoc)
          line = line.toLowerCase();
        return line.split("\\s+");
      }).forEach(words -> {
        Arrays.stream(words).forEach(word -> sortedWordsMap.merge(word, 1, (a, b) -> a + b));
      });
    } catch (IOException e) {
      throw new LoadFileException("File is not loaded", e);
    }

    Map<String, Integer> sortedWordsMapByCount = new LinkedHashMap<>();
    sortedWordsMap.entrySet().stream().filter(val -> val.getValue().intValue() >= amountWordsLoc.intValue())
        .sorted(Map.Entry.<String, Integer> comparingByValue().reversed()).forEachOrdered(entry -> sortedWordsMapByCount.put(entry.getKey(), entry.getValue()));
    sortedWordsMapByCount.forEach((k, v) -> System.out.printf(rowPatternLoc + System.lineSeparator(), k, v));
  }
}
