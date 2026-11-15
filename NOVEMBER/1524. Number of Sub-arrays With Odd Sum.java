class Solution {
    public int numOfSubarrays(int[] arr) {
        int kMod = 1_000_000_007;
        int n = arr.length;
        long ans = 0;
        int[] dp0 = new int[n + 1];
        int[] dp1 = new int[n + 1];
        for (int i = 1; i <= n; ++i) {
            if (arr[i - 1] % 2 == 1) {
                dp0[i] = dp1[i - 1];
                dp1[i] = dp0[i - 1] + 1;
            } else {
                dp0[i] = dp0[i - 1] + 1;
                dp1[i] = dp1[i - 1];
            }
            ans = (ans + dp1[i]) % kMod;
        }
        return (int) ans;
    }
}