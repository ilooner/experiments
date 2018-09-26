package com.tim.experiment;

import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CrawlTest
{
  @Test
  public void simpleCrawlLogicTest() {
    final Queue<String> urlQueue = new ConcurrentLinkedQueue<>();
    final Map<String, List<String>> firstDegreeTable = new ConcurrentHashMap<>();

    urlQueue.add("page");
    final Crawl crawl = new Crawl(urlQueue, firstDegreeTable, new MockFetcher(), new MockParser());

    Assert.assertFalse(crawl.crawlNext());

    final List<String> expectedQueue = new ArrayList<>();
    expectedQueue.add("aaa");
    expectedQueue.add("bbb");

    final List<String> actualQueue = new ArrayList<>();
    actualQueue.addAll(urlQueue);

    final Map<String, List<String>> expectedTable = new HashMap<>();
    final List<String> pageVals = new ArrayList<>();
    pageVals.add("aaa");
    pageVals.add("bbb");

    expectedTable.put("page", pageVals);

    Assert.assertEquals(expectedQueue, actualQueue);
    Assert.assertEquals(expectedTable, firstDegreeTable);
  }

  // TODO add tests for corner cases.

  public static class MockFetcher implements CrawlFetcher {
    @Override
    public String page(String url)
    {
      return "blah";
    }
  }

  public static class MockParser implements CrawlParser {

    @Override
    public List<String> parse(String page)
    {
      List<String> mockList = new ArrayList<>();
      mockList.add("aaa");
      mockList.add("bbb");

      return mockList;
    }
  }
}
