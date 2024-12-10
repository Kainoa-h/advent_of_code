import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class HoofIt {
    static ArrayList<Node> trailHeads;
    static ArrayList<ArrayList<Node>> map;

    public static void main(String[] args) throws Exception {
        init("data.txt");
        //print();
        //part1();
        part2();
    }

    static void init(String path) throws Exception {
        Scanner sc = new Scanner(new File(path));
        map = new ArrayList<>();
        trailHeads = new ArrayList<>();
        int rIdx = 0;
        while (sc.hasNext()) {
            char[] line = sc.nextLine().toCharArray();
            ArrayList<Node> row = new ArrayList<>();
            for (int i = 0; i < line.length; i++) {
                Node n = new Node(rIdx, i, line[i]);
                row.add(n);
                if (line[i] == '0') {
                    trailHeads.add(n);
                }
            }
            map.add(row);
            rIdx++;
        }
    }

    static void print() {
        map.forEach(System.out::println);
        for (Node trailHead : trailHeads) {
            System.out.printf("(%s, %s )  ", trailHead.rIdx, trailHead.cIdx);
        }
        System.out.println();
    }

    static void part1() {
        int sum = 0;
        for (Node trailHead : trailHeads) {
            sum += navigate(trailHead);
        }
        System.out.println(sum);
    }

    static Node getNextNodeIfValid(int rIdx, int cIdx, Character nextStep) {
        if (rIdx < 0 || cIdx < 0) return null;
        if (rIdx >= map.size() || cIdx >= map.getFirst().size()) return null;
        return  map.get(rIdx).get(cIdx).altitude == nextStep ? map.get(rIdx).get(cIdx) : null;
    }

    static int navigate(Node startNode) {
        Stack<Node> dfs = new Stack<>();
        dfs.push(startNode);
        HashSet<Node> uniqueNodesVisited = new HashSet<>();
        while (!dfs.empty()) {
            Node currNode = dfs.pop();
            if (currNode.altitude == '9') {
                uniqueNodesVisited.add(currNode);
                continue;
            }

            Node up, down, left, right;
            up = getNextNodeIfValid(currNode.rIdx-1, currNode.cIdx, (char) (currNode.altitude+1));
            down = getNextNodeIfValid(currNode.rIdx+1, currNode.cIdx, (char) (currNode.altitude+1));
            left = getNextNodeIfValid(currNode.rIdx, currNode.cIdx-1, (char) (currNode.altitude+1));
            right = getNextNodeIfValid(currNode.rIdx, currNode.cIdx+1, (char) (currNode.altitude+1));

            if (up != null) dfs.push(up);
            if (down != null) dfs.push(down);
            if (left != null) dfs.push(left);
            if (right != null) dfs.push(right);
        }
        return uniqueNodesVisited.size();
    }

    static void part2() {
        int sum = 0;
        for (Node trailHead : trailHeads) {
            sum += navigate_part2(trailHead);
        }
        System.out.println(sum);
    }

    static int navigate_part2(Node startNode) {
        Stack<Node> dfs = new Stack<>();
        dfs.push(startNode);
        int sum = 0;
        while (!dfs.empty()) {
            Node currNode = dfs.pop();
            if (currNode.altitude == '9') {
                sum++;
                continue;
            }

            Node up, down, left, right;
            up = getNextNodeIfValid(currNode.rIdx-1, currNode.cIdx, (char) (currNode.altitude+1));
            down = getNextNodeIfValid(currNode.rIdx+1, currNode.cIdx, (char) (currNode.altitude+1));
            left = getNextNodeIfValid(currNode.rIdx, currNode.cIdx-1, (char) (currNode.altitude+1));
            right = getNextNodeIfValid(currNode.rIdx, currNode.cIdx+1, (char) (currNode.altitude+1));

            if (up != null) dfs.push(up);
            if (down != null) dfs.push(down);
            if (left != null) dfs.push(left);
            if (right != null) dfs.push(right);
        }
        return sum;
    }


    static class Node {
        public int peaksReachable;
        public final int rIdx, cIdx;
        public final char altitude;

        public Node(int rIdx, int cIdx, char altitude) {
            this.rIdx = rIdx;
            this.cIdx = cIdx;
            this.altitude = altitude;
            peaksReachable = -1;
        }

        @Override
        public String toString() {
            return "{"+ peaksReachable +
                    ", " + altitude +
                    '}';
        }
    }

}
