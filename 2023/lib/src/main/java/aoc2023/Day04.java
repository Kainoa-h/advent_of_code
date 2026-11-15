package aoc2023;

import java.util.Arrays;
import java.util.BitSet;

class Day04 {

  static int getNumber(String str, int offset) {
    int t = str.charAt(offset) - '0';
    int o = str.charAt(offset + 1) - '0';
    return (t < 0 || t > 9 ? 0 : t * 10) + o;
  }

  static long part1(String input) {
    var lines = input.split("\n");
    long total = 0;
    for (var line : lines) {
      int matches = 0;

      BitSet winNums = new BitSet(100);
      var winNumStart = line.indexOf(':') + 2;
      var winNumEnd = line.indexOf('|') - 1;
      for (int i = winNumStart; i < winNumEnd; i += 3) {
        winNums.set(getNumber(line, i));
      }
      for (int i = winNumEnd + 3; i < line.length(); i += 3) {
        matches += winNums.get(getNumber(line, i)) ? 1 : 0;
      }

      total += matches == 0 ? 0 : 1 << (matches - 1);
    }
    return total;
  }

  static long part2(String input) {
    var lines = input.split("\n");
    long total = 0;
    int[] copiesDiffArray = new int[lines.length + 1];
    int running = 0;
    for (var x = 0; x < lines.length; x++) {
      var line = lines[x];
      running += copiesDiffArray[x];
      var copies = 1 + running;
      total += copies;

      int matches = 0;

      BitSet winNums = new BitSet(100);
      var winNumStart = line.indexOf(':') + 2;
      var winNumEnd = line.indexOf('|') - 1;
      for (int i = winNumStart; i < winNumEnd; i += 3) {
        winNums.set(getNumber(line, i));
      }
      for (int i = winNumEnd + 3; i < line.length(); i += 3) {
        matches += winNums.get(getNumber(line, i)) ? 1 : 0;
      }
      
      var start = x + 1;
      var end = x + matches +1;
      copiesDiffArray[start] += copies;
      copiesDiffArray[end] -= copies;
    }
    return total;
  }
}
