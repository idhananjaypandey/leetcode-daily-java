// 3583. Count Special Triplets

class Solution {
    public int specialTriplets(int[] nums) {
        int MOD = (int)1e9 + 7;
        int n = nums.length;

        int freq_array_size = 2 * 100000 + 1;
        
        int[] left_counts = new int[freq_array_size];
        int[] right_counts = new int[freq_array_size];
        
        for (int num : nums) {
            right_counts[num] += 1;
        }
            
        int total_triplets = 0;
        
        for (int val_j : nums) {
        
            right_counts[val_j] -= 1;
            
            int target_val = 2 * val_j;
            
            int count_i = left_counts[target_val];
            int count_k = right_counts[target_val];
                
            long term = (long)count_i * count_k;
            total_triplets = (int)(((long)total_triplets + term) % MOD);
            
            left_counts[val_j] += 1;
        }
            
        return total_triplets;
    }
}