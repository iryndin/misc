package net.iryndin.adexchange;

import net.iryndin.adexchange.model.BidRequest;
import net.iryndin.adexchange.stats.ExchangeStats;
import net.iryndin.adexchange.stats.IncomingRequestStats;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**

 THREADS1 influence how many time will it take to submit all BidRequests to processing.
 50 millis is timeout, this means that per second it can handle ~20 (1000/50) requests in one thread.
 If we want throughput around 10k rps, this means that we need: 10000/20=500 threads.

 THREADS2 influence

 64*20*8 = 64*160=10240
 64*20=1280 rps
 */
public class LoadTest {

    static final int DEMAND_PARTNERS = 20;
    static final int LOAD_THREADS = 10;
    static final int REQUESTS_PER_LOAD_THREAD = 10000;
    static final int THREAD_RATIO = 5;
    static final int WAIT_TIMEOUT_MILLIS = 50;

    public static void main(String[] args) throws Exception {
        int[] threads = {32, 64, 96, 128, 160, 192};
        //int[] threads = {64, 64, 64, 64, 64};
        List<LoadTestResult> results = new ArrayList<>();
        for (int i : threads) {
            LoadTestConfig loadTestConfig = new LoadTestConfig(
                    LOAD_THREADS,
                    REQUESTS_PER_LOAD_THREAD,
                    i,
                    i*THREAD_RATIO,
                    DEMAND_PARTNERS
            );
            LoadTestResult r = new LoadTest().test(loadTestConfig);
            results.add(r);

            System.out.println("======== DONE LOAD TEST =======");
            System.out.println("i=" + i);
            System.out.println("======== DONE LOAD TEST =======");
        }
        System.out.println("======== DONE =======");
        for (LoadTestResult ltr : results) {
            System.out.println(ltr);
        }
    }

    LoadTestResult test(LoadTestConfig cfg) throws Exception {
        // 1. Create DemandPartners
        List<DemandPartner> demandPartners = new ArrayList<>();
        for (int i=0; i<cfg.getDemandPartnerQty(); i++) {
            demandPartners.add(new RandomDemandPartner(1.0, WAIT_TIMEOUT_MILLIS));
        }

        // 2. Create stats and exchange
        final int totalRequests = cfg.getLoadThreads() * cfg.getRequestsPerLoadThread();
        ExchangeStats exchangeStats = new ExchangeStats(totalRequests);
        Exchange exchange = new NonBlockingExchange(demandPartners, exchangeStats,
                cfg.getBidRequestThreads(), cfg.getPartnerCallThreads(), WAIT_TIMEOUT_MILLIS);
        ExecutorService loadExecutorService = Executors.newCachedThreadPool();

        // 3. Run load
        IncomingRequestStats stats = new IncomingRequestStats(cfg.getLoadThreads());
        for (int i=0; i<cfg.getLoadThreads(); i++) {
            loadExecutorService.submit(new ExchangeLoader(stats, exchange, cfg.getRequestsPerLoadThread()));
        }

        stats.await();
        loadExecutorService.shutdown();

        // 4. Wait for all requests to be processed

        while (true) {
            System.out.println("======== Exchange stats ==========");
            System.out.println(exchangeStats);
            if (exchangeStats.isDone()) {
                break;
            } else {
                Thread.sleep(5000);
            }
        }

        // 5. Cleanup and return results

        long seconds = exchangeStats.getMillis()/1000;
        long rps = totalRequests/seconds;

        exchange.close();

        return new LoadTestResult(
                rps,
                exchangeStats.getMillis()/1000,
                cfg.getBidRequestThreads(),
                cfg.getPartnerCallThreads(),
                exchangeStats.getSuccessToFailRatio(),
                exchangeStats.getIntimeToTimeoutsRatio()
        );
    }

    /**
     * Send {@code requestsQty} requests to exchange
     */
    static class ExchangeLoader implements Runnable {

        private final IncomingRequestStats incomingRequestStats;
        private final Exchange exchange;
        private final int requestsQty;

        public ExchangeLoader(IncomingRequestStats stats, Exchange exchange, int requestsQty) {
            this.exchange = exchange;
            this.requestsQty = requestsQty;
            this.incomingRequestStats = stats;

            if (requestsQty <= 0) {
                throw new IllegalArgumentException("requestsQty should be > 0");
            }
        }

        @Override
        public void run() {
            for (int i=0; i<requestsQty; i++) {
                exchange.processBidRequest(new BidRequest("https://aa.com", "aa", randomId()));
                incomingRequestStats.reqIncr();
            }
            incomingRequestStats.countDown();
        }

        private static String randomId() {
            return UUID.randomUUID().toString();
        }
    }

    static class LoadTestConfig {
        private final int loadThreads;
        private final int requestsPerLoadThread;
        private final int bidRequestThreads;
        private final int partnerCallThreads;
        private final int demandPartnerQty;

        public LoadTestConfig(int loadThreads, int requestsPerLoadThread, int bidRequestThreads, int partnerCallThreads, int demandPartnerQty) {
            this.loadThreads = loadThreads;
            this.requestsPerLoadThread = requestsPerLoadThread;
            this.bidRequestThreads = bidRequestThreads;
            this.partnerCallThreads = partnerCallThreads;
            this.demandPartnerQty = demandPartnerQty;
        }

        public int getLoadThreads() {
            return loadThreads;
        }

        public int getRequestsPerLoadThread() {
            return requestsPerLoadThread;
        }

        public int getBidRequestThreads() {
            return bidRequestThreads;
        }

        public int getPartnerCallThreads() {
            return partnerCallThreads;
        }

        public int getDemandPartnerQty() {
            return demandPartnerQty;
        }
    }

    static class LoadTestResult {
        private final long rps;
        private final long elapsedSeconds;
        private final int bidRequestThreads;
        private final int partnerCallThreads;
        private final double successToFailRatio;
        private final double intimeToTimeoutRatio;

        public LoadTestResult(long rps, long elapsedSeconds, int bidRequestThreads, int partnerCallThreads, double successToFailRatio, double intimeToTimeoutRatio) {
            this.rps = rps;
            this.elapsedSeconds = elapsedSeconds;
            this.bidRequestThreads = bidRequestThreads;
            this.partnerCallThreads = partnerCallThreads;
            this.successToFailRatio = successToFailRatio;
            this.intimeToTimeoutRatio = intimeToTimeoutRatio;
        }

        public long getRps() {
            return rps;
        }

        public long getElapsedSeconds() {
            return elapsedSeconds;
        }

        public int getPartnerCallThreads() {
            return partnerCallThreads;
        }

        public int getBidRequestThreads() {
            return bidRequestThreads;
        }

        public double getSuccessToFailRatio() {
            return successToFailRatio;
        }

        public double getIntimeToTimeoutRatio() {
            return intimeToTimeoutRatio;
        }

        @Override
        public String toString() {
            return "LoadTestResult{" +
                    "rps=" + rps +
                    ", elapsedSeconds=" + elapsedSeconds +
                    ", bidRequestThreads=" + bidRequestThreads +
                    ", partnerCallThreads=" + partnerCallThreads +
                    ", successToFailRatio=" + successToFailRatio +
                    ", intimeToTimeoutRatio=" + intimeToTimeoutRatio +
                    '}';
        }
    }
}
