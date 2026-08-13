// 2213. Longest Substring of One Repeating Character

class Solution {

    static class SegmentTreeNode {

        int lo;
        int hi;

        char maxLetter;
        char prefixLetter;
        char suffixLetter;

        int maxLength;
        int prefixLength;
        int suffixLength;

        SegmentTreeNode left;
        SegmentTreeNode right;

        SegmentTreeNode(
                int lo,
                int hi,
                char maxLetter,
                char prefixLetter,
                char suffixLetter,
                int maxLength,
                int prefixLength,
                int suffixLength
        ) {
            this.lo = lo;
            this.hi = hi;
            this.maxLetter = maxLetter;
            this.prefixLetter = prefixLetter;
            this.suffixLetter = suffixLetter;
            this.maxLength = maxLength;
            this.prefixLength = prefixLength;
            this.suffixLength = suffixLength;
        }
    }


    SegmentTreeNode root;


    public int[] longestRepeating(
            String s,
            String queryCharacters,
            int[] queryIndices
    ) {

        root = build(s, 0, s.length() - 1);

        int[] answer = new int[queryIndices.length];

        for (int i = 0; i < queryIndices.length; i++) {

            update(root, queryIndices[i], queryCharacters.charAt(i));

            answer[i] = root.maxLength;
        }

        return answer;
    }


    // Build Segment Tree
    private SegmentTreeNode build(String s, int lo, int hi) {

        // Leaf node
        if (lo == hi) {

            char c = s.charAt(lo);

            return new SegmentTreeNode(
                    lo,
                    hi,
                    c,
                    c,
                    c,
                    1,
                    1,
                    1
            );
        }

        int mid = (lo + hi) / 2;

        SegmentTreeNode node = new SegmentTreeNode(
                lo,
                hi,
                ' ',
                ' ',
                ' ',
                0,
                0,
                0
        );

        node.left = build(s, lo, mid);
        node.right = build(s, mid + 1, hi);

        merge(node);

        return node;
    }


    // Update character at index
    private void update(SegmentTreeNode node, int index, char value) {

        // Leaf node
        if (node.lo == node.hi) {

            node.maxLetter = value;
            node.prefixLetter = value;
            node.suffixLetter = value;

            return;
        }

        int mid = (node.lo + node.hi) / 2;

        if (index <= mid) {
            update(node.left, index, value);
        } else {
            update(node.right, index, value);
        }

        // Recalculate current node
        merge(node);
    }


    // Merge left and right child
    private void merge(SegmentTreeNode node) {

        SegmentTreeNode left = node.left;
        SegmentTreeNode right = node.right;


        // ---------------------------
        // Calculate maxLength
        // ---------------------------

        if (left.maxLength >= right.maxLength) {

            node.maxLength = left.maxLength;
            node.maxLetter = left.maxLetter;

        } else {

            node.maxLength = right.maxLength;
            node.maxLetter = right.maxLetter;
        }


        // Check repeating sequence crossing middle
        if (left.suffixLetter == right.prefixLetter) {

            int combinedLength =
                    left.suffixLength + right.prefixLength;

            if (combinedLength > node.maxLength) {

                node.maxLength = combinedLength;
                node.maxLetter = left.suffixLetter;
            }
        }


        // ---------------------------
        // Calculate prefix
        // ---------------------------

        node.prefixLetter = left.prefixLetter;
        node.prefixLength = left.prefixLength;

        // If complete left segment has same character
        // as prefix of right segment
        if (left.prefixLength == left.hi - left.lo + 1
                && left.prefixLetter == right.prefixLetter) {

            node.prefixLength += right.prefixLength;
        }


        // ---------------------------
        // Calculate suffix
        // ---------------------------

        node.suffixLetter = right.suffixLetter;
        node.suffixLength = right.suffixLength;

        // If complete right segment has same character
        // as suffix of left segment
        if (right.suffixLength == right.hi - right.lo + 1
                && right.suffixLetter == left.suffixLetter) {

            node.suffixLength += left.suffixLength;
        }
    }
}