package net.iryndin.expreval;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author iryndin
 * @since 06/04/17
 */
public class ExpressionEvaluatorTest {

    @Test
    public void testEval() throws EvalException {
        ExpressionEvaluator ee = new ExpressionEvaluator(new StringTokenEmitter("1+2*3+7*8+1+2+3"));
        assertEquals(1+2*3+7*8+1+2+3, ee.eval());
    }

    @Test(expected = EvalException.class)
    public void testEvalFail1() throws EvalException {
        new ExpressionEvaluator(new StringTokenEmitter("+1")).eval();
    }

    @Test(expected = EvalException.class)
    public void testEvalFail2() throws EvalException {
        new ExpressionEvaluator(new StringTokenEmitter("1 * ")).eval();
    }

    @Test(expected = EvalException.class)
    public void testEvalFail3() throws EvalException {
        new ExpressionEvaluator(new StringTokenEmitter("1 2")).eval();
    }

    @Test(expected = EvalException.class)
    public void testEvalFail4() throws EvalException {
        new ExpressionEvaluator(new StringTokenEmitter("1+2 3*")).eval();
    }
}
