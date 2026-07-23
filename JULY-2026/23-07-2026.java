// 3513. Number of Unique XOR Triplets I

class Solution {
    public int uniqueXorTriplets(int[] nums) {
        int n = nums.length;
        
        if (n < 3) {
            return n;
        }
        
        int msb = 0;
        while ((1 << (msb + 1)) <= n) {
            msb++;
        }
        
        return (1 << (msb + 1));
    }
}
