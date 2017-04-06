package net.iryndin.expreval;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Token emitter which takes tokens from a string expression
 *
 * @author iryndin
 * @since 06/04/17
 */
public class StringTokenEmitter implements ITokenEmitter {

    private final List<String> list;
    private Iterator<String> iter;

    public StringTokenEmitter(String expression) {
        this.list = parseExpression(expression);
        this.iter = list.iterator();
    }

    static List<String> parseExpression(String expression) {
        List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder(16);
        for (char c : expression.toCharArray()) {
            if (Character.isWhitespace(c)) {
                if (sb.length() > 0) {
                    result.add(sb.toString());
                    sb.setLength(0);
                }
            } else if (c == '+') {
                if (sb.length() > 0) {
                    result.add(sb.toString());
                    sb.setLength(0);
                }
                result.add("+");
            } else if (c == '*') {
                if (sb.length() > 0) {
                    result.add(sb.toString());
                    sb.setLength(0);
                }
                result.add("*");
            } else if (Character.isDigit(c)) {
                sb.append(c);
            }
        }
        if (sb.length() > 0) {
            result.add(sb.toString());
            sb.setLength(0);
        }
        return result;
    }

    @Override
    public String toString() {
        return "StringTokenEmitter{" +
                "list=" + list +
                '}';
    }

    @Override
    public Token emit() throws EvalException {
        if (iter.hasNext()) {
            return Token.fromString(iter.next());
        }
        return null;
    }

    public static void main(String[] args) {
        StringTokenEmitter e = new StringTokenEmitter("  1 + 2*  5* 6* 7*8 *9+ \t5*6*\t7*8+6+7+8*7");
        System.out.println(e);
    }
}
