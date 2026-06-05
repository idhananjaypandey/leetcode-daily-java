// 3753. Total Waviness of Numbers in Range II

class Solution {

    // DP table: [idx][prev][prev2][isLess][isStarted]
    // Each state stores an array of size 2: {count, sum}
    private long[][][][][][] memo;

    public long totalWaviness(long num1, long num2) {
        return solve(num2) - solve(num1 - 1);
    }

    private long solve(long num) {
        if (num < 100) {
            return 0; // Numbers with fewer than 3 digits have 0 waviness
        }

        String s = Long.toString(num);
        int n = s.length();
        
        // Dimensions: 
        // idx (up to 16)
        // prev (11 states: 0-9, and 10 for dummy/unset)
        // prev2 (11 states: 0-9, and 10 for dummy/unset)
        // isLess (2 states: 0 or 1)
        // isStarted (2 states: 0 or 1)
        // result (2 states: 0 for count, 1 for sum)
        memo = new long[n][11][11][2][2][2];
        for (long[][][][][] a : memo) {
            for (long[][][][] b : a) {
                for (long[][][] c : b) {
                    for (long[][] d : c) {
                        for (long[] e : d) {
                            Arrays.fill(e, -1);
                        }
                    }
                }
            }
        }

        return borderSolve(s, 0, 10, 10, 0, 0)[1];
    }

    private long[] borderSolve(String s, int idx, int prev, int prev2, int isLess, int isStarted) {
        if (idx == s.length()) {
            return new long[]{1, 0}; // Base case: Found 1 valid number, 0 additional waviness
        }

        if (memo[idx][prev][prev2][isLess][isStarted][0] != -1) {
            return new long[]{
                memo[idx][prev][prev2][isLess][isStarted][0],
                memo[idx][prev][prev2][isLess][isStarted][1]
            };
        }

        long totalCount = 0;
        long totalSum = 0;

        int limit = (isLess == 1) ? 9 : (s.charAt(idx) - '0');

        for (int d = 0; d <= limit; d++) {
            int nextIsLess = (isLess == 1 || d < limit) ? 1 : 0;
            int nextIsStarted = (isStarted == 1 || d > 0) ? 1 : 0;

            int wavinessContribution = 0;
            // Check if the PREVIOUS digit forms a peak or a valley with prev2 and current digit d
            if (isStarted == 1 && prev2 != 10 && prev != 10) {
                if (prev > prev2 && prev > d) {
                    wavinessContribution = 1; // Peak
                } else if (prev < prev2 && prev < d) {
                    wavinessContribution = 1; // Valley
                }
            }

            long[] nextRes;
            if (nextIsStarted == 0) {
                // Leading zeros, keep states as unstarted/dummy
                nextRes = borderSolve(s, idx + 1, 10, 10, nextIsLess, 0);
            } else {
                if (isStarted == 0) {
                    // This digit 'd' is the very first actual digit of the number
                    nextRes = borderSolve(s, idx + 1, d, 10, nextIsLess, 1);
                } else {
                    // Transition naturally shifting the window tracking the digits
                    nextRes = borderSolve(s, idx + 1, d, prev, nextIsLess, 1);
                }
            }

            long countFromNext = nextRes[0];
            long sumFromNext = nextRes[1];

            totalCount += countFromNext;
            // Total sum = sum from subproblems + (waviness introduced here * quantity of valid branches down the line)
            totalSum += sumFromNext + (wavinessContribution * countFromNext);
        }

        memo[idx][prev][prev2][isLess][isStarted][0] = totalCount;
        memo[idx][prev][prev2][isLess][isStarted][1] = totalSum;

        return new long[]{totalCount, totalSum};
    }
}