package aoc2023;

class Day01 {
  public static long part1(String input) {
    String[] lines = input.split("\n");
    long total = 0;
    for (var line : lines) {
      Character first = null;
      Character last = null;
      for (int i = 0; i < line.length(); i++) {
        char c = line.charAt(i);
        if (Character.isDigit(c)) {
          if (first == null)
            first = c;
          last = c;
        }
      }
      if (first != null) {
        total += (first - '0') * 10 + (last - '0');
      }
    }
    return total;
  }

  public static long part2(String input) {
    String[] lines = input.split("\n");
    long total = 0;
    for (var line : lines) {
      Character first = null;
      Character last = null;
      for (int i = 0; i < line.length(); i++) {
        Character d = getDigit(line, i);
        if (d != null) {
          if (first == null)
            first = d;
          last = d;
        }
      }
      if (first != null) {
        total += (first - '0') * 10 + (last - '0');
      }
    }
    return total;
  }

  private static final String[] DIGIT_WORDS = {
      "zero", "one", "two", "three", "four",
      "five", "six", "seven", "eight", "nine"
  };

  static Character getDigit(String line, int idx) {
    char c = line.charAt(idx);
    if (Character.isDigit(c))
      return c;

    for (int digit = 0; digit < DIGIT_WORDS.length; digit++) {
      if (line.startsWith(DIGIT_WORDS[digit], idx)) {
        return (char) ('0' + digit);
      }
    }

    return null;
  }

}
