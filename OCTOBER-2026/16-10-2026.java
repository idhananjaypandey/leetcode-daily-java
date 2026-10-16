// 2916. Subarrays Distinct Element Sum of Squares II

class LazySegmentTree {
    private final int kMod;
    private final int n;
    private long[] lazy;
    private long[] sums;
    private long[] squaredSums;

    public LazySegmentTree(int n, int kMod) {
        this.n = n;
        this.kMod = kMod;
        this.lazy = new long[4 * n];
        this.sums = new long[4 * n];
        this.squaredSums = new long[4 * n];
    }

    public void updateRange(int l, int r) {
        updateRange(0, 0, n - 1, l, r);
    }

    private void propagate(int i, int l, int r) {
        long gap = r - l + 1;
        // (a + L)^2 + (b + L)^2 + (c + L)^2, where L = lazy[i]
        // a^2 + b^2 + c^2 + 2 * L (a + b + c) + L^2 * gap
        squaredSums[i] += 2 * lazy[i] * sums[i] + lazy[i] * lazy[i] * gap;
        squaredSums[i] %= kMod;
        sums[i] += lazy[i] * gap;
        sums[i] %= kMod;
        
        if (l < r) {
            lazy[i * 2 + 1] += lazy[i];
            lazy[i * 2 + 2] += lazy[i];
        }
        lazy[i] = 0;
    }

    public int getTreeSquaredSums() {
        return (int) squaredSums[0];
    }

    private void updateRange(int i, int start, int end, int l, int r) {
        if (lazy[i] > 0) {
            propagate(i, start, end);
        }
        if (end < l || start > r) {
            return;
        }
        if (start >= l && end <= r) {
            lazy[i] = 1;
            propagate(i, start, end);
            return;
        }
        
        int mid = (start + end) / 2;
        updateRange(i * 2 + 1, start, mid, l, r);
        updateRange(i * 2 + 2, mid + 1, end, l, r);
        
        sums[i] = (sums[i * 2 + 1] + sums[i * 2 + 2]) % kMod;
        squaredSums[i] = (squaredSums[i * 2 + 1] + squaredSums[i * 2 + 2]) % kMod;
    }
}

class Solution {
    public int sumCounts(int[] nums) {
        final int kMod = 1_000_000_007;
        final int n = nums.length;
        int ans = 0;
        Map<Integer, Integer> lastSeen = new HashMap<>();
        LazySegmentTree tree = new LazySegmentTree(n, kMod);
        
        for (int r = 0; r < n; ++r) {
            int l = lastSeen.containsKey(nums[r]) ? lastSeen.get(nums[r]) + 1 : 0;
            tree.updateRange(l, r);
            lastSeen.put(nums[r], r);
            ans = (int) ((ans + (long) tree.getTreeSquaredSums()) % kMod);
        }
        
        return ans;
    }
}