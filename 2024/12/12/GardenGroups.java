import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class GardenGroups {
    static ArrayList<List<Plot>> map;

    public static void main(String[] args) throws Exception {
        part2();
        //PrintMap();
        //PrintRegions();
    }

    static void init(String path) throws Exception {
        map = new ArrayList<>();
        Scanner sc = new Scanner(new File(path));
        while (sc.hasNext()) {
            map.add(sc.nextLine().chars().mapToObj(x -> new Plot((char) x)).toList());
        }
    }

    static void PrintMap() {
        System.out.println("\n\n=== Map ===");
        map.forEach(x -> {
            x.forEach(z -> {
                System.out.print(TerminalColors.GetOffsetColor(z.plant) + z.plant);
            });
            System.out.println(TerminalColors.TEXT_NO_BG);
        });
    }

    static void PrintRegions() {
        System.out.println("\n\n=== Regions ===");
        map.forEach(x -> {
            x.forEach(z -> {
                System.out.print(TerminalColors.GetOffsetColor((int) z.region.id) + z.plant);
            });
            System.out.println(TerminalColors.TEXT_NO_BG);
        });
    }

    static void part1() throws Exception {
        init("data.txt");
        ArrayList<Region> regions = new ArrayList<>();
        for (int row = 0; row < map.size(); row++) {
            for (int col = 0; col < map.getFirst().size(); col++) {
                Plot plot = map.get(row).get(col);
                NeighboursInfo neighbours = part1_getNeighbours(plot.plant, row, col);
                if (neighbours.region == null) {
                    Region r = new Region();
                    regions.add(r);
                    plot.region = r;
                } else {
                    plot.region = neighbours.region;
                }

                plot.region.area++;
                plot.region.perimeter += 4 - neighbours.neighboursCount;
            }
        }
        long sum = 0;
        for (Region r : regions) {
            //System.out.printf("area(%s), perimeter(%s)%n", r.area, r.perimeter);
            sum += r.getCost();
        }
        System.out.println(sum);
    }

    /**
     * GODDAMN
     * THIS IS SO WRONG.
     * "test4p2-reddit-2-ans-452.txt" <- THE CODE FAILS FOR THIS TEST CASE
     * HACK fix with `Math.max(horizontalSides, verticalSides)*2`
     *
     * @throws Exception
     */
    static void part2() throws Exception {
        init("test4p2-reddit-2-ans-452.txt");
        ArrayList<Region> regions = new ArrayList<>();
        for (int row = 0; row < map.size(); row++) {
            for (int col = 0; col < map.getFirst().size(); col++) {
                Plot plot = map.get(row).get(col);
                NeighboursInfo neighbours = part1_getNeighbours(plot.plant, row, col);
                if (neighbours.region == null) {
                    Region r = new Region();
                    regions.add(r);
                    plot.region = r;
                } else {
                    plot.region = neighbours.region;
                }

                plot.region.area++;
            }
        }
        long sum = 0;
        for (Region r : regions) {
            long horizontalSides = 0;
            for (int row = -1; row < map.size() + 1; row++) {
                boolean onTopSide = false;
                boolean onBotSide = false;
                for (int col = 0; col < map.getFirst().size() + 1; col++) {
                    Plot top = part2_safeGetPlot(row, col);
                    Plot bot = part2_safeGetPlot(row + 1, col);

                    if (top.region.id == r.id && top.region != bot.region) {
                        onTopSide = true;
                        continue;
                    }

                    if (bot.region.id == r.id && top.region != bot.region) {
                        onBotSide = true;
                        continue;
                    }

                    if (onTopSide) {
                        horizontalSides++;
                        onTopSide = false;
                    }
                    if (onBotSide) {
                        horizontalSides++;
                        onBotSide = false;
                    }
                }
            }

            long verticalSides = 0;
            for (int col = -1; col < map.getFirst().size() + 1; col++) {
                boolean onLeftSide = false;
                boolean onRightSide = false;
                for (int row = 0; row < map.size() + 1; row++) {
                    Plot left = part2_safeGetPlot(row, col);
                    Plot right = part2_safeGetPlot(row, col + 1);

                    if (left.region.id == r.id && left.region != right.region) {
                        onLeftSide = true;
                        continue;
                    }

                    if (right.region.id == r.id && left.region != right.region) {
                        onRightSide = true;
                        continue;
                    }

                    if (onLeftSide) {
                        verticalSides++;
                        onLeftSide = false;
                    }

                    if (onRightSide) {
                        verticalSides++;
                        onRightSide = false;
                    }
                }
            }
            long sides = Math.max(horizontalSides, verticalSides)*2;
            long subTotal = sides * r.area;
            System.out.printf("Region %s, Hor:%s, Ver:%s | Cost: %s%n", r.id, horizontalSides, verticalSides, subTotal);

            sum += subTotal;
        }
        System.out.println(sum);
    }

    static Plot emptyPlot = new Plot('-', new Region());

    static Plot part2_safeGetPlot(int row, int col) {
        try {
            return map.get(row).get(col);
        } catch (Exception _) {
            return emptyPlot;
        }
    }


    static NeighboursInfo part1_getNeighbours(char plant, int row, int col) {
        long sum = 0;
        Region region = part1_recursivelyGetRegion(plant, row, col, new HashSet<>());
        if (row > 0 && map.get(row - 1).get(col).plant == plant) {
            sum++;
        }
        if (row < map.size() - 1 && map.get(row + 1).get(col).plant == plant) {
            sum++;
        }
        if (col > 0 && map.get(row).get(col - 1).plant == plant) {
            sum++;
        }
        if (col < map.getFirst().size() - 1 && map.get(row).get(col + 1).plant == plant) {
            sum++;
        }

        return new NeighboursInfo(sum, region);
    }

    static Region part1_recursivelyGetRegion(char plant, int row, int col, HashSet<String> visitedPlots) {
        visitedPlots.add(row + "," + col);
        Region region = null;
        if (row > 0 && map.get(row - 1).get(col).plant == plant) {
            Region r = map.get(row - 1).get(col).region;
            if (r != null) return r;
            if (!visitedPlots.contains(((row - 1) + "," + col)))
                region = part1_recursivelyGetRegion(plant, row - 1, col, visitedPlots);
        }
        if (row < map.size() - 1 && map.get(row + 1).get(col).plant == plant && region == null) {
            Region r = map.get(row + 1).get(col).region;
            if (r != null) return r;
            if (!visitedPlots.contains(((row + 1) + "," + col)))
                region = part1_recursivelyGetRegion(plant, row + 1, col, visitedPlots);
        }
        if (col > 0 && map.get(row).get(col - 1).plant == plant && region == null) {
            Region r = map.get(row).get(col - 1).region;
            if (r != null) return r;
            if (!visitedPlots.contains((row + "," + (col - 1))))
                region = part1_recursivelyGetRegion(plant, row, col - 1, visitedPlots);
        }
        if (col < map.getFirst().size() - 1 && map.get(row).get(col + 1).plant == plant && region == null) {
            Region r = map.get(row).get(col + 1).region;
            if (r != null) return r;
            if (!visitedPlots.contains((row + "," + (col + 1))))
                region = part1_recursivelyGetRegion(plant, row, col + 1, visitedPlots);
        }
        return region;
    }

    record NeighboursInfo(long neighboursCount, Region region) {
    }

    static class Region {
        static long REGIONCOUNTER = 0;
        public long id;
        public long area;
        public long perimeter;

        public Region() {
            id = REGIONCOUNTER;
            REGIONCOUNTER++;
        }

        public long getCost() {
            return area * perimeter;
        }
    }

    static class Plot {
        public final char plant;
        public Region region;

        public Plot(char plant) {
            this.plant = plant;
            this.region = null;
        }

        public Plot(char plant, Region region) {
            this.plant = plant;
            this.region = region;
        }
    }

    static class TerminalColors {
        private static final String TEXT_NO_BG = "\u001B[0m";
        public static final String TEXT_BRIGHT_BG_RED = "\u001B[101;30m";
        public static final String TEXT_BRIGHT_BG_GREEN = "\u001B[102;30m";
        public static final String TEXT_BRIGHT_BG_YELLOW = "\u001B[103;30m";
        public static final String TEXT_BRIGHT_BG_BLUE = "\u001B[104;30m";
        public static final String TEXT_BRIGHT_BG_PURPLE = "\u001B[105;30m";
        public static final String TEXT_BRIGHT_BG_CYAN = "\u001B[106;30m";
        public static final String TEXT_BRIGHT_BG_WHITE = "\u001B[107;30m";

        public static final String TEXT_BRIGHT_BLACK = "\u001B[90m";
        public static final String TEXT_BRIGHT_RED = "\u001B[91m";
        public static final String TEXT_BRIGHT_GREEN = "\u001B[92m";
        public static final String TEXT_BRIGHT_YELLOW = "\u001B[93m";
        public static final String TEXT_BRIGHT_BLUE = "\u001B[94m";
        public static final String TEXT_BRIGHT_PURPLE = "\u001B[95m";
        public static final String TEXT_BRIGHT_WHITE = "\u001B[97m";


        static final String[] TEXTCOLORS = new String[]{TEXT_BRIGHT_BLACK, TEXT_BRIGHT_RED, TEXT_BRIGHT_BG_GREEN, TEXT_BRIGHT_YELLOW, TEXT_BRIGHT_BG_BLUE, TEXT_BRIGHT_PURPLE, TEXT_BRIGHT_BG_WHITE};
        static int idx = 0;

        public static void Reset() {
            idx = 0;
        }

        public static String GetNextColor() {
            idx = idx == TEXTCOLORS.length - 1 ? 0 : idx + 1;
            return TEXTCOLORS[idx];
        }

        public static String GetOffsetColor(int idx) {
            return TEXT_NO_BG + TEXTCOLORS[idx % TEXTCOLORS.length];
        }
    }


}
