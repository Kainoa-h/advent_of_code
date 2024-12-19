import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class WarehouseWoes {
    public static final String FILE = "data";

    public static void main(String[] args) throws Exception {
        new Part2().run();
    }

    static class Part2 {

        ArrayList<List<Character>> map;
        ArrayList<Character> steps;
        int robotRow, robotCol;
        HashMap<Character, Velocity> dir2Vel = new HashMap<>() {{
            put('<', new Velocity(0, -1));
            put('>', new Velocity(0, +1));
            put('^', new Velocity(-1, 0));
            put('v', new Velocity(+1, 0));
        }};

        void init() throws Exception {
            map = new ArrayList<>();
            steps = new ArrayList<>();
            Scanner sc = new Scanner(new File(FILE));
            boolean mode = true;
            int currRow = 0;
            boolean robotNotFound = true;
            while (sc.hasNext()) {
                String line = sc.nextLine().strip();
                if (line.isEmpty()) {
                    mode = false;
                    continue;
                }

                if (mode) {
                    ArrayList<Character> row = new ArrayList<>();
                    for (char c : line.replaceAll("#", "##")
                            .replaceAll("O", "[]")
                            .replaceAll("\\.", "..")
                            .replaceAll("@", "@.")
                            .toCharArray()) {
                        row.add(c);
                    }
                    map.add(row);

                    if (robotNotFound) {
                        int col = row.indexOf('@');
                        if (col > 0) {
                            robotRow = currRow;
                            robotCol = col;
                            robotNotFound = false;
                        }
                    }
                    currRow++;
                    continue;
                }

                steps.addAll(line.chars().mapToObj(x -> (char) x).toList());

            }
            sc.close();
            map.get(robotRow).set(robotCol, '.');
        }

        void spInit() throws Exception {
            map = new ArrayList<>();
            steps = new ArrayList<>();
            Scanner sc = new Scanner(new File(FILE));
            boolean mode = true;
            int currRow = 0;
            boolean robotNotFound = true;
            while (sc.hasNext()) {
                String line = sc.nextLine().strip();
                if (line.isEmpty()) {
                    mode = false;
                    continue;
                }

                if (mode) {
                    ArrayList<Character> row = new ArrayList<>();
                    for (char c : line.toCharArray()) {
                        row.add(c);
                    }
                    map.add(row);

                    if (robotNotFound) {
                        int col = row.indexOf('@');
                        if (col > 0) {
                            robotRow = currRow;
                            robotCol = col;
                            robotNotFound = false;
                        }
                    }
                    currRow++;
                    continue;
                }

                steps.addAll(line.chars().mapToObj(x -> (char) x).toList());

            }
            sc.close();
            map.get(robotRow).set(robotCol, '.');
        }

        void print() {
            char c = map.get(robotRow).get(robotCol);
            map.get(robotRow).set(robotCol, '@');
            map.forEach(x -> System.out.println(x.stream().map(Object::toString).collect(Collectors.joining())));
            System.out.println();
            map.get(robotRow).set(robotCol, c);
        }

        void save() throws Exception {
            char c = map.get(robotRow).get(robotCol);
            map.get(robotRow).set(robotCol, '@');
            FileWriter wr = new FileWriter("out.txt");
            for (List<Character> x : map) {
                wr.append(String.join("", x.stream().map(z -> String.valueOf((char) z)).toList()));
                wr.append("\n");
            }
            map.get(robotRow).set(robotCol, c);
            wr.close();
        }

        void count() {
            int sum = 0;
            for (int irow = 0; irow < map.size(); irow++) {
                for (int icol = 0; icol < map.getFirst().size(); icol++) {
                    if (map.get(irow).get(icol) == '[') {
                        sum += (100 * irow) + icol;
                    }
                }
            }
            System.out.print(sum);
        }

        public void run() throws Exception {
            init();
            print();
            //save();
            Scanner sc = new Scanner(System.in);
            int secs = 0;
            for (Character dir : steps) {


//                if (secs == 12287) {
//                    System.out.print(secs);
//                    print();
//                }
//
//                for(List<Character> row : map) {
//                    if (row.stream().map(Objects::toString).collect(Collectors.joining()).contains("[.")){
//                        System.out.print(secs);
//                        print();
//                    }
//                }


                Velocity v = dir2Vel.get(dir);
                if (v.rowV == 0) {
                    if (moveHorizontally(v, robotRow, robotCol)) {
                        robotCol += v.colV;
                    }
                } else {
                    moveVertically(v);
                }
                secs++;
                print();
            }
            sc.close();
            //print();
            //save();
            count();
        }

        void moveVertically(Velocity v) {
            int nextRow = robotRow + v.rowV;
            char next = map.get(nextRow).get(robotCol);
            if (next == '#') return;

            if (next == '.') {
                robotRow += v.rowV;
                return;
            }

            Box firstBox;
            if (next == '[') {
                firstBox = new Box(nextRow, robotCol, nextRow, robotCol + 1);
            } else {
                firstBox = new Box(nextRow, robotCol - 1, nextRow, robotCol);
            }
            ArrayList<Box> boxes = new ArrayList<>() {{
                add(firstBox);
            }};
            Stack<Box> boxStack = new Stack<>() {{
                add(firstBox);
            }};
            HashSet<String> addedBoxSet = new HashSet<>();
            while (!boxStack.empty()) {
                Box box = boxStack.pop();
                int curRow = box.rightRow;
                nextRow = curRow + v.rowV;
                char left = map.get(nextRow).get(box.leftCol);
                char right = map.get(nextRow).get(box.rightCol);

                if (left == '#' || right == '#') {
                    return;
                }

                if (left == '[') {
                    Box nextBox = new Box(nextRow, box.leftCol, nextRow, box.rightCol);
                    if(!addedBoxSet.contains(nextBox.toString())) {
                        boxStack.push(nextBox);
                        boxes.add(nextBox);
                        addedBoxSet.add(nextBox.toString());
                    }
                } else if (left == ']') {
                    Box nextBox = new Box(nextRow, box.leftCol - 1, nextRow, box.rightCol - 1);
                    if(!addedBoxSet.contains(nextBox.toString())) {
                        boxStack.push(nextBox);
                        boxes.add(nextBox);
                        addedBoxSet.add(nextBox.toString());
                    }
                }
                if (right == '[') {
                    Box nextBox = new Box(nextRow, box.leftCol + 1, nextRow, box.rightCol + 1);
                    if(!addedBoxSet.contains(nextBox.toString())) {
                        boxStack.push(nextBox);
                        boxes.add(nextBox);
                        addedBoxSet.add(nextBox.toString());
                    }
                }
            }

            if (v.rowV > 0) {
                boxes.sort((a,b)-> b.rightRow - a.rightRow);
            }
            else {
                boxes.sort((a,b)-> a.rightRow - b.rightRow);
            }

            for (Box b : boxes) {
                nextRow = b.rightRow + v.rowV;
                map.get(nextRow).set(b.leftCol, '[');
                map.get(nextRow).set(b.rightCol, ']');
                map.get(b.rightRow).set(b.leftCol, '.');
                map.get(b.rightRow).set(b.rightCol, '.');
//                System.out.print(b.leftRow + "," + b.leftCol);
//                print();
            }
            robotRow += v.rowV;
        }

        boolean moveHorizontally(Velocity v, int fromRow, int fromCol) {
            int nextRow = fromRow + v.rowV;
            int nextCol = fromCol + v.colV;
            char next = map.get(nextRow).get(nextCol);
            if (next == '#') {
                return false;
            } else if (next == '.') {
                return true;
            }

            if (moveHorizontally(v, nextRow, nextCol)) {
                char nextNext = map.get(nextRow + v.rowV).get(nextCol + v.colV);
                map.get(nextRow + v.rowV).set(nextCol + v.colV, next);
                map.get(nextRow).set(nextCol, nextNext);
                return true;
            }
            return false;

        }

        static class Box {
            public int leftRow;
            public int leftCol;
            public int rightRow;
            public int rightCol;

            public Box(int leftRow, int leftCol, int rightRow, int rightCol) {
                this.leftRow = leftRow;
                this.leftCol = leftCol;
                this.rightRow = rightRow;
                this.rightCol = rightCol;
            }

            @Override
            public String toString() {
                return "" + leftRow + leftCol + rightRow + rightCol;
            }
        }

        private record Velocity(int rowV, int colV) {
        }
    }


    static class Part1 {

        ArrayList<List<Character>> map;
        ArrayList<Character> steps;
        int robotRow, robotCol;
        HashMap<Character, Velocity> dir2Vel = new HashMap<>() {{
            put('<', new Velocity(0, -1));
            put('>', new Velocity(0, +1));
            put('^', new Velocity(-1, 0));
            put('v', new Velocity(+1, 0));
        }};

        void init() throws Exception {
            map = new ArrayList<>();
            steps = new ArrayList<>();
            Scanner sc = new Scanner(new File(FILE));
            boolean mode = true;
            int currRow = 0;
            boolean robotNotFound = true;
            while (sc.hasNext()) {
                String line = sc.nextLine().strip();
                if (line.isEmpty()) {
                    mode = false;
                    continue;
                }

                if (mode) {
                    ArrayList<Character> row = new ArrayList<>();
                    for (char c : line.toCharArray()) {
                        row.add(c);
                    }
                    map.add(row);

                    if (robotNotFound) {
                        int col = line.indexOf('@');
                        if (col > 0) {
                            robotRow = currRow;
                            robotCol = col;
                            robotNotFound = false;
                        }
                    }
                    currRow++;
                    continue;
                }

                steps.addAll(line.chars().mapToObj(x -> (char) x).toList());

            }
            sc.close();
            map.get(robotRow).set(robotCol, '.');
        }

        void print() {
            char c = map.get(robotRow).get(robotCol);
            map.get(robotRow).set(robotCol, '@');
            map.forEach(System.out::println);
            System.out.println();
            map.get(robotRow).set(robotCol, c);
        }

        void count() {
            long sum = 0L;
            for (int irow = 0; irow < map.size(); irow++) {
                for (int icol = 0; icol < map.getFirst().size(); icol++) {
                    if (map.get(irow).get(icol) == 'O') {
                        sum += 100L * irow + icol;
                    }
                }
            }
            System.out.print(sum);
        }

        public void run() throws Exception {
            init();

            for (Character dir : steps) {
                Velocity v = dir2Vel.get(dir);

                if (move(v, robotRow, robotCol)) {
                    robotRow += v.rowV;
                    robotCol += v.colV;
                }
            }
            //print();
            count();
        }

        boolean move(Velocity v, int fromRow, int fromCol) {
            int nextRow = fromRow + v.rowV;
            int nextCol = fromCol + v.colV;
            char me = map.get(fromRow).get(fromCol);
            char next = map.get(nextRow).get(nextCol);
            if (next == '#') {
                return false;
            } else if (next == '.') {
                return true;
            }

            if (move(v, nextRow, nextCol)) {
                char nextNext = map.get(nextRow + v.rowV).get(nextCol + v.colV);
                map.get(nextRow + v.rowV).set(nextCol + v.colV, next);
                map.get(nextRow).set(nextCol, nextNext);
                return true;
            }
            return false;
        }

        private record Velocity(int rowV, int colV) {
        }
    }


}
