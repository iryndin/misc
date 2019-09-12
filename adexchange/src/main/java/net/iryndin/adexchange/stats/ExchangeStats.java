package net.iryndin.adexchange.stats;

import java.util.concurrent.atomic.AtomicLong;

public class ExchangeStats {
    private final AtomicLong intime = new AtomicLong(0);
    private final AtomicLong timeout = new AtomicLong(0);
    private final AtomicLong issuedRequests = new AtomicLong(0);
    private final AtomicLong bidRequest = new AtomicLong(0);
    private final AtomicLong fail = new AtomicLong(0);
    private final AtomicLong success = new AtomicLong(0);
    private final AtomicLong total = new AtomicLong(0);
    private final long totalRequestsToProcess;
    private long millis = 0;
    private boolean done = false;
    private int bidRequestThreads;
    private int partnerCallThreads;

    public ExchangeStats(long totalRequestsToProcess) {
        this.totalRequestsToProcess = totalRequestsToProcess;
    }

    public void bidRequestIncr() {
        bidRequest.incrementAndGet();
        if (millis == 0) {
            millis = System.currentTimeMillis();
        }
    }

    public void failIncr() {
        fail.incrementAndGet();
    }

    public long getFail() {
        return fail.get();
    }

    public long getSuccess() {
        return success.get();
    }

    public double getSuccessToFailRatio() {
        return success.get()/(double)fail.get();
    }

    public void successIncr() {
        success.incrementAndGet();
    }

    public void addElapsedMillis(long elapsedMillis) {

    }

    public void issuedRequestsAdd(int a) {
        issuedRequests.addAndGet(a);
    }

    public void timeoutIncr() {
        timeout.incrementAndGet();
    }

    public void intimeIncr() {
        intime.incrementAndGet();
    }

    public long getMillis() {
        return millis;
    }

    public boolean isDone() {
        return done;
    }

    @Override
    public String toString() {
        return "ExchangeStats{" +
                "intime=" + intime +
                ", timeout=" + timeout +
                ", issuedRequests=" + issuedRequests +
                ", bidRequest=" + bidRequest +
                ", fail=" + fail +
                ", success=" + success +
                ", total=" + total +
                ", totalRequestsToProcess=" + totalRequestsToProcess +
                ", millis=" + millis +
                ", done=" + done +
                '}';
    }

    public void incrTotalProcessedRequests() {
        if (total.incrementAndGet() == totalRequestsToProcess) {
            millis = System.currentTimeMillis() - millis;
            done = true;
        }
    }

    public int getPartnerCallThreads() {
        return partnerCallThreads;
    }

    public int getBidRequestThreads() {
        return bidRequestThreads;
    }

    public void setBidRequestThreads(int bidRequestThreads) {
        this.bidRequestThreads = bidRequestThreads;
    }

    public void setPartnerCallThreads(int partnerCallThreads) {
        this.partnerCallThreads = partnerCallThreads;
    }

    public double getIntimeToTimeoutsRatio() {
        return intime.get()/(double)timeout.get();
    }
}
