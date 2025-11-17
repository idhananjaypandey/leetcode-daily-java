// 1437. Check If All 1's Are at Least Length K Places Away

class Solution {
    public boolean kLengthApart(int[] nums, int k) {
        if (k == 0) {
            return true;
        }

        int n = nums.length;
        
        int curr = -1;
        for (int i = 0; i < n; i++) {
            if (nums[i] == 1) {
                curr = i;
                break;
            }
        }

        if (curr == -1) {
            return true;
        }

        for (int next = curr + 1; next < n; next++) {
            if (nums[next] == 1) {
                if (next - curr <= k) {
                    return false;
                }
                
                curr = next;
            }
        }

        return true;
    }
}