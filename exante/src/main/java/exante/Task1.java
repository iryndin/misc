package exante;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Task1 {

    static final char OPEN_PAR = '(';
    static final char CLOSE_PAR = ')';

    @Test
    public void test1() {
        List<String> res = Arrays.asList("()");
        assertEquals(res, solution(1));
    }

    @Test
    public void test2() {
        List<String> res = Arrays.asList("()()", "(())");
        assertEquals(res, solution(2));
    }

    @Test
    public void test3() {
        List<String> res = Arrays.asList("()()()","()(())","(())()","(()())","((()))");
        assertEquals(res, solution(3));
    }

    static List<String> solution(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("n should be >= 1");
        }
        List<String> result = new ArrayList<>();
        generateParenthesis(result, new char[n*2], n, 0, 0,0);
        return result;
    }

    static void generateParenthesis(List<String> list, char[] buffer, int n, int pos, int open, int close) {
        if (close == n) {
            list.add(new String(buffer));
        } else {
            if (open > close) {
                buffer[pos] = CLOSE_PAR;
                generateParenthesis(list, buffer, n, pos+1, open, close+1);
            }
            if (open < n) {
                buffer[pos] = OPEN_PAR;
                generateParenthesis(list, buffer, n, pos+1, open+1, close);
            }
        }
    }
}
