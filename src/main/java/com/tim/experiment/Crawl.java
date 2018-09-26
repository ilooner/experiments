package com.tim.experiment;

import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Crawl implements Runnable
{
  private final Queue<String> urlQueue;
  private final Map<String, String> firstDegreeTable;
  private volatile boolean terminate = false;

  private CrawlFetcher fetcher = new CrawlFetcher();
  private CrawlParser parser = new CrawlParser();

  public Crawl(final Queue<String> urlQueue, final Map<String, String> firstDegreeTable) {
    this.urlQueue = urlQueue;
    this.firstDegreeTable = firstDegreeTable;
  }

  public void terminate() {
    terminate = true;
  }

  // Visible for testing
  public void logic(final String src) {

    final String pageData = fetcher.page(src);

    if (pageData == null) {
      // Skip bad page
      return;
    }

    List<String> dests = parser.parse(pageData);

    dests.forEach(dest -> {
      firstDegreeTable.put(src, dest);

      if (!firstDegreeTable.containsKey(dest)) {
        urlQueue.add(dest);
      }
    });
  }

  public void run() {
    while (!terminate) {
      final String src = urlQueue.poll();

      if (src == null) {
        try {
          Thread.sleep(100L);
        } catch (InterruptedException e) {
          // Do nothing
        }

        continue;
      }

      logic(src);
    }
  }
}
