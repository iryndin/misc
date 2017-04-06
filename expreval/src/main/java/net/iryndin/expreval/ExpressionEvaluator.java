package net.iryndin.expreval;

import java.util.EmptyStackException;
import java.util.Stack;

/**
 * @author iryndin
 * @since 06/04/17
 */
public class ExpressionEvaluator {

    private final ITokenEmitter tokenEmitter;
    private Stack<Token> stack = new Stack<>();

    public ExpressionEvaluator(ITokenEmitter tokenEmitter) {
        this.tokenEmitter = tokenEmitter;
    }

    public long eval() throws EvalException {
        Token nextToken;

        while ((nextToken = tokenEmitter.emit()) != null) {
            if (nextToken.isOperation()) {
                if (stack.isEmpty()) {
                    throw new EvalException("No first operand for operation: '" + nextToken + "'");
                }
                if (stack.peek().isOperation()) {
                    throw new EvalException("Two operations in a row: '"
                            + stack.peek() + "' and '" + nextToken + "'");
                }
            } else {
                if (!stack.isEmpty() && !stack.peek().isOperation()) {
                    throw new EvalException("Two numbers in a row: '"
                            + stack.peek() + "' and '" + nextToken + "'");
                }
            }
            if (nextToken.getType() == Token.TYPE_ADD) {
                evalStack();
            }
            stack.push(nextToken);
        }
        evalStack();
        Token result = stack.pop();
        if (result.isOperation()) {
            throw new EvalException("Smth goes wrong!");
        }
        return result.getValue();
    }

    private void evalStack() throws EvalException {
        while (!stack.isEmpty()) {
            Token operand2 = stack.pop();
            if (stack.isEmpty()) {
                stack.push(operand2);
                break;
            } else {
                try {
                    Token operation = stack.pop();
                    Token operand1 = stack.pop();
                    if (!operation.isOperation() || operand1.isOperation() || operand2.isOperation()) {
                        throw new EvalException("Wrong!");
                    }
                    switch (operation.getType()) {
                        case Token.TYPE_ADD: {
                            long res = operand1.getValue() + operand2.getValue();
                            stack.push(Token.createNumber(res));
                            break;
                        }
                        case Token.TYPE_MULTIPLY: {
                            long res = operand1.getValue() * operand2.getValue();
                            stack.push(Token.createNumber(res));
                            break;
                        }
                    }
                } catch (EmptyStackException ese) {
                    throw new EvalException("No operation or operand1", ese);
                }
            }
        }
    }

    public static void main(String[] args) throws EvalException {
        StringTokenEmitter ste = new StringTokenEmitter("1+2*4*6+9");
        ExpressionEvaluator ee = new ExpressionEvaluator(ste);
        System.out.println(ee.eval());
    }

}

/*
1+2*5*6*7*8*9+5*6*7*8=
5*4 +3 +3 +5 +6*8*7*9*90
number - no tokens ?

1 2 5 6 7 8 9 5 6 7 8
+ * * * * * + * * *
 */