import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class BridgeRepair {
    public static void main(String[] args) throws Exception {
        part2();
    }

    public static void part1() throws Exception {
        Scanner sc = new Scanner(new File("07/data.txt"));
        long sum = 0;
        while (sc.hasNext()) {
            String[] line = sc.nextLine().split(":");
            long testValue = Long.parseLong(line[0]);
            long[] numbers = Arrays.stream(line[1].trim().split(" ")).mapToLong(Long::parseLong).toArray();

            sum += new OperatorIterator(testValue, numbers).isValid();
        }
        System.out.println(sum);

    }

    static class OperatorIterator {
        long testValue;
        long[] numbers;
        boolean[] operators;
        long perms;
        long permsCount;

        public OperatorIterator(long testValue, long[] numbers) {
            this.testValue = testValue;
            this.numbers = numbers;
            operators = new boolean[numbers.length - 1];
            perms = (long) Math.pow(2, numbers.length - 1);
            permsCount = perms;
        }

        public long isValid() {
            while (hasNextPerm()) {
                nextPerm();

                long sum = numbers[0];
                for (int i = 0; i < numbers.length - 1; i++) {
                    if (operators[i]) {
                        sum *= numbers[i + 1];
                    } else {
                        sum += numbers[i + 1];
                    }
                }
                if (sum == testValue) return testValue;
            }
            return 0;
        }

        private boolean hasNextPerm() {
            return permsCount != 0;
        }

        private void nextPerm() {
            permsCount--;
            if (permsCount == perms - 1) return;
            if (operators[0]) {
                operators[0] = false;
                if (permsCount != 0) nextPerm(1);
            } else {
                operators[0] = true;
            }
        }

        private void nextPerm(int idx) {
            if (operators[idx]) {
                operators[idx] = false;
                if (permsCount != 0) nextPerm(idx + 1);
            } else {
                operators[idx] = true;
            }
        }
    }


    static void part2() throws Exception{
        Scanner sc = new Scanner(new File("07/data.txt"));
        long sum = 0;
        while (sc.hasNext()) {
            String[] line = sc.nextLine().split(":");
            long testValue = Long.parseLong(line[0]);
            long[] numbers = Arrays.stream(line[1].trim().split(" ")).mapToLong(Long::parseLong).toArray();

            sum += new OperatorIterator3(testValue, numbers).isValid();
        }
        System.out.println(sum);
    }

    static class OperatorIterator3 {
        long testValue;
        long[] numbers;
        short[] operators;
        long perms;
        long permsCount;

        public OperatorIterator3(long testValue, long[] numbers) {
            this.testValue = testValue;
            this.numbers = numbers;
            operators = new short[numbers.length - 1];
            perms = (long) Math.pow(3, numbers.length - 1);
            permsCount = perms;
        }

        public long isValid() {
            while (hasNextPerm()) {
                nextPerm();

                long sum = numbers[0];
                for (int i = 0; i < numbers.length - 1; i++) {
                    if (operators[i] == 0) {
                        sum += numbers[i + 1];
                    } else if (operators[i] == 1) {
                        sum *= numbers[i + 1];
                    } else {
                        sum = Long.parseLong("" + sum + numbers[i + 1]);
                    }
                }
                if (sum == testValue) return testValue;
            }
            return 0;
        }

        private boolean hasNextPerm() {
            return permsCount != 0;
        }

        private void nextPerm() {
            permsCount--;
            if (permsCount == perms - 1) return;
            if (operators[0] == 0) {
                operators[0] = 1;
            } else if (operators[0] == 1) {
                operators[0] = 2;
            } else {
                operators[0] = 0;
                if (permsCount != 0) nextPerm(1);
            }
        }

        private void nextPerm(int idx) {
            if (operators[idx] == 0) {
                operators[idx] = 1;
            } else if (operators[idx] == 1) {
                operators[idx] = 2;
            } else {
                operators[idx] = 0;
                if (permsCount != 0) nextPerm(idx+1);
            }
        }
    }
}
