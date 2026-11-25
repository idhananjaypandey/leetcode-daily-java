class Solution {
    private static final long MOD1 = 1_000_000_007L;
    private static final long MOD2 = 1_000_000_009L;
    private static final long BASE1 = 131_542_391L;
    private static final long BASE2 = 265_443_576L;

    public int smallestUniqueSubarray(int[] nums) {
        int n = nums.length;

        long[] pow1 = new long[n + 1];
        long[] pow2 = new long[n + 1];
        long[] pref1 = new long[n + 1];
        long[] pref2 = new long[n + 1];

        pow1[0] = 1;
        pow2[0] = 1;

        for (int i = 0; i < n; i++) {
            pow1[i + 1] = (pow1[i] * BASE1) % MOD1;
            pow2[i + 1] = (pow2[i] * BASE2) % MOD2;

            // Offset by +1 to keep all values strictly positive
            long val = nums[i] + 1L;
            pref1[i + 1] = (pref1[i] * BASE1 + val) % MOD1;
            pref2[i + 1] = (pref2[i] * BASE2 + val) % MOD2;
        }

        int low = 1, high = n;
        int ans = n;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (hasUniqueSubarray(mid, n, pref1, pref2, pow1, pow2)) {
                ans = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        return ans;
    }

    private boolean hasUniqueSubarray(int len, int n, long[] pref1, long[] pref2, long[] pow1, long[] pow2) {
        int count = n - len + 1;
        // Pre-size map to avoid rehashing overhead
        Map<Long, Integer> freq = new HashMap<>((int) (count / 0.75f) + 1);

        for (int i = 0; i < count; i++) {
            long h1 = (pref1[i + len] - (pref1[i] * pow1[len]) % MOD1 + MOD1) % MOD1;
            long h2 = (pref2[i + len] - (pref2[i] * pow2[len]) % MOD2 + MOD2) % MOD2;

            // Combine both 32-bit hashes into a single 64-bit key
            long combinedHash = (h1 << 32) | h2;
            freq.merge(combinedHash, 1, Integer::sum);
        }

        for (int val : freq.values()) {
            if (val == 1) {
                return true;
            }
        }
        return false;
    }
}