package exante

import java.util.*
import kotlin.math.ceil
import kotlin.math.sqrt

import org.junit.Assert.assertEquals

/**
 * Максимальное кол-во повторяющихся квадратных корней
 */
object Task2 {
    val squareMap = TreeMap<Int, SortedSet<Long>>(Comparator.reverseOrder())
    const val UPPER_LIMIT = 1_000_000_000

    fun solution(a: Int, b: Int): Int {
        if (b < a) {
            throw IllegalArgumentException("a should be LE than b")
        }
        if (a < 2) {
            throw IllegalArgumentException("a should be GE than 2")
        }
        if (b > UPPER_LIMIT) {
            throw IllegalArgumentException("b should be LE than $UPPER_LIMIT")
        }
        if (squareMap.isEmpty()) {
            clearAndFillMap()
        }
        if (a == b) {
            for (e in squareMap.entries) {
                if (e.value.contains(a.toLong())) {
                    return e.key
                }
            }
        } else {
            for (e in squareMap.entries) {
                for (square in e.value) {
                    if (square in a..b) {
                        return e.key
                    }
                }
            }
        }
        return 0
    }

    fun clearAndFillMap() {
        squareMap.clear()
        // fill 0
        fill0()
        var squareRootNumber = 1
        while (true) {
            val previousSquares = squareMap[squareRootNumber-1]!!
            val first = previousSquares.first()
            if (first*first > UPPER_LIMIT) {
                break
            }
            val squares = TreeSet<Long>()
            for (s in previousSquares) {
                val ss = s*s
                if (ss > UPPER_LIMIT) {
                    break
                }
                squares.add(ss)
            }
            squareMap[squareRootNumber] = squares
            squareRootNumber++
        }
    }

    fun fill0() {
        val max = ceil(sqrt(UPPER_LIMIT.toDouble())).toLong()
        val seed = TreeSet<Long>()
        for (i in 2..max) {
            seed.add(i)
        }
        squareMap[0] = seed
    }
}

fun main() {
    assertEquals(4, Task2.solution(2, Task2.UPPER_LIMIT));
    assertEquals(3, Task2.solution(6000, 7000))
    assertEquals(1, Task2.solution(3, 5))
    assertEquals(2, Task2.solution(16, 16))
}