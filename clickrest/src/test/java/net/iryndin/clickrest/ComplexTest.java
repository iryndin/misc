package net.iryndin.clickrest;

import net.iryndin.clickrest.controller.ClickController;
import net.iryndin.clickrest.dao.BannerStatsRepository;
import net.iryndin.clickrest.dao.BannerStatsRepositoryTestImpl;
import net.iryndin.clickrest.service.CachingClickService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

/**
 * @author iryndin
 * @since 09/02/17
 */
public class ComplexTest {

    final static Logger log = LoggerFactory.getLogger(ComplexTest.class);

    ClickController clickController;
    CachingClickService clickService;
    BannerStatsRepository bannerStatsRepository;

    @Before
    public void init() {
        bannerStatsRepository = new BannerStatsRepositoryTestImpl();
        clickService = new CachingClickService(bannerStatsRepository, 1000, 5000);
        clickController = new ClickController(clickService);
        clickService.init();
    }

    @After
    public void release() {
        clickService.release();
    }

    @Test
    public void test() {
        clickController.register("a", 100);
        clickController.register("a", 300);
        assertEquals(400, clickController.stats("a"));
    }
}
