import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicLong;

public class PlutonianPebbles {
    //0, 1, 10, 99, 999
    //125, 17
    //6563348, 67, 395, 0, 6, 4425, 89567, 739318
    static Node head;
    static int length;

    public static void main(String[] args) {
        //init(new int[]{6563348});//, 67, 395, 0, 6, 4425, 89567, 739318});
        //part1();
        //Print();
        //System.out.println(length);

        part2_SomeOneElseSolution();
    }

    static void init(int[] arr) {
        head = new Node(arr[0]);
        Node prevNode = head;
        for (int i = 1; i < arr.length; i++) {
            prevNode.next = new Node(arr[i]);
            prevNode = prevNode.next;
        }
        length = arr.length;
    }

    static void Print() {
        Node currNode = head;
        ArrayList<Long> out = new ArrayList<>();
        while (currNode != null) {
            out.add(currNode.value);
            currNode = currNode.next;
        }
        System.out.println(out);
    }

    static void part1() {
        for (int z = 0; z < 25; z++) {
            Node currNode = head;
            while (currNode != null) {
                if (currNode.value == 0) {
                    currNode.value = 1;
                    currNode = currNode.next;
                    continue;
                }

                String val = Long.toString(currNode.value);
                if (val.length() % 2 == 0) {
                    currNode.value = Long.parseLong(val.substring(0, val.length() / 2));

                    Node nextNode = currNode.next;
                    currNode.next = new Node(Long.parseLong(val.substring(val.length() / 2)), nextNode);
                    currNode = nextNode;

                    length++;
                    continue;
                }

                currNode.value *= 2024;
                currNode = currNode.next;
            }
            System.out.println(length + " : " + z + "/74");
        }
    }

    // https://github.com/meier-andersen/AoC/blob/main/src/code/2024/11/code/2.js
    static void part2_SomeOneElseSolution() {
        // k -> number, v -> occurrences
        HashMap<Long, AtomicLong> stones = new HashMap<>() {{
            put(6563348L, new AtomicLong(1L));
            put(67L, new AtomicLong(1L));
            put(395L, new AtomicLong(1L));
            put(0L, new AtomicLong(1L));
            put(6L, new AtomicLong(1L));
            put(4425L, new AtomicLong(1L));
            put(89567L, new AtomicLong(1L));
            put(739318L, new AtomicLong(1L));
        }};
        for (int run = 0; run < 75; run++) {
            HashMap<Long, AtomicLong> newStones = new HashMap<>();
            stones.forEach((number, occur) -> {
                if (number == 0) {
                    newStones.computeIfAbsent(1L, v -> new AtomicLong()).addAndGet(occur.get());
                    return;
                }

                String val = Long.toString(number);
                if (val.length() % 2 == 0) {
                    long num1 = Long.parseLong(val.substring(0, val.length() / 2));
                    newStones.computeIfAbsent(num1, v -> new AtomicLong()).addAndGet(occur.get());

                    long num2 = Long.parseLong(val.substring(val.length() / 2));
                    newStones.computeIfAbsent(num2, v -> new AtomicLong()).addAndGet(occur.get());
                    return;
                }

                long num = number * 2024;
                newStones.computeIfAbsent(num, v -> new AtomicLong()).addAndGet(occur.get());
            });
            stones = newStones;
        }

        AtomicLong sum = new AtomicLong();
        stones.forEach((k,v) -> {
            sum.addAndGet(v.get());
        });
        System.out.println(sum.get());
    }


    static void part2() {
        int[] startNumbers = new int[]{125, 17};
        final int iterations = 6;
        HashMap<String, Long> stateToSizeMap;
        long sum = 0;
        for (int n : startNumbers) {
            Stack<hehe> st = new Stack<>();
            st.add(new hehe((long) n, iterations));
            while (!st.empty()) {
                hehe x = st.pop();

            }
        }
    }

    public record hehe(Long number, int iteration) {
        public String state() {
            return number + "," + iteration;
        }
    }

    static class CachedNode {
        // HashMap | k -> number-iterationsLeft, v -> size
        HashMap<Long, Long> numberToSizeMap;

    }

//    static HashMap<Long, StateCache> pp;
//
//    static class StateCache {
//        public long offset;
//        public State state;
//        public StateCache(long offset, State state) {
//            this.offset = offset;
//            this.state = state;
//        }
//    }
//
//    static class State {
//        public long[] lengthAtState;
//        public Node head;
//        public long length;
//        public long currentState;
//
//    }

    static class Node {
        public Node next;
        public long value;

        public Node(long value) {
            this.value = value;
            next = null;
        }

        public Node(long value, Node next) {
            this.value = value;
            this.next = next;
        }

    }
}


