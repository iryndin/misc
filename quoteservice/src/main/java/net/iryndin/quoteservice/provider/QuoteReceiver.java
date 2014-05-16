package net.iryndin.quoteservice.provider;

import net.iryndin.quoteservice.dto.QuoteDTO;

import java.util.List;

/**
 * Created by iryndin on 15.05.14.
 */
public interface QuoteReceiver {
    void sendQuotes(List<QuoteDTO> quotes);
}
