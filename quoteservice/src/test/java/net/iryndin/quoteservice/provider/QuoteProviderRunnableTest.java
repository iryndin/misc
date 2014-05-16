package net.iryndin.quoteservice.provider;

import net.iryndin.quoteservice.dto.QuoteDTO;
import net.iryndin.quoteservice.dto.SymbolEnum;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by iryndin on 15.05.14.
 */
public class QuoteProviderRunnableTest {

    @Test
    public void test() {
        test(1000);
        test(30000);
        test(60000);
    }

    AtomicInteger counter = new AtomicInteger();

    QuoteReceiver countingReceiver = new QuoteReceiver() {
        @Override
        public void sendQuotes(List<QuoteDTO> quotes) {
            counter.getAndAdd(quotes.size());
        }
    };

    void test(long millis) {
        System.out.println("-------------- START QuoteProviderRunnableTest -----------------");
        counter.set(0);
        QuoteProviderRunnable t = new QuoteProviderRunnable(SymbolEnum.EURJPY, countingReceiver);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(t);

        try {
            Thread.sleep(millis);
            executorService.shutdownNow();
            int quotesReceived = counter.get();
            System.out.println("Quotes received: " + quotesReceived);
            System.out.println("Quotes sent: " + t.getTotalQuotesSent());

            Assert.assertEquals(t.getTotalQuotesSent(), quotesReceived);

        } catch (InterruptedException e) {
            //
        }
    }
}
