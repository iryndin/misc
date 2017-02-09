package net.iryndin.clickrest.dao;

import com.mongodb.WriteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * @author iryndin
 * @since 09/02/17
 */
public class BannerStatsRepositoryImpl implements BannerStatsRepositoryCustom {

    final static Logger log = LoggerFactory.getLogger(BannerStatsRepositoryImpl.class);

    private MongoTemplate mongoTemplate;

    @Autowired
    public BannerStatsRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * We want to run queries with incremental updates (upserts) only.
     * We want to run atomic upsert queries:
     * db.bannercost.update({_id: "cc"}, {$inc: {cost: 20}}, {upsert: true});
     */
    @Override
    public void incrementBannerCost(String id, long cost) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update().inc("cost", cost);
        WriteResult wr = mongoTemplate.upsert(query, update, BannerStats.class);
        log.debug(wr.toString());
    }
}
