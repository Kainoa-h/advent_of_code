import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class ClawContraption {
    public static void main(String[] args) throws Exception {
        part2();
    }
    
    static void part1() throws Exception {
        ArrayList<Machine> machines = init("data.txt");
        long totalTokenCost = 0;
        for (Machine m : machines) {
            Button good = m.B, bad = m.A;
            if (m.A.distancePerToken > m.B.distancePerToken) {
                good = m.A;
                bad = m.B;
            }
            long goodCount = Math.min(m.prizeX / good.x, m.prizeY / good.y);
            long badCount = 0;

            while (goodCount != 0) {
                long remainX = m.prizeX - (good.x * goodCount);
                long remainY = m.prizeY - (good.y * goodCount);
                long badPressX = remainX/bad.x;
                long badPressY = remainY/bad.y;
                badCount = Math.min(badPressX,badPressY);

                long x = good.x * goodCount + bad.x * badCount;
                long y = good.y * goodCount + bad.y * badCount;

                if (x == m.prizeX && y == m.prizeY) {
                    totalTokenCost += good.tokenCost * goodCount + bad.tokenCost * badCount;
                    break;
                }
                goodCount--;
            }
        }
        System.out.println(totalTokenCost);
    }

    // 8400 = 94*a + 22*b
    static void part2() throws Exception {
        ArrayList<Machine> machines = init2("test.txt");
        long totalTokenCost = 0;
        for (Machine m : machines) {
            Button good = m.B, bad = m.A;
            if (m.A.distancePerToken > m.B.distancePerToken) {
                good = m.A;
                bad = m.B;
            }
            long goodCount = Math.min(m.prizeX / good.x, m.prizeY / good.y);
            long badCount = 0;

            while (goodCount != 0) {
                long remainX = m.prizeX - (good.x * goodCount);
                long remainY = m.prizeY - (good.y * goodCount);
                long badPressX = remainX/bad.x;
                long badPressY = remainY/bad.y;
                badCount = Math.min(badPressX,badPressY);

                long x = good.x * goodCount + bad.x * badCount;
                long y = good.y * goodCount + bad.y * badCount;

                if (x == m.prizeX && y == m.prizeY) {
                    totalTokenCost += good.tokenCost * goodCount + bad.tokenCost * badCount;
                    break;
                }
                goodCount--;
            }
        }
        System.out.println(totalTokenCost);
    }


    static ArrayList<Machine> init(String path) throws Exception {
        ArrayList<Machine> machines = new ArrayList<>();
        Scanner sc = new Scanner(new File(path));
        while (sc.hasNext()) {
            Button a = new Button(sc.nextLine(), 3);
            Button b = new Button(sc.nextLine(), 1);
            Machine m = new Machine(a, b, sc.nextLine());
            machines.add(m);
            if (sc.hasNext()) sc.nextLine();
        }
        return machines;
    }

    static ArrayList<Machine> init2(String path) throws Exception {
        ArrayList<Machine> machines = new ArrayList<>();
        Scanner sc = new Scanner(new File(path));
        while (sc.hasNext()) {
            Button a = new Button(sc.nextLine(), 3);
            Button b = new Button(sc.nextLine(), 1);
            Machine m = new Machine(a, b, sc.nextLine(), true);
            machines.add(m);
            if (sc.hasNext()) sc.nextLine();
        }
        return machines;
    }

    static class Machine {
        public final Button A, B;
        public final long prizeX, prizeY;

        public Machine(Button A, Button B, int prizeX, int prizeY) {
            this.A = A;
            this.B = B;
            this.prizeX = prizeX;
            this.prizeY = prizeY;
        }

        public Machine(Button A, Button B, String strPrize) {
            this.A = A;
            this.B = B;
            String[] arr = strPrize.replaceAll("[ A-Za-z:=]", "").split(",");
            this.prizeX = Integer.parseInt(arr[0]);
            this.prizeY = Integer.parseInt(arr[1]);
        }

        public Machine(Button A, Button B, String strPrize, boolean isPart2) {
            this.A = A;
            this.B = B;
            String[] arr = strPrize.replaceAll("[ A-Za-z:=]", "").split(",");
            this.prizeX = Long.parseLong(arr[0]) + 10000000000000L;
            this.prizeY = Long.parseLong(arr[1]) + 10000000000000L;
        }

    }

    static class Button {
        public final int x, y;
        public final double distancePerToken;
        public final int tokenCost;

        public Button(int x, int y, int tokenCost) {
            this.x = x;
            this.y = y;
            this.tokenCost = tokenCost;
            this.distancePerToken = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) / tokenCost;
        }

        public Button(String str, int tokenCost) {
            String[] arr = str.replaceAll("[ A-Za-z:+=]", "").split(",");
            x = Integer.parseInt(arr[0]);
            y = Integer.parseInt(arr[1]);
            this.tokenCost = tokenCost;
            this.distancePerToken = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) / tokenCost;
        }
    }
}
