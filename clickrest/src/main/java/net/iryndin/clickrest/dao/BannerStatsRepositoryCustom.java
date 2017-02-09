package net.iryndin.clickrest.dao;

/**
 * @author iryndin
 * @since 09/02/17
 */
public interface BannerStatsRepositoryCustom {
    /**
     * We want to run queries with incremental updates (upserts) only.
     * We want to run atomic upsert queries:
     * db.bannercost.update({_id: "cc"}, {$inc: {cost: 20}}, {upsert: true});
     */
    void incrementBannerCost(String id, long cost);
}
