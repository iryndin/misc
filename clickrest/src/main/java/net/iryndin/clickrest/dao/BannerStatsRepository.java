package net.iryndin.clickrest.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author iryndin
 * @since 09/02/17
 */
@Repository
public interface BannerStatsRepository extends CrudRepository<BannerStats, String>, BannerStatsRepositoryCustom {
}
