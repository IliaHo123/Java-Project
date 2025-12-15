package com.hit.algorithm;

import java.util.List;

/**
 * Interface for clustering algorithms.
 * Defines the common API that all clustering implementations must follow.
 *
 * @param <T> The type of elements to be clustered
 */
public interface IAlgoCluster<T extends IClusterable<T>> {

    /**
     * Divides the input data into clusters.
     *
     * @param data The list of items to cluster
     * @param k    The number of clusters (used by K-Means, ignored by DBSCAN)
     * @return     A list of clusters
     */
    List<List<T>> cluster(List<T> data, int k);
}
