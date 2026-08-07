// 3348. Smallest Divisible Digit Product II

class Solution {
  public String smallestNumber(String num, long t) {
    Pair<Map<Integer, Integer>, Boolean> primeCountResult = getPrimeCount(t);
    Map<Integer, Integer> primeCount = primeCountResult.getKey();
    boolean isDivisible = primeCountResult.getValue();
    if (!isDivisible)
      return "-1";

    Map<Integer, Integer> factorCount = getFactorCount(primeCount);
    if (sumValues(factorCount) > num.length())
      return construct(factorCount);

    Map<Integer, Integer> primeCountPrefix = getPrimeCount(num);
    int firstZeroIndex = num.indexOf('0');
    if (firstZeroIndex == -1) {
      firstZeroIndex = num.length();
      if (isSubset(primeCount, primeCountPrefix))
        return num;
    }

    for (int i = num.length() - 1; i >= 0; --i) {
      final int d = num.charAt(i) - '0';
      // Remove the current digit's factors from primeCountPrefix.
      primeCountPrefix = subtract(primeCountPrefix, FACTOR_COUNTS.get(d));
      final int spaceAfterThisDigit = num.length() - 1 - i;
      if (i > firstZeroIndex)
        continue;
      for (int biggerDigit = d + 1; biggerDigit < 10; ++biggerDigit) {
        // Compute the required factors after replacing with a larger digit.
        Map<Integer, Integer> factorsAfterReplacement = getFactorCount(
            subtract(subtract(primeCount, primeCountPrefix), FACTOR_COUNTS.get(biggerDigit)));
        // Check if the replacement is possible within the available space.
        if (sumValues(factorsAfterReplacement) <= spaceAfterThisDigit) {
          // Fill extra space with '1', if any, and construct the result.
          final int fillOnes = spaceAfterThisDigit - sumValues(factorsAfterReplacement);
          return num.substring(0, i) + // Keep the prefix unchanged.
              biggerDigit +            // Replace the current digit.
              "1".repeat(fillOnes) + // Fill remaining space with '1'.
              construct(factorsAfterReplacement);
        }
      }
    }

    // No solution of the same length exists, so we need to extend the number
    // by prepending '1's and adding the required factors.
    Map<Integer, Integer> factorsAfterExtension = getFactorCount(primeCount);
    return "1".repeat(num.length() + 1 - sumValues(factorsAfterExtension)) +
        construct(factorsAfterExtension);
  }

  private static final Map<Integer, Map<Integer, Integer>> FACTOR_COUNTS = Map.of(
      0, Map.of(), 1, Map.of(), 2, Map.of(2, 1), 3, Map.of(3, 1), 4, Map.of(2, 2), 5, Map.of(5, 1),
      6, Map.of(2, 1, 3, 1), 7, Map.of(7, 1), 8, Map.of(2, 3), 9, Map.of(3, 2));

  // Returns the prime count of t and if t is divisible by 2, 3, 5, 7.
  private Pair<Map<Integer, Integer>, Boolean> getPrimeCount(long t) {
    Map<Integer, Integer> count = new HashMap<>(Map.of(2, 0, 3, 0, 5, 0, 7, 0));
    for (int prime : new int[] {2, 3, 5, 7}) {
      while (t % prime == 0) {
        t /= prime;
        count.put(prime, count.get(prime) + 1);
      }
    }
    return new Pair<>(count, t == 1);
  }

  // Returns the prime count of `num`.
  private Map<Integer, Integer> getPrimeCount(String num) {
    Map<Integer, Integer> count = new HashMap<>(Map.of(2, 0, 3, 0, 5, 0, 7, 0));
    for (final char c : num.toCharArray()) {
      Map<Integer, Integer> digitFactors = FACTOR_COUNTS.get(c - '0');
      for (Map.Entry<Integer, Integer> entry : digitFactors.entrySet()) {
        final int prime = entry.getKey();
        final int freq = entry.getValue();
        count.merge(prime, freq, Integer::sum);
      }
    }
    return count;
  }

  private Map<Integer, Integer> getFactorCount(Map<Integer, Integer> count) {
    // 2^3 = 8
    final int count8 = count.get(2) / 3;
    final int remaining2 = count.get(2) % 3;
    // 3^2 = 9
    final int count9 = count.get(3) / 2;
    int count3 = count.get(3) % 2;
    // 2^2 = 4
    int count4 = remaining2 / 2;
    int count2 = remaining2 % 2;
    // Combine 2 and 3 to 6 if both are present
    int count6 = 0;
    if (count2 == 1 && count3 == 1) {
      count2 = 0;
      count3 = 0;
      count6 = 1;
    }
    // Combine 3 and 4 to 2 and 6 if both are present
    if (count3 == 1 && count4 == 1) {
      count2 = 1;
      count6 = 1;
      count3 = 0;
      count4 = 0;
    }
    return Map.of(2, count2, 3, count3, 4, count4, 5, count.get(5), 6, count6, 7, count.get(7), 8,
                  count8, 9, count9);
  }

  private String construct(Map<Integer, Integer> factors) {
    StringBuilder sb = new StringBuilder();
    for (int digit = 2; digit < 10; ++digit)
      sb.append(String.valueOf(digit).repeat(factors.get(digit)));
    return sb.toString();
  }

  // Returns true if a is a subset of b.
  private boolean isSubset(Map<Integer, Integer> a, Map<Integer, Integer> b) {
    for (Map.Entry<Integer, Integer> entry : a.entrySet())
      if (b.get(entry.getKey()) < entry.getValue())
        return false;
    return true;
  }

