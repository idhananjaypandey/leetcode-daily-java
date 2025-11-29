// 3512. Minimum Operations to Make Array Sum Divisible by K

class Solution {
  public int minOperations(int[] nums, int k) {
    return Arrays.stream(nums).sum() % k;
  }
}