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

        //map.get(rowIdx)[colIdx] = '.';
        int startRowIdx = rowIdx;
        int startColIdx = colIdx;
        int startOffSetRowIdx = offSetRowIdx;
        int startOffSetColIdx = offSetColIdx;
        ArrayList<Integer> pa_pathRowIdx = new ArrayList<>();
        ArrayList<Integer> pa_pathColIdx = new ArrayList<>();
        while (true) {
            char c = map.get(rowIdx)[colIdx];
            if (c == '.') {
                map.get(rowIdx)[colIdx] = 'x';
                pa_pathRowIdx.add(rowIdx);
                pa_pathColIdx.add(colIdx);
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
            }

            rowIdx = nextRowIdx;
            colIdx = nextColIdx;
        }

        int sum = 0;
        for (int i = 0; i < pa_pathRowIdx.size(); i++) {
            map.get(pa_pathRowIdx.get(i))[pa_pathColIdx.get(i)] = '#';
            sum += WalkNSeeIfLoop(map,startRowIdx,startColIdx,startOffSetRowIdx,startOffSetColIdx,rowsCount);
            map.get(pa_pathRowIdx.get(i))[pa_pathColIdx.get(i)] = 'x';
        }

        System.out.println(sum);
    }

    static int WalkNSeeIfLoop(ArrayList<char[]> map, int rowIdx, int colIdx, int offSetRowIdx, int offSetColIdx, int rowsCount) {
        HashSet<String> visitedBlockedSqrs = new HashSet<>();
        while (true) {
            String enc = "" + rowIdx + colIdx + offSetRowIdx + offSetColIdx;
            if (visitedBlockedSqrs.contains(enc)) {
                return 1;
            }

            int nextRowIdx = rowIdx + offSetRowIdx;
            int nextColIdx = colIdx + offSetColIdx;
            if (nextRowIdx < 0 || nextRowIdx >= map.size() || nextColIdx < 0 || nextColIdx >= rowsCount) {
                return 0;
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
    }

    static void PrintMap(ArrayList<char[]> map, int r, int c) {
//        char x = map.get(r)[c];
//        map.get(r)[c] = 'x';
        System.out.println("\n\n\n\n\n");
        for (char[] arr : map) {
            System.out.println(String.valueOf(arr));
        }
//        map.get(r)[c] = x;
    }
}

