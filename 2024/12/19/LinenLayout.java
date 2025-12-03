import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

public class LinenLayout {
    static final String FILE = "data";

    public static void main(String[] args) throws Exception {
        new Part1().run();
    }

    static class Part1 {
        static final HashMap<Character, ArrayList<String>> a2zTowels = new HashMap<>();
        static final ArrayList<String> patterns = new ArrayList<>();

        public Part1() throws Exception {
            Scanner sc = new Scanner(new File(FILE));
            String[] towels = sc.nextLine().split(", ");
            for (String p : towels) {
                a2zTowels.computeIfAbsent(p.charAt(0), v -> new ArrayList<>()).add(p);
            }
            sc.nextLine();

            while (sc.hasNext()) {
                patterns.add(sc.nextLine());
            }
        }

        void run() throws Exception {
            int sum = 0;
            for (String p : patterns) {
                sum += isTowelArragementPossible(p) ? 1 : 0;
            }
            System.out.print(sum);
        }

        boolean isTowelArragementPossible(String pattern) {
            Stack<Integer> dfsStack = new Stack<>();
            dfsStack.push(0);
            while (!dfsStack.isEmpty()) {
                int idx = dfsStack.pop();
                ArrayList<String> candidates = a2zTowels.get(pattern.charAt(idx));
                if (candidates == null) continue;
                for (String towel : candidates) {
                    if (pattern.substring(idx).matches("^(" + towel + ")[a-z]*$")) {
                        if (towel.length() + idx == pattern.length()) return true;
                        dfsStack.push(idx + towel.length());
                    }
                }
            }
            return false;
        }

        record dfsNode(int startIdx, String towel) {
        }
    }
}
