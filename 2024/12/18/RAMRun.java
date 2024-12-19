import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class RAMRun {
    static final String FILE = "data";
    static final int SIZE = 71; //7,71;
    static final int DROPPEDCOUNT = 1024; //12,1024;

    public static void main(String[] args) throws Exception {
        new Part2().run();
    }

    static class Part2 {
        char[][] map;
        Node startNode;
        Node endNode;

        public Part2() throws Exception {
            Scanner sc = new Scanner(new File(FILE));
            map = new char[SIZE][SIZE];

            for (int i = 0; i < DROPPEDCOUNT; i++) {
                int[] colRow = Arrays.stream(sc.nextLine().split(",")).mapToInt(Integer::parseInt).toArray();
                map[colRow[1]][colRow[0]] = '#';
            }
            startNode = new Node(0, 0, new Velocity(0, 1));
            endNode = new Node(SIZE - 1, SIZE - 1, null);
        }

        void print() {
            for (char[] chars : map) {
                System.out.println();
                for (char c : chars) {
                    System.out.print(c);
                }
            }
            System.out.println();
        }

        void print(Node n) {
            final char[][] m = new char[SIZE][];
            for (int i = 0; i < SIZE; i++) {
                m[i] = Arrays.copyOf(map[i], SIZE);
            }

            while (n != null) {
                //System.out.println(n.toString() + ":" + n.cost);
                m[n.irow][n.icol] = '0';
                n = n.prevNode;
            }
            for (char[] row : m) {
                System.out.println();
                for (char x : row) {
                    if (x == '0')
                        System.out.print("\u001B[32m" + x + "\u001B[0m");
                    else if (x == '#')
                        System.out.print(x);
                    else System.out.print('.');
                }
            }
            System.out.println();
        }

        Node dickStra() {
            HashMap<String, Node> poppedNodes = new HashMap<>();
            PriorityQueue<Node> nodesQueue = new PriorityQueue<>((a, b) -> a.getTotalCost() - b.getTotalCost());
            nodesQueue.add(startNode);

            while (!nodesQueue.isEmpty()) {
                Node node = nodesQueue.poll();
                poppedNodes.put(node.toString(), node);
                Velocity velocity = node.velocity;

                //print(node);

                if (node.toString().equals(endNode.toString())) {
                    System.out.println(node.getTotalCost());
                    return node;
                    //print(node);
                }

                // Straight
                Node straight = getNextNode(node, velocity);
                if (straight != null) {
                    straight.pathCost = Math.abs(straight.irow - node.irow) + Math.abs(straight.icol - node.icol) + node.pathCost;
                    straight.prevNode = node;
                    Node visitedNode = poppedNodes.get(straight.toString());
                    if (visitedNode == null || visitedNode.heuristicCost > straight.heuristicCost) {
                        nodesQueue.add(straight);
                    }
                }
                // Left
                Node left = getNextNode(node, velocity.turnLeft());
                if (left != null) {
                    left.pathCost = Math.abs(left.irow - node.irow) + Math.abs(left.icol - node.icol) + node.pathCost;
                    left.prevNode = node;
                    Node visitedNode = poppedNodes.get(left.toString());
                    if (visitedNode == null || visitedNode.heuristicCost > left.heuristicCost) {
                        nodesQueue.add(left);
                    }
                }
                // Right
                Node right = getNextNode(node, velocity.turnRight());
                if (right != null) {
                    right.pathCost = Math.abs(right.irow - node.irow) + Math.abs(right.icol - node.icol) + node.pathCost;
                    right.prevNode = node;
                    Node visitedNode = poppedNodes.get(right.toString());
                    if (visitedNode == null || visitedNode.heuristicCost > right.heuristicCost) {
                        nodesQueue.add(right);
                    }
                }
            }
            return null;
        }

        public String backTrackPath(Node node) {
            StringBuilder sbPath = new StringBuilder();
            Node n = node;
            while (n.prevNode != null) {
                int r = n.prevNode.irow, c = n.prevNode.icol;
                while (r != n.irow || c != n.icol) {
                    sbPath.append(c).append(",").append(r).append(" ");
                    r += n.velocity.velRow;
                    c += n.velocity.velCol;
                }
                n = n.prevNode;
            }
            return sbPath.toString();
        }

        public void run() throws Exception {
            //print();
            Node solutionNode = dickStra();

            Scanner sc = new Scanner(new File(FILE));
            for (int i = 0; i < DROPPEDCOUNT; i++) {
                sc.nextLine();
            }
            String path = backTrackPath(solutionNode);
            while (sc.hasNext()) {
                String line = sc.nextLine();
                int[] colRow = Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray();
                map[colRow[1]][colRow[0]] = '#';
                if (!path.contains(line)) continue;
                solutionNode = dickStra();
                if (solutionNode == null) {
                    System.out.print(line);
                    break;
                }
                path = backTrackPath(solutionNode);
            }
            /**
             * Find best path.
             * If new block blocks best path, recalculate local A* :(
             *
             */

        }


        Node getNextNode(Node currentNode, Velocity velocity) {
            int nextIRow = currentNode.irow + velocity.velRow;
            int nextICol = currentNode.icol + velocity.velCol;
            while (true) {
                Character c = mapGetPos(nextIRow, nextICol);
                if (c == null || c == '#') return null;
                if (nextIRow == nextICol && nextICol == SIZE - 1) return new Node(nextIRow, nextICol, velocity);

                Velocity vLeft = velocity.turnLeft();
                Velocity vRight = velocity.turnRight();
                Character cLeft = mapGetPos(nextIRow + vLeft.velRow, nextICol + vLeft.velCol);
                Character cRight = mapGetPos(nextIRow + vRight.velRow, nextICol + vRight.velCol);
                if ((cLeft != null && cLeft == '\u0000') || (cRight != null && cRight == '\u0000')) {
                    return new Node(nextIRow, nextICol, velocity);
                }
                nextIRow += velocity.velRow;
                nextICol += velocity.velCol;
            }
        }

        Character mapGetPos(int irow, int icol) {
            if (irow >= map.length || icol >= SIZE) return null;
            if (irow < 0 || icol < 0) return null;
            return map[irow][icol];
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
            public final int heuristicCost;
            public int pathCost;
            public Node prevNode;
            public Velocity velocity;

            public Node(int irow, int icol, Velocity velocity) {
                this.irow = irow;
                this.icol = icol;
                this.heuristicCost = Math.abs(irow - (SIZE - 1)) + Math.abs(icol - (SIZE - 1));
                this.pathCost = 0;
                this.velocity = velocity;
            }

            public int getTotalCost() {
                return heuristicCost + pathCost;
            }

            @Override
            public String toString() {
                return irow + "," + icol;
            }
        }
    }

    static class Part1 {
        char[][] map;
        Node startNode;
        Node endNode;

        public Part1() throws Exception {
            Scanner sc = new Scanner(new File(FILE));
            map = new char[SIZE][SIZE];

            for (int i = 0; i < DROPPEDCOUNT; i++) {
                int[] colRow = Arrays.stream(sc.nextLine().split(",")).mapToInt(Integer::parseInt).toArray();
                map[colRow[1]][colRow[0]] = '#';
            }
            startNode = new Node(0, 0, new Velocity(0, 1));
            endNode = new Node(SIZE - 1, SIZE - 1, null);
        }

        void print() {
            for (char[] chars : map) {
                System.out.println();
                for (char c : chars) {
                    System.out.print(c);
                }
            }
            System.out.println();
        }

        void print(Node n) {
            final char[][] m = new char[SIZE][];
            for (int i = 0; i < SIZE; i++) {
                m[i] = Arrays.copyOf(map[i], SIZE);
            }

            while (n != null) {
                //System.out.println(n.toString() + ":" + n.cost);
                m[n.irow][n.icol] = '0';
                n = n.prevNode;
            }
            for (char[] row : m) {
                System.out.println();
                for (char x : row) {
                    if (x == '0')
                        System.out.print("\u001B[32m" + x + "\u001B[0m");
                    else if (x == '#')
                        System.out.print(x);
                    else System.out.print('.');
                }
            }
            System.out.println();
        }

        public void run() throws Exception {
            //print();
            HashMap<String, Node> poppedNodes = new HashMap<>();
            PriorityQueue<Node> nodesQueue = new PriorityQueue<>((a, b) -> a.getTotalCost() - b.getTotalCost());
            nodesQueue.add(startNode);
            while (!nodesQueue.isEmpty()) {
                Node node = nodesQueue.poll();
                poppedNodes.put(node.toString(), node);
                Velocity velocity = node.velocity;

                //print(node);

                if (node.toString().equals(endNode.toString())) {
                    System.out.println(node.getTotalCost());
                    //print(node);
                    return;
                }

                // Straight
                Node straight = getNextNode(node, velocity);
                if (straight != null) {
                    straight.pathCost = Math.abs(straight.irow - node.irow) + Math.abs(straight.icol - node.icol) + node.pathCost;
                    straight.prevNode = node;
                    Node visitedNode = poppedNodes.get(straight.toString());
                    if (visitedNode == null || visitedNode.heuristicCost > straight.heuristicCost) {
                        nodesQueue.add(straight);
                    }
                }
                // Left
                Node left = getNextNode(node, velocity.turnLeft());
                if (left != null) {
                    left.pathCost = Math.abs(left.irow - node.irow) + Math.abs(left.icol - node.icol) + node.pathCost;
                    left.prevNode = node;
                    Node visitedNode = poppedNodes.get(left.toString());
                    if (visitedNode == null || visitedNode.heuristicCost > left.heuristicCost) {
                        nodesQueue.add(left);
                    }
                }
                // Right
                Node right = getNextNode(node, velocity.turnRight());
                if (right != null) {
                    right.pathCost = Math.abs(right.irow - node.irow) + Math.abs(right.icol - node.icol) + node.pathCost;
                    right.prevNode = node;
                    Node visitedNode = poppedNodes.get(right.toString());
                    if (visitedNode == null || visitedNode.heuristicCost > right.heuristicCost) {
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
                if (nextIRow == nextICol && nextICol == SIZE - 1) return new Node(nextIRow, nextICol, velocity);

                Velocity vLeft = velocity.turnLeft();
                Velocity vRight = velocity.turnRight();
                Character cLeft = mapGetPos(nextIRow + vLeft.velRow, nextICol + vLeft.velCol);
                Character cRight = mapGetPos(nextIRow + vRight.velRow, nextICol + vRight.velCol);
                if ((cLeft != null && cLeft == '\u0000') || (cRight != null && cRight == '\u0000')) {
                    return new Node(nextIRow, nextICol, velocity);
                }
                nextIRow += velocity.velRow;
                nextICol += velocity.velCol;
            }
        }

        Character mapGetPos(int irow, int icol) {
            if (irow >= map.length || icol >= SIZE) return null;
            if (irow < 0 || icol < 0) return null;
            return map[irow][icol];
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
            public final int heuristicCost;
            public int pathCost;
            public Node prevNode;
            public Velocity velocity;

            public Node(int irow, int icol, Velocity velocity) {
                this.irow = irow;
                this.icol = icol;
                this.heuristicCost = Math.abs(irow - (SIZE - 1)) + Math.abs(icol - (SIZE - 1));
                this.pathCost = 0;
                this.velocity = velocity;
            }

            public int getTotalCost() {
                return heuristicCost + pathCost;
            }

            @Override
            public String toString() {
                return irow + "," + icol;
            }
        }
    }
}
