// 3518. Smallest Palindromic Rearrangement II

class Solution {
    private static final long LIMIT = 1_000_001; // Cap at upper limit to prevent overflow

    public String smallestPalindrome(String s, int k) {
        int n = s.length();
        int[] cnt = new int[26];
        for (int i = 0; i < n; i++) {
            cnt[s.charAt(i) - 'a']++;
        }

        int[] half = new int[26];
        char mid = 0;
        int oddCount = 0;

        for (int i = 0; i < 26; i++) {
            half[i] = cnt[i] / 2;
            if (cnt[i] % 2 != 0) {
                mid = (char) ('a' + i);
                oddCount++;
            }
        }

        // If more than one character has an odd frequency, a palindrome cannot be formed
        if (oddCount > 1) {
            return "";
        }

        int m = n / 2;
        long totalWays = countWaysTotal(half, m);
        if (totalWays < k) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        long currentK = k;

        // Build the first half character by character
        for (int pos = 0; pos < m; pos++) {
            int remLen = m - 1 - pos;

            for (int c = 0; c < 26; c++) {
                if (half[c] == 0) continue;

                // Try placing character 'c' at position pos
                half[c]--;
                long ways = countWaysTotal(half, remLen);

                if (currentK <= ways) {
                    sb.append((char) ('a' + c));
                    break; // Successfully placed, move to next position
                } else {
                    currentK -= ways;
                    half[c]++; // Backtrack and try next character
                }
            }
        }

        // Construct full palindromic string
        String firstHalf = sb.toString();
        StringBuilder res = new StringBuilder(firstHalf);
        if (n % 2 != 0) {
            res.append(mid);
        }
        for (int i = firstHalf.length() - 1; i >= 0; i--) {
            res.append(firstHalf.charAt(i));
        }

        return res.toString();
    }

    private long countWaysTotal(int[] f, int len) {
        long ways = 1;
        int rem = len;
        for (int i = 0; i < 26; i++) {
            if (f[i] > 0) {
                long c = comb(rem, f[i]);
                ways = capMul(ways, c);
                rem -= f[i];
            }
        }
        return ways;
    }

    private long capMul(long a, long b) {
        if (a == 0 || b == 0) return 0;
        if (a >= LIMIT || b >= LIMIT || a > LIMIT / b) return LIMIT;
        return Math.min(LIMIT, a * b);
    }

    private long comb(int n, int r) {
        if (r < 0 || r > n) return 0;
        r = Math.min(r, n - r);
        if (r == 0) return 1;

        long res = 1;
        for (int i = 1; i <= r; i++) {
            res = res * (n - r + i) / i;
            if (res >= LIMIT) return LIMIT;
        }
        return res;
    }
}