package net.iryndin.clickrest.dao;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author iryndin
 * @since 09/02/17
 */
public class BannerStatsRepositoryTestImpl implements BannerStatsRepository {

    private final ConcurrentMap<String, AtomicLong> cache = new ConcurrentHashMap<>();

    @Override
    public BannerStats findOne(String bannerId) {
        return Optional.ofNullable(cache.get(bannerId))
                .map(cost -> new BannerStats(bannerId, cost.get())).orElseGet(() -> null);
    }

    @Override
    public void incrementBannerCost(String bannerId, long cost) {
        AtomicLong cachedCostValue = cache.putIfAbsent(bannerId, new AtomicLong(cost));
        if (cachedCostValue != null) {
            cachedCostValue.addAndGet(cost);
        }
    }

    @Override
    public <S extends BannerStats> S save(S s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends BannerStats> Iterable<S> save(Iterable<S> iterable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean exists(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterable<BannerStats> findAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterable<BannerStats> findAll(Iterable<String> iterable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long count() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(BannerStats bannerStats) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Iterable<? extends BannerStats> iterable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException();
    }
}
