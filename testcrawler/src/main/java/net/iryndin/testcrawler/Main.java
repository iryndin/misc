package net.iryndin.testcrawler;

import com.google.common.util.concurrent.*;
import net.iryndin.testcrawler.dto.CrawlerResultsDTO;
import net.iryndin.testcrawler.dto.CrawlerSettingsDTO;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    private static int maxCrawledPageQty;
    private static long delayMillis;
    private static String resultFilename;
    private static List<String> domains = new ArrayList<>();
    private static Set<String> allLinks = new ConcurrentSkipListSet<>();

    public static void main(String[] args) throws Exception {
        final long startMillis = System.currentTimeMillis();

        if (args.length == 0) {
            System.out.println("Please set filename where from read properties");
            System.exit(1);
        }
        String filename = args[0];
        readProperties(filename);

        submitDomains4Crawl();
        writeLinks();

        final long elapsedMillis = System.currentTimeMillis() - startMillis;
        System.out.println("DONE, time spent (seconds): " + elapsedMillis/1000);
    }

    private static void writeLinks() throws FileNotFoundException {
        System.out.println("Writing results file: " + resultFilename);
        try (PrintWriter writer = new PrintWriter(resultFilename)) {
            for (String s : allLinks) {
                writer.println(s);
            }
        }
    }

    private static void submitDomains4Crawl() {
        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

        final CountDownLatch latch = new CountDownLatch(domains.size());

        for (String domain : domains) {
            ListenableFuture<CrawlerResultsDTO> future = executorService.submit(new CrawlerCallable(new CrawlerSettingsDTO(domain, maxCrawledPageQty, delayMillis)));

            Futures.addCallback(future, new FutureCallback<CrawlerResultsDTO>() {
                public void onSuccess(CrawlerResultsDTO res) {
                    allLinks.addAll(res.getLinks());
                    latch.countDown();
                }

                public void onFailure(Throwable thrown) {
                    // do nothing here
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            executorService.shutdownNow();
            executorService.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            //
        }
    }

    private static void readProperties(String filename) throws Exception {
        Properties p = new Properties();
        try {
            p.load(new FileInputStream(filename));
        } catch (IOException e) {
            System.out.println("Cannot open file: " + filename);
            throw e;
        }

        maxCrawledPageQty = Integer.parseInt(p.getProperty("site.maxPageQty"));
        delayMillis = Long.parseLong(p.getProperty("site.delayMillis"));
        String domainListFilename = p.getProperty("domains.filename");
        resultFilename = p.getProperty("result.links.filename");

        try {
            readDomains(domainListFilename);
        } catch (IOException e) {
            System.out.println("Cannot open file: " + domainListFilename);
            throw e;
        }
    }

    private static void readDomains(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine())  != null) {
                line = line.trim().toLowerCase();
                if (line.isEmpty()) {
                    continue;
                }
                domains.add(line);
            }
        }
    }
}
