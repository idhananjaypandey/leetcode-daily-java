// 3619. Count Islands With Total Value Divisible by K

class Solution {
    public int countIslands(int[][] grid, int k) {
        int m = grid.length;
        int n = grid[0].length;
        boolean[][] visited = new boolean[m][n];
        int count = 0;
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] > 0 && !visited[i][j]) {
                    long totalValue = bfs(grid, visited, i, j);
                    if (totalValue % k == 0) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
    
    private long bfs(int[][] grid, boolean[][] visited, int startRow, int startCol) {
        int m = grid.length;
        int n = grid[0].length;
        long sum = 0;
        
        int[] qRows = new int[m * n];
        int[] qCols = new int[m * n];
        int head = 0, tail = 0;
        
        qRows[tail] = startRow;
        qCols[tail] = startCol;
        tail++;
        visited[startRow][startCol] = true;
        
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};
        
        while (head < tail) {
            int r = qRows[head];
            int c = qCols[head];
            head++;
            
            sum += grid[r][c];
            
            for (int i = 0; i < 4; i++) {
                int nr = r + dr[i];
                int nc = c + dc[i];
                
                if (nr >= 0 && nr < m && nc >= 0 && nc < n && grid[nr][nc] > 0 && !visited[nr][nc]) {
                    visited[nr][nc] = true;
                    qRows[tail] = nr;
                    qCols[tail] = nc;
                    tail++;
                }
            }
        }
        
        return sum;
    }
}