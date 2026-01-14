// 3454. Separate Squares II

import java.util.*;

class SegmentTree {
    private final int n; // the number of segments (xs.length - 1)
    private final int[] xs;
    private final int[] coveredCount;
    private final int[] coveredWidth;

    public SegmentTree(int[] xs) {
        this.xs = xs;
        this.n = xs.length - 1;
        this.coveredCount = new int[4 * n];
        this.coveredWidth = new int[4 * n];
    }

    // Adds val to the range [i, j].
    public void add(int i, int j, int val) {
        update(0, 0, n - 1, i, j, val);
    }

    public int getCoveredWidth() {
        return coveredWidth[0];
    }

    private void update(int treeIndex, int lo, int hi, int i, int j, int val) {
        // Range check: if the segment [xs[lo], xs[hi+1]] is outside [i, j]
        if (j <= xs[lo] || xs[hi + 1] <= i) {
            return;
        }

        // If the segment is fully contained within [i, j]
        if (i <= xs[lo] && xs[hi + 1] <= j) {
            coveredCount[treeIndex] += val;
        } else {
            int mid = (lo + hi) / 2;
            update(2 * treeIndex + 1, lo, mid, i, j, val);
            update(2 * treeIndex + 2, mid + 1, hi, i, j, val);
        }

        // Update the coveredWidth for this node
        if (coveredCount[treeIndex] > 0) {
            coveredWidth[treeIndex] = xs[hi + 1] - xs[lo];
        } else if (lo == hi) {
            coveredWidth[treeIndex] = 0;
        } else {
            coveredWidth[treeIndex] = coveredWidth[2 * treeIndex + 1] + coveredWidth[2 * treeIndex + 2];
        }
    }
}

class Event implements Comparable<Event> {
    int y, delta, xl, xr;

    public Event(int y, int delta, int xl, int xr) {
        this.y = y;
        this.delta = delta;
        this.xl = xl;
        this.xr = xr;
    }

    @Override
    public int compareTo(Event other) {
        if (this.y != other.y) {
            return Integer.compare(this.y, other.y);
        }
        return Integer.compare(this.delta, other.delta);
    }
}

public class Solution {
    public double separateSquares(int[][] squares) {
        List<Event> events = new ArrayList<>();
        TreeSet<Integer> xCoords = new TreeSet<>();

        for (int[] square : squares) {
            int x = square[0];
            int y = square[1];
            int l = square[2];
            events.add(new Event(y, 1, x, x + l));
            events.add(new Event(y + l, -1, x, x + l));
            xCoords.add(x);
            xCoords.add(x + l);
        }

        Collections.sort(events);

        // Convert TreeSet to array for the SegmentTree
        int[] xs = new int[xCoords.size()];
        int idx = 0;
        for (int x : xCoords) {
            xs[idx++] = x;
        }

        double totalArea = getArea(events, xs);
        double halfArea = totalArea / 2.0;
        
        double currentArea = 0;
        int prevY = events.get(0).y;
        SegmentTree tree = new SegmentTree(xs);

        for (Event event : events) {
            int coveredWidth = tree.getCoveredWidth();
            long areaGain = (long) coveredWidth * (event.y - prevY);
            
            if (currentArea + areaGain >= halfArea) {
                return prevY + (halfArea - currentArea) / coveredWidth;
            }
            
            currentArea += areaGain;
            tree.add(event.xl, event.xr, event.delta);
            prevY = event.y;
        }

        return prevY; 
    }

    private double getArea(List<Event> events, int[] xs) {
        double totalArea = 0;
        int prevY = events.get(0).y;
        SegmentTree tree = new SegmentTree(xs);
        
        for (Event event : events) {
            totalArea += (long) tree.getCoveredWidth() * (event.y - prevY);
            tree.add(event.xl, event.xr, event.delta);
            prevY = event.y;
        }
        return totalArea;
    }
}