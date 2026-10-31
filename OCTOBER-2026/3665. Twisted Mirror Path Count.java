class Solution {
    public int uniquePaths(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        long MOD = 1_000_000_007L;

        int[][] right = new int[m][n];
        int[][] down = new int[m][n];

        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {

                if (grid[i][j] == 0) {
                    right[i][j] = i * n + j;
                    down[i][j] = i * n + j;
                } else {
                    right[i][j] = (i + 1 < m)
                            ? down[i + 1][j] : -1;

                    down[i][j] = (j + 1 < n)
                            ? right[i][j + 1] : -1;
                }
            }
        }

        long[] dp = new long[m * n];
        dp[0] = 1;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {

                if (grid[i][j] == 1) {
                    continue;
                }

                long ways = dp[i * n + j];

                if (ways == 0) {
                    continue;
                }

                if (j + 1 < n) {
                    int next = right[i][j + 1];

                    if (next != -1) {
                        dp[next] = (dp[next] + ways) % MOD;
                    }
                }

                if (i + 1 < m) {
                    int next = down[i + 1][j];

                    if (next != -1) {
                        dp[next] = (dp[next] + ways) % MOD;
                    }
                }
            }
        }

        return (int) dp[m * n - 1];
    }
}