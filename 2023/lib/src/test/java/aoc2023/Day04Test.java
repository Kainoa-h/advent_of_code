package aoc2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class Day04Test {

  @ParameterizedTest(name = "{0}")
  @CsvSource({
      "'99', 99",
      "'01', 1",
      "' 1', 1"
  })
  void testGetNumber(String input, int expected) {
    assertEquals(expected, Day04.getNumber(input, 0));
  }

  @Test
  void testPart1t1() throws IOException {
    String input = TestLoader.loadInput("/day04/test1.txt");
    assertEquals(13, Day04.part1(input));
  }

  @Test
  void part1() throws IOException {
    String input = TestLoader.loadInput("/day04/real.txt");
    System.out.println(Day04.part1(input));
  }
}
