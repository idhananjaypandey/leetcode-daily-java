// 1689. Partitioning Into Minimum Number Of Deci-Binary Numbers

class Solution {
  public int minPartitions(String n) {
    return Character.getNumericValue(n.chars().max().getAsInt());
  }
}