package net.iryndin.quoteservice.service;

import net.iryndin.quoteservice.dto.QuoteDTO;
import net.iryndin.quoteservice.dto.SymbolEnum;
import net.iryndin.quoteservice.dto.TrendBarDTO;
import net.iryndin.quoteservice.dto.TrendBarPeriodEnum;

import java.io.Closeable;
import java.util.Collection;
import java.util.Date;

/**
 * Created by iryndin on 15.05.14.
 */
public interface TrendBarService extends Closeable {
    /**
     * Accept quote
     * @param quote
     */
    void addQuote(QuoteDTO quote);

    /**
     * Provide trend bars history.
     *
     * @param symbol
     * @param period
     * @param from
     * @param to
     * @return list of completed trendbars
     */
    Collection<TrendBarDTO> getTrendBarHistory(SymbolEnum symbol, TrendBarPeriodEnum period, Date from, Date to);
}
