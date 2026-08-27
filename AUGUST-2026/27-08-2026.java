// 3720. Lexicographically Smallest Permutation Greater Than Target

class Solution {
    public String lexGreaterPermutation(String s, String target) {
        int n = s.length();
        int[] count = new int[26];
        for (char c : s.toCharArray()) {
            count[c - 'a']++;
        }

        // Try to find the position i where we diverge from target with a strictly larger character
        for (int i = n - 1; i >= 0; i--) {
            // Check if target[0...i-1] can be matched using s's characters
            int[] prefixCount = new int[26];
            boolean canMatchPrefix = true;
            
            for (int k = 0; k < i; k++) {
                int idx = target.charAt(k) - 'a';
                prefixCount[idx]++;
                if (prefixCount[idx] > count[idx]) {
                    canMatchPrefix = false;
                    break;
                }
            }

            if (!canMatchPrefix) {
                continue;
            }

            // Try to place a character at position i strictly greater than target[i]
            int targetChar = target.charAt(i) - 'a';
            for (int c = targetChar + 1; c < 26; c++) {
                if (prefixCount[c] < count[c]) {
                    // Valid divergence found! Reconstruct the string.
                    StringBuilder sb = new StringBuilder();
                    
                    // 1. Add matching prefix target[0...i-1]
                    sb.append(target.substring(0, i));
                    
                    // 2. Add the strictly greater character at position i
                    sb.append((char) ('a' + c));
                    prefixCount[c]++;
                    
                    // 3. Add remaining characters in lexicographically smallest (sorted) order
                    for (int charIdx = 0; charIdx < 26; charIdx++) {
                        int remaining = count[charIdx] - prefixCount[charIdx];
                        while (remaining > 0) {
                            sb.append((char) ('a' + charIdx));
                            remaining--;
                        }
                    }
                    
                    return sb.toString();
                }
            }
        }

        return "";
    }
}