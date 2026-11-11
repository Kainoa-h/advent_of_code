package aoc2023;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class TestLoader {
  public static String loadInput(String path) throws IOException {
    try (var stream = TestLoader.class.getResourceAsStream(path)) {
      if (stream == null) {
        throw new IOException("Missing resource: " + path);
      }

      return new String(
          stream.readAllBytes(),
          StandardCharsets.UTF_8);
    }
  }
}
