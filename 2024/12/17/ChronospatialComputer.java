import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class ChronospatialComputer {
    static final String FILE = "data";

    public static void main(String[] args) throws Exception {
        new Part2().run();
    }

    public static class Part2 {
        final int regA, regB, regC;
        final ArrayList<Character> instructions;

        public Part2() throws Exception {
            instructions = new ArrayList<>();
            Scanner sc = new Scanner(new File(FILE));
            regA = Integer.parseInt(sc.nextLine().substring(12).trim());
            regB = Integer.parseInt(sc.nextLine().substring(12).trim());
            regC = Integer.parseInt(sc.nextLine().substring(12).trim());

            sc.nextLine();
            char[] arr = sc.nextLine().substring(9).toCharArray();
            for (int i = 0; i < arr.length; i += 2) {
                instructions.add(arr[i]);
            }
        }

        ArrayList<Integer> execute(int regA, int regB, int regC) {
            final ArrayList<Integer> output = new ArrayList<>();
            for (int i = 0; i < instructions.size(); i += 2) {
                final char opCode = instructions.get(i);
                final int litOperand = instructions.get(i + 1) - '0';
                final int comboOperand = switch (instructions.get(i + 1)) {
                    case '0' -> 0;
                    case '1' -> 1;
                    case '2' -> 2;
                    case '3' -> 3;
                    case '4' -> regA;
                    case '5' -> regB;
                    case '6' -> regC;
                    default -> throw new RuntimeException("NOOOOO");
                };

                switch (opCode) {
                    case '0' -> {
                        regA = (int) (regA / Math.pow(2, comboOperand));
                    }
                    case '1' -> {
                        regB = regB ^ litOperand;
                    }
                    case '2' -> {
                        regB = Math.floorMod(comboOperand, 8);
                    }
                    case '3' -> {
                        if (regA != 0) {
                            i = litOperand - 2;
                        }
                    }
                    case '4' -> {
                        regB = regB ^ regC;
                    }
                    case '5' -> {
                        output.add(Math.floorMod(comboOperand, 8));
                    }
                    case '6' -> {
                        regB = (int) (regA / Math.pow(2, comboOperand));
                    }
                    case '7' -> {
                        regC = (int) (regA / Math.pow(2, comboOperand));
                    }
                }
            }
            return output;
        }

        void vm(regA, regB, regC) {
            ArrayList<Integer> output = new ArrayList<>();
            while (regA != 0) {
                regB = Math.floorMod(regA, 8); //2,4,
                regB = regB ^ 5; //1,5,
                regC = (int) (regA / Math.pow(2, regB)); //7,5,
                regB = regB ^ 6;//1,6,
                regA = (int) (regA / Math.pow(2, 3));//0,3,
                regB = regB ^ regC;//4,0,
                output.add(Math.floorMod(regB, 8));//5,5,
            }
            System.out.print(output);
        }


        public void run() throws Exception {
            final ArrayList<Integer> output = execute(regA, regB, regC);
            int possibleRegA = 0;
            while (true) {
                System.out.println(possibleRegA);
                if (executeAndTest(possibleRegA, regB, regC)) {
                    System.out.print(possibleRegA);
                    return;
                }
                if (possibleRegA < 0) {
                    throw new Exception("INT IS TOO SMALL");
                }
                possibleRegA++;
            }

        }


    }

    public static class Part1 {
        int regA, regB, regC;
        ArrayList<Character> instructions;
        ArrayList<Integer> output;

        private void init() throws Exception {
            instructions = new ArrayList<>();
            output = new ArrayList<>();

            Scanner sc = new Scanner(new File(FILE));
            regA = Integer.parseInt(sc.nextLine().substring(12).trim());
            regB = Integer.parseInt(sc.nextLine().substring(12).trim());
            regC = Integer.parseInt(sc.nextLine().substring(12).trim());

            sc.nextLine();
            char[] arr = sc.nextLine().substring(9).toCharArray();
            for (int i = 0; i < arr.length; i += 2) {
                instructions.add(arr[i]);
            }
        }

        int getOperand(char op) {
            return switch (op) {
                case '0' -> 0;
                case '1' -> 1;
                case '2' -> 2;
                case '3' -> 3;
                case '4' -> regA;
                case '5' -> regB;
                case '6' -> regC;
                default -> throw new RuntimeException("NOOOOO");
            };
        }

        public void run() throws Exception {
            init();
            for (int i = 0; i < instructions.size(); i += 2) {
                int opCode = instructions.get(i);
                int litOperand = instructions.get(i + 1) - '0';
                int comboOperand = getOperand(instructions.get(i + 1));

                switch (opCode) {
                    case '0' -> {
                        regA = (int) (regA / Math.pow(2, comboOperand));
                    }
                    case '1' -> {
                        regB = regB ^ litOperand;
                    }
                    case '2' -> {
                        regB = Math.floorMod(comboOperand, 8);
                    }
                    case '3' -> {
                        if (regA != 0) {
                            i = litOperand - 2;
                        }
                    }
                    case '4' -> {
                        regB = regB ^ regC;
                    }
                    case '5' -> {
                        output.add(Math.floorMod(comboOperand, 8));
                    }
                    case '6' -> {
                        regB = (int) (regA / Math.pow(2, comboOperand));
                    }
                    case '7' -> {
                        regC = (int) (regA / Math.pow(2, comboOperand));
                    }
                }
            }
            System.out.print(output);
        }


    }
}
