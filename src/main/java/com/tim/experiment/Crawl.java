package com.tim.experiment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Crawl implements Runnable
{
  private final Queue<String> urlQueue;
  private final Map<String, List<String>> firstDegreeTable;
  private volatile boolean terminate = false;

  private final CrawlFetcher fetcher;
  private final CrawlParser parser;

  public Crawl(final Queue<String> urlQueue,
               final Map<String, List<String>> firstDegreeTable,
               final CrawlFetcher fetcher,
               final CrawlParser parser) {
    this.urlQueue = urlQueue;
    this.firstDegreeTable = firstDegreeTable;
    this.fetcher = fetcher;
    this.parser = parser;
  }

  public void terminate() {
    terminate = true;
  }

  /**
   * Visible for testing
   * Returns true on an empty queue.
   * @return True if the queue is empty. False if it was not.
   */
  public boolean crawlNext() {
    final String src = urlQueue.poll();

    if (src == null) {
      return true;
    }

    final String pageData = fetcher.page(src);

    if (pageData == null) {
      // Skip bad page
      return false;
    }

    List<String> dests = parser.parse(pageData);

    dests.forEach(dest -> {
      List<String> destList = firstDegreeTable.get(src);

      if (destList == null) {
        destList = Collections.synchronizedList(new ArrayList<>());
      }

      destList.add(dest);
      firstDegreeTable.put(src, destList);

      // There is a chance that two workers will encounter the same destination twice. In this case the same
      // destination will be added to the queue twice and the work will be duplicated. We are okay with that
      // since parsing a page should be idempotent so crawling it twice will not corrupt any data. The assumption
      // here is that the coordination to prevent this case will cost more than occassionally duplicating work. Also
      // the code is much simpler this way :).
      if (!firstDegreeTable.containsKey(dest)) {
        urlQueue.add(dest);
      }
    });

    return false;
  }

  public void run() {
    while (!terminate) {
      if (crawlNext()) {
        // Queue was empty so wait before polling again.

        try {
          Thread.sleep(100L);
        } catch (InterruptedException e) {
          // Do nothing
        }

        continue;
      }
    }
  }
}
