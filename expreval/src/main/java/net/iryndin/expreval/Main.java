package net.iryndin.expreval;

/**
 * @author iryndin
 * @since 06/04/17
 */
public class Main {
    public static void main(String[] args) throws EvalException {
        ExpressionEvaluator ee = new ExpressionEvaluator(new StringTokenEmitter("1 +2*3+7"));
        System.out.println(ee.eval());
    }
}
