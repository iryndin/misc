package net.iryndin.quoteservice.provider;

import net.iryndin.quoteservice.dto.QuoteDTO;
import net.iryndin.quoteservice.dto.SymbolEnum;
import net.iryndin.quoteservice.util.DateTimeUtils;

import java.util.List;
import java.util.Random;

public class QuoteProviderRunnable implements Runnable {
    private final int minQuotesPerMinute;
    private final int maxQuotesPerMinute;
    private final QuoteProvider quoteProvider;
    private final QuoteReceiver quoteReceiver;

    private final static int SLEEP_INTERVAL_MILLIS = 10;

    /**
     * Qty of quotes that will be generated during current minute
     */
    private int currentMinuteQuotesTotal;

    /**
     * Qty of quotes that is already generated during current minute
     */
    private int currentMinuteQuotesSent;

    /**
     * start of next minute in millis
     */
    private long nextMinuteTimestamp;

    private long totalQuotesSent;

    public QuoteProviderRunnable(SymbolEnum symbol, QuoteReceiver quoteReceiver) {
        this(1000, 1000000, symbol, quoteReceiver);
    }


    public QuoteProviderRunnable(int minQuotesPerMinute, int maxQuotesPerMinute, SymbolEnum symbol, QuoteReceiver quoteReceiver) {
        this.minQuotesPerMinute = minQuotesPerMinute;
        this.maxQuotesPerMinute = maxQuotesPerMinute;
        this.quoteProvider = new QuoteProvider(symbol);
        this.quoteReceiver = quoteReceiver;
    }


    @Override
    public void run() {
        System.out.println("Start QuoteProviderRunnable(" + quoteProvider.getSymbol()+")");

        // set start of closest next minute
        nextMinuteTimestamp = DateTimeUtils.getNextMinuteStart().getTime();
        currentMinuteQuotesTotal = selectQuotesRateForCurrentMinute();

        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(SLEEP_INTERVAL_MILLIS);
                final long now = System.currentTimeMillis();
                int remainedToSend = currentMinuteQuotesTotal - currentMinuteQuotesSent;
                long remainedTimeMillis = nextMinuteTimestamp - now;

                if (remainedTimeMillis <= SLEEP_INTERVAL_MILLIS) {
                    sendQuotes(remainedToSend);
                    // select rate for next minute
                    currentMinuteQuotesTotal = selectQuotesRateForCurrentMinute();
                    currentMinuteQuotesSent = 0;
                    nextMinuteTimestamp = DateTimeUtils.getNextMinuteStart().getTime();
                } else {
                    int intervalFractionToSend = remainedToSend / (int)(remainedTimeMillis / SLEEP_INTERVAL_MILLIS);
                    sendQuotes(intervalFractionToSend);
                }
            } catch (InterruptedException e) {
                break;
            }
        }

        System.out.println("Shutdown QuoteProviderRunnable(" + quoteProvider.getSymbol()+")");
    }

    private void sendQuotes(int fraction) {
        if (fraction > 0) {
            List<QuoteDTO> list = quoteProvider.getQuotes(fraction);
            quoteReceiver.sendQuotes(list);
            totalQuotesSent += fraction;
            currentMinuteQuotesSent += fraction;
        }
    }

    private int selectQuotesRateForCurrentMinute() {
        int n = maxQuotesPerMinute - minQuotesPerMinute;
        return minQuotesPerMinute + new Random().nextInt(n);
    }

    public long getTotalQuotesSent() {
        return totalQuotesSent;
    }
}
