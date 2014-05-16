package net.iryndin.quoteservice.provider;

import net.iryndin.quoteservice.dto.QuoteDTO;
import net.iryndin.quoteservice.dto.SymbolEnum;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class QuoteProvider {

    private final SymbolEnum symbol;

    public QuoteProvider(SymbolEnum symbol) {
        this.symbol = symbol;
    }

    public QuoteDTO getQuote() {
        double price1 = new Random().nextDouble();
        int mul = new Random().nextInt(200)+1;
        double price2 = price1*mul;
        BigDecimal priceBd = BigDecimal.valueOf(price2);
        return new QuoteDTO(symbol, priceBd, new Date());
    }

    public List<QuoteDTO> getQuotes(int n) {
        List<QuoteDTO> list = new ArrayList<>(n);
        while (n>0) {
            list.add(getQuote());
            n--;
        }
        return list;
    }

    public SymbolEnum getSymbol() {
        return symbol;
    }
}
