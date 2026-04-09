// 3655. XOR After Range Multiplication Queries II

import java.util.*;

class Solution {
    static final int MOD = 1_000_000_007;

    public int xorAfterQueries(int[] nums, int[][] queries) {
        int n = nums.length;
        int B = (int) Math.sqrt(n);

        long[] mul = new long[n];
        Arrays.fill(mul, 1);

        Map<Integer, List<int[]>> smallK = new HashMap<>();

        for (int[] q : queries) {
            int l = q[0], r = q[1], k = q[2], v = q[3];

            if (k > B) {
                for (int i = l; i <= r; i += k) {
                    mul[i] = (mul[i] * v) % MOD;
                }
            } else {
                smallK.computeIfAbsent(k, x -> new ArrayList<>()).add(q);
            }
        }

        for (int k : smallK.keySet()) {
            List<int[]> list = smallK.get(k);
            Map<Integer, long[]> diffMap = new HashMap<>();

            for (int[] q : list) {
                int l = q[0], r = q[1], v = q[3];
                int rem = l % k;

                int len = (n - rem + k - 1) / k;
                diffMap.putIfAbsent(rem, new long[len + 1]);

                long[] diff = diffMap.get(rem);

                int L = (l - rem) / k;
                int R = (r - rem) / k;

                if (diff[L] == 0) diff[L] = 1;
                diff[L] = (diff[L] * v) % MOD;

                if (R + 1 < diff.length) {
                    if (diff[R + 1] == 0) diff[R + 1] = 1;
                    diff[R + 1] = (diff[R + 1] * modInverse(v)) % MOD;
                }
            }

            for (int rem : diffMap.keySet()) {
                long[] diff = diffMap.get(rem);
                long cur = 1;

                for (int i = 0; i < diff.length; i++) {
                    long val = (diff[i] == 0 ? 1 : diff[i]);
                    cur = (cur * val) % MOD;

                    int idx = rem + i * k;
                    if (idx < n) {
                        mul[idx] = (mul[idx] * cur) % MOD;
                    }
                }
            }
        }

        int xor = 0;
        for (int i = 0; i < n; i++) {
            long val = (nums[i] * mul[i]) % MOD;
            xor ^= (int) val;
        }

        return xor;
    }

    private long modInverse(long x) {
        return power(x, MOD - 2);
    }

    private long power(long a, long b) {
        long res = 1;
        a %= MOD;
        while (b > 0) {
            if ((b & 1) == 1) res = (res * a) % MOD;
            a = (a * a) % MOD;
            b >>= 1;
        }
        return res;
    }
}