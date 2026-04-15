// 2515. Shortest Distance to Target String in a Circular Array

class Solution {
  public int closestTarget(String[] words, String target, int startIndex) {
    final int n = words.length;

    for (int i = 0; i < n; ++i) {
      // Check forward (clockwise)
      if (words[(startIndex + i + n) % n].equals(target))
        return i;
      // Check backward (counter-clockwise)
      if (words[(startIndex - i + n) % n].equals(target))
        return i;
    }

    return -1;
  }
}