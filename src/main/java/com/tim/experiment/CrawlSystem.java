package com.tim.experiment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CrawlSystem
{
  private int numCrawlers = 4;
  private final PageLoader pageLoader = new PageLoader();
  private final CrawlFetcher fetcher;
  private final CrawlParser parser;

  private List<Crawl> crawlers = new ArrayList<>();
  private Queue<String> urlQueue = new ConcurrentLinkedQueue<>();
  private Map<String, String> firstDegreeTable = new ConcurrentHashMap<>();
  private ExecutorService workerPool = Executors.newFixedThreadPool(numCrawlers);

  public CrawlSystem(final CrawlFetcher fetcher, final CrawlParser parser) {
    this.fetcher = fetcher;
    this.parser = parser;
  }

  public void initialize(File seed) throws FileNotFoundException
  {
    final List<String> urls = pageLoader.load(seed);

    urlQueue.addAll(urls);

    for (int counter = 0; counter < numCrawlers; counter++) {
      final Crawl crawl = new Crawl(urlQueue, firstDegreeTable, fetcher, parser);
      crawlers.add(crawl);
      workerPool.submit(crawl);
    }
  }

  /**
   * @throws InterruptedException If not able to shutdown cleanly.
   */
  public void terminate() throws InterruptedException
  {
    crawlers.forEach(crawl -> crawl.terminate());
    workerPool.shutdown();
    workerPool.awaitTermination(1, TimeUnit.MINUTES);
  }

  public List<List<String>> getShortestConnections(String page1, String page2) {
    return null;
  }
}
