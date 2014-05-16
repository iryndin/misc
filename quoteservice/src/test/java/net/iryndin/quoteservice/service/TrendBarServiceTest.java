package net.iryndin.quoteservice.service;

import net.iryndin.quoteservice.dto.QuoteDTO;
import net.iryndin.quoteservice.dto.SymbolEnum;
import net.iryndin.quoteservice.provider.QuoteProviderRunnable;
import net.iryndin.quoteservice.provider.QuoteReceiver;
import net.iryndin.quoteservice.service.impl.TrendBarServiceImpl;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by iryndin on 15.05.14.
 */
public class TrendBarServiceTest {

    @Test
    public void test() {
        test(130000);
    }

    AtomicInteger counter = new AtomicInteger();

    void test(long millis) {

        final TrendBarService trendBarService = new TrendBarServiceImpl();

        /*
        QuoteProviderRunnable[] providers = new QuoteProviderRunnable[SymbolEnum.values().length];
        int i=0;
        for (SymbolEnum s : SymbolEnum.values()) {
            providers[i++] = new QuoteProviderRunnable(s, new QuoteReceiver() {
                @Override
                public void sendQuotes(List<QuoteDTO> quotes) {
                    counter.getAndAdd(quotes.size());
                    for (QuoteDTO q : quotes) {
                        trendBarService.addQuote(q);
                    }
                }
            });
        }
        */

        QuoteProviderRunnable[] providers = new QuoteProviderRunnable[1];
        int i=0;
        {
            providers[i++] = new QuoteProviderRunnable(100,200,SymbolEnum.EURJPY, new QuoteReceiver() {
                @Override
                public void sendQuotes(List<QuoteDTO> quotes) {
                    counter.getAndAdd(quotes.size());
                    for (QuoteDTO q : quotes) {
                        trendBarService.addQuote(q);
                    }
                }
            });
        }

        ExecutorService executorService = Executors.newCachedThreadPool();
        for (QuoteProviderRunnable p : providers) {
            executorService.submit(p);
        }

        try {
            Thread.sleep(millis);
            executorService.shutdownNow();
            System.out.println(counter.get());
            trendBarService.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
