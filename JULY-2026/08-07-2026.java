// 3756. Concatenate Non-Zero Digits and Multiply by Sum II

class Solution {
    public int[] sumAndMultiply(String s, int[][] queries) {
        int m = s.length();
        int q = queries.length;
        long MOD = 1000000007L;

        long[] pow10 = new long[m + 1];
        pow10[0] = 1;
        for (int i = 1; i <= m; i++) {
            pow10[i] = (pow10[i - 1] * 10) % MOD;
        }

        int[] nonZeroCount = new int[m + 1];
        long[] xPref = new long[m + 1];
        int[] sumPref = new int[m + 1];

        int nzIdx = 0;
        for (int i = 0; i < m; i++) {
            int digit = s.charAt(i) - '0';
            if (digit != 0) {
                nzIdx++;
                xPref[nzIdx] = (xPref[nzIdx - 1] * 10 + digit) % MOD;
                sumPref[nzIdx] = sumPref[nzIdx - 1] + digit;
            }
            nonZeroCount[i + 1] = nzIdx;
        }

        int[] answer = new int[q];

        for (int i = 0; i < q; i++) {
            int L = queries[i][0];
            int R = queries[i][1];

            int idxL = nonZeroCount[L];
            int idxR = nonZeroCount[R + 1];

            if (idxL == idxR) {
                answer[i] = 0;
                continue;
            }

            long totalSum = sumPref[idxR] - sumPref[idxL];
            int k = idxR - idxL;

            long xRaw = xPref[idxR] - (xPref[idxL] * pow10[k]) % MOD;
            if (xRaw < 0) {
                xRaw += MOD;
            }

            long ans = (xRaw * (totalSum % MOD)) % MOD;
            answer[i] = (int) ans;
        }

        return answer;
    }
}