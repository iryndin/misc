package exante;

import org.junit.Assert.assertEquals
import java.util.Arrays.asList

object Task1 {

    const val OPEN_PAR = '('
    const val CLOSE_PAR = ')'

    fun allTests() {
        test1()
        test2()
        test3()
    }

    fun test1() {
        val res = asList("()")
        assertEquals(res, solution(1))
    }

    fun test2() {
        val res = asList("()()", "(())")
        assertEquals(res, solution(2))
    }

    fun test3() {
        val res = asList("()()()","()(())","(())()","(()())","((()))")
        assertEquals(res, solution(3))
    }

    fun solution(n: Int): List<String> {
        if (n < 1) {
            throw IllegalArgumentException("n should be >= 1")
        }
        val result = ArrayList<String>()
        generateParenthesis(result, CharArray(n*2), n, 0, 0,0)
        return result
    }

    fun generateParenthesis(list: MutableList<String>, buffer: CharArray, n: Int, pos: Int, openb: Int, close: Int) {
        if (close == n) {
            list += String(buffer)
        } else {
            if (openb > close) {
                buffer[pos] = CLOSE_PAR
                generateParenthesis(list, buffer, n, pos+1, openb, close+1)
            }
            if (openb < n) {
                buffer[pos] = OPEN_PAR
                generateParenthesis(list, buffer, n, pos+1, openb+1, close)
            }
        }
    }
}

fun main() {
    Task1.allTests()
}