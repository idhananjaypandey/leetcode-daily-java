// 3625. Count Number of Trapezoids II

import java.util.HashMap;

class Solution {
    public int countTrapezoids(int[][] points) {
        HashMap<Integer, HashMap<Integer, Integer>> slopes = new HashMap<>();
        HashMap<Integer, HashMap<Integer, Integer>> lines  = new HashMap<>();
        int n = points.length;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int dx = points[j][0] - points[i][0];
                int dy = points[j][1] - points[i][1];

                if (dx < 0 || (dx == 0 && dy < 0)) {
                    dx = -dx;
                    dy = -dy;
                }

                int g  = gcd(dx, Math.abs(dy));
                int sx = dx / g;
                int sy = dy / g;

                int des  = sx * points[i][1] - sy * points[i][0];
                int key1 = (sx << 12) | (sy + 2000);
                int key2 = (dx << 12) | (dy + 2000);

                slopes.computeIfAbsent(key1, k -> new HashMap<>())
                      .merge(des, 1, Integer::sum);
                lines.computeIfAbsent(key2, k -> new HashMap<>())
                     .merge(des, 1, Integer::sum);
            }
        }

        return count(slopes) - count(lines) / 2;
    }

    private int count(HashMap<Integer, HashMap<Integer, Integer>> map) {
        long ans = 0;

        for (HashMap<Integer, Integer> inner : map.values()) {
            long sum = 0;
            for (int val : inner.values()) {
                sum += val;
            }
            for (int val : inner.values()) {
                sum -= val;
                ans += (long) val * sum;
            }
        }

        return (int) ans;
    }

    private int gcd(int a, int b) {
        while (b != 0) {
            int t = a % b;
            a = b;
            b = t;
        }
        return Math.abs(a);
    }
}
