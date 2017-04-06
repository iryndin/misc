package net.iryndin.expreval;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author iryndin
 * @since 06/04/17
 */
public class StringTokenEmitterTest {

    @Test
    public void testParseExpression() {
        List<String> list = StringTokenEmitter.parseExpression("  1 + 2*  5* 6* 7*8 *9+ \t5*6*\t7*8+6+7+8*7");
        assertEquals(Arrays.asList("1", "+", "2", "*", "5", "*", "6", "*", "7", "*", "8", "*", "9",
                "+", "5", "*", "6", "*", "7", "*", "8", "+", "6", "+", "7", "+", "8", "*", "7"), list);

        assertEquals(Arrays.asList("1", "*"), StringTokenEmitter.parseExpression("1 * "));
    }

    @Test
    public void testStringTokenEmitter() throws EvalException {
        StringTokenEmitter ste = new StringTokenEmitter("1+2");
        assertEquals(Token.createNumber(1), ste.emit());
        assertEquals(Token.createAddOperation(), ste.emit());
        assertEquals(Token.createNumber(2), ste.emit());
        assertNull(ste.emit());
    }
}
