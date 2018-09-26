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
  private Map<String, List<String>> firstDegreeTable = new ConcurrentHashMap<>();
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

  public List<List<String>> getShortestConnections(String page1,
                                                   String page2) {
    return getShortestConnections(firstDegreeTable, page1, page2);
  }

  public static List<List<String>> getShortestConnections(Map<String, List<String>> firstDegreeTable,
                                                          String page1,
                                                          String page2) {
    List<List<String>> connections1To2 = shortestConnection(firstDegreeTable, page1, page2);
    List<List<String>> connections2To1 = shortestConnection(firstDegreeTable, page2, page1);

    // Get all the connections
    connections1To2.addAll(connections2To1);
    return connections1To2;
  }

  public static List<List<String>> shortestConnection(Map<String, List<String>> firstDegreeTable,
                                                      String src,
                                                      String dest) {

    return null;
  }
}
