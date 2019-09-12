package net.iryndin.adexchange;

import net.iryndin.adexchange.model.BidRequest;
import net.iryndin.adexchange.model.BidResponse;
import net.iryndin.adexchange.model.ImmutablePair;
import net.iryndin.adexchange.model.Notify;
import net.iryndin.adexchange.stats.ExchangeStats;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class NonBlockingExchange implements Exchange  {

    private static class DemandPartnerCallable implements Callable<ImmutablePair<DemandPartner, BidResponse>> {

        private final DemandPartner demandPartner;
        private final BidRequest bidRequest;

        public DemandPartnerCallable(DemandPartner demandPartner, BidRequest bidRequest) {
            this.demandPartner = demandPartner;
            this.bidRequest = bidRequest;
        }

        @Override
        public ImmutablePair<DemandPartner, BidResponse> call() throws Exception {
            BidResponse bidResponse = demandPartner.processRequest(bidRequest);
            return new ImmutablePair<>(demandPartner, bidResponse);
        }
    }

    private final long waitTimeoutMillis;
    private final ExchangeStats exchangeStats;
    private final List<DemandPartner> partners;
    private final ExecutorService bidRequestExecutorService;
    private final ExecutorService demandPartnerCallsExecutorService;

    //private final ExecutorService executor = Executors.newFixedThreadPool(THREADS1);

    public NonBlockingExchange(List<DemandPartner> partners, ExchangeStats exchangeStats,
                               int bidRequestThreads, int partnerCallThreads,
                               long waitTimeoutMillis) {
        this.partners = partners;
        this.exchangeStats = exchangeStats;
        this.waitTimeoutMillis = waitTimeoutMillis;
        this.bidRequestExecutorService = Executors.newFixedThreadPool(bidRequestThreads);
        this.demandPartnerCallsExecutorService = Executors.newFixedThreadPool(partnerCallThreads);
        exchangeStats.setBidRequestThreads(bidRequestThreads);
        exchangeStats.setPartnerCallThreads(partnerCallThreads);
    }

    @Override
    public void close() throws Exception {
        bidRequestExecutorService.shutdown();
        demandPartnerCallsExecutorService.shutdown();
    }

    @Override
    public void processBidRequest(BidRequest bidRequest) {
        exchangeStats.bidRequestIncr();
        bidRequestExecutorService.submit(() -> {
            final long startMillis = System.currentTimeMillis();
            Optional<ImmutablePair<DemandPartner, BidResponse>> result = processBidRequestBlockingWay(bidRequest);
            final long elapsedMillis = System.currentTimeMillis() - startMillis;
            exchangeStats.addElapsedMillis(elapsedMillis);
            exchangeStats.incrTotalProcessedRequests();
            if (result.isPresent()) {
                ImmutablePair<DemandPartner, BidResponse> pair = result.get();
                pair.getFirst().processWin(new Notify(bidRequest.getRequestId(), pair.getSecond().getBidPrice()));
                exchangeStats.successIncr();
            } else {
                exchangeStats.failIncr();
            }
        });
    }

    /**
     * Send {@link BidRequest} to each partner,
     * gather responses from them,
     * get maximum bidPrice and return it along with {@link DemandPartner} that proposed this price.
     * @param bidRequest bid request that is sent to all the partners
     * @return
     */
    private Optional<ImmutablePair<DemandPartner, BidResponse>> processBidRequestBlockingWay(BidRequest bidRequest) {
        // 1. Create callables for each partner
        List<DemandPartnerCallable> demandPartnerResults = partners.stream()
                .map(dp -> new DemandPartnerCallable(dp, bidRequest))
                .collect(Collectors.toList());
        try {
            // 2. Send requests to each partner
            List<Future<ImmutablePair<DemandPartner, BidResponse>>> futures =
                    demandPartnerCallsExecutorService.invokeAll(demandPartnerResults, waitTimeoutMillis, TimeUnit.MILLISECONDS);
            exchangeStats.issuedRequestsAdd(futures.size());
            // 3. Get results from responses and finally get maximum result
            return selectBestResponse(futures);
        } catch (InterruptedException e) {
            //e.printStackTrace();
            return Optional.empty();
        }
    }

    private Optional<ImmutablePair<DemandPartner, BidResponse>> selectBestResponse(List<Future<ImmutablePair<DemandPartner, BidResponse>>> futures) {
        return futures.stream()
                .map(f -> futureToResultOrNull(f))
                .filter(pair -> {
                    if (pair == null) {
                        exchangeStats.timeoutIncr();
                        return false;
                    } else {
                        exchangeStats.intimeIncr();
                        return true;
                    }
                })
                .max(Comparator.comparingLong(p -> p.getSecond().getBidPrice()));
    }

    /**
     * Returns result from future or null if future was cancelled or error happens.
     * If future was cancelled, this is possible only in case when timeout happened.
     * So cancelCounter is incremented.
     *
     * @param f future to get results from
     * @param <T>
     * @return result from future or null
     */
    private static <T> T futureToResultOrNull(Future<T> f) {
        try {
            return f.get();
        } catch (Exception e) {
            return null;
        }
    }
}
