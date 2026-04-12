// 1320. Minimum Distance to Type a Word Using Two Fingers

class Solution {
    public int minimumDistance(String word) {
        int n = word.length();
        final int INF = 1 << 30;
      
        // dp[i][j][k] represents the minimum distance to type first i+1 characters
        // with left finger on character j and right finger on character k
        int[][][] dp = new int[n][26][26];
      
        // Initialize all states with infinity
        for (int[][] row : dp) {
            for (int[] cell : row) {
                Arrays.fill(cell, INF);
            }
        }
      
        // Base case: first character can be typed by either finger with 0 cost
        // The other finger can be at any position
        int firstChar = word.charAt(0) - 'A';
        for (int j = 0; j < 26; ++j) {
            dp[0][firstChar][j] = 0;  // Left finger types first char, right at position j
            dp[0][j][firstChar] = 0;  // Right finger types first char, left at position j
        }
      
        // Fill dp table for remaining characters
        for (int i = 1; i < n; ++i) {
            int prevChar = word.charAt(i - 1) - 'A';
            int currChar = word.charAt(i) - 'A';
            int distance = dist(prevChar, currChar);
          
            for (int j = 0; j < 26; ++j) {
                // Case 1: Move the finger that was on previous character to current character
                // Left finger was on prevChar, move it to currChar
                dp[i][currChar][j] = Math.min(dp[i][currChar][j], dp[i - 1][prevChar][j] + distance);
                // Right finger was on prevChar, move it to currChar
                dp[i][j][currChar] = Math.min(dp[i][j][currChar], dp[i - 1][j][prevChar] + distance);
              
                // Case 2: Use the other finger (that wasn't on prevChar) to type currChar
                if (j == prevChar) {
                    for (int k = 0; k < 26; ++k) {
                        int moveDistance = dist(k, currChar);
                        // Left finger was at k, move it to currChar (right stays at prevChar=j)
                        dp[i][currChar][j] = Math.min(dp[i][currChar][j], dp[i - 1][k][prevChar] + moveDistance);
                        // Right finger was at k, move it to currChar (left stays at prevChar=j)
                        dp[i][j][currChar] = Math.min(dp[i][j][currChar], dp[i - 1][prevChar][k] + moveDistance);
                    }
                }
            }
        }
      
        // Find minimum distance among all valid final states
        // One finger must be on the last character
        int result = INF;
        int lastChar = word.charAt(n - 1) - 'A';
        for (int j = 0; j < 26; ++j) {
            result = Math.min(result, dp[n - 1][j][lastChar]);  // Right finger on last char
            result = Math.min(result, dp[n - 1][lastChar][j]);  // Left finger on last char
        }
      
        return result;
    }
  
    /**
     * Calculate Manhattan distance between two keys on a 6x5 keyboard layout
     * Keys are numbered 0-25 (A-Z) in row-major order
     */
    private int dist(int a, int b) {
        int row1 = a / 6, col1 = a % 6;
        int row2 = b / 6, col2 = b % 6;
        return Math.abs(row1 - row2) + Math.abs(col1 - col2);
    }
}