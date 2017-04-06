package net.iryndin.expreval;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author iryndin
 * @since 06/04/17
 */
public class TokenTest {

    @Test
    public void testFromString() throws EvalException {
        long[] a = {12,34,34,34,34,36,45,7656,756,756,75,7,657,67,65,765,78888888};
        for (long l : a) {
            assertEquals(l, Token.fromString(Long.toString(l)).getValue());
        }
        assertEquals(Token.TYPE_ADD, Token.fromString("+").getType());
        assertEquals(Token.TYPE_MULTIPLY, Token.fromString("*").getType());
    }

    @Test
    public void testFromStringFail() throws EvalException {
        String[] a = {"sdfsf", "5656dd", "0xcxcxcx", "23435wfsdc", "____", "2342wfasdt4357546uhdfghfdcxvx   "};
        for (String s : a) {
            try {
                Token.fromString(s);
                fail("Should not create token: "+ s);
            } catch (EvalException ee) {
                //
            }
        }
    }
}
