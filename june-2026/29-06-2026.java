// 1967. Number of Strings That Appear as Substrings in Word

class Solution {
  public int numOfStrings(String[] patterns, String word) {
    return (int) Arrays.stream(patterns).filter(pattern -> word.contains(pattern)).count();
  }
}