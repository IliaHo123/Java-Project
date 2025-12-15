package com.hit.algorithm;

/**
 * Interface for objects that can be clustered.
 * Any class that wants to be clustered must implement this interface.
 *
 * @param <T> The type of object to calculate distance to
 */
public interface IClusterable<T> {

    /**
     * Calculates the distance from this object to another object.
     *
     * @param other The other object
     * @return The distance between the two objects
     */
    double distanceTo(T other);
}
