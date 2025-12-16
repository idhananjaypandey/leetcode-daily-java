// 3562. Maximum Profit from Trading Stocks with Discounts

class Solution {
    private List<Integer>[] tree;
    private int[] present;
    private int[] future;
    private int budget;

    public int maxProfit(int n, int[] present, int[] future, int[][] hierarchy, int budget) {
        this.present = present;
        this.future = future;
        this.budget = budget;
        this.tree = new ArrayList[n];
        
        for (int i = 0; i < n; i++) {
            tree[i] = new ArrayList<>();
        }
        for (int[] edge : hierarchy) {
            // Adjusting for 1-based indexing in hierarchy
            tree[edge[0] - 1].add(edge[1] - 1);
        }

        // Result[0] is noDiscount, Result[1] is withDiscount
        int[][] result = dp(0, -1);
        
        int maxProfit = 0;
        for (int p : result[0]) {
            maxProfit = Math.max(maxProfit, p);
        }
        return maxProfit;
    }

    private int[][] dp(int u, int parent) {
        int[] noDiscount = new int[budget + 1];
        int[] withDiscount = new int[budget + 1];

        for (int v : tree[u]) {
            if (v == parent) continue;
            
            int[][] childRes = dp(v, u);
            noDiscount = merge(noDiscount, childRes[0]);
            withDiscount = merge(withDiscount, childRes[1]);
        }

        int[] newDp0 = Arrays.copyOf(noDiscount, budget + 1);
        int[] newDp1 = Arrays.copyOf(noDiscount, budget + 1);

        // 1. Buy current node at full cost
        int fullCost = present[u];
        int profitFull = future[u] - fullCost;
        for (int b = budget; b >= fullCost; b--) {
            // Note: withDiscount is used here because child nodes 
            // can be discounted if current node is bought
            newDp0[b] = Math.max(newDp0[b], withDiscount[b - fullCost] + profitFull);
        }

        // 2. Buy current node at half cost (discounted by parent)
        int halfCost = present[u] / 2;
        int profitHalf = future[u] - halfCost;
        for (int b = budget; b >= halfCost; b--) {
            newDp1[b] = Math.max(newDp1[b], withDiscount[b - halfCost] + profitHalf);
        }

        return new int[][]{newDp0, newDp1};
    }

    private int[] merge(int[] dpA, int[] dpB) {
        int[] merged = new int[budget + 1];
        Arrays.fill(merged, -1); // Using -1 for negative infinity 
        merged[0] = 0;

        for (int i = 0; i <= budget; i++) {
            if (dpA[i] == -1) continue;
            for (int j = 0; j <= budget - i; j++) {
                if (dpB[j] == -1) continue;
                merged[i + j] = Math.max(merged[i + j], dpA[i] + dpB[j]);
            }
        }
        return merged;
    }
}