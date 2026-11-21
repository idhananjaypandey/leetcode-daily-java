class Solution {
  public int[] findSubtreeSizes(int[] parent, String s) {
    final int n = parent.length;
    List<Integer>[] origTree = new ArrayList[n];
    for (int i = 0; i < n; ++i) {
      origTree[i] = new ArrayList<>();
    }
    for (int i = 1; i < n; ++i) {
      origTree[parent[i]].add(i);
    }

    int[] newParent = parent.clone();
    int[] lastSeen = new int[26];
    Arrays.fill(lastSeen, -1);

    // Single DFS to find closest same-character ancestor for every node in O(n)
    assignNewParents(origTree, 0, s, lastSeen, newParent);

    List<Integer>[] newTree = new ArrayList[n];
    for (int i = 0; i < n; ++i) {
      newTree[i] = new ArrayList<>();
    }
    for (int i = 1; i < n; ++i) {
      newTree[newParent[i]].add(i);
    }

    int[] ans = new int[n];
    calcSubtreeSize(newTree, 0, ans);
    return ans;
  }

  private void assignNewParents(
      List<Integer>[] origTree, int u, String s, int[] lastSeen, int[] newParent) {
    final int c = s.charAt(u) - 'a';
    final int prev = lastSeen[c];

    if (prev != -1) {
      newParent[u] = prev;
    }

    lastSeen[c] = u;
    for (final int v : origTree[u]) {
      assignNewParents(origTree, v, s, lastSeen, newParent);
    }
    lastSeen[c] = prev; // Backtrack so sibling subtrees aren't affected
  }

  private int calcSubtreeSize(List<Integer>[] tree, int u, int[] ans) {
    int sz = 1;
    for (final int v : tree[u]) {
      sz += calcSubtreeSize(tree, v, ans);
    }
    return ans[u] = sz;
  }
}