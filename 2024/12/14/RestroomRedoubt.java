import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class RestroomRedoubt {
    public static final String file = "data";
    public static final int ITERATIONS = 10000;
    public static final int ROWS = 103; //7;
    public static final int COLS = 101; //11;
    public static final int MIDROW = ROWS / 2;
    public static final int MIDCOL = COLS / 2;

    public static void main(String[] args) throws Exception {
        new part2().run();
    }

    public static class part1 {
        ArrayList<Robot> robots = new ArrayList<>();

        public void run() throws Exception {
            Scanner sc = new Scanner(new File(file));
            while (sc.hasNext()) {
                String[] arr = sc.nextLine().split(" ");
                String[] pos = arr[0].substring(2).split(",");
                String[] vel = arr[1].substring(2).split(",");
                Robot r = new Robot(Integer.parseInt(pos[1]), Integer.parseInt(pos[0]), Integer.parseInt(vel[1]), Integer.parseInt(vel[0]));
                robots.add(r);
            }
            sc.close();

            int q1 = 0, q2 = 0, q3 = 0, q4 = 0;
            for (Robot r : robots) {
                int finalRow = Math.floorMod((r.vRow * ITERATIONS + r.startRow), ROWS);
                int finalCol = Math.floorMod((r.vCol * ITERATIONS + r.startCol), COLS);

                if (finalRow < MIDROW && finalCol < MIDCOL) q1++;
                else if (finalRow < MIDROW && finalCol > MIDCOL) q2++;
                else if (finalRow > MIDROW && finalCol < MIDCOL) q3++;
                else if (finalRow > MIDROW && finalCol > MIDCOL) q4++;
            }
            System.out.println(q1 + "," + q2 + "," + q3 + "," + q4);
            System.out.println(q1 * q2 * q3 * q4);
        }

        private record Robot(int startRow, int startCol, int vRow, int vCol) {
        }
    }

    public static class part2 {
        ArrayList<Robot> robots = new ArrayList<>();

        public void run() throws Exception {
            Scanner sc = new Scanner(new File(file));
            while (sc.hasNext()) {
                String[] arr = sc.nextLine().split(" ");
                String[] pos = arr[0].substring(2).split(",");
                String[] vel = arr[1].substring(2).split(",");
                Robot r = new Robot(Integer.parseInt(pos[1]), Integer.parseInt(pos[0]), Integer.parseInt(vel[1]), Integer.parseInt(vel[0]));
                robots.add(r);
            }
            sc.close();

            for (int i = 1; i < ITERATIONS + 1; i++) {
                int q1 = 0, q2 = 0, q3 = 0, q4 = 0;
                int[][] map = new int[ROWS][COLS];
                for (Robot r : robots) {
                    int finalRow = Math.floorMod((r.vRow * i + r.startRow), ROWS);
                    int finalCol = Math.floorMod((r.vCol * i + r.startCol), COLS);

                    if (finalRow < MIDROW && finalCol < MIDCOL) q1++;
                    else if (finalRow < MIDROW && finalCol > MIDCOL) q2++;
                    else if (finalRow > MIDROW && finalCol < MIDCOL) q3++;
                    else if (finalRow > MIDROW && finalCol > MIDCOL) q4++;
                    map[finalRow][finalCol]++;
                }
                int safety = q1 * q2 * q3 * q4;

                boolean base = false;
                for (int[] row : map) {
                    int count= 0;
                    int bef = 0;
                    for (int x : row) {
                        if (count >= 7) {
                            base = true;
                            break;
                        }
                        if (bef > 0 && x > 0) {
                            count++;
                        }
                        else {
                            count = 0;
                        }
                        bef = x;
                    }

                }

                if (base) {
                    System.out.println(safety);
                    System.out.println(i);
                    for (int[] row : map) {
                        for (int x : row) {
                            if (x > 0) System.out.print("\u001B[42m"+x+"\u001B[0m");
                            else System.out.print(' ');
                        }
                        System.out.println();
                    }
                    System.out.println();
                }
            }
        }

        private record Robot(int startRow, int startCol, int vRow, int vCol) {
        }
    }
}
