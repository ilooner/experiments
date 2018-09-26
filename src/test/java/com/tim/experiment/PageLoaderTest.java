package com.tim.experiment;

import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class PageLoaderTest
{
  @Test
  public void shouldLoadCorrectly() throws FileNotFoundException
  {
    final List<String> expected = new ArrayList<>();
    expected.add("https://en.wikipedia.org/wiki/Potato");
    expected.add("https://en.wikipedia.org/wiki/Snake");

    final PageLoader loader = new PageLoader();
    final File testFile = new File("./src/test/resources/stuff.txt");

    final List<String> actual = loader.load(testFile);
    Assert.assertEquals(expected, actual);
  }
}
