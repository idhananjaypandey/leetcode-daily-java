// 3702. Longest Subsequence With Non-Zero Bitwise XOR

class Solution {
    public int longestSubsequence(int[] nums) {
        int x = 0;
        int res = nums.length;
        boolean isAllZero = true;

        for (int n : nums) {
            x ^= n;
            if (n != 0 && isAllZero) {
                isAllZero = false;
            }
        }
        
        if (isAllZero) {
            return 0;
        }
        if (x != 0) {
            return res;
        }
        return res - 1;
    }
}