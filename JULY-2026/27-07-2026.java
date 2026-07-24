// 3514. Number of Unique XOR Triplets II

class Solution {
    public int uniqueXorTriplets(int[] nums) {
        Set<Integer> uniqueSet = new HashSet<>();
        for (int num : nums) {
            uniqueSet.add(num);
        }
        
        List<Integer> u = new ArrayList<>(uniqueSet);
        int m = u.size();

        boolean[] pairXor = new boolean[2048];
        for (int i = 0; i < m; i++) {
            for (int j = i; j < m; j++) {
                pairXor[u.get(i) ^ u.get(j)] = true;
            }
        }

        List<Integer> pairs = new ArrayList<>();
        for (int val = 0; val < 2048; val++) {
            if (pairXor[val]) {
                pairs.add(val);
            }
        }

        boolean[] tripletXor = new boolean[2048];
        for (int p : pairs) {
            for (int val : u) {
                tripletXor[p ^ val] = true;
            }
        }

        int count = 0;
        for (boolean present : tripletXor) {
            if (present) count++;
        }

        return count;
    }
}