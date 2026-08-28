// 3734. Lexicographically Smallest Palindromic Permutation Greater Than Target

class Solution {
    public String lexPalindromicPermutation(String s, String target) {
        int n = s.length();
        int[] count = new int[26];
        for (char c : s.toCharArray()) {
            count[c - 'a']++;
        }

        int oddCount = 0;
        char midChar = 0;
        int[] halfCount = new int[26];

        for (int i = 0; i < 26; i++) {
            if (count[i] % 2 != 0) {
                oddCount++;
                midChar = (char) ('a' + i);
            }
            halfCount[i] = count[i] / 2;
        }

        if (oddCount > 1) {
            return "";
        }

        int halfLen = n / 2;

        // Try to find the longest common prefix of length L with target's first half
        for (int L = halfLen; L >= 0; L--) {
            // Check if target[0...L-1] can be formed using available halfCount
            int[] currentHalf = halfCount.clone();
            boolean possiblePrefix = true;
            for (int i = 0; i < L; i++) {
                char tChar = target.charAt(i);
                if (currentHalf[tChar - 'a'] > 0) {
                    currentHalf[tChar - 'a']--;
                } else {
                    possiblePrefix = false;
                    break;
                }
            }

            if (!possiblePrefix) continue;

            // Case 1: L == halfLen (Exact match on first half)
            if (L == halfLen) {
                StringBuilder prefix = new StringBuilder(target.substring(0, halfLen));
                StringBuilder full = new StringBuilder(prefix);
                if (n % 2 != 0) {
                    full.append(midChar);
                }
                full.append(new StringBuilder(prefix).reverse());

                if (full.toString().compareTo(target) > 0) {
                    return full.toString();
                }

                // If odd length, try increasing midChar if possible
                if (n % 2 != 0) {
                    for (char c = (char) (target.charAt(halfLen) + 1); c <= 'z'; c++) {
                        if (c == midChar) { // midChar is fixed by frequency
                            StringBuilder candidate = new StringBuilder(prefix);
                            candidate.append(midChar);
                            candidate.append(new StringBuilder(prefix).reverse());
                            if (candidate.toString().compareTo(target) > 0) {
                                return candidate.toString();
                            }
                        }
                    }
                }
                continue;
            }

            // Case 2: Diverge at position L (pick character larger than target[L])
            char targetChar = target.charAt(L);
            for (int c = targetChar - 'a' + 1; c < 26; c++) {
                if (currentHalf[c] > 0) {
                    // Make choice
                    currentHalf[c]--;
                    
                    StringBuilder firstHalf = new StringBuilder(target.substring(0, L));
                    firstHalf.append((char) ('a' + c));

                    // Fill remaining half lexicographically smallest
                    for (int ch = 0; ch < 26; ch++) {
                        while (currentHalf[ch] > 0) {
                            firstHalf.append((char) ('a' + ch));
                            currentHalf[ch]--;
                        }
                    }

                    // Construct full palindrome
                    StringBuilder result = new StringBuilder(firstHalf);
                    if (n % 2 != 0) {
                        result.append(midChar);
                    }
                    result.append(new StringBuilder(firstHalf).reverse());

                    return result.toString();
                }
            }
        }

        return "";
    }
}