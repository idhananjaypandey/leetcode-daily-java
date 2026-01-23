// 3510. Minimum Pair Removal to Sort Array II

import java.util.*;

class Solution {
    private static class Node implements Comparable<Node> {
        long sum;
        int index;

        Node(long sum, int index) {
            this.sum = sum;
            this.index = index;
        }

        @Override
        public int compareTo(Node other) {
            // Min-heap behavior: smallest sum first, then smallest index
            if (this.sum != other.sum) {
                return Long.compare(this.sum, other.sum);
            }
            return Integer.compare(this.index, other.index);
        }
    }

    public int minimumPairRemoval(int[] a) {
        int n = a.length;
        if (n < 2) return 0;

        long[] nums = new long[n];
        int[] left = new int[n + 1];
        int[] right = new int[n + 1];
        
        // Use a much larger constant than 1e5 to prevent collision with valid sums
        long INF = Long.MAX_VALUE / 4; 

        for (int i = 0; i < n; i++) {
            nums[i] = a[i];
        }

        for (int i = 0; i <= n; i++) {
            left[i] = i - 1;
            right[i] = i + 1;
        }

        PriorityQueue<Node> q = new PriorityQueue<>();
        int bad = 0;
        int ans = 0;

        // Initialize "bad" pairs (where right < left)
        for (int i = 1; i < n; i++) {
            q.add(new Node(nums[i] + nums[i - 1], i));
            if (nums[i] < nums[i - 1]) {
                bad++;
            }
        }

        // Loop as long as there are bad pairs to resolve
        while (bad > 0 && !q.isEmpty()) {
            Node top = q.poll();
            long sum = top.sum;
            int r = top.index;
            int l = left[r];

            // Lazy deletion check: verify this pair is still valid and active
            if (l == -1 || nums[l] + nums[r] != sum) {
                continue;
            }

            // This pair is being merged, so if it was bad, decrement count
            if (nums[r] < nums[l]) {
                bad--;
            }

            // Update linked list to bypass index 'r'
            right[l] = right[r];
            if (right[r] < n + 1) {
                left[right[r]] = l;
            }

            int rr = right[r];
            int ll1 = left[l];

            // Update bad count for the interface on the left (ll1, l)
            if (ll1 != -1) {
                // Change = (new state is bad?) - (old state was bad?)
                int oldBad = (nums[l] < nums[ll1] ? 1 : 0);
                int newBad = (sum < nums[ll1] ? 1 : 0);
                bad += (newBad - oldBad);
                q.add(new Node(nums[ll1] + sum, l));
            }

            // Update bad count for the interface on the right (l, rr)
            if (rr < n) {
                // Change = (new state is bad?) - (old state was bad?)
                int oldBad = (nums[rr] < nums[r] ? 1 : 0);
                int newBad = (nums[rr] < sum ? 1 : 0);
                bad += (newBad - oldBad);
                q.add(new Node(sum + nums[rr], rr));
            }

            // Perform the merge: update the left element and "delete" the right one
            nums[l] = sum;
            nums[r] = INF; 
            ans++;
        }

        return ans;
    }
}