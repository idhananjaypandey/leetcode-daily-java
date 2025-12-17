// 3573. Best Time to Buy and Sell Stock V

import java.util.Arrays;

class Solution {
    private long[][][] memo;
    private int[] prices;
    private final long INF = Long.MAX_VALUE / 4;

    public long maximumProfit(int[] prices, int k) {
        int n = prices.length;
        this.prices = prices;
        memo = new long[n][k + 1][3];
        for (long[][] row : memo) {
            for (long[] col : row) {
                Arrays.fill(col, -1);
            }
        }

        return dfs(n - 1, k, 0);
    }

    private long dfs(int i, int j, int endState) {
        if (j < 0) {
            return -INF;
        }
        if (i < 0) {
            return endState != 0 ? -INF : 0;
        }
        
        if (memo[i][j][endState] != -1) {
            return memo[i][j][endState];
        }

        long res;
        int p = prices[i];

        if (endState == 0) {
            res = Math.max(dfs(i - 1, j, 0), 
                  Math.max(dfs(i - 1, j, 1) + p, 
                           dfs(i - 1, j, 2) - p));
        } else if (endState == 1) {
            res = Math.max(dfs(i - 1, j, 1), dfs(i - 1, j - 1, 0) - p);
        } else {
            res = Math.max(dfs(i - 1, j, 2), dfs(i - 1, j - 1, 0) + p);
        }

        return memo[i][j][endState] = res;
    }
}