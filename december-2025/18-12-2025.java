// 3652. Best Time to Buy and Sell Stock using Strategy

class Solution {
    public long maxProfit(int[] prices, int[] strategy, int k) {
        int n = prices.length;
        if (n == 0) return 0;

        // Using separate arrays instead of vector<pair> for better performance in Java
        long[] psFirst = new long[n];  // Prefix sum of current profits (prices[i] * strategy[i])
        long[] psSecond = new long[n]; // Prefix sum of raw prices
        long[] ssFirst = new long[n];  // Suffix sum of current profits
        long[] ssSecond = new long[n]; // Suffix sum of raw prices

        // Calculate Prefix Sums
        long currentTotalProfit = 0;
        long currentPriceSum = 0;
        for (int i = 0; i < n; i++) {
            currentTotalProfit += (long) prices[i] * strategy[i];
            currentPriceSum += prices[i];
            psFirst[i] = currentTotalProfit;
            psSecond[i] = currentPriceSum;
        }

        // Calculate Suffix Sums
        ssFirst[n - 1] = (long) prices[n - 1] * strategy[n - 1];
        ssSecond[n - 1] = prices[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            ssFirst[i] = (long) prices[i] * strategy[i] + ssFirst[i + 1];
            ssSecond[i] = (long) prices[i] + ssSecond[i + 1];
        }

        // Initialize res with the original profit (no changes)
        long res = psFirst[n - 1];

        // Sliding Window logic
        for (int i = 0; i <= n - k; i++) {
            // Profit before the window
            long preRes = (i - 1 >= 0) ? psFirst[i - 1] : 0;
            
            // Profit after the window
            long suffRes = (i + k < n) ? ssFirst[i + k] : 0;
            
            // Sum of prices within the window [i, i + k - 1]
            // Note: Correcting the window sum logic from your snippet
            long windowPriceSum = psSecond[i + k - 1] - (i - 1 >= 0 ? psSecond[i - 1] : 0);
            
            res = Math.max(res, preRes + suffRes + windowPriceSum);
        }

        return res;
    }
}