// 2913. Subarrays Distinct Element Sum of Squares I

class Solution {
    public int sumCounts(List<Integer> nums) {
        
        int totalSum = 0;
        int n = nums.size();
        
        // Generate all possible subarrays
        for (int i = 0; i < n; i++) {
            Set<Integer> distinctElements = new HashSet<>();
            for (int j = i; j < n; j++) {
                distinctElements.add(nums.get(j));
                int distinctCount = distinctElements.size();
                totalSum += distinctCount * distinctCount;
            }
        }
        
        return totalSum;
    }
}