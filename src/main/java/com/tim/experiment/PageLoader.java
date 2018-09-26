package com.tim.experiment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PageLoader
{
  public PageLoader() {
  }

  public List<String> load(File file) throws FileNotFoundException
  {
    final Scanner scanner = new Scanner(file);
    final List<String> urls = new ArrayList<>();

    while (scanner.hasNextLine()) {
      final String url = scanner.nextLine().trim();

      if (url.isEmpty()) {
        // Skip empty lines
        continue;
      }

      urls.add(url);
    }

    return urls;
  }
}
