package net.iryndin.quoteservice.dto;

import com.google.common.collect.ImmutableList;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by iryndin on 15.05.14.
 */
public class TrendBarHistory {
    private final SymbolEnum symbol;
    private final TrendBarPeriodEnum period;
    private final List<TrendBarDTO> list = new CopyOnWriteArrayList<>();

    private AtomicReference<Date> latestDateRef = new AtomicReference<>();

    public TrendBarHistory(SymbolEnum symbol, TrendBarPeriodEnum period) {
        this.symbol = symbol;
        this.period = period;
    }

    public SymbolEnum getSymbol() {
        return symbol;
    }

    public TrendBarPeriodEnum getPeriod() {
        return period;
    }

    public List<TrendBarDTO> getList() {
        return new ImmutableList.Builder<TrendBarDTO>().addAll(list).build();
    }

    public void add(TrendBarDTO t) {
        Date latestDate = latestDateRef.get();
        Date tbDate = t.getTimestamp();
        if (latestDate != null && tbDate.before(latestDate)) {
            throw new IllegalArgumentException("TrendBar timestamp is earlier than previous trendbar timestamp!");
        }
        list.add(t);
    }
}
