package exante;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Максимальное кол-во повторяющихся квадратных корней
 */
public class Task2 {

    static int UPPER_LIMIT = 1_000_000_000;

    static final SortedMap<Integer, SortedSet<Long>> squareMap = new TreeMap<>((a,b) -> b-a);

    @Test
    public void test() {
        assertEquals(4, solution(2, UPPER_LIMIT));
        //assertEquals(3, solution(6000, 7000));
        //assertEquals(1, solution1(3, 5));
        assertEquals(2, solution1(16, 16));
    }

    public static void main(String[] args) {
        assertEquals(4, solution(2, UPPER_LIMIT));
        //assertEquals(3, solution(6000, 7000));
        //assertEquals(1, solution1(3, 5));
        assertEquals(2, solution1(16, 16));
    }

    static void clearAndFillMap() {
        squareMap.clear();
        // fill 0
        fill0();
        int squareRootNumber = 1;
        while (true) {
            SortedSet<Long> previousSquares = squareMap.get(squareRootNumber-1);
            long first = previousSquares.first();
            if (first*first > UPPER_LIMIT) {
                break;
            }
            SortedSet<Long> squares = new TreeSet<>();
            for (long s : previousSquares) {
                long ss = s*s;
                if (ss > UPPER_LIMIT) {
                    break;
                }
                squares.add(ss);
            }
            squareMap.put(squareRootNumber, squares);
            squareRootNumber++;
        }
    }

    private static final void fill0() {
        long max = (long)Math.ceil(Math.sqrt(UPPER_LIMIT));
        SortedSet<Long> seed = new TreeSet<>();
        for (long i=2; i<=max; i++) {
            seed.add(i);
        }
        squareMap.put(0, seed);
    }

    static int solution(int a, int b) {
        if (b < a) {
            throw new IllegalArgumentException("a should be LE than b");
        }
        if (a < 2) {
            throw new IllegalArgumentException("a should be GE than 2");
        }
        if (b > UPPER_LIMIT) {
            throw new IllegalArgumentException("b should be LE than " + UPPER_LIMIT);
        }
        if (squareMap.isEmpty()) {
            clearAndFillMap();
        }
        if (a == b) {
            for (Map.Entry<Integer, SortedSet<Long>> e : squareMap.entrySet()) {
                if (e.getValue().contains((long)a)) {
                    return e.getKey();
                }
            }
        } else {
            for (Map.Entry<Integer, SortedSet<Long>> e : squareMap.entrySet()) {
                for (long square : e.getValue()) {
                    if (a <= square && square <= b) {
                        return e.getKey();
                    }
                }
            }
        }
        return 0;
    }

    static int solution1(int a, int b) {
        if (a > b) {
            throw new IllegalArgumentException("a should be LE than b");
        }
        if (a < 2) {
            throw new IllegalArgumentException("a should be GE than 2");
        }
        if (b > UPPER_LIMIT) {
            throw new IllegalArgumentException("b should be LE than " + UPPER_LIMIT);
        }

        int result = 0;
        if (a == b) {
            int c = a;
            while ((c = sqrtInt(c)) != -1) {
                result++;
            }
        } else {
            // a < b

        }

        return result;
    }

    static int sqrtInt(int a) {
        double sqrt = Math.sqrt(a);
        int b = (int)Math.round(sqrt);
        return b*b == a ? b : -1;
    }
}
