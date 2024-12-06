import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class HistorianHysteria {
    public static void main(String[] args) {
        part2();
    }

    static void part1() {
        try (Scanner sc = new Scanner(new File("data.txt"))) {
            ArrayList<Integer> leftList = new ArrayList<>();
            ArrayList<Integer> rightList = new ArrayList<>();
            while (sc.hasNext()) {
                String[] arr = sc.nextLine().split(" {3}");
                leftList.add(Integer.parseInt(arr[0]));
                rightList.add(Integer.parseInt(arr[1]));
            }

            leftList.sort((a, b) -> a - b);
            rightList.sort((a, b) -> a - b);
            int sum = 0;
            for (int i = 0; i < leftList.size(); i++) {
                sum += Math.abs(leftList.get(i) - rightList.get(i));
            }
            System.out.println(sum);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static void part2() {
        try (Scanner sc = new Scanner(new File("data.txt"))) {
            ArrayList<Integer> leftList = new ArrayList<>();
            ArrayList<Integer> rightList = new ArrayList<>();
            while (sc.hasNext()) {
                String[] arr = sc.nextLine().split(" {3}");
                leftList.add(Integer.parseInt(arr[0]));
                rightList.add(Integer.parseInt(arr[1]));
            }

            HashMap<Integer, Integer> dict = new HashMap<>();
            rightList.forEach((x) -> {
                int t = dict.computeIfAbsent(x, v -> 0);
                dict.put(x, ++t);
            });

            int sum = 0;
            for (Integer x : leftList) {
                Integer c = dict.get(x);
                sum += x * (c != null ? c : 0);
            }
            System.out.println(sum);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
