package net.iryndin.expreval;

/**
 * Emit tokens. Can emit tokens from string, or from infinite incoming stream.
 *
 * @author iryndin
 * @since 06/04/17
 */
public interface ITokenEmitter {
    /**
     * Emit tokens in the order they income to this emitter.
     * Token may be an integer number, or "+" or "*".
     * Return null when tokens are ended.
     * The call to this method may block current thread until new token comes or end of list/stream of tokens is reached.
     *
     * @return next emitted token or null when no tokens anymore
     * @throws EvalException when evaluation error occurs
     */
    Token emit() throws EvalException;
}
