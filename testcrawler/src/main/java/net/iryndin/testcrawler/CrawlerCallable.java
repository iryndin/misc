package net.iryndin.testcrawler;

import net.iryndin.testcrawler.dto.CrawlerResultsDTO;
import net.iryndin.testcrawler.dto.CrawlerSettingsDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * Crawl single domain
 */
public class CrawlerCallable implements Callable<CrawlerResultsDTO> {

    static final int TIMEOUT = 5000;

    private final CrawlerSettingsDTO settings;
    private String baseHost;

    private final Set<String> links = new HashSet<>();
    private final Set<String> crawledPages = new HashSet<>();
    private final Queue<String> pagesToCrawlQueue = new ArrayDeque<>();
    private final Set<String>   pagesToCrawlSet = new HashSet<>();

    private UrlNormalizer urlNormalizer = new UrlNormalizer();

    public CrawlerCallable(CrawlerSettingsDTO settings) {
        this.settings = settings;
        String url = urlNormalizer.normalize(settings.getSeedUrl());
        url = urlNormalizer.normalize(handlePossibleRedirect(url));
        try {
            baseHost = new URL(url).getHost();
        } catch (MalformedURLException e) {
            //
        }
        pagesToCrawlQueue.add(url);
    }

    private String handlePossibleRedirect(String url) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection)new URL(url).openConnection();
            conn.setInstanceFollowRedirects(false);
            int resp = conn.getResponseCode();
            if (resp == HttpURLConnection.HTTP_MOVED_PERM || resp == HttpURLConnection.HTTP_MOVED_TEMP) {
                return conn.getHeaderField("Location");
            }
        } catch (IOException e) {
            //
        }
        return url;
    }

    private void addNewPageToCrawlQueue(String s) {
        if (!pagesToCrawlSet.contains(s)) {
            pagesToCrawlSet.add(s);
            pagesToCrawlQueue.add(s);
        }
    }

    @Override
    public CrawlerResultsDTO call() throws Exception {
        System.out.println("Start crawl: " + settings.getSeedUrl());

        final long startMillis = System.currentTimeMillis();
        {
            doJob();
        }
        final long elapsedMillis = System.currentTimeMillis() - startMillis;
        return new CrawlerResultsDTO(elapsedMillis, settings.getSeedUrl(), Collections.unmodifiableSet(links));
    }

    private void doJob() throws Exception {
        while (crawledPages.size() < settings.getMaxCrawledPageQty() && !pagesToCrawlQueue.isEmpty()) {
            try {
                String page = pagesToCrawlQueue.poll();
                pagesToCrawlSet.remove(page);

                crawlPage(page);

                Thread.sleep(settings.getDelayMillis());
            } catch (Exception e) {
                //
            }
        }
    }

    private void crawlPage(String pageUrl) throws IOException {
        System.out.println(" --> " + pageUrl);
        try {
            Document doc = Jsoup.parse(new URL(pageUrl), TIMEOUT);

            Elements ahrefs = doc.select("a[href]");
            for (Element href : ahrefs) {
                String link = href.attr("abs:href").trim();

                if (!link.startsWith("http://") && !link.startsWith("https://")) {
                    continue;
                }

                String normalizedLink = urlNormalizer.normalize(link);
                String host = new URL(normalizedLink).getHost();

                if (host.equals(baseHost) && !crawledPages.contains(normalizedLink)) {
                    addNewPageToCrawlQueue(normalizedLink);
                }

                if (!link.isEmpty()) {
                    links.add(link);
                }
            }
        } finally {
            crawledPages.add(pageUrl);
        }
    }
}
