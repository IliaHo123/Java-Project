package com.hit.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for clustering algorithms.
 * Contains helper methods that both K-Means and DBSCAN use.
 *
 * @param <T> The type of elements to be clustered
 */
public abstract class AbstractAlgoCluster<T extends IClusterable<T>> implements IAlgoCluster<T> {

    /**
     * Checks if input data is valid.
     */
    protected boolean isValidData(List<T> data) {
        return data != null && !data.isEmpty();
    }

    /**
     * Checks if k is valid (must be positive).
     */
    protected boolean isValidK(int k) {
        return k > 0;
    }

    /**
     * Creates k empty lists to hold the clusters.
     */
    protected List<List<T>> createEmptyClusters(int k) {
        List<List<T>> clusters = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            clusters.add(new ArrayList<>());
        }
        return clusters;
    }

    /**
     * Calculates sum of distances from one point to all points in a list.
     */
    protected double calculateTotalDistance(T point, List<T> points) {
        double total = 0;
        for (T other : points) {
            total += point.distanceTo(other);
        }
        return total;
    }

    /**
     * Finds the best center point in a cluster.
     * Returns the point that has minimum total distance to all other points.
     */
    protected T findCenterPoint(List<T> cluster) {
        if (cluster == null || cluster.isEmpty()) {
            return null;
        }

        T bestPoint = cluster.get(0);
        double minDistance = Double.MAX_VALUE;

        // Try each point as potential center
        for (T candidate : cluster) {
            double totalDist = calculateTotalDistance(candidate, cluster);
            if (totalDist < minDistance) {
                minDistance = totalDist;
                bestPoint = candidate;
            }
        }

        return bestPoint;
    }
}
