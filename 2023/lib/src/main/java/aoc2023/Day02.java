package aoc2023;

public class Day02 {

  public static int[] parseSubset(String subset) {
    int[] rgb = new int[3];
    if (subset.isBlank())
      return rgb;

    var balls = subset.split(",");
    for (var ball : balls) {
      var n = ball.trim().split(" ")[0];
      var count = Integer.parseInt(n);
      if (ball.endsWith("d")) {
        rgb[0] = count;
      } else if (ball.endsWith("n")) {
        rgb[1] = count;
      } else {
        rgb[2] = count;
      }
    }
    return rgb;
  }

  static final int[] part1RGB = new int[] { 12, 13, 14 };

  public static int part1(String input) {
    var lines = input.split("\n");
    var result = 0;
    for (int game = 0; game < lines.length; game++) {
      var subsets = lines[game].split(":")[1].split(";");
      var isGameValid = true;
      for (var subset : subsets) {
        var rgb = parseSubset(subset);
        if (rgb[0] > part1RGB[0] || rgb[1] > part1RGB[1] || rgb[2] > part1RGB[2]) {
          isGameValid = false;
          break;
        }
      }
      result += isGameValid ? game + 1 : 0;
    }
    return result;
  }

  public static long part2(String input) {
    var lines = input.split("\n");
    long powerSum = 0;
    for (int game = 0; game < lines.length; game++) {
      long r = 0, g = 0, b = 0;
      var subsets = lines[game].split(":")[1].split(";");
      for (var subset : subsets) {
        var rgb = parseSubset(subset);
        r = Math.max(r, rgb[0]);
        g = Math.max(g, rgb[1]);
        b = Math.max(b, rgb[2]);
      }
      powerSum += r * g * b;
    }
    return powerSum;
  }
}
