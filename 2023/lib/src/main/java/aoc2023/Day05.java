package aoc2023;

import java.util.Arrays;
import java.util.Optional;

public class Day05 {
  public record Mapping(long[] destinations, long[] sources, long[] ranges, long size) {
  };

  static long[] parseSeeds(String input) {
    var seedStrs = input.split(":")[1].trim().split(" ");
    long[] seeds = new long[seedStrs.length];
    for (int i = 0; i < seedStrs.length; i++) {
      seeds[i] = Long.parseLong(seedStrs[i]);
    }
    return seeds;
  }

  static Mapping parseMappingChunk(String input) {
    var lines = input.split("\n");
    var n = lines.length - 1;
    long[] destinations = new long[n];
    long[] sources = new long[n];
    long[] ranges = new long[n];

    for (int i = 1; i < lines.length; i++) {
      var vals = lines[i].split(" ");
      destinations[i - 1] = Long.parseLong(vals[0]);
      sources[i - 1] = Long.parseLong(vals[1]);
      ranges[i - 1] = Long.parseLong(vals[2]); }
    return new Mapping(destinations, sources, ranges, n);
  }

  static Optional<Long> findMapping(long record, Mapping mapping) {
    for (int m = 0; m < mapping.size(); m++) {
      var range = mapping.ranges()[m];
      var source = mapping.sources()[m];
      if (record >= source && record < source + range) {
        var diff = record - source;
        return Optional.of(mapping.destinations()[m] + diff);
      }
    }
    return Optional.empty();
  }

  static void mutablyMapRecords(long[] records, Mapping mapping) {
    for (int i = 0; i < records.length; i++) {
      var rec = records[i];
      records[i] = findMapping(rec, mapping).orElse(rec);
    }
  }

  static long part1(String input) {
    var chunks = input.split("\n\n");

    var records = parseSeeds(chunks[0]);

    for (int i = 1; i < chunks.length; i++) {
      var mapping = parseMappingChunk(chunks[i]);
      mutablyMapRecords(records, mapping);
    }

    return Arrays.stream(records).min().orElseThrow();
  }
}
