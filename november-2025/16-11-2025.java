// 1513. Number of Substrings With Only 1s

class Solution {
    public int numSub(String s) {
        final int kMod = 1_000_000_007;

        int ans = 0;
        int l = -1; 

        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == '0') {
                l = i; 
            }
            
            int currentLength = i - l;
            
            ans = (ans + currentLength) % kMod;
        }

        return ans;
    }
}