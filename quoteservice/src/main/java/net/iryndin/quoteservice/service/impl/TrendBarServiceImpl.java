package net.iryndin.quoteservice.service.impl;

import com.google.common.collect.ImmutableMap;
import net.iryndin.quoteservice.dto.*;
import net.iryndin.quoteservice.service.TrendBarService;
import net.iryndin.quoteservice.util.DateTimeUtils;
import net.iryndin.quoteservice.util.NumberUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class TrendBarServiceImpl implements TrendBarService {

    private BlockingQueue<QuoteDTO> quotesForProcessingQueue = new LinkedBlockingQueue<>();
    private AtomicReference<Date> prevDateRef = new AtomicReference<>();
    private Timer timer = new Timer();
    private final ImmutableMap<SymbolEnum,TrendBarHistory> trendBarHistoryHolderMinutely = buildTrendBarHistoryHolderForPeriod(TrendBarPeriodEnum.M1);
    private final ImmutableMap<SymbolEnum,TrendBarHistory> trendBarHistoryHolderHourly = buildTrendBarHistoryHolderForPeriod(TrendBarPeriodEnum.H1);
    private final ImmutableMap<SymbolEnum,TrendBarHistory> trendBarHistoryHolderDaily = buildTrendBarHistoryHolderForPeriod(TrendBarPeriodEnum.D1);

    static final int EMPTYVAL = -1;

    {
        scheduleTimerTask();
    }

    private void scheduleTimerTask() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                constructMinuteTBs();
                constructHourTb();
                constructDailyTb();
            }
        }, DateTimeUtils.getNextMinuteStart(), 1000*60);
    }

    private ImmutableMap<SymbolEnum,TrendBarHistory> buildTrendBarHistoryHolderForPeriod(TrendBarPeriodEnum period) {
        ImmutableMap.Builder<SymbolEnum,TrendBarHistory> builder = new ImmutableMap.Builder<>();
        for (SymbolEnum s : SymbolEnum.values()) {
            builder.put(s, new TrendBarHistory(s, period));
        }
        return builder.build();
    }

    private void constructMinuteTBs() {
        List<QuoteDTO> list = new LinkedList<>();
        quotesForProcessingQueue.drainTo(list);

        Map<SymbolEnum, TrendBarDTO> map = createQuotesHolderMap(DateTimeUtils.getPrevMinuteStart());
        fillTrendBars(map, list);

        for (TrendBarDTO tb : map.values()) {
            if (tb.getOpenPrice().intValue() == EMPTYVAL && tb.getClosePrice().intValue() == EMPTYVAL) {
                // this is unfilled trendbar
                continue;
            }
            trendBarHistoryHolderMinutely.get(tb.getSymbol()).add(tb);
        }
    }

    private void constructHourTb() {
        Calendar now = Calendar.getInstance();
        if (now.get(Calendar.MINUTE) != 0) {
            return;
        }
        for (TrendBarHistory history : trendBarHistoryHolderMinutely.values()) {
            buildPrevHourTrendbar(history);
        }
    }

    private void buildPrevHourTrendbar(TrendBarHistory minuteTrendbarsHistory) {
        List<TrendBarDTO> trendbars = minuteTrendbarsHistory.getList();
        if (trendbars.isEmpty()) {
            return;
        }
        Collections.reverse(trendbars);
        Date prevHourStart = DateTimeUtils.getPrevHourStart();
        SymbolEnum symbol = minuteTrendbarsHistory.getSymbol();

        BigDecimal openPrice = null;
        BigDecimal closePrice = null;
        BigDecimal highPrice = BigDecimal.valueOf(Integer.MIN_VALUE);
        BigDecimal lowPrice  =  BigDecimal.valueOf(Integer.MAX_VALUE);

        for (TrendBarDTO tb : trendbars) {
            if (tb.getTimestamp().before(prevHourStart)) {
                break;
            }
            openPrice = tb.getOpenPrice();
            if (closePrice == null) {
                closePrice = tb.getClosePrice();
            }
            if (NumberUtils.lt(tb.getLowPrice(), lowPrice)) {
                lowPrice = tb.getLowPrice();
            }
            if (NumberUtils.gt(tb.getHighPrice(), highPrice)) {
                highPrice = tb.getHighPrice();
            }
        }
        TrendBarDTO hourlyTrendbar = new TrendBarDTO(symbol, TrendBarPeriodEnum.H1, openPrice, closePrice, highPrice, lowPrice, prevHourStart);
        trendBarHistoryHolderHourly.get(symbol).add(hourlyTrendbar);
    }

    private void constructDailyTb() {
        Calendar now = Calendar.getInstance();
        if (now.get(Calendar.HOUR_OF_DAY) != 0) {
            return;
        }
        for (TrendBarHistory history : trendBarHistoryHolderHourly.values()) {
            buildPrevDayTrendbar(history);
        }

    }

    private void buildPrevDayTrendbar(TrendBarHistory hourlyTrendbarsHistory) {
        List<TrendBarDTO> trendbars = hourlyTrendbarsHistory.getList();
        if (trendbars.isEmpty()) {
            return;
        }
        Collections.reverse(trendbars);
        Date prevDayStart = DateTimeUtils.getPrevDayStart();
        SymbolEnum symbol = hourlyTrendbarsHistory.getSymbol();

        BigDecimal openPrice = null;
        BigDecimal closePrice = null;
        BigDecimal highPrice = BigDecimal.valueOf(Integer.MIN_VALUE);
        BigDecimal lowPrice  =  BigDecimal.valueOf(Integer.MAX_VALUE);

        for (TrendBarDTO tb : trendbars) {
            if (tb.getTimestamp().before(prevDayStart)) {
                break;
            }
            openPrice = tb.getOpenPrice();
            if (closePrice == null) {
                closePrice = tb.getClosePrice();
            }
            if (NumberUtils.lt(tb.getLowPrice(), lowPrice)) {
                lowPrice = tb.getLowPrice();
            }
            if (NumberUtils.gt(tb.getHighPrice(), highPrice)) {
                highPrice = tb.getHighPrice();
            }
        }
        TrendBarDTO hourlyTrendbar = new TrendBarDTO(symbol, TrendBarPeriodEnum.H1, openPrice, closePrice, highPrice, lowPrice, prevDayStart);
        trendBarHistoryHolderDaily.get(symbol).add(hourlyTrendbar);
    }

    private void fillTrendBars(Map<SymbolEnum, TrendBarDTO> map, List<QuoteDTO> list) {
        for (QuoteDTO q : list) {
            TrendBarDTO tb = map.get(q.getSymbol());

            BigDecimal price = q.getPrice();
            if (tb.getOpenPrice().intValue() == EMPTYVAL) {
                tb.setOpenPrice(price);
            }
            tb.setClosePrice(price);
            if (NumberUtils.lt(price, tb.getLowPrice())) {
                tb.setLowPrice(price);
            }
            if (NumberUtils.gt(price, tb.getHighPrice())) {
                tb.setHighPrice(price);
            }
        }
    }

    private Map<SymbolEnum, TrendBarDTO> createQuotesHolderMap(Date timestamp) {
        Map<SymbolEnum, TrendBarDTO> map = new HashMap<>();
        for (SymbolEnum s : SymbolEnum.values()) {
            BigDecimal price = BigDecimal.valueOf(EMPTYVAL);
            TrendBarDTO tb = new TrendBarDTO(s, TrendBarPeriodEnum.M1, price, price, BigDecimal.valueOf(Integer.MIN_VALUE), BigDecimal.valueOf(Integer.MAX_VALUE), timestamp);
            map.put(s, tb);
        }
        return map;
    }

    @Override
    public void addQuote(QuoteDTO quote) {
        Date prevDate = prevDateRef.get();
        Date quoteTs = quote.getTimestamp();
        if (prevDate != null && quoteTs.before(prevDate)) {
            throw new IllegalArgumentException("Quote timestamp is earlier than previous quote timestamp!");
        }
        quotesForProcessingQueue.add(quote);
        prevDateRef.set(quoteTs);
    }

    @Override
    public Collection<TrendBarDTO> getTrendBarHistory(SymbolEnum symbol, TrendBarPeriodEnum period, Date from, Date to) {
        return null;
    }

    @Override
    public void close() throws IOException {
        timer.cancel();
    }
}
