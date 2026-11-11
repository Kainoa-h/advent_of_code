package aoc2023;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class Day01Test {

  @ParameterizedTest(name = "{0}")
  @CsvSource({
    "'Empty input', '', 0",
    "'Empty line', '1\n\n2', 33",
  })
  void testPart1Edge(String _name, String input, long expected) {
    assertEquals(expected, Day01.part1(input));
  }

  @Test
  void testPart1t1() throws IOException {
    String input = TestLoader.loadInput("/day01/test1.txt");
    assertEquals(142, Day01.part1(input));
  }

  @Test
  void realPart1() throws IOException {
    String input = TestLoader.loadInput("/day01/real.txt");
    System.out.println(Day01.part1(input));
  }

  @ParameterizedTest(name = "{0}")
  @CsvSource({
    "'zero', '0'",
    "'one', '1'",
    "'two', '2'",
    "'three', '3'",
    "'four', '4'",
    "'five', '5'",
    "'six', '6'",
    "'seven', '7'",
    "'eight', '8'",
    "'nine', '9'",
    "'jaja',",
    "'z',"
  })
  void testPart2gd(String input, Character expected) {
    assertEquals(expected, Day01.getDigit(input, 0));
  }

  @ParameterizedTest(name="{0}")
  @CsvSource({
    "'zero', 0",
    "'z', 0",
  })
  void testPart2Edge(String input, long expected) {
    assertEquals(expected, Day01.part2(input));
  }

  @Test
  void testPart2t1() throws IOException {
    String input = TestLoader.loadInput("/day01/test2.txt");
    var result = Day01.part2(input);
    System.out.println(result);
    assertEquals(281, result);
  }

  @Test
  void realPart2() throws IOException {
    String input = TestLoader.loadInput("/day01/real.txt");
    var result = Day01.part2(input);
    System.out.println(result);
  }

}
