package com.hit.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * K-Means clustering algorithm implementation.
 * Divides data into exactly k clusters.
 *
 * @param <T> The type of data points
 */
public class KMeansAlgoImpl<T extends IClusterable<T>> extends AbstractAlgoCluster<T> {

    private static final int MAX_ITERATIONS = 100;
    private final Random random;

    public KMeansAlgoImpl() {
        this.random = new Random();
    }

    /**
     * Constructor with seed for testing (makes results reproducible).
     */
    public KMeansAlgoImpl(long seed) {
        this.random = new Random(seed);
    }

    @Override
    public List<List<T>> cluster(List<T> data, int k) {
        // Check input
        if (!isValidData(data)) {
            return new ArrayList<>();
        }
        if (!isValidK(k)) {
            return new ArrayList<>();
        }
        
        // If k is bigger than data size, each point is its own cluster
        if (k >= data.size()) {
            List<List<T>> result = new ArrayList<>();
            for (T item : data) {
                List<T> single = new ArrayList<>();
                single.add(item);
                result.add(single);
            }
            return result;
        }

        // Pick k random starting points
        List<T> centers = pickRandomCenters(data, k);
        List<List<T>> clusters = new ArrayList<>();

        // Main loop - repeat until centers stop moving
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            
            // Create empty clusters
            clusters = createEmptyClusters(k);

            // Put each point in the closest cluster
            for (T item : data) {
                int closest = findClosestCenter(item, centers);
                clusters.get(closest).add(item);
            }

            // Calculate new centers
            List<T> newCenters = new ArrayList<>();
            for (int j = 0; j < k; j++) {
                List<T> cluster = clusters.get(j);
                
                if (cluster.isEmpty()) {
                    // Keep old center if cluster is empty
                    newCenters.add(centers.get(j));
                } else {
                    // Find best center point in this cluster
                    T newCenter = findCenterPoint(cluster);
                    newCenters.add(newCenter);
                }
            }

            // Check if centers changed
            if (centersAreSame(centers, newCenters)) {
                break; // Done - no more changes
            }

            centers = newCenters;
        }

        return clusters;
    }

    /**
     * Picks k random points from data as starting centers.
     */
    private List<T> pickRandomCenters(List<T> data, int k) {
        List<T> centers = new ArrayList<>();
        List<T> copy = new ArrayList<>(data);

        for (int i = 0; i < k; i++) {
            int index = random.nextInt(copy.size());
            centers.add(copy.get(index));
            copy.remove(index);
        }
        
        return centers;
    }

    /**
     * Finds which center is closest to the given item.
     */
    private int findClosestCenter(T item, List<T> centers) {
        int closestIndex = 0;
        double minDist = Double.MAX_VALUE;

        for (int i = 0; i < centers.size(); i++) {
            double dist = item.distanceTo(centers.get(i));
            if (dist < minDist) {
                minDist = dist;
                closestIndex = i;
            }
        }
        
        return closestIndex;
    }

    /**
     * Checks if old centers equal new centers.
     */
    private boolean centersAreSame(List<T> oldCenters, List<T> newCenters) {
        if (oldCenters.size() != newCenters.size()) {
            return false;
        }
        
        for (int i = 0; i < oldCenters.size(); i++) {
            if (!oldCenters.get(i).equals(newCenters.get(i))) {
                return false;
            }
        }
        
        return true;
    }
}
