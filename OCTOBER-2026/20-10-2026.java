// 3886. Sum of Sortable Integers

class Solution {
    public int sortableIntegers(int[] nums) {
        int n = nums.length;
        int ans = 0;

        // Check every divisor k of n
        for (int k = 1; k <= n; k++) {
            if (n % k != 0) {
                continue;
            }

            boolean sortable = true;

            int prevMax = -1;

            // Process consecutive blocks of length k
            for (int start = 0; start < n; start += k) {
                int end = start + k;

                int min = nums[start];
                int max = nums[start];
                int descents = 0;

                // Count descents and find minimum and maximum
                for (int i = start; i < end; i++) {
                    min = Math.min(min, nums[i]);
                    max = Math.max(max, nums[i]);

                    if (i > start && nums[i - 1] > nums[i]) {
                        descents++;
                    }
                }

                // Include the circular boundary
                if (k > 1 && nums[end - 1] > nums[start]) {
                    descents++;
                }

                // A block must be sortable by cyclic rotation
                if (descents > 1) {
                    sortable = false;
                    break;
                }

                // Consecutive sorted blocks must join in order
                if (start > 0 && prevMax > min) {
                    sortable = false;
                    break;
                }

                prevMax = max;
            }

            if (sortable) {
                ans += k;
            }
        }

        return ans;
    }
}