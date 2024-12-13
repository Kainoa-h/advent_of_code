import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class ClawContraption {
    public static void main(String[] args) throws Exception {
        part1();
    }

    static void part1() throws Exception {
        ArrayList<Machine> machines = init("data.txt");
        int totalTokenCost = 0;
        for (Machine m : machines) {
            Button good = m.B, bad = m.A;
            if (m.A.distancePerToken > m.B.distancePerToken) {
                good = m.A;
                bad = m.B;
            }
            int goodCount = Math.min(m.prizeX / good.x, m.prizeY / good.y);
            int badCount = 0;

            if (goodCount > 100) continue;

            while (goodCount != 0) {
                int remainX = m.prizeX - (good.x * goodCount);
                int remainY = m.prizeY - (good.y * goodCount);
                int badPressX = remainX/bad.x;
                int badPressY = remainY/bad.y;
                badCount = Math.min(badPressX,badPressY);

                int x = good.x * goodCount + bad.x * badCount;
                int y = good.y * goodCount + bad.y * badCount;

                if (x == m.prizeX && y == m.prizeY) {
//                    System.out.println(good.tokenCost * goodCount + bad.tokenCost * badCount);
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

    static class Machine {
        public final Button A, B;
        public final int prizeX, prizeY;

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
