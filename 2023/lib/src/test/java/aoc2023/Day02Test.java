package aoc2023;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.FieldSource;
import org.junit.jupiter.params.provider.Arguments;

import aoc2023.TestLoader;

import java.util.List;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class Day02Test {
  static List<Arguments> subsets = List.of(
      Arguments.of("All zeros", new int[] { 0, 0, 0 }, ""),
      Arguments.of("Missing red", new int[] { 0, 1, 1 }, "1 green, 1 blue"));

  @ParameterizedTest(name = "{0}")
  @FieldSource("subsets")
  void testParseSubsets(String name, int[] expected, String input) {
    assertArrayEquals(expected, Day02.parseSubset(input));
  }

  @Test
  void testPart1t1() throws IOException {
    String input = TestLoader.loadInput("/day02/test1.txt");
    assertEquals(8, Day02.part1(input));
  }

  @Test
  void testPart1Real() throws IOException {
    String input = TestLoader.loadInput("/day02/real.txt");
    System.out.println(Day02.part1(input));
  }

  @Test
  void testPart2t1() throws IOException {
    String input = TestLoader.loadInput("/day02/test1.txt");
    assertEquals(2286, Day02.part2(input));
  }

  @Test
  void testPart2Real() throws IOException {
    String input = TestLoader.loadInput("/day02/real.txt");
    System.out.println(Day02.part2(input));
  }
}
