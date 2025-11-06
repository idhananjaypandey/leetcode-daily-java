// 3607. Power Grid Maintenance

import java.util.*;

class Solution {

    // --- DSU Implementation Fields ---
    private int[] parent;
    private int[] size; // Used for Union by Size heuristic
    
    // Store the set of ONLINE station IDs for the root of each grid.
    // The TreeSet automatically keeps the IDs sorted and allows for O(log N) insertion/deletion.
    // Only the root of a set will have a non-null TreeSet.
    private TreeSet<Integer>[] onlineStations;
    
    // Tracks the operational status of each station (for Type 1 checks)
    private boolean[] isOnline; 

    // --- DSU Core Methods ---

    /**
     * Finds the representative (root) of the set containing station i with path compression.
     * @param i The station ID.
     * @return The root ID.
     */
    private int find(int i) {
        if (parent[i] == i) {
            return i;
        }
        // Path compression: set the parent to the root directly
        return parent[i] = find(parent[i]);
    }

    /**
     * Merges the sets containing stations u and v using the Union by Size heuristic.
     * The TreeSets of the online station IDs are also merged from the smaller set to the larger one.
     * @param u Station u ID.
     * @param v Station v ID.
     */
    private void union(int u, int v) {
        int rootU = find(u);
        int rootV = find(v);

        if (rootU != rootV) {
            // Union by Size: attach the root of the smaller set to the root of the larger set
            if (size[rootU] < size[rootV]) {
                // Swap to ensure rootU is the larger set
                int temp = rootU;
                rootU = rootV;
                rootV = temp;
            }

            // DSU merge
            parent[rootV] = rootU;
            size[rootU] += size[rootV];

            // Merge the TreeSets: move all elements from the smaller set (rootV) to the larger set (rootU)
            // This ensures the set at the new root (rootU) contains all online stations from both merged grids.
            onlineStations[rootU].addAll(onlineStations[rootV]);
            
            // Clear the set of the absorbed root (rootV)
            onlineStations[rootV] = null; 
        }
    }
    
    // --- Main Solution Method ---

    /**
     * Solves the Power Grid Maintenance problem.
     * @param c The number of power stations.
     * @param connections The list of initial cable connections.
     * @param queries The list of maintenance and offline queries.
     * @return An array of results for Type 1 queries.
     */
    public int[] processQueries(int c, int[][] connections, int[][] queries) {
        // Initialize DSU and auxiliary structures (1-based indexing: size c + 1)
        parent = new int[c + 1];
        size = new int[c + 1];
        
        // Suppress the unchecked warning for the array of generic types
        @SuppressWarnings("unchecked")
        TreeSet<Integer>[] tempSetArray = new TreeSet[c + 1];
        onlineStations = tempSetArray;
        isOnline = new boolean[c + 1];
        
        // --- Initialization: O(c) ---
        for (int i = 1; i <= c; i++) {
            parent[i] = i;
            size[i] = 1;
            // Initially, every station is online and forms its own set with its ID
            onlineStations[i] = new TreeSet<>();
            onlineStations[i].add(i);
            isOnline[i] = true; 
        }

        // --- Build Initial Power Grids: O(connections.length * alpha(c) * log c) ---
        for (int[] connection : connections) {
            union(connection[0], connection[1]);
        }
        
        // 

        // --- Process Queries ---
        List<Integer> results = new ArrayList<>();

        for (int[] query : queries) {
            int type = query[0];
            int x = query[1];

            if (type == 1) { // Type 1: Maintenance Check [1, x]
                int result;
                if (isOnline[x]) {
                    // Case 1: Station x is online, it resolves the check itself.
                    result = x;
                } else {
                    // Case 2: Station x is offline. Find the smallest operational station in its grid.
                    int root = find(x);
                    TreeSet<Integer> onlineSet = onlineStations[root];
                    
                    if (onlineSet == null || onlineSet.isEmpty()) {
                        // No operational station exists in the grid.
                        result = -1;
                    } else {
                        // The smallest operational station is the first element in the sorted TreeSet.
                        result = onlineSet.first();
                    }
                }
                results.add(result);

            } else { // Type 2: Station goes Offline [2, x]
                if (isOnline[x]) {
                    // Only process if it was previously online
                    isOnline[x] = false;
                    int root = find(x);
                    
                    // Remove station x from the set of online stations for its grid root
                    TreeSet<Integer> onlineSet = onlineStations[root];
                    if (onlineSet != null) {
                        onlineSet.remove(x); // O(log c) operation
                    }
                }
            }
        }

        // Convert the List<Integer> to int[]
        return results.stream().mapToInt(i -> i).toArray();
    }
}