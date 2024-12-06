import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class GuardGallivant {

    public static void main(String[] args) throws Exception {
        part2();
    }

    static void part1() throws Exception {
        Scanner sc = new Scanner(new File("data.txt"));
        ArrayList<char[]> map = new ArrayList<>();
        int rowIdx = -1, colIdx = -1;
        int offSetRowIdx = 0, offSetColIdx = 0;
        int rowsCount = 0;
        while (sc.hasNext()) {
            String line = sc.nextLine();
            map.add(line.toCharArray());
            if (rowIdx == -1) {
                for (int idx = 0; idx < line.length(); idx++) {
                    char c = line.charAt(idx);
                    if (c == '^') {
                        rowIdx = rowsCount;
                        colIdx = idx;
                        offSetRowIdx = -1;
                        offSetColIdx = 0;
                    } else if (c == '>') {
                        rowIdx = rowsCount;
                        colIdx = idx;
                        offSetRowIdx = 0;
                        offSetColIdx = 1;
                    } else if (c == 'v') {
                        rowIdx = rowsCount;
                        colIdx = idx;
                        offSetRowIdx = 1;
                        offSetColIdx = 0;
                    } else if (c == '<') {
                        rowIdx = rowsCount;
                        colIdx = idx;
                        offSetRowIdx = 0;
                        offSetColIdx = -1;
                    }
                }
            }
            rowsCount++;
        }
        map.get(rowIdx)[colIdx] = '.';

        HashSet<String> visitedBlockedSqrs = new HashSet<>();
        int count = 0;
        while (true) {
            //If on a blocked square gg in the same direction | lol turns out this is useless
            String enc = "" + rowIdx + colIdx + offSetRowIdx + offSetColIdx;
            if (visitedBlockedSqrs.contains(enc)) {
                break;
            }

            char c = map.get(rowIdx)[colIdx];
            if (c == '.') {
                count++;
                map.get(rowIdx)[colIdx] = 'x';
            }

            int nextRowIdx = rowIdx + offSetRowIdx;
            int nextColIdx = colIdx + offSetColIdx;
            if (nextRowIdx < 0 || nextRowIdx >= map.size() || nextColIdx < 0 || nextColIdx >= rowsCount) {
                break;
            }

            if (map.get(nextRowIdx)[nextColIdx] == '#') {
                visitedBlockedSqrs.add("" + rowIdx + colIdx + offSetRowIdx + offSetColIdx);
                int temp = offSetRowIdx;
                offSetRowIdx = offSetColIdx;
                offSetColIdx = temp * -1;
                nextRowIdx = rowIdx + offSetRowIdx;
                nextColIdx = colIdx + offSetColIdx;
            }

            rowIdx = nextRowIdx;
            colIdx = nextColIdx;
        }
        System.out.println(count);
    }

    static void part2() throws Exception {
        Scanner sc = new Scanner(new File("test.txt"));
        ArrayList<char[]> map = new ArrayList<>();
        int rowIdx = -1, colIdx = -1;
        int offSetRowIdx = 0, offSetColIdx = 0;
        int rowsCount = 0;
        while (sc.hasNext()) {
            String line = sc.nextLine();
            map.add(line.toCharArray());
            if (rowIdx == -1) {
                for (int idx = 0; idx < line.length(); idx++) {
                    char c = line.charAt(idx);
                    if (c == '^') {
                        rowIdx = rowsCount;
                        colIdx = idx;
                        offSetRowIdx = -1;
                        offSetColIdx = 0;
                    } else if (c == '>') {
                        rowIdx = rowsCount;
                        colIdx = idx;
                        offSetRowIdx = 0;
                        offSetColIdx = 1;
                    } else if (c == 'v') {
                        rowIdx = rowsCount;
                        colIdx = idx;
                        offSetRowIdx = 1;
                        offSetColIdx = 0;
                    } else if (c == '<') {
                        rowIdx = rowsCount;
                        colIdx = idx;
                        offSetRowIdx = 0;
                        offSetColIdx = -1;
                    }
                }
            }
            rowsCount++;
        }
        map.get(rowIdx)[colIdx] = '.';

        int count = 0;
        int startRowIdx = rowIdx;
        int startColIdx = colIdx;
        Last2StepsChecker last2Steps = new Last2StepsChecker();
        int steps = 0;
        while (true) {

            char c = map.get(rowIdx)[colIdx];
            if (c == '.') {
                //map.get(rowIdx)[colIdx] = 'x';
            }

            int nextRowIdx = rowIdx + offSetRowIdx;
            int nextColIdx = colIdx + offSetColIdx;
            if (nextRowIdx < 0 || nextRowIdx >= map.size() || nextColIdx < 0 || nextColIdx >= rowsCount) {
                break;
            }

            if (map.get(nextRowIdx)[nextColIdx] == '#') {
                int temp = offSetRowIdx;
                offSetRowIdx = offSetColIdx;
                offSetColIdx = temp * -1;
                nextRowIdx = rowIdx + offSetRowIdx;
                nextColIdx = colIdx + offSetColIdx;
                int distanceToLoopingBlock = last2Steps.InsertAndGetDistanceToLoopBlock(steps);
                steps = 0;

                if (distanceToLoopingBlock != -1) {
                    int loopBlockRow = nextRowIdx;
                    int loopBlockCol = nextColIdx;
                    if (offSetRowIdx == 0)  //moving horizontally
                        loopBlockCol += offSetColIdx * distanceToLoopingBlock;
                    else
                        loopBlockRow += offSetRowIdx * distanceToLoopingBlock;


                    if ((loopBlockRow != startRowIdx || loopBlockCol != startColIdx) && (loopBlockRow >= 0 && loopBlockCol >= 0 && loopBlockRow < map.size() && loopBlockCol < rowsCount) && map.get(loopBlockRow)[loopBlockCol] != '#') {
                        map.get(loopBlockRow)[loopBlockCol] = 'O';
                        count++;
                    }
                }
            }
            PrintMap(map, rowIdx, colIdx, last2Steps);
            steps++;
            rowIdx = nextRowIdx;
            colIdx = nextColIdx;
        }
        System.out.println(count);

    }

    static void PrintMap(ArrayList<char[]> map, int r, int c, Last2StepsChecker l2) {
        char x = map.get(r)[c];
        map.get(r)[c] = 'x';
        System.out.println("\n\n\n\n\n");
        for (char[] arr : map) {
            System.out.println(String.valueOf(arr));
        }
        map.get(r)[c] = x;
        System.out.println(l2);
    }

    private static class Last2StepsChecker {
        int[] arr;

        public Last2StepsChecker() {
            arr = new int[]{-1,-1};
        }

        /**
         * @return int | -1 when no loop possible, x when loop is possible
         */
        public int InsertAndGetDistanceToLoopBlock(int i) {
            arr[0] = arr[1];
            arr[1] = i;

            return arr[0];
        }

        @Override
        public String toString() {
            return Arrays.toString(arr);
        }
    }
}

