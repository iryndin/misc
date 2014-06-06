package net.iryndin.testcrawler.dto;

public class CrawlerSettingsDTO {
    private final String seedUrl;
    private final int maxCrawledPageQty;
    private final long delayMillis;

    public CrawlerSettingsDTO(String seedUrl, int maxCrawledPageQty, long delayMillis) {
        this.seedUrl = seedUrl;
        this.maxCrawledPageQty = maxCrawledPageQty;
        this.delayMillis = delayMillis;
    }

    public String getSeedUrl() {
        return seedUrl;
    }

    public int getMaxCrawledPageQty() {
        return maxCrawledPageQty;
    }

    public long getDelayMillis() {
        return delayMillis;
    }
}
