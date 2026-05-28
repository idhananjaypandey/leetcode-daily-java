// 3093. Longest Common Suffix Queries

class TrieNode {
    public TrieNode[] children = new TrieNode[26];
    public boolean isWord = false;
    public int length = Integer.MAX_VALUE;
    public int index = -1;
}

class Solution {
    private TrieNode root = new TrieNode();

    public int[] stringIndices(String[] wordsContainer, String[] wordsQuery) {
        int[] ans = new int[wordsQuery.length];
        int minIndex = 0;

        for (int i = 0; i < wordsContainer.length; ++i) {
            insert(wordsContainer[i], i);
            if (wordsContainer[i].length() < wordsContainer[minIndex].length()) {
                minIndex = i;
            }
        }

        for (int i = 0; i < wordsQuery.length; ++i) {
            final int foundIndex = search(wordsQuery[i]);
            ans[i] = foundIndex == -1 ? minIndex : foundIndex;
        }

        return ans;
    }

    private void insert(final String word, int wordIdx) {
        TrieNode node = root;
        
        if (node.length > word.length()) {
            node.length = word.length();
            node.index = wordIdx;
        }

        for (int i = word.length() - 1; i >= 0; --i) {
            final int charIndex = word.charAt(i) - 'a';
            
            if (node.children[charIndex] == null) {
                node.children[charIndex] = new TrieNode();
            }
            node = node.children[charIndex];
            
            if (node.length > word.length()) {
                node.length = word.length();
                node.index = wordIdx;
            }
        }
    }

    private int search(final String word) {
        TrieNode node = root;
        
        for (int i = word.length() - 1; i >= 0; --i) {
            final int charIndex = word.charAt(i) - 'a';
            
            if (node.children[charIndex] == null) {
                return node.index;
            }
            node = node.children[charIndex];
        }
        
        return node.index;
    }
}