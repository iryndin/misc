package net.iryndin.expreval;

import static java.lang.String.format;

/**
 * Token class.
 * Token can be either integer positive number, or "+" operation, or "*" operation.
 *
 * @author iryndin
 * @since 06/04/17
 */
public class Token {

    public static final int TYPE_NUMBER = 1;
    public static final int TYPE_ADD = 2;
    public static final int TYPE_MULTIPLY = 3;

    private final long value;
    private final int type;

    private Token(long value, int type) {
        this.value = value;
        this.type = type;
    }

    public long getValue() {
        return value;
    }

    public int getType() {
        return type;
    }

    public boolean isOperation() {
        return type == TYPE_ADD || type == TYPE_MULTIPLY;
    }

    @Override
    public String toString() {
        switch (type) {
            case TYPE_ADD:
                return "+";
            case TYPE_MULTIPLY:
                return "*";
            default:
                return Long.toString(value);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;

        if (value != token.value) return false;
        return type == token.type;

    }

    @Override
    public int hashCode() {
        int result = (int) (value ^ (value >>> 32));
        result = 31 * result + type;
        return result;
    }

    public static Token createNumber(long n) {
        return new Token(n, TYPE_NUMBER);
    }

    public static Token createAddOperation() {
        return new Token(0, TYPE_ADD);
    }

    public static Token createMultiplyOperation() {
        return new Token(0, TYPE_MULTIPLY);
    }

    public static Token fromString(String s) throws EvalException {
        s = s.trim();
        if (s.equals("*")) {
            return createMultiplyOperation();
        } else if (s.equals("+")) {
            return createAddOperation();
        } else {
            try {
                return createNumber(Long.parseLong(s));
            } catch (NumberFormatException nfe) {
                throw new EvalException(format("Cannot evaluate token '%s'", s), nfe);
            }
        }
    }
}
