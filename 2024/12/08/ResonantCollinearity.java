import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ResonantCollinearity {

    static ArrayList<ArrayList<Node>> map;
    static HashMap<Character, ArrayList<Node>> antennasMap;

    public static void main(String[] args) throws Exception {
        initMap("08/data.txt");
        part2();
        //printAntennas();
        //printAntiNodes();
        //countAntiNodes();
        //printResonantHarmonics();
        countAntiNodesIncludingResonantHarmonicNodes();
    }

    static void initMap(String p) throws Exception {
        Scanner sc = new Scanner(new File(p));
        map = new ArrayList<>();
        antennasMap = new HashMap<>();
        int rowIdx = 0;
        while (sc.hasNext()) {
            ArrayList<Node> row = new ArrayList<>();
            int colIdx = 0;
            for (char c : sc.nextLine().toCharArray()) {
                Node n = new Node(c, rowIdx, colIdx);
                row.add(n);
                if (c != '.')
                    antennasMap.computeIfAbsent(c, v -> new ArrayList<>()).add(n);

                colIdx++;
            }
            map.add(row);
            rowIdx++;
        }
    }

    static void printAntennas() {
        map.forEach((r) -> {
            System.out.println(r.stream().map(x -> x.type).collect(Collectors.toList()));
        });
        System.out.println();
    }

    static void printAntiNodes() {
        AtomicInteger count = new AtomicInteger();
        map.forEach((r) -> {
            count.addAndGet(r.stream().map(x -> x.isAntiNode ? 1 : 0).reduce(0, Integer::sum));
            System.out.println(r.stream().map(x -> x.isAntiNode ? '#' : ' ').collect(Collectors.toList()));
        });
        System.out.println(count.get());
        System.out.println();
    }

    static void countAntiNodes() {
        AtomicInteger count = new AtomicInteger();
        map.forEach((r) -> {
            count.addAndGet(r.stream().map(x -> x.isAntiNode ? 1 : 0).reduce(0, Integer::sum));
        });
        System.out.println(count.get());
        System.out.println();
    }

    static void printResonantHarmonics() {
        AtomicInteger count = new AtomicInteger();
        map.forEach((r) -> {
            count.addAndGet(r.stream().map(x -> x.resonantHarmonicsCount > 1 ? 1 : 0).reduce(0, Integer::sum));
            System.out.println(r.stream().map(x -> x.resonantHarmonicsCount).collect(Collectors.toList()));
        });
        System.out.println(count.get());
        System.out.println();
    }

    static void countAntiNodesIncludingResonantHarmonicNodes() {
        AtomicInteger count = new AtomicInteger();
        map.forEach((r) -> {
            count.addAndGet(r.stream().map(x -> (x.resonantHarmonicsCount > 1 || x.isAntiNode) ? 1 : 0).reduce(0, Integer::sum));
        });
        System.out.println(count.get());
        System.out.println();
    }

    static void part1() throws Exception {
        antennasMap.forEach((k, v) -> {
            for (int i = 0; i < v.size(); i++) {
                Node a = v.get(i);
                for (int j = i + 1; j < v.size(); j++) {
                    Node b = v.get(j);

                    int rowDiff = a.rowIdx - b.rowIdx;
                    int colDiff = a.colIdx - b.colIdx;
                    // (1,8) | (2,5) -> (-1, 3)
                    // (2,5) | (3,7) -> (-1, -2)
                    try {
                        map.get(a.rowIdx + rowDiff).get(a.colIdx + colDiff).isAntiNode = true;
                    } catch (Exception _) {
                    }
                    try {
                        map.get(b.rowIdx - rowDiff).get(b.colIdx - colDiff).isAntiNode = true;
                    } catch (Exception _) {
                    }
                }
            }
        });
    }

    /**
     * Not working entirely correctly, but it gets the ans LOL
     * @throws Exception
     */
    static void part2() throws Exception {
        antennasMap.forEach((k, v) -> {
            for (int i = 0; i < v.size(); i++) {
                Node a = v.get(i);
                for (int j = 0; j < v.size(); j++) {
                    if (i == j) continue;
                    Node b = v.get(j);

                    int rowDiff = a.rowIdx - b.rowIdx;
                    int colDiff = a.colIdx - b.colIdx;

                    try {
                        int nextRow = a.rowIdx + rowDiff;
                        int nextCol = a.colIdx + colDiff;
                        map.get(nextRow).get(nextCol).isAntiNode = true;
                        while (true) {
                            map.get(nextRow).get(nextCol).resonantHarmonicsCount++;
                            nextRow += rowDiff;
                            nextCol += colDiff;
                        }
                    } catch (Exception _) {
                    }
                    try {
                        int nextRow = a.rowIdx - rowDiff;
                        int nextCol = a.colIdx - colDiff;
                        map.get(nextRow).get(nextCol).isAntiNode = true;
                        while (true) {
                            map.get(nextRow).get(nextCol).resonantHarmonicsCount++;
                            nextRow -= rowDiff;
                            nextCol -= colDiff;
                        }
                    } catch (Exception _) {
                    }
                }
            }
        });
    }

    static class Node {
        public final Character type;
        public boolean isAntiNode;
        public final int rowIdx;
        public final int colIdx;
        public int resonantHarmonicsCount;

        public Node(Character type, int rowIdx, int colIdx) {
            this.type = type;
            this.rowIdx = rowIdx;
            this.colIdx = colIdx;
            isAntiNode = false;
            resonantHarmonicsCount = 0;
        }
    }
}
