import java.util.*;

class Solution {

    private int[] parent;
    private int[] size;
    
    private TreeSet<Integer>[] onlineStations;
    
    private boolean[] isOnline; 

    private int find(int i) {
        if (parent[i] == i) {
            return i;
        }
        return parent[i] = find(parent[i]);
    }

    private void union(int u, int v) {
        int rootU = find(u);
        int rootV = find(v);

        if (rootU != rootV) {
            if (size[rootU] < size[rootV]) {
                int temp = rootU;
                rootU = rootV;
                rootV = temp;
            }

            parent[rootV] = rootU;
            size[rootU] += size[rootV];

            onlineStations[rootU].addAll(onlineStations[rootV]);
            
            onlineStations[rootV] = null; 
        }
    }
    
    public int[] processQueries(int c, int[][] connections, int[][] queries) {
        parent = new int[c + 1];
        size = new int[c + 1];
        
        @SuppressWarnings("unchecked")
        TreeSet<Integer>[] tempSetArray = new TreeSet[c + 1];
        onlineStations = tempSetArray;
        isOnline = new boolean[c + 1];
        
        for (int i = 1; i <= c; i++) {
            parent[i] = i;
            size[i] = 1;
            onlineStations[i] = new TreeSet<>();
            onlineStations[i].add(i);
            isOnline[i] = true; 
        }

        for (int[] connection : connections) {
            union(connection[0], connection[1]);
        }
        
        List<Integer> results = new ArrayList<>();

        for (int[] query : queries) {
            int type = query[0];
            int x = query[1];

            if (type == 1) {
                int result;
                if (isOnline[x]) {
                    result = x;
                } else {
                    int root = find(x);
                    TreeSet<Integer> onlineSet = onlineStations[root];
                    
                    if (onlineSet == null || onlineSet.isEmpty()) {
                        result = -1;
                    } else {
                        result = onlineSet.first();
                    }
                }
                results.add(result);

            } else {
                if (isOnline[x]) {
                    isOnline[x] = false;
                    int root = find(x);
                    
                    TreeSet<Integer> onlineSet = onlineStations[root];
                    if (onlineSet != null) {
                        onlineSet.remove(x);
                    }
                }
            }
        }

        return results.stream().mapToInt(i -> i).toArray();
    }
}