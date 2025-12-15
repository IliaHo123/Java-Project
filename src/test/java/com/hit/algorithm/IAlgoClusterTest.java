package com.hit.algorithm;

import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests for K-Means and DBSCAN clustering algorithms.
 */
public class IAlgoClusterTest {

    // ========== K-Means Tests ==========

    @Test
    public void testKMeansBasic() {
        List<Point2D> data = new ArrayList<>();

        // Group 1 - around (0,0)
        data.add(new Point2D(0, 0));
        data.add(new Point2D(1, 1));
        data.add(new Point2D(0, 1));

        // Group 2 - around (10,10)
        data.add(new Point2D(10, 10));
        data.add(new Point2D(11, 11));
        data.add(new Point2D(10, 11));

        // Group 3 - around (100,100)
        data.add(new Point2D(100, 100));
        data.add(new Point2D(101, 101));
        data.add(new Point2D(100, 101));

        IAlgoCluster<Point2D> algo = new KMeansAlgoImpl<>(42);
        List<List<Point2D>> clusters = algo.cluster(data, 3);

        Assert.assertEquals("Should return 3 clusters", 3, clusters.size());

        // Check all points are assigned
        int total = 0;
        for (List<Point2D> c : clusters) {
            total += c.size();
        }
        Assert.assertEquals("All 9 points should be in clusters", 9, total);

        System.out.println("K-Means basic test passed!");
    }

    @Test
    public void testKMeansEdgeCases() {
        IAlgoCluster<Point2D> algo = new KMeansAlgoImpl<>();

        // Empty list
        List<List<Point2D>> result1 = algo.cluster(new ArrayList<>(), 3);
        Assert.assertTrue("Empty input should return empty", result1.isEmpty());

        // Null input
        List<List<Point2D>> result2 = algo.cluster(null, 3);
        Assert.assertTrue("Null input should return empty", result2.isEmpty());

        // k = 0
        List<Point2D> data = new ArrayList<>();
        data.add(new Point2D(1, 1));
        List<List<Point2D>> result3 = algo.cluster(data, 0);
        Assert.assertTrue("k=0 should return empty", result3.isEmpty());

        System.out.println("K-Means edge cases test passed!");
    }

    // ========== DBSCAN Tests ==========

    @Test
    public void testDBSCANBasic() {
        List<Point2D> data = new ArrayList<>();

        // Dense area - should be one cluster
        data.add(new Point2D(1, 1));
        data.add(new Point2D(1, 2));
        data.add(new Point2D(2, 1));
        data.add(new Point2D(2, 2));

        // Far away point - should be noise
        data.add(new Point2D(50, 50));

        IAlgoCluster<Point2D> algo = new DBSCANAlgoImpl<>(1.5, 2);
        List<List<Point2D>> clusters = algo.cluster(data, 0);

        Assert.assertEquals("Should find 1 cluster", 1, clusters.size());
        Assert.assertEquals("Cluster should have 4 points", 4, clusters.get(0).size());

        System.out.println("DBSCAN basic test passed!");
    }

    @Test
    public void testDBSCANTwoClusters() {
        List<Point2D> data = new ArrayList<>();

        // Cluster 1
        data.add(new Point2D(0, 0));
        data.add(new Point2D(0, 1));
        data.add(new Point2D(1, 0));
        data.add(new Point2D(1, 1));

        // Cluster 2 - far from cluster 1
        data.add(new Point2D(10, 10));
        data.add(new Point2D(10, 11));
        data.add(new Point2D(11, 10));
        data.add(new Point2D(11, 11));

        IAlgoCluster<Point2D> algo = new DBSCANAlgoImpl<>(1.5, 2);
        List<List<Point2D>> clusters = algo.cluster(data, 0);

        Assert.assertEquals("Should find 2 clusters", 2, clusters.size());

        System.out.println("DBSCAN two clusters test passed!");
    }

    @Test
    public void testDBSCANEdgeCases() {
        IAlgoCluster<Point2D> algo = new DBSCANAlgoImpl<>(1.5, 2);

        // Empty list
        List<List<Point2D>> result1 = algo.cluster(new ArrayList<>(), 0);
        Assert.assertTrue("Empty input should return empty", result1.isEmpty());

        // Null input
        List<List<Point2D>> result2 = algo.cluster(null, 0);
        Assert.assertTrue("Null input should return empty", result2.isEmpty());

        System.out.println("DBSCAN edge cases test passed!");
    }

    // ========== Constructor validation tests ==========

    @Test(expected = IllegalArgumentException.class)
    public void testDBSCANBadEps() {
        new DBSCANAlgoImpl<>(-1.0, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDBSCANBadMinPts() {
        new DBSCANAlgoImpl<>(1.0, 0);
    }
}
