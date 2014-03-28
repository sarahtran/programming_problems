/*************************************************************************
 *  Compilation:  javac KeywordContext.java
 *  Execution:    java KeywordContext corpus.txt keyword1 keyword2 ...
 *  Dependencies: PrinterWriter.java
 *
 *  Read corpus from file and a list of keywords specified as command line
 *  argument. Groups all the keywords together and within their contexts
 *  and writes it to a file.
 * 
 *************************************************************************/

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.lang.Math;
import java.io.*;

public class KeywordContext {
  private class Suffix {
    int index;
    String suffix;
  }

  private class SuffixComparator implements Comparator<Suffix> {
    @Override
    public int compare(Suffix a, Suffix b) {
      return a.suffix.compareTo(b.suffix);
    }
  }

  private int M = 20;   // max length for keyword
  private int N = 50;   // max chars surrounding keyword
  private String text;  // processed text
  private List<Suffix> suffixArray;

  public KeywordContext(String filename) {
    In in = new In(filename);
    text = preprocessText(in.readAll());
    suffixSort(text);
  }

  // removes all nonspace whitespaces and downcase text
  private String preprocessText(String text) {
    return text.replaceAll("[\\s]+", " ").toLowerCase();
  }

  // construct the suffix array (using only M characters) for
  // the text and sort the suffix array to use in binary search
  private void suffixSort(String text) {
    suffixArray = new ArrayList<Suffix>();
    for (int i = 0; i < text.length(); i++) {
      if (Character.isLetter(text.charAt(i))) {
        Suffix suffix = new Suffix();
        suffix.index = i;
        suffix.suffix = text.substring(i, i + Math.min(M, text.length() - i));
        suffixArray.add(suffix);
      }
    }
    Collections.sort(suffixArray, new SuffixComparator());
  }

  // returns all the contexts of the keyword
  public String[] keywordContexts(String keyword) {
    int index = binarySearch(keyword, 0, suffixArray.size() - 1);
    int first = findFirstOccurrence(keyword, index);
    int last  = findLastOccurrence(keyword, index);
    return constructKeywordContexts(keyword.length(), first, last);
  }

  // construct the context around keyword from the original text
  private String[] constructKeywordContexts(int keywordLength, int first, int last) {
    String[] contexts = new String[last - first];
    for (int i = 0; i < last - first; i++) {
      int start = Math.max(suffixArray.get(first + i).index - N, 0);
      int end   = Math.min(suffixArray.get(first + i).index + N + keywordLength, text.length());
      contexts[i] = text.substring(start, end);
    }
    return contexts;
  }

  // find first occurrence of the keyword in the suffix array
  private int findFirstOccurrence(String keyword, int index) {
    int first = index;
    while (true) {
      if (first - 1 >= 0 && suffixArray.get(first - 1).suffix.startsWith(keyword))
        first--;
      else
        break;
    }
    return first;
  }

  // find last occurrence of the keyword in the suffix array
  private int findLastOccurrence(String keyword, int index) {
    int last = index;
    while (true) {
      if (last + 1 < suffixArray.size() && suffixArray.get(last + 1).suffix.startsWith(keyword))
        last++;
      else
        break;
    }
    return last;
  }

  // binary search to find the keyword in the suffix array
  private int binarySearch(String keyword, int lo, int hi) {
    if (lo > hi) { return -1; }

    int mid = lo + (hi - lo) / 2;

    if (suffixArray.get(mid).suffix.startsWith(keyword)) {
      return mid;
    }

    int index = -1;
    if (suffixArray.get(mid).suffix.compareTo(keyword) < 0) {
      index = binarySearch(keyword, mid + 1, hi);
    }
    else if (suffixArray.get(mid).suffix.compareTo(keyword) > 0) {
      index = binarySearch(keyword, lo, mid - 1);
    }
    return index;
  }

  public static void main(String[] args) {
    KeywordContext kc = new KeywordContext(args[0]);

    for (int i = 1; i < args.length; i++) {
      try {
        String[] keywordContexts = kc.keywordContexts(args[i]);

        PrintWriter writer = new PrintWriter("context_for_" + args[i] + ".txt");
        for (String s : keywordContexts) {
          writer.printf("%s\n", s);
        }
        writer.close();
      }
      catch (FileNotFoundException e) {
        StdOut.printf("File Not Found\n");
      }
    }
  }
}
