import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class DiskFragmenter {

    static ArrayList<Integer> diskBlocks;
    static ArrayList<Integer> diskBlockSizeMap;
    static ArrayList<Integer> paddingSizeMap;

    public static void main(String[] args) throws Exception {
        init("09/data.txt");
        //System.out.println(diskBlocks);
        part2();
        //System.out.println(diskBlocks);
        calculateCheckSum();
    }

    static void init(String path) throws Exception {
        diskBlocks = new ArrayList<>();
        diskBlockSizeMap = new ArrayList<>();
        paddingSizeMap = new ArrayList<>();
        Scanner sc = new Scanner(new File(path));
        char[] arr = sc.nextLine().toCharArray();
        int id = 0;
        for (int i = 0; i < arr.length; i += 2) {
            int size = Character.getNumericValue(arr[i]);
            int padding = (i + 1 < arr.length) ? Character.getNumericValue(arr[i + 1]) : 0;
            for (int j = 0; j < size; j++) {
                diskBlocks.add(id);
            }
            for (int j = 0; j < padding; j++) {
                diskBlocks.add(-1);
            }
            diskBlockSizeMap.add(size);
            paddingSizeMap.add(padding);
            id++;
        }
    }

    static void calculateCheckSum() {
        long sum = 0;
        for (int i = 0; i < diskBlocks.size(); i++) {
            if (diskBlocks.get(i) == -1) continue;
            sum += diskBlocks.get(i) * i;
        }
        System.out.println(sum);
    }

    static int getIndexOfLastDataBlock(int previousLastBlockIndex) {
        for (int i = previousLastBlockIndex; i >= 0; i--) {
            if (diskBlocks.get(i) != -1)
                return i;
        }
        return -1;
    }

    static void part1() {
        int firstBlankIdx = diskBlocks.indexOf(-1);
        int lastDataBlockIdx = getIndexOfLastDataBlock(diskBlocks.size() - 1);
        while (firstBlankIdx < lastDataBlockIdx) {
            diskBlocks.set(firstBlankIdx, diskBlocks.get(lastDataBlockIdx));
            diskBlocks.set(lastDataBlockIdx, -1);
            firstBlankIdx = diskBlocks.indexOf(-1);
            lastDataBlockIdx = getIndexOfLastDataBlock(lastDataBlockIdx);
        }
    }

    static int getSizeOfPadding(int firstIndex) {
        int sum = 0;
        for (int i = firstIndex; i < diskBlocks.size(); i++) {
            if (diskBlocks.get(i) != -1) break;
            sum++;
        }
        return sum;
    }

    static void part2() {
        for (int i = diskBlockSizeMap.size() - 1; i >= 0; i--) {
            int size = diskBlockSizeMap.get(i);
            int firstIndexOfBlock = diskBlocks.indexOf(i);

            for (int x = 0; x < firstIndexOfBlock; ) {
                if (diskBlocks.get(x) != -1) {
                    x++;
                    continue;
                }
                int paddingSize = getSizeOfPadding(x);
                if (paddingSize < size) {
                    x += paddingSize;
                    continue;
                }

                for (int p = x; p < x + size; p++){
                    diskBlocks.set(p, i);
                }
                for (int p = firstIndexOfBlock; p < firstIndexOfBlock + size; p++){
                    diskBlocks.set(p, -1);
                }
                    break;
            }
        }
    }

}
