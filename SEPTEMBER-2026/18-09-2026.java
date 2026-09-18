// 1520. Maximum Number of Non-Overlapping Substrings

class Solution {
    public List<String> maxNumOfSubstrings(String s) {
        int n = s.length();
        int[] first = new int[26];
        int[] last = new int[26];
        Arrays.fill(first, -1);
        Arrays.fill(last, -1);

        for (int i = 0; i < n; i++) {
            int ch = s.charAt(i) - 'a';
            if (first[ch] == -1) {
                first[ch] = i;
            }
            last[ch] = i;
        }

        List<String> result = new ArrayList<>();
        int lastEnd = -1;

        for (int i = 0; i < n; i++) {
            int ch = s.charAt(i) - 'a';
            if (i == first[ch]) {
                int newEnd = getValidEnd(s, i, first, last);
                if (newEnd != -1) {
                    if (i > lastEnd) {
                        result.add("");
                    }
                    lastEnd = newEnd;
                    result.set(result.size() - 1, s.substring(i, lastEnd + 1));
                }
            }
        }

        return result;
    }

    private int getValidEnd(String s, int start, int[] first, int[] last) {
        int end = last[s.charAt(start) - 'a'];
        for (int i = start; i <= end; i++) {
            int ch = s.charAt(i) - 'a';
            if (first[ch] < start) {
                return -1;
            }
            end = Math.max(end, last[ch]);
        }
        return end;
    }
}