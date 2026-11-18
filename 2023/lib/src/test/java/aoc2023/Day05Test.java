package aoc2023;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.Test;

class Day05Test {

  @Test
  void seedParser() {
    assertArrayEquals(new long[] { 79, 14, 55, 13 }, Day05.parseSeeds("seeds: 79 14 55 13"));
  }

  @Test
  void chunkParser() {
    String input = "seed-to-soil map:\n50 98 2\n52 50 48";
    Day05.Mapping m = Day05.parseMappingChunk(input);
    assertArrayEquals(new long[] { 50, 52 }, m.destinations());
    assertArrayEquals(new long[] { 98, 50 }, m.sources());
    assertArrayEquals(new long[] { 2, 48 }, m.ranges());
  }

  @Test
  void findMapping() {
    Day05.Mapping m = new Day05.Mapping(
        new long[] { 50, 52 },
        new long[] { 98, 50 },
        new long[] { 2, 48 }, 2);
    assertEquals(Optional.of(81l), Day05.findMapping(79, m));
    assertEquals(Optional.empty(), Day05.findMapping(14, m));
    assertEquals(Optional.of(57l), Day05.findMapping(55, m));
  }

  @Test
  void part1t1() throws IOException {
    String input = TestLoader.loadInput("/day05/test1.txt");
    assertEquals(35, Day05.part1(input));
  }

  @Test
  void part1() throws IOException {
    String input = TestLoader.loadInput("/day05/real.txt");
    System.out.println(Day05.part1(input));
  }
}
