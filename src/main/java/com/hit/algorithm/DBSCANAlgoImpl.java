package com.hit.algorithm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * DBSCAN clustering algorithm implementation.
 * Finds clusters based on density - doesn't need to know k in advance.
 *
 * @param <T> The type of data points
 */
public class DBSCANAlgoImpl<T extends IClusterable<T>> extends AbstractAlgoCluster<T> {

    // Maximum distance for two points to be neighbors
    private final double eps;
    
    // Minimum points needed to form a cluster
    private final int minPts;

    /**
     * Creates DBSCAN with given parameters.
     *
     * @param eps    Max distance between neighbors
     * @param minPts Min points to form a cluster
     */
    public DBSCANAlgoImpl(double eps, int minPts) {
        if (eps <= 0) {
            throw new IllegalArgumentException("eps must be positive");
        }
        if (minPts <= 0) {
            throw new IllegalArgumentException("minPts must be positive");
        }
        this.eps = eps;
        this.minPts = minPts;
    }

    /**
     * Finds clusters in the data.
     * Note: k parameter is ignored - DBSCAN finds number of clusters automatically.
     */
    @Override
    public List<List<T>> cluster(List<T> data, int k) {
        if (!isValidData(data)) {
            return new ArrayList<>();
        }

        List<List<T>> clusters = new ArrayList<>();
        Set<T> visited = new HashSet<>();
        Set<T> inCluster = new HashSet<>();

        for (T point : data) {
            // Skip if already checked
            if (visited.contains(point)) {
                continue;
            }

            visited.add(point);

            // Find neighbors
            List<T> neighbors = findNeighbors(point, data);

            // If enough neighbors, start a new cluster
            if (neighbors.size() >= minPts) {
                List<T> newCluster = new ArrayList<>();
                growCluster(point, neighbors, newCluster, visited, inCluster, data);
                clusters.add(newCluster);
            }
            // Otherwise it's noise - we skip it
        }

        return clusters;
    }

    /**
     * Expands a cluster by adding all connected dense points.
     */
    private void growCluster(T point, List<T> neighbors, List<T> cluster,
                             Set<T> visited, Set<T> inCluster, List<T> data) {

        cluster.add(point);
        inCluster.add(point);

        // Check all neighbors (list might grow during loop)
        for (int i = 0; i < neighbors.size(); i++) {
            T neighbor = neighbors.get(i);

            if (!visited.contains(neighbor)) {
                visited.add(neighbor);

                // Check this neighbor's neighbors
                List<T> neighborNeighbors = findNeighbors(neighbor, data);
                
                // If it's also a dense point, add its neighbors to check
                if (neighborNeighbors.size() >= minPts) {
                    for (T nn : neighborNeighbors) {
                        if (!neighbors.contains(nn)) {
                            neighbors.add(nn);
                        }
                    }
                }
            }

            // Add to cluster if not already in one
            if (!inCluster.contains(neighbor)) {
                cluster.add(neighbor);
                inCluster.add(neighbor);
            }
        }
    }

    /**
     * Finds all points within eps distance from center.
     */
    private List<T> findNeighbors(T center, List<T> data) {
        List<T> neighbors = new ArrayList<>();
        
        for (T point : data) {
            if (center.distanceTo(point) <= eps) {
                neighbors.add(point);
            }
        }
        
        return neighbors;
    }

    public double getEps() {
        return eps;
    }

    public int getMinPts() {
        return minPts;
    }
}
