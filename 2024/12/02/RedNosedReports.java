import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class RedNosedReports {

    public static void main(String[] args) {
        part1();
        part2();
    }

    static void part1() {
        try (Scanner sc = new Scanner(new File("data.txt"))) {
            int sum = 0;
            while (sc.hasNext()) {
                List<Integer> arr = Arrays.stream(sc.nextLine().split(" ")).map(Integer::parseInt).toList();
                if (arr.size() < 2) continue;

                int prev = arr.get(0);
                final boolean isIncreasing = arr.get(0) < arr.get(1);
                boolean isSafe = true;
                for (int i = 1; i < arr.size(); i++) {
                    int x = arr.get(i);
                    if (isIncreasing != prev < x) {
                        isSafe = false;
                        break;
                    }
                    int step = Math.abs(prev - x);
                    if (step < 1 || step > 3) {
                        isSafe = false;
                        break;
                    }
                    prev = x;
                }
                if (isSafe) sum++;
            }
            System.out.println(sum);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*
    * 1 2 3 4 5 6
    * 1 5 6 7 8 9 remove before offence
    * 1 2 3 2 4 5 remove current offence
    * 5 6 3 5
    */
    static void part2() {
        try (Scanner sc = new Scanner(new File("data.txt"))) {
            int sum = 0;
            while (sc.hasNext()) {
                List<Integer> arr = Arrays.stream(sc.nextLine().split(" ")).map(Integer::parseInt).toList();
                if (arr.size() < 2) continue;
                boolean isSafe = false;
                for (int i = 0; i < arr.size(); i++) {
                    List<Integer> r = new ArrayList<>(arr);
                    r.remove(i);
                    if (part2_helper(r)) {
                        isSafe = true;
                        break;
                    }
                }
                if (isSafe) sum++;
            }
            System.out.println(sum);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static boolean part2_helper(List<Integer> arr) {
        int prev = arr.get(0);
        final boolean isIncreasing = arr.get(0) < arr.get(1);
        boolean isSafe = true;
        for (int i = 1; i < arr.size(); i++) {
            int x = arr.get(i);
            if (isIncreasing != prev < x) {
                isSafe = false;
                break;
            }
            int step = Math.abs(prev - x);
            if (step < 1 || step > 3) {
                isSafe = false;
                break;
            }
            prev = x;
        }
        return isSafe;
    }
}
