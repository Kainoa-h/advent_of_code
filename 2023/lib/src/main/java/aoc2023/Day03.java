package aoc2023;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Day03 {

  static boolean checkForSymbol(String[] lines, int r, int c) {
    var rows = lines.length;
    var cols = lines[0].length();
    if (r < 0 || r >= rows || c < 0 || c >= cols) {
      return false;
    }
    var ch = lines[r].charAt(c);
    return !Character.isDigit(ch) && ch != '.';
  }

  static long part1(String input) {
    var lines = input.split("\n");
    var rows = lines.length;
    var cols = lines[0].length();

    var result = 0;
    StringBuilder sb = new StringBuilder();
    for (int r = 0; r < rows; r++) {
      var isCurNumValid = false;
      for (int c = 0; c < cols; c++) {
        var ch = lines[r].charAt(c);
        if (Character.isDigit(ch)) {
          if (sb.isEmpty()) {
            isCurNumValid = checkForSymbol(lines, r, c - 1) ||
                checkForSymbol(lines, r - 1, c - 1) ||
                checkForSymbol(lines, r + 1, c - 1);
          }
          if (!isCurNumValid) {
            isCurNumValid = checkForSymbol(lines, r - 1, c) ||
                checkForSymbol(lines, r + 1, c);
          }
          sb.append(ch);
        } else if (!sb.isEmpty()) {
          if (isCurNumValid ||
              checkForSymbol(lines, r, c) ||
              checkForSymbol(lines, r - 1, c) ||
              checkForSymbol(lines, r + 1, c)) {
            result += Integer.parseInt(sb.toString());
          }
          sb.setLength(0);
        }
      }
      if (!sb.isEmpty() && isCurNumValid)
        result += Integer.parseInt(sb.toString());
      sb.setLength(0);
    }

    return result;
  }

  static String findGear(String[] lines, int r, int c) {
    var rows = lines.length;
    var cols = lines[0].length();
    if (r < 0 || r >= rows || c < 0 || c >= cols) {
      return null;
    }
    var ch = lines[r].charAt(c);
    return (ch == '*') ? r + "," + c : null;
  }

  static long part2(String input) {
    var lines = input.split("\n");
    var rows = lines.length;
    var cols = lines[0].length();

    long result = 0;
    Map<String, List<Integer>> gearMap = new HashMap<>();
    StringBuilder sb = new StringBuilder();
    for (int r = 0; r < rows; r++) {
      List<String> gears = new ArrayList<>();
      for (int c = 0; c < cols; c++) {
        var ch = lines[r].charAt(c);
        if (Character.isDigit(ch)) {
          if (sb.isEmpty()) {
            gears.add(findGear(lines, r, c - 1));
            gears.add(findGear(lines, r - 1, c - 1));
            gears.add(findGear(lines, r + 1, c - 1));
          }
          gears.add(findGear(lines, r + 1, c));
          gears.add(findGear(lines, r - 1, c));
          sb.append(ch);
        } else if (!sb.isEmpty()) {
          gears.add(findGear(lines, r, c));
          gears.add(findGear(lines, r - 1, c));
          gears.add(findGear(lines, r + 1, c));

          for (String gear : gears) {
            if (gear != null) {
              var list = gearMap.computeIfAbsent(gear, k -> new ArrayList<Integer>());
              list.add(Integer.parseInt(sb.toString()));
            }
          }
          gears.clear();
          sb.setLength(0);
        }
      }
      if (!sb.isEmpty()) {
        for (String gear : gears) {
          if (gear != null) {
            var list = gearMap.computeIfAbsent(gear, k -> new ArrayList<Integer>());
            list.add(Integer.parseInt(sb.toString()));
          }
        }
      }
      sb.setLength(0);
    }

    for (var kv : gearMap.entrySet()) {
      var v = kv.getValue();
      if (v.size() == 2) {
        result += v.get(0) * v.get(1);
      }
    }

    return result;
  }
}
