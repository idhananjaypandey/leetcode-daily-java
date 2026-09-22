// 3525. Find X Value of Array II

class Solution {
    int k, n;
    int[][] cnt;   // cnt[node] is flattened k*k table: cnt[node][s*k+x]
    int[] prod;    // prod[node]

    public int[] resultArray(int[] nums, int k, int[][] queries) {
        this.k = k;
        this.n = nums.length;
        int[] arr = nums.clone();
        cnt = new int[4 * n][];
        prod = new int[4 * n];
        build(1, 0, n - 1, arr);

        int[] result = new int[queries.length];
        for (int i = 0; i < queries.length; i++) {
            int idx = queries[i][0], val = queries[i][1], start = queries[i][2], x = queries[i][3];
            update(1, 0, n - 1, idx, val);
            int[] res = queryRange(start, n - 1);
            result[i] = res[x];
        }
        return result;
    }

    private void setLeaf(int node, int val) {
        int p = val % k;
        int[] c = new int[k * k];
        for (int s = 0; s < k; s++) {
            int x = (s * p) % k;
            c[s * k + x] = 1;
        }
        cnt[node] = c;
        prod[node] = p;
    }

    private void pull(int node) {
        int[] l = cnt[2 * node], r = cnt[2 * node + 1];
        int lp = prod[2 * node], rp = prod[2 * node + 1];
        int[] c = new int[k * k];
        for (int s = 0; s < k; s++) {
            int ls = (s * lp) % k;
            int base = s * k;
            int rbase = ls * k;
            for (int x = 0; x < k; x++) {
                c[base + x] = l[base + x] + r[rbase + x];
            }
        }
        cnt[node] = c;
        prod[node] = (lp * rp) % k;
    }

    private void build(int node, int lo, int hi, int[] arr) {
        if (lo == hi) {
            setLeaf(node, arr[lo]);
            return;
        }
        int mid = (lo + hi) / 2;
        build(2 * node, lo, mid, arr);
        build(2 * node + 1, mid + 1, hi, arr);
        pull(node);
    }

    private void update(int node, int lo, int hi, int idx, int val) {
        if (lo == hi) {
            setLeaf(node, val);
            return;
        }
        int mid = (lo + hi) / 2;
        if (idx <= mid) update(2 * node, lo, mid, idx, val);
        else update(2 * node + 1, mid + 1, hi, idx, val);
        pull(node);
    }

    // ---- range query returning merged (cnt, prod) over [l, r] ----

    private int[] resCnt; // scratch not needed, we return via helper objects

    private static class NodeRes {
        int[] cnt;
        int prod;
    }

    private int[] queryRange(int l, int r) {
        NodeRes res = queryHelper(1, 0, n - 1, l, r);
        int[] ans = new int[k];
        int s0 = 1 % k;
        if (res != null) {
            for (int x = 0; x < k; x++) ans[x] = res.cnt[s0 * k + x];
        }
        return ans;
    }

    private NodeRes queryHelper(int node, int lo, int hi, int l, int r) {
        if (r < lo || hi < l) return null;
        if (l <= lo && hi <= r) {
            NodeRes nr = new NodeRes();
            nr.cnt = cnt[node];
            nr.prod = prod[node];
            return nr;
        }
        int mid = (lo + hi) / 2;
        NodeRes left = queryHelper(2 * node, lo, mid, l, r);
        NodeRes right = queryHelper(2 * node + 1, mid + 1, hi, l, r);
        if (left == null) return right;
        if (right == null) return left;
        return mergeNodes(left, right);
    }

    private NodeRes mergeNodes(NodeRes left, NodeRes right) {
        int lp = left.prod, rp = right.prod;
        int[] c = new int[k * k];
        for (int s = 0; s < k; s++) {
            int ls = (s * lp) % k;
            int base = s * k, rbase = ls * k;
            for (int x = 0; x < k; x++) {
                c[base + x] = left.cnt[base + x] + right.cnt[rbase + x];
            }
        }
        NodeRes nr = new NodeRes();
        nr.cnt = c;
        nr.prod = (lp * rp) % k;
        return nr;
    }
}