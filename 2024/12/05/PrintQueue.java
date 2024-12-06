import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class PrintQueue {
    public static void main(String[] args) throws FileNotFoundException {
        part2();
    }

    public static void part1() throws FileNotFoundException {
        HashMap<String, HashSet<String>> cannotBeAfterMap = new HashMap<>();
        int sum = 0;
        Scanner sc = new Scanner(new File("data.txt"));
        boolean isFirstPortion = true;
        while (sc.hasNext()) {
            String line = sc.nextLine();
            if (isFirstPortion) {
                if (line.isBlank()) {
                    isFirstPortion = false;
                    continue;
                }
                String[] lineArr = line.split("\\|");
                HashSet<String> s = cannotBeAfterMap.computeIfAbsent(lineArr[1], v -> new HashSet<>());
                s.add(lineArr[0]);
                continue;
            }

            String[] arr = line.split(",");
            HashSet<String> invalidPagesMap = new HashSet<>();
            boolean isValid = true;
            for (String s : arr) {
                if (invalidPagesMap.contains(s)) {
                    isValid = false;
                    break;
                }
                if (cannotBeAfterMap.get(s) != null)
                    invalidPagesMap.addAll(cannotBeAfterMap.get(s));
            }

            if (isValid) {
                sum += Integer.parseInt(arr[arr.length / 2]);
            }
        }
        System.out.println(sum);
    }

    public static void part2() throws FileNotFoundException {
        HashMap<String, HashSet<String>> cannotBeAfterMap = new HashMap<>();
        int sum = 0;
        Scanner sc = new Scanner(new File("data.txt"));
        boolean isFirstPortion = true;
        while (sc.hasNext()) {
            String line = sc.nextLine();
            if (isFirstPortion) {
                if (line.isBlank()) {
                    isFirstPortion = false;
                    continue;
                }
                String[] lineArr = line.split("\\|");
                HashSet<String> s = cannotBeAfterMap.computeIfAbsent(lineArr[1], v -> new HashSet<>());
                s.add(lineArr[0]);
                continue;
            }

            String[] arr = line.split(",");
            HashSet<String> invalidPagesMap = new HashSet<>();
            for (int i = 0; i < arr.length; i++) {
                String s = arr[i];
                if (invalidPagesMap.contains(s)) {
                    arr[i] = arr[i-1];
                    arr[i-1] = s;
                    sum += part2_helper(cannotBeAfterMap, arr);
                    break;
                }
                if (cannotBeAfterMap.get(s) != null)
                    invalidPagesMap.addAll(cannotBeAfterMap.get(s));
            }

        }
        System.out.println(sum);
    }

    static int part2_helper(HashMap<String, HashSet<String>> cannotBeAfterMap, String[] arr) {
        HashSet<String> invalidPagesMap = new HashSet<>();
        boolean isValid = true;
        for (int i = 0; i < arr.length; i++) {
            String s = arr[i];
            if (invalidPagesMap.contains(s)) {
                arr[i] = arr[i-1];
                arr[i-1] = s;
                return part2_helper(cannotBeAfterMap, arr);
            }
            if (cannotBeAfterMap.get(s) != null)
                invalidPagesMap.addAll(cannotBeAfterMap.get(s));
        }
        return Integer.parseInt(arr[arr.length/2]);
    }
}
