package exante;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Минимально возможное число той же разрядности
 */
public class Task3 {

    @Test
    public void test1() {
        assertEquals(0, solution(1));
        assertEquals(0, solution(9));
        assertEquals(100, solution(143));
        assertEquals(10000, solution(99999));
    }

    @Test
    public void testAll() {
        for (int i=1; i<=9; i++) {
            universalTest(i);
        }
    }

    static void universalTest(int digits) {
        System.out.println("Running test for all integers with digits: " + digits);
        final int answer = digits == 1 ? 0 : pow10(digits-1);
        final int min = pow10(digits-1);
        final int max = min*10;
        for (int i=min; i<max; i++) {
            int result = solution(i);
            if (result != answer) {
                throw new IllegalStateException(String.format("for %d expected answer is %d, but got %d", i, answer, result));
            }
        }
    }

    /**
     * Возвращает минимальное число такой же разрядности, как и n
     *
     * @param n
     * @return
     */
    static int solution(int n) {
        int digits = getDigitsQty(n);
        if (digits == 1) {
            return 0;
        } else {
            return pow10(digits-1);
        }
    }

    /**
     * Получить кол-во разрядов в числе
     * @param n
     * @return
     */
    static int getDigitsQty(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n should be positive: " + n);
        }
        int k=0;
        for (int nn = n; nn > 0; nn /= 10) {
            k++;
        }
        return k;
    }

    /**
     * Возводит 10 в степень n
     *
     * @param n
     * @return
     */
    static int pow10(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n should be positive: " + n);
        }
        if (n == 0) {
            return 1;
        } else {
            int res = 1;
            for (int i=0; i<n; i++) {
                res *= 10;
            }
            return res;
        }
    }
}
