package exante;

import java.util

import org.junit.Assert.assertEquals

object Task1Scala {

    val OPEN_PAR = '('
    val CLOSE_PAR = ')'

    def main(args: Array[String]) = {
        test1()
        test2()
        test3()
    }

    def test1(): Unit = {
        val res = util.Arrays.asList("()")
        assertEquals(res, solution(1))
        println("test1 passed")
    }

    def test2(): Unit = {
        val res = util.Arrays.asList("()()", "(())")
        assertEquals(res, solution(2))
        println("test2 passed")
    }

    def test3() {
        val res = util.Arrays.asList("()()()","()(())","(())()","(()())","((()))")
        assertEquals(res, solution(3))
        println("test3 passed")
    }

    def solution(n: Int): util.List[String] = {
        if (n < 1) {
            throw new IllegalArgumentException("n should be >= 1")
        }
        val result = new util.ArrayList[String]()
        generateParenthesis(result, new Array[Char](n*2), n, 0, 0,0)
        result
    }

    def generateParenthesis(list: util.List[String], buffer: Array[Char], n: Int, pos: Int, open: Int, close: Int): Unit = {
        if (close == n) {
            list.add(new String(buffer))
        } else {
            if (open > close) {
                buffer(pos) = CLOSE_PAR
                generateParenthesis(list, buffer, n, pos+1, open, close+1)
            }
            if (open < n) {
                buffer(pos) = OPEN_PAR
                generateParenthesis(list, buffer, n, pos+1, open+1, close)
            }
        }
    }
}
