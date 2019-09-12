package net.iryndin.adexchange;

import net.iryndin.adexchange.model.BidRequest;
import net.iryndin.adexchange.model.BidResponse;
import net.iryndin.adexchange.model.Notify;

import java.security.SecureRandom;
import java.util.Random;

public class RandomDemandPartner implements DemandPartner {

    private final static double ALMOST_ZERO_RATIO = 1e-4;
    private final static double ALMOST_INFINITY_RATIO = 1e4;
    private final static int MIN_BIDPRICE = 1;
    private final static int MAX_BIDPRICE = 100;

    private final Random random = new SecureRandom();

    private final int minBidPrice;
    private final int maxBidPrice;
    private final int minSleepMillis;
    private final int maxSleepMillis;

    public RandomDemandPartner(double intimeToTimeoutRatio, long timeoutMillis) {
        this(MIN_BIDPRICE, MAX_BIDPRICE, intimeToTimeoutRatio, timeoutMillis);
    }

    public RandomDemandPartner(int minBidPrice, int maxBidPrice, double intimeToTimeoutRatio, long timeoutMillis) {
        this.minBidPrice = minBidPrice;
        this.maxBidPrice = maxBidPrice;
        int[] sleepTimes = calculateMinMaxSleepMillis(intimeToTimeoutRatio, timeoutMillis);
        this.minSleepMillis = sleepTimes[0];
        this.maxSleepMillis = sleepTimes[1];
    }

    private static int[] calculateMinMaxSleepMillis(double intimeToTimeoutRatio, long timeoutMillisLong) {
        if (intimeToTimeoutRatio < 0.0) {
            throw new IllegalArgumentException("Incorrect intimeToTimeoutRatio: " + intimeToTimeoutRatio);
        }
        int timeoutMillis = (int)timeoutMillisLong;
        int minSleepMillis, maxSleepMillis;
        if (intimeToTimeoutRatio <= ALMOST_ZERO_RATIO) {
            /**
             * If ratio is almost zero, this means there are no intime responses,
             * all responses are above given timeout.
             */
            minSleepMillis = timeoutMillis;
            maxSleepMillis = timeoutMillis * 2;
        } else if (intimeToTimeoutRatio >= ALMOST_INFINITY_RATIO) {
            /**
             * If ratio is almost infinity, this means all responses are in-time,
             * i.e. are below given timeout
             */
            minSleepMillis = 0;
            maxSleepMillis = timeoutMillis;
        } else {
            /**
             * All response times fall into one of two categories:
             *  - in-time (below given timeout)
             *  - out-time (above given timeout)
             *
             *  Let I - is probability of in-time response,
             *  O - probability of out-time response, ==>
             *  I + O = 1.0
             *
             *  Ratio of intime-to-outtime responses is R, hence:
             *  R = I/O
             *  I = R*O
             *  I + O = O*R + O = O*(R + 1) = 100%, ==>
             *  O = 1.0/(R+1)  (response time > timeoutMillis)
             *  I = R/(R+1)    (response time < timeoutMillis)
             *
             * min: x1
             * max x2
             *
             * (timeoutMillis-x1)/(x2 - timeoutMillis) = R
             *
             * If R>=1.0: let set x1=0.
             * (timeoutMillis-x1)/(x2 - timeoutMillis) = R, hence
             * timeoutMillis/(x2 - timeoutMillis) = R, hence
             * x2 = ((R+1)/R)*timeoutMillis/R
             * Examples:
             *   for R=1: x1=0, x2=100.
             *   for R=5: x1=0, x2=1.2*50=60
             *   for R=10: x1=0, x2=1.1*50=55
             *
             * If R < 1.0: let set x2=2 * timeoutMillis
             * (timeoutMillis-x1)/(x2 - timeoutMillis) = R, hence
             * (timeoutMillis-x1)/timeoutMillis = R, hence
             * timeoutMillis-x1 = R*timeoutMillis, hence
             * x1 = (1-R)*timeoutMillis
             *
             * Examples:
             *   for R=0.5: x1=0.5*50=25, x2=100
             *   for R=0.1: x1=0.9*50=45, x2=100
             */
            if (intimeToTimeoutRatio >= 1.0) {
                minSleepMillis = 0;
                double coef = (intimeToTimeoutRatio+1.0)/intimeToTimeoutRatio;
                maxSleepMillis = (int)(coef*timeoutMillis);
                if (maxSleepMillis == timeoutMillis) {
                    maxSleepMillis = timeoutMillis+1;
                }
            } else {
                maxSleepMillis = 2*timeoutMillis;
                double coef = 1.0 - intimeToTimeoutRatio;
                minSleepMillis = (int)(coef*timeoutMillis);
                if (minSleepMillis==timeoutMillis) {
                    minSleepMillis = timeoutMillis-1;
                }
            }
        }

        return new int[]{minSleepMillis, maxSleepMillis};
    }

    @Override
    public BidResponse processRequest(BidRequest bidRequest) {
        final long bidPrice = minBidPrice + random.nextInt(maxBidPrice-minBidPrice);
        final long sleepMillis = minSleepMillis + random.nextInt(maxSleepMillis - minSleepMillis);

        try {
            Thread.sleep(sleepMillis);
        } catch (InterruptedException e) {
            //e.printStackTrace();
        }
        return new BidResponse(bidPrice);
    }

    @Override
    public void processWin(Notify notify) {
        // do nothing here
    }
}
