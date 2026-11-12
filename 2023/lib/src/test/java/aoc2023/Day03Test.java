package aoc2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.FieldSource;
import org.junit.jupiter.params.provider.Arguments;

class Day03Test {

  static List<Arguments> symbolTests = List.of(
    Arguments.of("star", new String[]{"*"}, 0, 0, true),
    Arguments.of("equals", new String[]{"="}, 0, 0, true),
    Arguments.of("minus", new String[]{"-"}, 0, 0, true),
    Arguments.of("plus", new String[]{"+"}, 0, 0, true),
    Arguments.of("divide", new String[]{"/"}, 0, 0, true),
    Arguments.of("caret", new String[]{"^"}, 0, 0, true),
    Arguments.of("dot", new String[]{"."}, 0, 0, false),
    Arguments.of("digit", new String[]{"0"}, 0, 0, false),
    Arguments.of("OOB neg", new String[]{"0"}, -1, 0, false),
    Arguments.of("OOB over", new String[]{"0"}, 1, 0, false)
  );

  @ParameterizedTest(name = "{0}")
  @FieldSource("symbolTests")
  void testCheckForSymb(String name, String[] lines, int r, int c, boolean expected) {
    assertEquals(expected, Day03.checkForSymbol(lines, r, c));
  }

  static List<Arguments> angleTests = List.of(
    Arguments.of("top left", "*..\n.1.\n...", 1),
    Arguments.of("left", "...\n*1.\n...", 1),
    Arguments.of("bottom left", "...\n.1.\n*..", 1),
    Arguments.of("top", ".*.\n.1.\n...", 1),
    Arguments.of("bottom", "...\n.1.\n.*.", 1),
    Arguments.of("top right", "..*\n.1.\n...", 1),
    Arguments.of("right", "...\n.1*\n...", 1),
    Arguments.of("bottom right", "...\n.1.\n..*", 1),
    Arguments.of("end", "*..\n.11\n...", 11)
  );

  @ParameterizedTest(name = "{0}")
  @FieldSource("angleTests")
  void testCheckAngles(String name, String input, long expected) {
    assertEquals(expected, Day03.part1(input));
  }


  @Test 
  void testPart1t1() throws IOException {
    String input = TestLoader.loadInput("/day03/test1.txt");
    assertEquals(4361, Day03.part1(input));
  }

  @Test 
  void part1() throws IOException {
    String input = TestLoader.loadInput("/day03/real.txt");
    System.out.println(Day03.part1(input));
  }

  @Test 
  void testPart2t1() throws IOException {
    String input = TestLoader.loadInput("/day03/test1.txt");
    assertEquals(467835, Day03.part2(input));
  }

  @Test 
  void part2() throws IOException {
    String input = TestLoader.loadInput("/day03/real.txt");
    System.out.println(Day03.part2(input));
  }
}
