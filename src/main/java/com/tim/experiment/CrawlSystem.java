package com.tim.experiment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CrawlSystem
{
  private PageLoader pageLoader = new PageLoader();

  private Queue<String> urlQueue = new ConcurrentLinkedQueue<>();
  private Map<String, String> firstDegreeTable = new ConcurrentHashMap<>();
  private ExecutorService workerPool = Executors.newFixedThreadPool(4);

  public CrawlSystem() {
  }

  public void initialize(File seed) throws FileNotFoundException
  {
    final List<String> urls = pageLoader.load(seed);

    urlQueue.addAll(urls);

    //workerPool.submit()
  }

  public List<List<String>> getShortestConnections() {
    return null;
  }
}
