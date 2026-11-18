package aoc2023;

import java.util.ArrayList;
import java.util.List;
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
      ranges[i - 1] = Long.parseLong(vals[2]);
    }
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

  public record RecordRange(long first, long range) {
  };

  static List<RecordRange> parseSeedRanges(String input) {
    var seedStrs = input.split(":")[1].trim().split(" ");
    List<RecordRange> seeds = new ArrayList<>();
    for (int i = 0; i < seedStrs.length; i += 2) {
      long first = Long.parseLong(seedStrs[i]);
      long range = Long.parseLong(seedStrs[i + 1]);
      seeds.add(new RecordRange(first, range));
    }
    return seeds;
  }

  static List<RecordRange> mapRecords2(List<RecordRange> records, Mapping mapping) {
    List<RecordRange> newRanges = new ArrayList<>();
    for (int i = 0; i < records.size(); i++) {
      var rec = records.get(i);
      boolean mapped = false;
      for (int m = 0; m < mapping.size(); m++) {
        var source = mapping.sources()[m];
        var range = mapping.ranges()[m];
        var sourceLimit = source + range;
        if (rec.first() >= source && rec.first() < sourceLimit) {
          mapped = true;
          var recLastVal = rec.first() + rec.range() - 1;
          if (recLastVal < sourceLimit) {
            var diff = rec.first() - source;
            var dest = mapping.destinations[m];
            newRanges.add(new RecordRange(dest + diff, rec.range()));
          } else {
            var spillOver = recLastVal - sourceLimit + 1;
            records.add(new RecordRange(sourceLimit, spillOver));
            var diff = rec.first() - source;
            var dest = mapping.destinations[m];
            newRanges.add(new RecordRange(dest + diff, rec.range - spillOver));
          }
        }
      }
      if (!mapped) {
        newRanges.add(rec);
      }
    }
    return newRanges;
  }

  static long part2(String input) {
    var chunks = input.split("\n\n");

    var records = parseSeedRanges(chunks[0]);

    for (int i = 1; i < chunks.length; i++) {
      var mapping = parseMappingChunk(chunks[i]);
      records = mapRecords2(records, mapping);
    }

    return records.stream().mapToLong(r -> r.first).min().orElseThrow();
  }
}
