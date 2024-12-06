import java.io.File;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class MullItOver {
    public static void main(String[] args) {
        part2();
    }

    static void part1() {
        String regex = "mul\\(-?\\d+,-?\\d+\\)";

        File f = new File("data1.txt");
        try (Scanner sc = new Scanner(f)) {
            int sum = 0;
            while (sc.hasNext()) {
                String line = sc.nextLine();
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(line);
                while (m.find()) {
                    String x = m.group();
                    String[] arr = x.split(",");
                    int left = Integer.parseInt(arr[0].substring(4));
                    int right = Integer.parseInt(arr[1].substring(0, arr[1].length() - 1));
                    sum += left * right;
                }
            }
            System.out.println(sum);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static void part2() {
        String regex = "mul\\(-?\\d+,-?\\d+\\)|do\\(\\)|don't\\(\\)";

        File f = new File("data1.txt");
        try (Scanner sc = new Scanner(f)) {
            int sum = 0;
            boolean isEnabled = true;
            while (sc.hasNext()) {
                String line = sc.nextLine();
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(line);
                while (m.find()) {
                    String x = m.group();

                    if (x.equals("do()")) {
                        isEnabled = true;
                        continue;
                    }

                    if (x.equals("don't()")) {
                        isEnabled = false;
                        continue;
                    }

                    if (!isEnabled) continue;

                    String[] arr = x.split(",");
                    int left = Integer.parseInt(arr[0].substring(4));
                    int right = Integer.parseInt(arr[1].substring(0, arr[1].length() - 1));
                    sum += left * right;
                }
            }
            System.out.println(sum);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}