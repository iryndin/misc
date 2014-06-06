package net.iryndin.testcrawler.dto;

import java.util.Set;

public class CrawlerResultsDTO {
    private final long elapsedMillis;
    private final String seedUrl;
    private final Set<String> links;

    public CrawlerResultsDTO(long elapsedMillis, String seedUrl, Set<String> links) {
        this.elapsedMillis = elapsedMillis;
        this.seedUrl = seedUrl;
        this.links = links;
    }

    public long getElapsedMillis() {
        return elapsedMillis;
    }

    public String getSeedUrl() {
        return seedUrl;
    }

    public Set<String> getLinks() {
        return links;
    }
}
