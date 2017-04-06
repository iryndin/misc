package net.iryndin.expreval;

/**
 * @author iryndin
 * @since 06/04/17
 */
public class EvalException extends Exception{

    public EvalException(String s) {
        super(s);
    }

    public EvalException(String message, Throwable cause) {
        super(message, cause);
    }
}
