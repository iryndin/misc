package exante;

import org.junit.Assert.assertEquals

/**
 * Минимально возможное число той же разрядности
 */
fun main() {
    test1()
    testAll()
}

fun test1() {
    assertEquals(0, solution(1))
    assertEquals(0, solution(9))
    assertEquals(100, solution(143))
    assertEquals(10000, solution(99999))
}

fun testAll() {
    for (i in 1..9) {
        universalTest(i)
    }
}

fun universalTest(digits: Int) {
    println("Running test for all integers with digits: $digits")
    val answer = if (digits == 1) 0 else pow10(digits-1)
    val min = pow10(digits-1)
    val max = min*10-1
    for (i in min..max) {
        val result = solution(i)
        if (result != answer) {
            throw IllegalStateException("for $i expected answer is $answer, but got $result")
        }
    }
}

/**
 * Возвращает минимальное число такой же разрядности, как и n
 *
 * @param n
 * @return
 */
fun solution(n: Int): Int {
    val digits = getDigitsQty(n)
    return if (digits == 1) {
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
fun getDigitsQty(n: Int): Int {
    if (n < 0) {
        throw IllegalArgumentException("n should be positive: $n")
    }
    var k=0
    var nn = n
    while (nn > 0) {
        k++
        nn /= 10
    }
    return k
}

/**
 * Возводит 10 в степень n
 *
 * @param n
 * @return
 */
fun pow10(n: Int): Int {
    if (n < 0) {
        throw IllegalArgumentException("n should be positive: $n")
    }
    return if (n == 0) {
        1
    } else {
        var res = 1
        for (i in 1..n) {
            res *= 10
        }
        res
    }
}

