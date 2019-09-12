package net.iryndin.adexchange.stats;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

public class IncomingRequestStats {

    private final CountDownLatch countDownLatch;
    private final AtomicLong requestQty = new AtomicLong(0);
    private long millisElapsed;

    public IncomingRequestStats(int latchCount) {
        this.countDownLatch = new CountDownLatch(latchCount);
        millisElapsed = System.currentTimeMillis();
    }

    public void reqIncr() {
        requestQty.incrementAndGet();
    }

    public long getRequestQty() {
        return requestQty.get();
    }

    public void countDown() {
        countDownLatch.countDown();
    }

    public long await() throws InterruptedException {
        countDownLatch.await();
        return System.currentTimeMillis() - millisElapsed;
    }
}
