package net.iryndin.quoteservice.dto;

import java.math.BigDecimal;
import java.util.Date;

public class QuoteDTO {
    private final SymbolEnum symbol;
    private final BigDecimal price;
    private final Date timestamp;

    public QuoteDTO(SymbolEnum symbol, BigDecimal price, Date timestamp) {
        this.symbol = symbol;
        this.price = price;
        this.timestamp = timestamp;
    }

    public SymbolEnum getSymbol() {
        return symbol;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "QuoteDTO{" +
                "symbol=" + symbol +
                ", price=" + price +
                ", timestamp=" + timestamp +
                '}';
    }
}
