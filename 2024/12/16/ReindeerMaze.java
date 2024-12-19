import javax.sql.rowset.FilteredRowSet;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class ReindeerMaze {
    public static final String FILE = "data";

    public static void main(String[] args) throws Exception {
        new Part2().run();

    }

    static class Part2 {
        ArrayList<ArrayList<Character>> map;
        ArrayList<ArrayList<Character>> finalMap;
        Node startNode;
        Node endNode;

        void init() throws Exception {
            Scanner sc = new Scanner(new File(FILE));
            map = new ArrayList<>();
            finalMap = new ArrayList<>();

            while (sc.hasNext()) {
                char[] row = sc.nextLine().toCharArray();
                ArrayList<Character> rowNodes = new ArrayList<>();
                for (int i = 0; i < row.length; i++) {
                    rowNodes.add(row[i]);
                    if (row[i] == 'S') startNode = new Node(map.size(), i, new Velocity(0, 1));
                    else if (row[i] == 'E') endNode = new Node(map.size(), i, null);
                }
                map.add(rowNodes);
                finalMap.add(new ArrayList<>(rowNodes));
            }

            sc.close();
        }

        void print() {
            finalMap.forEach(row -> {
                System.out.println();
                for (Character c : row) {
                    if (c == '0') System.out.print("\u001B[42m" + c + "\u001B[0m");
                    else System.out.print(c);
                }
            });
            System.out.println();
        }


        void colorIn(Node n) {
            while (n != null) {
                finalMap.get(n.irow).set(n.icol, '0');
                if (n.prevNode != null) {
                    Velocity v = n.velocity;
                    if (v.velRow != 0) {
                        for (int i = n.prevNode.irow; i != n.irow; i += v.velRow) {
                            finalMap.get(i).set(n.icol, '0');
                        }
                    } else {
                        for (int i = n.prevNode.icol; i != n.icol; i += v.velCol) {
                            finalMap.get(n.irow).set(i, '0');
                        }
                    }
                }
                n = n.prevNode;
            }
        }

        public void run() throws Exception {
            init();
            HashMap<String, Node> poppedNodes = new HashMap<>();
            PriorityQueue<Node> nodesQueue = new PriorityQueue<>((a, b) -> a.cost - b.cost);
            nodesQueue.add(startNode);
            Node foundEnd = null;
            while (!nodesQueue.isEmpty()) {
                Node node = nodesQueue.poll();
                poppedNodes.put(node.toString(), node);
                Velocity velocity = node.velocity;

                if (node.toString().equals(endNode.toString())) {
                    System.out.println(node.cost);
                    if (foundEnd == null) {
                        foundEnd = node;
                    } else if (foundEnd.cost != node.cost) continue;

                    colorIn(node);
                    //print();
                    continue;
                }

                // Straight
                Node straight = getNextNode(node, velocity);
                if (straight != null) {
                    straight.cost = Math.abs(straight.irow - node.irow) + Math.abs(straight.icol - node.icol) + node.cost;
                    straight.prevNode = node;
                    Node visitedNode = poppedNodes.get(straight.toString());
                    if (visitedNode == null || visitedNode.cost > straight.cost) {
                        nodesQueue.add(straight);
                    }
                }
                // Left
                Node left = getNextNode(node, velocity.turnLeft());
                if (left != null) {
                    left.cost = 1000 + Math.abs(left.irow - node.irow) + Math.abs(left.icol - node.icol) + node.cost;
                    left.prevNode = node;
                    Node visitedNode = poppedNodes.get(left.toString());
                    if (visitedNode == null || visitedNode.cost > left.cost) {
                        nodesQueue.add(left);
                    }
                }
                // Right
                Node right = getNextNode(node, velocity.turnRight());
                if (right != null) {
                    right.cost = 1000 + Math.abs(right.irow - node.irow) + Math.abs(right.icol - node.icol) + node.cost;
                    right.prevNode = node;
                    Node visitedNode = poppedNodes.get(right.toString());
                    if (visitedNode == null || visitedNode.cost > right.cost) {
                        nodesQueue.add(right);
                    }
                }
            }
            int count = 0;
            for (ArrayList<Character> row : finalMap) {
                for (Character c : row) {
                    if (c == '0') count++;
                }
            }
            System.out.print(count);
            print();
        }

        Node getNextNode(Node currentNode, Velocity velocity) {
            int nextIRow = currentNode.irow + velocity.velRow;
            int nextICol = currentNode.icol + velocity.velCol;
            while (true) {
                Character c = mapGetPos(nextIRow, nextICol);
                if (c == null || c == '#') return null;
                if (c == 'E') return new Node(nextIRow, nextICol, velocity);

                Velocity vLeft = velocity.turnLeft();
                Velocity vRight = velocity.turnRight();
                Character cLeft = mapGetPos(nextIRow + vLeft.velRow, nextICol + vLeft.velCol);
                Character cRight = mapGetPos(nextIRow + vRight.velRow, nextICol + vRight.velCol);
                if (cLeft == '.' || cRight == '.') {
                    return new Node(nextIRow, nextICol, velocity);
                }
                nextIRow += velocity.velRow;
                nextICol += velocity.velCol;
            }
        }

        Character mapGetPos(int irow, int icol) {
            if (irow >= map.size() || icol >= map.getFirst().size()) return null;
            if (irow < 0 || icol < 0) return null;
            return map.get(irow).get(icol);
        }

        record Velocity(int velRow, int velCol) {
            public Velocity turnRight() {
                return new Velocity(velCol, velRow * -1);
            }

            public Velocity turnLeft() {
                return new Velocity(velCol * -1, velRow);
            }
        }

        static class Node {
            public final int irow, icol;
            public int cost;
            public Node prevNode;
            public Velocity velocity;

            public Node(int irow, int icol, Velocity velocity) {
                this.irow = irow;
                this.icol = icol;
                this.cost = 0;
                this.velocity = velocity;
            }

            @Override
            public String toString() {
                return irow + "," + icol;
            }
        }
    }

    static class Part1 {
        ArrayList<ArrayList<Character>> map;
        Node startNode;
        Node endNode;

        void init() throws Exception {
            Scanner sc = new Scanner(new File(FILE));
            map = new ArrayList<>();

            while (sc.hasNext()) {
                char[] row = sc.nextLine().toCharArray();
                ArrayList<Character> rowNodes = new ArrayList<>();
                for (int i = 0; i < row.length; i++) {
                    rowNodes.add(row[i]);
                    if (row[i] == 'S') startNode = new Node(map.size(), i, new Velocity(0, 1));
                    else if (row[i] == 'E') endNode = new Node(map.size(), i, null);
                }
                map.add(rowNodes);
            }

            sc.close();
        }

        void print() {
            map.forEach(row -> {
                System.out.println();
                row.forEach(System.out::print);
            });
            System.out.println();
        }

        void print(Node n) {
            ArrayList<ArrayList<Character>> m = map.stream().map(ArrayList::new).collect(Collectors.toCollection(ArrayList::new));
            while (n != null) {
                System.out.println(n.toString() + ":" + n.cost);
                m.get(n.irow).set(n.icol, '0');
                n = n.prevNode;
            }
            m.forEach(row -> {
                System.out.println();
                row.forEach(x -> {
                    if (x == '0')
                        System.out.print("\u001B[32m" + x + "\u001B[0m");
                    else
                        System.out.print(x);
                });
            });
            System.out.println();
        }

        public void run() throws Exception {
            init();
            //print();
            HashMap<String, Node> poppedNodes = new HashMap<>();
            PriorityQueue<Node> nodesQueue = new PriorityQueue<>((a, b) -> a.cost - b.cost);
            nodesQueue.add(startNode);
            while (!nodesQueue.isEmpty()) {
                Node node = nodesQueue.poll();
                poppedNodes.put(node.toString(), node);
                Velocity velocity = node.velocity;

                if (node.toString().equals(endNode.toString())) {
                    System.out.println(node.cost);
                    //print(node);
                    return;
                }

                // Straight
                Node straight = getNextNode(node, velocity);
                if (straight != null) {
                    straight.cost = Math.abs(straight.irow - node.irow) + Math.abs(straight.icol - node.icol) + node.cost;
                    straight.prevNode = node;
                    Node visitedNode = poppedNodes.get(straight.toString());
                    if (visitedNode == null || visitedNode.cost > straight.cost) {
                        nodesQueue.add(straight);
                    }
                }
                // Left
                Node left = getNextNode(node, velocity.turnLeft());
                if (left != null) {
                    left.cost = 1000 + Math.abs(left.irow - node.irow) + Math.abs(left.icol - node.icol) + node.cost;
                    left.prevNode = node;
                    Node visitedNode = poppedNodes.get(left.toString());
                    if (visitedNode == null || visitedNode.cost > left.cost) {
                        nodesQueue.add(left);
                    }
                }
                // Right
                Node right = getNextNode(node, velocity.turnRight());
                if (right != null) {
                    right.cost = 1000 + Math.abs(right.irow - node.irow) + Math.abs(right.icol - node.icol) + node.cost;
                    right.prevNode = node;
                    Node visitedNode = poppedNodes.get(right.toString());
                    if (visitedNode == null || visitedNode.cost > right.cost) {
                        nodesQueue.add(right);
                    }
                }
            }

        }

        Node getNextNode(Node currentNode, Velocity velocity) {
            int nextIRow = currentNode.irow + velocity.velRow;
            int nextICol = currentNode.icol + velocity.velCol;
            while (true) {
                Character c = mapGetPos(nextIRow, nextICol);
                if (c == null || c == '#') return null;
                if (c == 'E') return new Node(nextIRow, nextICol, velocity);

                Velocity vLeft = velocity.turnLeft();
                Velocity vRight = velocity.turnRight();
                Character cLeft = mapGetPos(nextIRow + vLeft.velRow, nextICol + vLeft.velCol);
                Character cRight = mapGetPos(nextIRow + vRight.velRow, nextICol + vRight.velCol);
                if (cLeft == '.' || cRight == '.') {
                    return new Node(nextIRow, nextICol, velocity);
                }
                nextIRow += velocity.velRow;
                nextICol += velocity.velCol;
            }
        }

        Character mapGetPos(int irow, int icol) {
            if (irow >= map.size() || icol >= map.getFirst().size()) return null;
            if (irow < 0 || icol < 0) return null;
            return map.get(irow).get(icol);
        }

        record Velocity(int velRow, int velCol) {
            public Velocity turnRight() {
                return new Velocity(velCol, velRow * -1);
            }

            public Velocity turnLeft() {
                return new Velocity(velCol * -1, velRow);
            }
        }

        static class Node {
            public final int irow, icol;
            public int cost;
            public Node prevNode;
            public Velocity velocity;

            public Node(int irow, int icol, Velocity velocity) {
                this.irow = irow;
                this.icol = icol;
                this.cost = 0;
                this.velocity = velocity;
            }

            @Override
            public String toString() {
                return irow + "," + icol;
            }
        }
    }
}
