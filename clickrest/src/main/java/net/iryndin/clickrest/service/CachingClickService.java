package net.iryndin.clickrest.service;

import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import net.iryndin.clickrest.dao.BannerStats;
import net.iryndin.clickrest.dao.BannerStatsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author iryndin
 * @since 09/02/17
 */
@Service
public class CachingClickService implements IClickService {

    final static Logger log = LoggerFactory.getLogger(CachingClickService.class);

    private AtomicReference<ConcurrentMap<String, AtomicLong>> cacheRef =
            new AtomicReference<>(new ConcurrentHashMap<>());

    private Timer timer;

    private BannerStatsRepository bannerStatsRepository;

    @Value("${clickrest.clickservice.cache.delay}")
    private long saveCacheStartDelayMillis;
    @Value("${clickrest.clickservice.cache.period}")
    private long saveCachePeriodMillis;

    @Autowired
    public CachingClickService(BannerStatsRepository bannerStatsRepository) {
        this.bannerStatsRepository = bannerStatsRepository;
    }

    public CachingClickService(BannerStatsRepository bannerStatsRepository,
                               long saveCacheStartDelayMillis, long saveCachePeriodMillis) {
        this.bannerStatsRepository = bannerStatsRepository;
        this.saveCacheStartDelayMillis = saveCacheStartDelayMillis;
        this.saveCachePeriodMillis = saveCachePeriodMillis;
    }

    @PostConstruct
    public void init() {
        log.debug("Init CachingClickService, saveCacheStartDelayMillis={}, saveCachePeriodMillis={}",
                saveCacheStartDelayMillis, saveCachePeriodMillis);
        timer = new Timer();
        timer.scheduleAtFixedRate(new CacheRefresherTask(), saveCacheStartDelayMillis, saveCachePeriodMillis);
    }

    @PreDestroy
    public void release() {
        log.debug("Releasing CachingClickService");
        timer.cancel();
        saveCacheToDatabase();
    }

    /**
     * Save cache to database
     */
    private void saveCacheToDatabase() {
        ConcurrentMap<String, AtomicLong> oldCache = cacheRef.getAndSet(new ConcurrentHashMap<>());
        if (oldCache.isEmpty()) {
            log.debug("Going to save cache to DB, but nothing to save");
        } else {
            log.debug("Going to save cache to DB, updating {} items", oldCache.size());
            oldCache.forEach((id, cost) -> bannerStatsRepository.incrementBannerCost(id, cost.get()));
        }
    }

    public void registerClick(String bannerId, int cost) {
        AtomicLong cachedCostValue = cacheRef.get().putIfAbsent(bannerId, new AtomicLong(cost));
        if (cachedCostValue != null) {
            cachedCostValue.addAndGet(cost);
        }
    }

    /**
     * Check values in cache and database
     * @param bannerId
     * @return
     */
    public Optional<Long> getStats(String bannerId) {
        Optional<Long> cacheVal = Optional.ofNullable(cacheRef.get().get(bannerId)).map(AtomicLong::get);
        Optional<Long> dbVal = Optional.ofNullable(bannerStatsRepository.findOne(bannerId)).map(BannerStats::getCost);
        if (!cacheVal.isPresent() && !dbVal.isPresent()) {
            return Optional.empty();
        } else {
            return Optional.of(cacheVal.orElse(0L) + dbVal.orElse(0L));
        }
    }

    private class CacheRefresherTask extends TimerTask {

        @Override
        public void run() {
            saveCacheToDatabase();
        }
    }
}
