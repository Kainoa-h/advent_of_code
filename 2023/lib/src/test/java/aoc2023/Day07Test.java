package aoc2023;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class Day07Test {

  @ParameterizedTest(name="{0}")
  @CsvSource({
    "'A2345', '0MABCD'",
    "'6789T', '0EFGHI'",
    "'TJQKA', '0IJKLM'",
    "'AAAAA', '6MMMMM'",
    "'KKKKK', '6LLLLL'",
    "'KKKK2', '5LLLLA'",
    "'JJTT5', '2JJIID'",
    "'JJTTT', '4JJIII'",
  })
  void testHandScore(String hand, String expected) {
    assertEquals(expected, Day07.getHandScore(hand));
  }

  @Test
  void part1t1() throws IOException {
    String input = TestLoader.loadInput("/day07/test1.txt");
    assertEquals(6440, Day07.part1(input));
  }

  @Test
  void part1() throws IOException {
    String input = TestLoader.loadInput("/day07/real.txt");
    System.out.println(Day07.part1(input));
  }
}
