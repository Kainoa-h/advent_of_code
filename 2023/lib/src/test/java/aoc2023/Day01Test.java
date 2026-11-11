package aoc2023;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import aoc2023.TestLoader;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class Day01Test {

  @ParameterizedTest(name = "{0}")
  @CsvSource({
    "'Empty input', '', 0",
  })
  void testPart1Edge(String _name, String input, long expected) {
    assertEquals(expected, Day01.part1(input));
  }

  @Test
  void testPart1t1() throws IOException {
    String input = TestLoader.loadInput("/day01/test1.txt");
    assertEquals(142, Day01.part1(input));
  }

}
