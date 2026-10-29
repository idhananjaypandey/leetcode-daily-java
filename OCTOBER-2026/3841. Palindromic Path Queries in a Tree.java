class Solution {
    public List<Boolean> palindromePath(int n, int[][] edges, String s, String[] queries) {
        List<Integer>[] graph = new ArrayList[n];

        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int[] edge : edges) {
            graph[edge[0]].add(edge[1]);
            graph[edge[1]].add(edge[0]);
        }

        int[] parent = new int[n];
        int[] depth = new int[n];
        int[] size = new int[n];
        int[] heavy = new int[n];
        int[] head = new int[n];
        int[] pos = new int[n];
        Arrays.fill(heavy, -1);

        int[] order = new int[n];
        int count = 0;
        int[] stack = new int[n];
        int top = 0;

        stack[top++] = 0;
        parent[0] = -1;

        while (top > 0) {
            int u = stack[--top];
            order[count++] = u;

            for (int v : graph[u]) {
                if (v == parent[u]) {
                    continue;
                }

                parent[v] = u;
                depth[v] = depth[u] + 1;
                stack[top++] = v;
            }
        }

        for (int i = n - 1; i >= 0; i--) {
            int u = order[i];
            size[u] = 1;

            for (int v : graph[u]) {
                if (parent[v] == u) {
                    size[u] += size[v];

                    if (heavy[u] == -1 || size[v] > size[heavy[u]]) {
                        heavy[u] = v;
                    }
                }
            }
        }

        int[] chainStack = new int[n];
        int chainTop = 0;
        int timer = 0;
        chainStack[chainTop++] = 0;

        while (chainTop > 0) {
            int start = chainStack[--chainTop];
            int u = start;

            while (u != -1) {
                head[u] = start;
                pos[u] = timer++;

                for (int v : graph[u]) {
                    if (parent[v] == u && v != heavy[u]) {
                        chainStack[chainTop++] = v;
                    }
                }

                u = heavy[u];
            }
        }

        int[] bit = new int[n + 1];
        char[] chars = s.toCharArray();

        for (int i = 0; i < n; i++) {
            fenwickUpdate(bit, pos[i] + 1, 1 << (chars[i] - 'a'));
        }

        List<Boolean> answer = new ArrayList<>();

        for (String query : queries) {
            String[] parts = query.split(" ");

            if (parts[0].equals("update")) {
                int u = Integer.parseInt(parts[1]);
                char c = parts[2].charAt(0);

                fenwickUpdate(bit, pos[u] + 1, 1 << (chars[u] - 'a'));
                chars[u] = c;
                fenwickUpdate(bit, pos[u] + 1, 1 << (chars[u] - 'a'));
            } else {
                int u = Integer.parseInt(parts[1]);
                int v = Integer.parseInt(parts[2]);
                int mask = 0;

                while (head[u] != head[v]) {
                    if (depth[head[u]] < depth[head[v]]) {
                        int temp = u;
                        u = v;
                        v = temp;
                    }

                    mask ^= fenwickQuery(bit, pos[u] + 1)
                          ^ fenwickQuery(bit, pos[head[u]]);

                    u = parent[head[u]];
                }

                if (depth[u] > depth[v]) {
                    int temp = u;
                    u = v;
                    v = temp;
                }

                mask ^= fenwickQuery(bit, pos[v] + 1)
                      ^ fenwickQuery(bit, pos[u]);

                answer.add((mask & (mask - 1)) == 0);
            }
        }

        return answer;
    }

    private void fenwickUpdate(int[] bit, int index, int value) {
        while (index < bit.length) {
            bit[index] ^= value;
            index += index & -index;
        }
    }

    private int fenwickQuery(int[] bit, int index) {
        int result = 0;

        while (index > 0) {
            result ^= bit[index];
            index -= index & -index;
        }

        return result;
    }
}