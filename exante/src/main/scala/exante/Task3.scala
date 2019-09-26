package exante

import org.junit.Assert.assertEquals

/**
 * Минимально возможное число той же разрядности
 */
object Task3Scala {

    def main(args: Array[String]): Unit = {
        test1()
        testAll()
    }

    def test1(): Unit = {
        assertEquals(0, solution(1))
        assertEquals(0, solution(9))
        assertEquals(100, solution(143))
        assertEquals(10000, solution(99999))
        println("test1 passed")
    }

    def testAll(): Unit = {
        for (i <- 1 to 9) {
            universalTest(i)
        }
        println("All universal tests passed")
    }

    def universalTest(digits: Int): Unit = {
        println(s"Running test for all integers with digits: $digits")
        val answer = if (digits == 1) 0 else pow10(digits-1)
        val min = pow10(digits-1)
        val max = min*10
        for (i <- min until max) {
            val result = solution(i)
            if (result != answer) {
                throw new IllegalStateException(s"for $i expected answer is $answer, but got $result")
            }
        }
    }

    /**
     * Возвращает минимальное число такой же разрядности, как и n
     *
     * @param n
     * @return
     */
    def solution(n: Int): Int = {
        val digits = getDigitsQty(n)
        if (digits == 1) {
            0
        } else {
            pow10(digits-1)
        }
    }

    /**
     * Получить кол-во разрядов в числе
     * @param n
     * @return
     */
    def getDigitsQty(n: Int): Int = {
        if (n < 0) {
            throw new IllegalArgumentException(s"n should be positive: $n")
        }
        var k = 0
        var nn = n
        while (nn > 0) {
            nn /= 10
            k += 1
        }
        k
    }

    /**
     * Возводит 10 в степень n
     *
     * @param n
     * @return
     */
    def pow10(n: Int): Int = {
        if (n < 0) {
            throw new IllegalArgumentException(s"n should be positive: $n")
        }
        if (n == 0) {
            1
        } else {
            var res = 1
            for (_ <- 0 until n) {
                res = res * 10
            }
            res
        }
    }
}
