package exante;
import scala.jdk.CollectionConverters._
import java.util.stream.Collectors

import util.control.Breaks._
import java.util

import org.junit.Assert.assertEquals

import scala.collection.immutable

/**
 * Максимальное кол-во повторяющихся квадратных корней
 */
object Task2Scala {

    val UPPER_LIMIT: Int = 1000000000
    val UPPER_LIMIT_SQRT: Int = Math.ceil(Math.sqrt(UPPER_LIMIT)).toInt

    val squareMap = new util.TreeMap[Integer, util.SortedSet[Long]]((o1: Integer, o2: Integer) => o2 - o1)

    def test(): Unit = {
        assertEquals(4, solution(2, UPPER_LIMIT))
        assertEquals(3, solution(6000, 7000))
        assertEquals(1, solution(3, 5))
        assertEquals(2, solution(16, 16))
        println("Tests passed")
    }

    def main(args: Array[String]): Unit = {
        test()
    }

    def solution(a: Int, b: Int): Int = {
        if (b < a) throw new IllegalArgumentException("a should be LE than b")
        if (a < 2) throw new IllegalArgumentException("a should be GE than 2")
        if (b > UPPER_LIMIT) throw new IllegalArgumentException("b should be LE than " + UPPER_LIMIT)
        if (squareMap.isEmpty) clearAndFillMap()
        if (a == b) {
            for ((k,v) <- squareMap.asScala) {
                if (v.contains(a.toLong)) return k
            }
        }
        else {
            for ((k,v) <- squareMap.asScala) {
                for (square <- v.asScala) {
                    if (a <= square && square <= b) return k
                }
            }
        }
        0
    }

    def leUpperLimitSqrt(n: Long): Boolean = n <= UPPER_LIMIT_SQRT

    def clearAndFillMap(): Unit = {
        squareMap.clear()
        // fill 0
        fill0()
        // fill next
        var squareRootNumber = 1
        breakable {
            while (true) {
                val previousSquares: util.SortedSet[Long] = squareMap.get(squareRootNumber - 1)
                val first = previousSquares.first()
                if (first > UPPER_LIMIT_SQRT) break
                //val squares = previousSquares.stream().map(s => s*s).filter(s => (s <= 1))
                //new TreeSet[Long]()
                val nextSquaresList = previousSquares.stream()
                  .filter(leUpperLimitSqrt)
                  .map(s => s * s)
                  .collect(Collectors.toList)
                squareMap.put(squareRootNumber, new util.TreeSet[Long](nextSquaresList))
                squareRootNumber += 1
            }
        }
    }

    def fill0(): Unit = {
        import scala.language.postfixOps
        val numbers: immutable.List[Long] = 2.toLong to UPPER_LIMIT_SQRT.toLong toList
        val seed = new util.TreeSet[Long](numbers.asJava)
        squareMap.put(0, seed)
    }

    def sqrtInt(a: Int): Int = {
        val sqrt = Math.sqrt(a)
        val b = Math.round(sqrt).toInt
        if (b * b == a) b else -1
    }
}
