import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CeresSearch {

    public static void main(String[] args) {
        part2();
    }

    static void part1() {
        List<List<Character>> ahhh = new ArrayList<>();
        try (Scanner sc = new Scanner(new File("data.txt"))) {
            while (sc.hasNext()) {
                List<Character> r = sc.nextLine().chars().mapToObj(x -> (char) x).collect(Collectors.toList());
                ahhh.add(r);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Pattern p1 = Pattern.compile("XMAS");
        Pattern p2 = Pattern.compile("SAMX");

        int sum = 0;
        // Check Row
        for (List<Character> row : ahhh) {
            StringBuilder sb = new StringBuilder();
            row.forEach(sb::append);
            String s = sb.toString();
            Matcher m1 = p1.matcher(s);
            while (m1.find()) sum++;
            Matcher m2 = p2.matcher(s);
            while (m2.find()) sum++;
        }

        // Check Col
        for (int col = 0; col < ahhh.getFirst().size(); col++) {
            StringBuilder sb = new StringBuilder();
            for (int row = 0; row < ahhh.size(); row++) {
                sb.append(ahhh.get(row).get(col));
            }
            String s = sb.toString();
            Matcher m1 = p1.matcher(s);
            while (m1.find()) sum++;
            Matcher m2 = p2.matcher(s);
            while (m2.find()) sum++;
        }

        // Check right diag
        sum += checkDiag(ahhh, p1, p2);

        // Invert Array
        for (List<Character> characters : ahhh) {
            Collections.reverse(characters);
        }

        // Check left diag
        sum += checkDiag(ahhh, p1, p2);

        System.out.println(sum);
    }

    static int checkDiag(List<List<Character>> ahhh, Pattern p1, Pattern p2) {
        int sum = 0;
        for (int rStart = 0; rStart < ahhh.size(); rStart++) {
            int row = rStart;
            StringBuilder sb = new StringBuilder();
            for (int col = 0; col < ahhh.getFirst().size(); col++) {
                if (row >= ahhh.size()) break;
                sb.append(ahhh.get(row).get(col));
                row++;
            }
            String s = sb.toString();
            Matcher m1 = p1.matcher(s);
            while (m1.find()) sum++;
            Matcher m2 = p2.matcher(s);
            while (m2.find()) sum++;
        }
        for (int cStart = 1; cStart < ahhh.getFirst().size(); cStart++) {
            int col = cStart;
            StringBuilder sb = new StringBuilder();
            for (int row = 0; row < ahhh.size(); row++) {
                if (col >= ahhh.getFirst().size()) break;
                sb.append(ahhh.get(row).get(col));
                col++;
            }
            String s = sb.toString();
            Matcher m1 = p1.matcher(s);
            while (m1.find()) sum++;
            Matcher m2 = p2.matcher(s);
            while (m2.find()) sum++;
        }
        return sum;
    }

    static void part2() {
        List<List<Character>> ahhh = new ArrayList<>();
        try (Scanner sc = new Scanner(new File("data.txt"))) {
            while (sc.hasNext()) {
                List<Character> r = sc.nextLine().chars().mapToObj(x -> (char) x).collect(Collectors.toList());
                ahhh.add(r);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        int sum = 0;
        for (int row = 1; row < ahhh.size() - 1; row++) {
            for (int col = 1; col < ahhh.getFirst().size() - 1; col++) {
                if (ahhh.get(row).get(col) != 'A') continue;
                String s = String.valueOf(ahhh.get(row - 1).get(col - 1)) +
                        ahhh.get(row - 1).get(col + 1) +
                        ahhh.get(row + 1).get(col - 1) +
                        ahhh.get(row + 1).get(col + 1);
                if (s.equals("MMSS") || s.equals("SSMM") || s.equals("SMSM") || s.equals("MSMS")) sum++;
            }
        }
        System.out.println(sum);
    }
}