  // Returns a - b.
  private Map<Integer, Integer> subtract(Map<Integer, Integer> a, Map<Integer, Integer> b) {
    Map<Integer, Integer> res = new HashMap<>(a);
    for (Map.Entry<Integer, Integer> entry : b.entrySet()) {
      final int key = entry.getKey();
      final int value = entry.getValue();
      res.put(key, Math.max(0, res.get(key) - value));
    }
    return res;
  }

  // Returns the sum of the values in `count`.
  private int sumValues(Map<Integer, Integer> count) {
    return count.values().stream().mapToInt(Integer::intValue).sum();
  }
}


===========2nd approach===========

import java.util.*;

class Solution {
    int[] A = new int[10]; // exponent of 2 contributed by digit
    int[] B = new int[10]; // exponent of 3
    int[] C = new int[10]; // exponent of 5
    int[] G = new int[10]; // exponent of 7
    int CAP2, CAP3;
    int[][] dp; // dp[a][b] = min digits (from {2,3,4,6,8,9}) to reach exponent2>=a, exponent3>=b

    public String smallestNumber(String num, long t) {
        long tt = t;
        long e2 = 0, e3 = 0, e5 = 0, e7 = 0;
        while (tt % 2 == 0) { tt /= 2; e2++; }
        while (tt % 3 == 0) { tt /= 3; e3++; }
        while (tt % 5 == 0) { tt /= 5; e5++; }
        while (tt % 7 == 0) { tt /= 7; e7++; }
        if (tt != 1) return "-1"; // t has a prime factor other than 2,3,5,7 -> impossible

        A[2]=1; A[4]=2; A[6]=1; A[8]=3;
        B[3]=1; B[6]=1; B[9]=2;
        C[5]=1;
        G[7]=1;

        CAP2 = (int) e2;
        CAP3 = (int) e3;
        buildDp();

        int n = num.length();
        int z = n;
        for (int i = 0; i < n; i++) if (num.charAt(i) == '0') { z = i; break; }

        long[] cum2 = new long[z + 1], cum3 = new long[z + 1], cum5 = new long[z + 1], cum7 = new long[z + 1];
        for (int i = 1; i <= z; i++) {
            int dig = num.charAt(i - 1) - '0';
            cum2[i] = cum2[i - 1] + A[dig];
            cum3[i] = cum3[i - 1] + B[dig];
            cum5[i] = cum5[i - 1] + C[dig];
            cum7[i] = cum7[i - 1] + G[dig];
        }

        if (z == n) { // num itself is zero-free
            if (cum2[n] >= e2 && cum3[n] >= e3 && cum5[n] >= e5 && cum7[n] >= e7) return num;
        }

        int startP = (z == n) ? n - 1 : z;
        for (int p = startP; p >= 0; p--) {
            long re2 = Math.max(0, e2 - cum2[p]);
            long re3 = Math.max(0, e3 - cum3[p]);
            long re5 = Math.max(0, e5 - cum5[p]);
            long re7 = Math.max(0, e7 - cum7[p]);
            int startDigit = (num.charAt(p) - '0') + 1;
            int m = n - 1 - p;
            for (int d = startDigit; d <= 9; d++) {
                long ne2 = Math.max(0, re2 - A[d]);
                long ne3 = Math.max(0, re3 - B[d]);
                long ne5 = Math.max(0, re5 - C[d]);
                long ne7 = Math.max(0, re7 - G[d]);
                long total = dp[(int) ne2][(int) ne3] + ne5 + ne7;
                if (total <= m) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(num, 0, p).append((char) ('0' + d)).append(fillSuffix(m, ne2, ne3, ne5, ne7));
                    return sb.toString();
                }
            }
        }

        int D = dp[CAP2][CAP3] + (int) e5 + (int) e7;
        int L = Math.max(n + 1, D);
        return fillSuffix(L, e2, e3, e5, e7);
    }

    void buildDp() {
        dp = new int[CAP2 + 1][CAP3 + 1];
        int[] digits = {2, 3, 4, 6, 8, 9};
        for (int s = 0; s <= CAP2 + CAP3; s++) {
            for (int a = Math.max(0, s - CAP3); a <= Math.min(CAP2, s); a++) {
                int b = s - a;
                if (a == 0 && b == 0) { dp[0][0] = 0; continue; }
                int best = Integer.MAX_VALUE;
                for (int dig : digits) {
                    int na = Math.max(0, a - A[dig]), nb = Math.max(0, b - B[dig]);
                    if (na == a && nb == b) continue;
                    best = Math.min(best, dp[na][nb] + 1);
                }
                dp[a][b] = best;
            }
        }
    }

    String fillSuffix(int m, long e2, long e3, long e5, long e7) {
        StringBuilder sb = new StringBuilder();
        long ce2 = e2, ce3 = e3, ce5 = e5, ce7 = e7;
        for (int i = 0; i < m; i++) {
            int remaining = m - 1 - i;
            for (int d = 1; d <= 9; d++) {
                long ne2 = Math.max(0, ce2 - A[d]);
                long ne3 = Math.max(0, ce3 - B[d]);
                long ne5 = Math.max(0, ce5 - C[d]);
                long ne7 = Math.max(0, ce7 - G[d]);
                long total = dp[(int) ne2][(int) ne3] + ne5 + ne7;
                if (total <= remaining) {
                    sb.append((char) ('0' + d));
                    ce2 = ne2; ce3 = ne3; ce5 = ne5; ce7 = ne7;
                    break;
                }
            }
        }
        return sb.toString();
    }
}