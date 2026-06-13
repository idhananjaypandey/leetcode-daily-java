// 3838. Weighted Word Mapping

class Solution {
    public String mapWordWeights(String[] words, int[] weights) {
        int n = words.length;
        char[] result = new char[n];
        for (int i = 0; i < n; i++) {
            int sum = 0;
            for (char c : words[i].toCharArray()) {
                sum += weights[c - 'a'];
            }
            int rem = sum % 26;
            result[i] = (char) ('z' - rem);
        }
        return new String(result);
    }
}