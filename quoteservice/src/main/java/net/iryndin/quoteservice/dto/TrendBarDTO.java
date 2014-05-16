package net.iryndin.quoteservice.dto;

import java.math.BigDecimal;
import java.util.Date;

public class TrendBarDTO {
    private final SymbolEnum symbol;
    private final TrendBarPeriodEnum period;
    private BigDecimal openPrice;
    private BigDecimal closePrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private Date timestamp;


    public TrendBarDTO(SymbolEnum symbol, TrendBarPeriodEnum period, BigDecimal openPrice, BigDecimal closePrice, BigDecimal highPrice, BigDecimal lowPrice, Date timestamp) {
        this.symbol = symbol;
        this.period = period;
        this.openPrice = openPrice;
        this.closePrice = closePrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.timestamp = timestamp;
    }

    public SymbolEnum getSymbol() {
        return symbol;
    }

    public BigDecimal getOpenPrice() {
        return openPrice;
    }

    public BigDecimal getClosePrice() {
        return closePrice;
    }

    public BigDecimal getHighPrice() {
        return highPrice;
    }

    public BigDecimal getLowPrice() {
        return lowPrice;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public TrendBarPeriodEnum getPeriod() {
        return period;
    }

    public void setOpenPrice(BigDecimal openPrice) {
        this.openPrice = openPrice;
    }

    public void setClosePrice(BigDecimal closePrice) {
        this.closePrice = closePrice;
    }

    public void setHighPrice(BigDecimal highPrice) {
        this.highPrice = highPrice;
    }

    public void setLowPrice(BigDecimal lowPrice) {
        this.lowPrice = lowPrice;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
